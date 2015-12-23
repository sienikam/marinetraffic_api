package com.example.sienikam.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by sienikam on 2015-12-22.
 */
public class XMLParser extends AsyncTask {
    String xml_data_url;
    public NodeList getXmlFromUrl(String xml_data_url) {
        try {
            URL url = new URL(xml_data_url);
            URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            NodeList nodes = doc.getElementsByTagName("VESSEL_ETA");
            return nodes;
            //Log.e("nodelength", String.valueOf(nodes.getLength()));
            //for (int i = 0; i < nodes.getLength(); i++) {
                //Element element = (Element) nodes.item(i);
                //Log.e("LAT", element.getAttribute("LAT"));
                //Log.e("LON", element.getAttribute("LON"));
                //return element;
            //}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}
