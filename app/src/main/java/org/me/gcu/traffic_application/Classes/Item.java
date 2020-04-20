// Student Name: Darren Lally
// Student ID: S1622370
package org.me.gcu.traffic_application.Classes;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Item {

    //Private variables
    private String Title;
    private String Description;
    private String Link;
    private Double Lat;
    private Double Lon;
    private String Coordinates;
    private Date pubDate;
    private String datish;
    private Date startDate;
    private Date endDate;
    private ArrayList<Item> item_list;


    //Inital constructor
    public Item(){
        this.Lat = 0.0;
        this.Lon = 0.0;
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
    public Item(String title, String desc, String datish, String link, String coordinates, Date DatePublished, Double lat, Double lon, Date start_date, Date EndDate, ArrayList<Item> channelItems){
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
    public Double getLat(){return Lat;}
    public Date getPublishDate(){return pubDate;}
    public Date getStartDate(){return startDate;}
    public Date getEndDate(){return endDate;}
    public ArrayList getItemList(){return item_list;}
    public double getLon(){return Lon;}
    //Setters
    public void setTitle(String title){Title = title;}
    public void setDatish(String dat){datish = dat;}
    public void setDescription(String desc){Description = desc;}
    public void setLink(String link){Link = link;}
    public void setPublishDate(Date DatePublished){pubDate = DatePublished;}
    public void setCoord(String coordinates){Coordinates = coordinates;}
    public void setStartDate(Date start_date){startDate = start_date;}
    public void setLat(Double lat){Lat=lat;}
    public void setLon(Double lon){Lon=lon;}


    public LatLng getCoordinates(){
        return new LatLng(Lat,Lon);
    }
    //public void addItemList(Item item){item_list.add(item);}





}
