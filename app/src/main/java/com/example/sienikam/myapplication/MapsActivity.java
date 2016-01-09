package com.example.sienikam.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        XMLParser test = new XMLParser();
        test.filename="/data/data/com.example.sienikam.myapplication/ships.xml";

        Bundle map_type = getIntent().getExtras();
        Log.e("map_type", map_type.getString("map_type"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if(map_type.getString("map_type").equals("ALL")) {
            for (int i = 0; i < test.readxml(test.filename).getLength(); i++) {
                Element element = (Element) test.readxml(test.filename).item(i);
                String SHIPNAME = element.getAttribute("SHIPNAME");
                String TYPE_NAME = element.getAttribute("TYPE_NAME");
                String ETA_CALC = element.getAttribute("ETA_CALC");
                String SPEED = element.getAttribute("SPEED");
                String LAST_PORT = element.getAttribute("LAST_PORT");
                if(ETA_CALC.equals("")) {
                    ETA_CALC = "Unknown";
                }
                double LAT = Double.parseDouble(element.getAttribute("LAT"));
                double LON = Double.parseDouble(element.getAttribute("LON"));
                LatLng vessel = new LatLng(LAT, LON);
                mMap.addMarker(new MarkerOptions().position(vessel).title(SHIPNAME).snippet("Vessel type: " + TYPE_NAME + "\n" + "Last port: " + LAST_PORT + "\n" + "ETA PLSZZ: " + ETA_CALC + "\n" + "Speed: " + SPEED).icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))).showInfoWindow();
                builder.include(vessel);
            }
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.animateCamera(cu);
        } else {
            double LAT = Double.parseDouble(map_type.getString("LAT"));
            double LON = Double.parseDouble(map_type.getString("LON"));
            String SHIPNAME = map_type.getString("SHIPNAME");
            String TYPE_NAME = map_type.getString("SHIP_TYPE");
            String ETA_CALC = map_type.getString("ETA_CALC");
            String SPEED = map_type.getString("SPEED");
            String LAST_PORT = map_type.getString("LAST_PORT");
            LatLng vessel = new LatLng(LAT, LON);
            mMap.addMarker(new MarkerOptions().position(vessel).title(SHIPNAME).snippet("Vessel type: " + TYPE_NAME + "\n" + "Last port: " + LAST_PORT + "\n" + "ETA PLSZZ: " + ETA_CALC + "\n" + "Speed: " + SPEED).icon(BitmapDescriptorFactory.fromResource(R.drawable.photo))).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(vessel));
            float MAP_ZOOM_MAX = mMap.getMaxZoomLevel()-15;
            Log.e("zoom", String.valueOf(MAP_ZOOM_MAX));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vessel, MAP_ZOOM_MAX));
        }
    }
}
