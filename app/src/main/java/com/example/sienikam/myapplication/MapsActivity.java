package com.example.sienikam.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Element;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        XMLParser test = new XMLParser();
        test.xml_data_url="http://arch.edu.pl/~k3/statki.xml";
        Log.e("ship counter", String.valueOf(test.getXmlFromUrl(test.xml_data_url).getLength()));

        for(int i=0; i < test.getXmlFromUrl(test.xml_data_url).getLength();i++) {
            Element element = (Element) test.getXmlFromUrl(test.xml_data_url).item(i);
            String SHIPNAME = element.getAttribute("SHIPNAME");
            String TYPE_NAME = element.getAttribute("TYPE_NAME");
            double LAT = Double.parseDouble(element.getAttribute("LAT"));
            double LON = Double.parseDouble(element.getAttribute("LON"));
            LatLng vessel = new LatLng(LAT, LON);
            mMap.addMarker(new MarkerOptions().position(vessel).title(SHIPNAME)).setSnippet(TYPE_NAME);
        }
        LatLng PLSZZ = new LatLng(53.441, 14.595);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PLSZZ));
    }
}
