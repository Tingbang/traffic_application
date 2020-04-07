package org.me.gcu.traffic_application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.traffic_application.R;
import org.me.gcu.traffic_application.models.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemRecyleHolder> implements Filterable {
    //private final MainActivity mActivty;
    private ArrayList<Item> mArrayList;
    private ArrayList<Item> mArrayListCopy;
    //private itemFilter

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
            startDate =itemView.findViewById(R.id.startDATE);

        }
    }
    public ItemRecycleAdapter(ArrayList<Item> itemList){
        this.mArrayList = itemList;
        this.mArrayListCopy = itemList;

        notifyDataSetChanged();

    }

    public void set_data(ArrayList<Item>newList){
        if(mArrayList != null){
            mArrayList.clear();
            mArrayList =newList;
            notifyDataSetChanged();
        }else{
            mArrayList = newList;
        }
       // notifyDataSetChanged();
    }

    public void updateData(ArrayList<Item> newList){
        mArrayList.clear();
        this.mArrayList = new ArrayList<Item>();
        this.mArrayList.addAll(newList);
        int size = this.mArrayList.size();
        notifyDataSetChanged();

    }
    public void RemoveData(ArrayList<Item> newList){
        int size = this.mArrayList.size();
        this.mArrayList.clear();
        //this.mArrayList =newList;
        notifyItemRangeRemoved(0,size);

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
        holder.pubDate.setText(sdf.format(currentItem.getPublishDate()));
        //holder.startDate.setText(sdf.format(currentItem.getStartDate()));

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //mArrayListCopy.clear();
            //mArrayListCopy = mArrayList;

            SimpleDateFormat format = new SimpleDateFormat("ddd, dd MMM");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar currentDate = Calendar.getInstance();
            //String timeSTR = format.format(currentDate.getTime());
            //constraint = timeSTR;

            ArrayList<Item> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() ==0){
                filteredList.addAll(mArrayListCopy);
            }else{
                //String filterPattern = constraint.toString().toLowerCase().trim();

                for(Item item : mArrayListCopy){
                    if(item.getDatish().equals(constraint)){
                        filteredList.add(item);

                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            //Log.i("search", String.valueOf(filteredList));
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mArrayListCopy.clear();
            mArrayListCopy.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };


}
