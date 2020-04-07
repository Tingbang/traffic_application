package org.me.gcu.traffic_application.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class ItemChannel extends ArrayList<ItemChannel> {

    private String Title;
    private String Description;
    private String Link;
    private Date pubDate;
    private int Ttl;
    private ArrayList<Item> ChannelItems;


    // Inital Constructor
    public ItemChannel() {
        this.Title = "";
        this.Description = "";
        this.Link = "";
        this.Ttl = 0;
        this.ChannelItems = new ArrayList<Item>();
    }

    //Setters
    public void setTitle(String title) {this.Title = title;}
    public void setDescription(String description) {this.Description = description;}
    public void setPubDate(Date pubDate){this.pubDate = pubDate; }
    public void setLink(String link) {this.Link = link;}
    public void setChannelItems(ArrayList<Item> channelItems) {this.ChannelItems = channelItems;}
    public void setTtl(int ttl) {Ttl = ttl;}

    //Getters
    public String getTitle() {return Title;}
    public Date getPubDate(){return this.pubDate;}
    public String getDescription() {return Description;}
    public String getLink() {return Link;}
    public int getTtl() {return Ttl;}


    //public void addItemList(Item item){ChannelItems.add(item);}
    public void addItemList(Item item){
        this.ChannelItems.add(item);
    }

    public ArrayList<Item> getChannelItems(){
        return ChannelItems;
    }


}
