package com.wordpress.kagsme.s1313540_podcastapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PodcastRSSparser {
    //Declare Variables------------------------------------------
    private PodcastDataItem pDataItem;
    private ArrayList<EpisodeDataItem> eDataItems = new ArrayList<EpisodeDataItem>();
    private boolean podcastInfoOnly = false;

    //Declare Constructor----------------------------------------
    public PodcastRSSparser(){
    }

    public PodcastRSSparser(String podcastUrl, boolean justPodcast){
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
    public void parseRSSPodcastDataItem(XmlPullParser parser, int eventType){
        pDataItem = new PodcastDataItem();
        boolean isPodcastInfo = true;

        try
        {
            while(eventType != XmlPullParser.END_DOCUMENT || !isPodcastInfo)
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
                    if(parser.getName().equalsIgnoreCase("link"))
                    {
                        if(isPodcastInfo)
                        {
                            pDataItem.setPodcastLink(parser.nextText());
                        }
                    }
                    else
                    if(parser.getName().endsWith("subtitle"))
                    {
                        Log.e("s1313540", "gotPodcastInfo");
                        if(isPodcastInfo)
                        {
                            pDataItem.setPodcastDesc(parser.nextText());
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
                    if(parser.getName().equalsIgnoreCase("link"))
                    {
                        if(!isPodcastInfo)
                        {
                            tmpEDataItem.setEpisodeLink(parser.nextText());
                        }
                    }
                    else
                    if(parser.getName().endsWith("subtitle"))
                    {
                        itemCount++;
                        Log.d("s1313540", "end off item" + itemCount);
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

            if(justPodcast) parseRSSPodcastDataItem(RSSxmlPP, eventType);
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
        return url.openConnection().getInputStream();
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
