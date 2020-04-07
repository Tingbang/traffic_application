package org.me.gcu.traffic_application.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Item {

    //Private variables
    private String Title;
    private String Description;
    private String Link;
    private String Lat;
    private String Lon;
    private String Coordinates;
    private Date pubDate;
    private String datish;
    private Date startDate;
    private Date endDate;
    private ArrayList<Item> item_list;

    //Inital constructor
    public Item(){
        this.Lat = "";
        this.Lon = "";
        this.datish= "";
        this.Title = "";
        this.Description = "";
        this.Link ="";
        this.Coordinates = "";
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY,0);
        this.pubDate = new Date();
        this.startDate = today.getTime();
        this.endDate = today.getTime();
        this.item_list = new ArrayList<Item>();
    }

    //Pass Item Details in and set to data passed in.
    public Item(String title, String desc,String datish, String link, String coordinates, Date DatePublished, String lat, String lon, Date start_date, Date EndDate, ArrayList<Item> channelItems){
        //Set Item details
        this.Title = title;
        this.datish =datish;
        this.Description = desc;
        this.Link= link;
        this.Coordinates = coordinates;
        this.pubDate = new Date(String.valueOf(DatePublished));
        this.startDate= start_date;
        this.endDate=EndDate;
        this.item_list = channelItems;
        this.Lat = lat;
        this.Lon = lon;
    }

    //Getters
    public String getTitle(){return Title;}
    public String getDatish(){return datish;}
    public String getDescription(){return Description;}
    public String getLink(){return Link;}
    public String getCoordinates(){return Coordinates;}
    public String getLat(){return Lat;}
    public Date getPublishDate(){return pubDate;}
    public Date getStartDate(){return startDate;}
    public Date getEndDate(){return endDate;}
    public ArrayList getItemList(){return item_list;}
    //Setters
    public void setTitle(String title){Title = title;}
    public void setDatish(String dat){datish = dat;}
    public void setDescription(String desc){Description = desc;}
    public void setLink(String link){Link = link;}
    public void setPublishDate(Date DatePublished){pubDate = DatePublished;}
    public void setCoord(String coordinates){Coordinates = coordinates;}
    public void setStartDate(Date start_date){startDate = start_date;}
    public void setLat(String lat){Lat=lat;}
    public void setLon(String lon){Lon=lon;}
    //public void addItemList(Item item){item_list.add(item);}





}
