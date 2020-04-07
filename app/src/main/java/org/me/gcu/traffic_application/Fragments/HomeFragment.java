package org.me.gcu.traffic_application.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.traffic_application.HandleXml;
import org.me.gcu.traffic_application.R;
import org.me.gcu.traffic_application.models.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerViewAdapter mAdapter;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            //String urls = getArguments().getString("url");
            //change to r.frag
            View view = inflater.inflate(R.layout.recview2, container,false);
            ArrayList<Item> items = new ArrayList<>();
            ArrayList<Item> items2 = new ArrayList<>();
            ArrayList<Item> items3 = new ArrayList<>();
            HandleXml obj = new HandleXml("https://trafficscotland.org/rss/feeds/roadworks.aspx");
            HandleXml obj2 = new HandleXml("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
            HandleXml obj3 = new HandleXml("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
            try {
                items = obj.fetchXml();
                items2 = obj2.fetchXml();
                items3 = obj3.fetchXml();

                //items.addAll(items2);
                //items.addAll(items3);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            this.mAdapter = new RecyclerViewAdapter(items);

            RecyclerView recyclerView =view.findViewById(R.id.recView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(this.mAdapter);
            mAdapter.notifyDataSetChanged();

            return view;
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
            public TextView title;
            public TextView desc;
            public TextView pubDate;

            public RecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
                super(inflater.inflate(R.layout.adapter_view_layout,container,false));

                mCardView = itemView.findViewById(R.id.card_container);
                title =itemView.findViewById(R.id.title);
                desc =itemView.findViewById(R.id.desc);
                pubDate =itemView.findViewById(R.id.pubDate);
            }
        }

            public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
            private  ArrayList<Item> mList;

            public RecyclerViewAdapter(ArrayList<Item> items){

                if(this.mList == null){
                    this.mList = items;
                }else{
                    this.mList.clear();
                    this.mList = items;
                }

                notifyDataSetChanged();
            }
            @NonNull
            @Override
            public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
