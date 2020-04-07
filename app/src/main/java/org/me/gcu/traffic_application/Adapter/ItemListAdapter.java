package org.me.gcu.traffic_application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import org.me.gcu.traffic_application.R;
import org.me.gcu.traffic_application.models.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ViewHolder {
    TextView title;
    TextView desc;
    TextView pubDate;
    TextView lat;
    TextView link;
    TextView coord;
    TextInputEditText dateInput;
}

public class ItemListAdapter extends ArrayAdapter<Item> implements Filterable {
    private TextInputEditText todays_date;
    public static final String TAG = "ItemListAdapter";
    private ArrayList<Item> newfilter;
    private Context mContext;
    private int lastPosition = -1;
    int mResource;
    private Object FragmentManager;

    public ItemListAdapter(Context context, int resource, ArrayList<Item> object){
        super(context,resource,object);
        this.newfilter = new ArrayList<>(object);
        mContext = context;
        mResource = resource;
    }

    @SuppressLint("WrongViewCast")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String description = getItem(position).getDescription();
        //SimpleDateFormat sdf = SimpleDateFormat("")
        Date pubDate = getItem(position).getPublishDate();
        String coord = getItem(position).getCoordinates();
        String link = getItem(position).getLink();
        final View result;

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.pubDate = (TextView) convertView.findViewById(R.id.pubDate);

            result = convertView;
            convertView.setTag(holder);



        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);

        result.startAnimation(animation);
        lastPosition = position;

        holder.title.setText(title);
        holder.desc.setText(description);

        //final DatePicker dpicker = new DatePicker(this.todays_date);
       //final MaterialDatePicker dp = dpicker.build();
        //dp.show(FragmentManager, "geeee");
        //holder.dateInput.setText(dpicker.Today_View_Roadworks());

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        holder.pubDate.setText(sdf.format(pubDate));


        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        return smthFilter;
    }

    private Filter smthFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Item> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(newfilter);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Item item : filteredList){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            newfilter.clear();
            newfilter.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };
}
