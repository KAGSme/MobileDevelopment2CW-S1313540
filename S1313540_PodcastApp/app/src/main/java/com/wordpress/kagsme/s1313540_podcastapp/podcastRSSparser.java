package com.wordpress.kagsme.s1313540_podcastapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TooManyListenersException;

public class PodcastRSSparser {
    //Declare Variables------------------------------------------
    private PodcastDataItem pDataItem = new PodcastDataItem();
    private ArrayList<EpisodeDataItem> eDataItems = new ArrayList<EpisodeDataItem>();
    private boolean podcastInfoOnly = false;
    private Context appContext;

    //Declare Constructor----------------------------------------
    public PodcastRSSparser(Context context){
        appContext = context;
    }

    public PodcastRSSparser(Context context, String podcastUrl, boolean justPodcast){
        appContext = context;
        podcastInfoOnly = justPodcast;
        try
        {
            ParseRSSData(podcastUrl, podcastInfoOnly);
        }
        catch (MalformedURLException e)
        {
            Log.e("s1313540", "Malformed URL Exception");
        }
    }

    //Declare Getter & Setters-----------------------------------
    public PodcastDataItem getpDataItem()
    {
        return pDataItem;
    }

    public ArrayList<EpisodeDataItem> geteDataItems()
    {
        return eDataItems;
    }

    //Parsing Functionality--------------------------------------
    public void parseRSSPodcastDataItem(XmlPullParser parser, int eventType, String pLink){
        pDataItem = new PodcastDataItem();
        boolean isPodcastInfo = true;
        pDataItem.setPodcastLink(pLink);
        try
        {
            while(eventType != XmlPullParser.END_DOCUMENT && isPodcastInfo)
            {
                if(eventType == XmlPullParser.START_TAG)
                {
                    if(parser.getName().equalsIgnoreCase("title"))
                    {
                        if(isPodcastInfo)
                        {
                            pDataItem.setPodcastTitle(parser.nextText());
                        }
                    }
                    else
                    if(parser.getName().equalsIgnoreCase("description"))
                    {
                        Log.e("s1313540", "got Podcast Info");
                        if(isPodcastInfo)
                        {
                            pDataItem.setPodcastDesc(parser.nextText());
                        }
                    }
                    if(parser.getName().equalsIgnoreCase("itunes:image"))
                    {
                        Log.e("s1313540", "got Podcast Info");
                        if(isPodcastInfo)
                        {
                            pDataItem.setPodcastDesc(parser.getAttributeValue(null, "href"));
                            DownloadsMgr.DownloadImageFile(appContext, pDataItem.getPodcastImageLink(),pDataItem.getPodcastImageFName());
                        }
                    }
                    else
                    if(parser.getName().equalsIgnoreCase("item"))
                    {
                        isPodcastInfo = false;
                    }
                }
                eventType = parser.next();
            }
        }
        catch(XmlPullParserException parserE)
        {
            Log.e("s1313540", "parsing error " + parserE.toString());
        }
        catch(IOException parserE)
        {
            Log.e("s1313540", "IO error during parsing");
        }
    }

    public void parseRSSDataItem(XmlPullParser parser, int eventType) {
        EpisodeDataItem tmpEDataItem = null;
        eDataItems.clear();
        boolean isPodcastInfo = true;
        int itemCount = 0;

        try
        {
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_TAG)
                {
                    if(parser.getName().equalsIgnoreCase("item"))
                    {
                        isPodcastInfo = false;
                    }
                    else
                    if(parser.getName().equalsIgnoreCase("title"))
                    {
                        if(!isPodcastInfo)
                        {
                            tmpEDataItem = new EpisodeDataItem();
                            tmpEDataItem.setEpisodeTitle(parser.nextText());
                        }
                    }
                    else
                    if(parser.getName().equalsIgnoreCase("Enclosure"))
                    {
                        if(!isPodcastInfo)
                        {
                            tmpEDataItem.setEpisodeLink(parser.getAttributeValue(null, "url"));
                        }
                    }
                    else
                    if(parser.getName().endsWith("subtitle"))
                    {
                        itemCount++;
                        Log.d("s1313540", "end of item " + itemCount);
                        if(!isPodcastInfo)
                        {
                            tmpEDataItem.setEpisodeDesc(parser.nextText());
                            eDataItems.add(tmpEDataItem);
                        }
                    }
                }
                eventType = parser.next();
            }
        }
        catch(XmlPullParserException parserE)
        {
            Log.e("s1313540", "parsing error " + parserE.toString());
        }
        catch(IOException parserE)
        {
            Log.e("s1313540", "IO error during parsing");
        }
    }

    public void ParseRSSData(String RSSFeed, boolean justPodcast) throws MalformedURLException {
        URL rssURL = new URL(RSSFeed);
        InputStream rssInputStream;
        try
        {
            XmlPullParserFactory parseRSSfactory = XmlPullParserFactory.newInstance();
            parseRSSfactory.setNamespaceAware(true);
            XmlPullParser RSSxmlPP = parseRSSfactory.newPullParser();
            String xmlRSS = getStringFromInputStream(getInputStream(rssURL), "UTF-8");
            RSSxmlPP.setInput(new StringReader(xmlRSS));
            int eventType = RSSxmlPP.getEventType();

            if(justPodcast) parseRSSPodcastDataItem(RSSxmlPP, eventType, RSSFeed);
            else parseRSSDataItem(RSSxmlPP, eventType);
        }
        catch(XmlPullParserException e)
        {
            Log.e("s1313540", "Parsing error, " + e.toString());
        }
        catch(IOException e)
        {
            Log.e("s1313540", "IO error during parsing");
        }

        Log.e("s1313540", "End Document");
    }

    public InputStream getInputStream(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /*milliseconds*/);
        conn.setConnectTimeout(15000 /*milliseconds*/);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();

    }

    public static String getStringFromInputStream(InputStream stream, String charsetName) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, charsetName);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}
