package org.me.gcu.traffic_application.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.traffic_application.HandleXml;
import org.me.gcu.traffic_application.R;
import org.me.gcu.traffic_application.models.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlannedRoadFragment extends Fragment {
    private PlannedRoadFragment.RecyclerViewAdapter mAdapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //change to r.frag
        View view = inflater.inflate(R.layout.recview2, container,false);
        ArrayList<Item> items2 = new ArrayList<>();
        HandleXml obj2 = new HandleXml("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        try {
            items2 = obj2.fetchXml();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        this.mAdapter2 = new PlannedRoadFragment.RecyclerViewAdapter(items2);

        RecyclerView recyclerView2 =view.findViewById(R.id.planned_rec);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setAdapter(this.mAdapter2);
        mAdapter2.notifyDataSetChanged();

        return view;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView title;
        public TextView desc;
        public TextView pubDate;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.adapter_view_layout2,container,false));
            mCardView = itemView.findViewById(R.id.card_container2);
            title =itemView.findViewById(R.id.title2);
            desc =itemView.findViewById(R.id.desc2);
            pubDate =itemView.findViewById(R.id.pubDate2);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<PlannedRoadFragment.RecyclerViewHolder>{
        private  ArrayList<Item> mList = new ArrayList<Item>();

        public RecyclerViewAdapter(ArrayList<Item> items){
            if(this.mList == null){
                this.mList = items;
            }else{
                this.mList.clear();
                this.mList = new ArrayList<Item>(items);
                notifyDataSetChanged();
            }

            notifyDataSetChanged();


        }
        @NonNull
        @Override
        public PlannedRoadFragment.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inlater = LayoutInflater.from(getActivity());
            return new RecyclerViewHolder(inlater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            Item currentItem = mList.get(position);
            holder.title.setText(currentItem.getTitle());
            holder.desc.setText(currentItem.getDescription());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            holder.pubDate.setText(sdf.format(currentItem.getPublishDate()));

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


}
