// Student Name: Darren Lally
// Student ID: S1622370
package org.me.gcu.traffic_application;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.me.gcu.traffic_application.Classes.Item;
import org.me.gcu.traffic_application.Classes.ItemChannel;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HandleXml extends  MainActivity {

    public enum ParserScope{
        Channel,
        Item,
    }

    private String urlString = "https://trafficscotland.org/rss/feeds/roadworks.aspx" ;
    // Create new item
    private Item trafficItem = new Item();
    private ItemChannel itemChannel = new ItemChannel();
    //Keep track of the scope using enums
    private ParserScope scope = ParserScope.Channel;
    //Handles the URL
    public HandleXml(String url){this.urlString = url;}

    public ArrayList<Item> fetchXml() throws XmlPullParserException {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url= new URL(urlString);
                        HttpURLConnection connect = (HttpURLConnection)url.openConnection();
                        connect.setReadTimeout(10000);
                        connect.setConnectTimeout(15000);
                        connect.setRequestMethod("GET");
                        connect.setDoInput(true);
                        connect.connect();

                        InputStream stream = connect.getInputStream();

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        XmlPullParser myparser = factory.newPullParser();
                        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        myparser.setInput(stream,null);

                        try {
                            int event = myparser.getEventType();
                            while (event != XmlPullParser.END_DOCUMENT) {
                                switch (event) {
                                    case XmlPullParser.START_TAG:
                                        if(myparser.getName().equalsIgnoreCase("pubDate")){
                                            String datish= myparser.nextText();
                                            String newstr = datish.substring(0,11);
                                            trafficItem.setDatish(newstr);
                                        }

                                        switch (myparser.getName().toLowerCase()){
                                            case "georss:point":
                                                String latString = myparser.nextText();
                                                if(latString.isEmpty()){

                                                }else{
                                                    trafficItem.setCoord(latString);

                                                    String[] latlongs = latString.split(" ");
                                                    try{
                                                        Double lat = Double.parseDouble(latlongs[0]);
                                                        Double lon = Double.parseDouble(latlongs[1]);

                                                        LatLng z = new LatLng(lat,lon);

                                                        trafficItem.setLat(lat);
                                                        trafficItem.setLon(lon);

                                                    }catch (Exception e){

                                                    }
                                                }

                                                break;
                                            case "channel":
                                                scope = ParserScope.Channel;
                                                break;
                                            case "item":
                                                scope = ParserScope.Item;
                                                break;

                                            case "title":
                                                String title = myparser.nextText();
                                                if(scope.equals(ParserScope.Channel)){
                                                    itemChannel.setTitle(title);
                                                }else{
                                                    trafficItem.setTitle(title);
                                                }
                                                break;

                                            case "description":
                                                String desc = myparser.nextText();

                                                if(scope.equals(ParserScope.Channel)){
                                                    itemChannel.setDescription(desc);
                                                }else{ ;
                                                 trafficItem.setDescription(desc);
                                                }
                                                break;

                                            case "link":
                                                String link = myparser.nextText();
                                                if(scope.equals(ParserScope.Channel)){
                                                    itemChannel.setLink(link);
                                                }else{
                                                    trafficItem.setLink(link);
                                                }

                                                break;

                                            case "pubDate":
                                                String DatePublished = myparser.nextText();
                                                try{
                                                    Date date = new SimpleDateFormat("E, dd MM YYYY HH:mm:ss z").parse(DatePublished);
                                                    trafficItem.setPublishDate(date);

                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                trafficItem.setDatish(DatePublished);
                                                break;
                                            default:
                                                break;
                                        }

                                        break;
                                    case XmlPullParser.END_TAG:

                                        if(myparser.getName()!=null){
                                            //Log.i("ENDTAG", myparser.getName());
                                        }
                                        //Item has ended
                                        if(myparser.getName().toLowerCase().equalsIgnoreCase("item") && scope == ParserScope.Item){
                                            //Removes HTML Tags from description
                                            trafficItem.setDescription(trafficItem.getDescription().replaceAll("<br />", "\\\n"));
                                            itemChannel.addItemList(trafficItem);
                                            trafficItem = new Item();
                                            scope = ParserScope.Channel;
                                            }
                                        break;
                                    default:
                                        if(myparser.getName() != null){

                                        }
                                        break;
                                }
                                event = myparser.next();
                            }


                        }catch (XmlPullParserException e){
                            Log.i("error", e.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        return itemChannel.getChannelItems();
    }

}

