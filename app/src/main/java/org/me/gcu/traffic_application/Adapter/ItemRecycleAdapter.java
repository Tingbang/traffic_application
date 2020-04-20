// Student Name: Darren Lally
// Student ID: S1622370
package org.me.gcu.traffic_application.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.traffic_application.Classes.Item;
import org.me.gcu.traffic_application.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemRecyleHolder> implements Filterable {
    private ArrayList<Item> mArrayList;
    private ArrayList<Item> mArrayListCopy;
    private ArrayList<Item> mRoadworks;

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    public static class ItemRecyleHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView desc;
        public TextView pubDate;
        public TextView startDate;


        public ItemRecyleHolder(@NonNull View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            desc =itemView.findViewById(R.id.desc);
            pubDate =itemView.findViewById(R.id.pubDate);


        }
    }
    public ItemRecycleAdapter(ArrayList<Item> itemList, ArrayList<Item> otherList){
        this.mArrayList = itemList;
        this.mRoadworks = itemList;
        this.mArrayListCopy =itemList;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        String dateString1 = sdf.format(calendar.getTime());
        notifyDataSetChanged();

    }

    public void updateData(ArrayList<Item> newList){
        mArrayList.clear();
        this.mArrayList = new ArrayList<Item>();
        this.mArrayList.addAll(newList);
        int size = this.mArrayList.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemRecyleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_layout, parent, false);
        ItemRecyleHolder irh = new ItemRecyleHolder(v);
        return irh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyleHolder holder, int position) {

        Item currentItem = mArrayList.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.desc.setText(currentItem.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        holder.pubDate.setText(currentItem.getDatish());
    }

    @Override
        public int getItemCount() {
        return mArrayList.size();
    }


    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            SimpleDateFormat format = new SimpleDateFormat("ddd, dd MMM");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar currentDate = Calendar.getInstance();

            ArrayList<Item> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(mArrayList);
            }else{

                for(Item item : mArrayList){
                    if(item.getDatish().equals(constraint)){filteredList.add(item);}
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mRoadworks.clear();
            mRoadworks.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };


}
