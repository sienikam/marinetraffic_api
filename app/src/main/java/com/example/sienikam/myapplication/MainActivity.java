package com.example.sienikam.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    public void generate_ships_list() {
        final XMLParser test = new XMLParser();
        test.filename="/data/data/com.example.sienikam.myapplication/demo.xml";

        mainListView = (ListView) findViewById( R.id.listView );

        ArrayList<String> planetList = new ArrayList<String>();

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);

        for (int i = 0; i < test.readxml(test.filename).getLength(); i++) {
            Element element = (Element) test.readxml(test.filename).item(i);
            //Log.e("for", String.valueOf(element.getAttribute("SHIPNAME")));
            String SHIPNAME = element.getAttribute("SHIPNAME");
            String TYPE_NAME = element.getAttribute("TYPE_NAME");
            String FLAG = element.getAttribute("FLAG");
            String ETA_CALC = element.getAttribute("ETA_CALC");
            if(ETA_CALC.equals("")) {
                listAdapter.add(SHIPNAME + " (" + FLAG + ")" + "\n" + TYPE_NAME);
            } else {
                listAdapter.add(SHIPNAME + " (" + FLAG + ")" + "\n" + TYPE_NAME + "\n" + ETA_CALC);
            }

        }
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("map_type", "ONE");
                //Toast.makeText(getApplicationContext(), "om om om", Toast.LENGTH_SHORT).show();
                Element element = (Element) test.readxml(test.filename).item(position);
                String LAT = element.getAttribute("LAT");
                String LON = element.getAttribute("LON");
                String SHIPNAME = element.getAttribute("SHIPNAME");
                String TYPE_NAME = element.getAttribute("TYPE_NAME");
                i.putExtra("LAT", LAT);
                i.putExtra("LON", LON);
                i.putExtra("SHIPNAME", SHIPNAME);
                i.putExtra("SHIP_TYPE", TYPE_NAME);
                //Toast.makeText(getApplicationContext(), String.valueOf(LAT), Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }

    public class DownloadXML extends AsyncTask {
        String url_xml;
        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            Log.e("download", "start");
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL(url_xml);

                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                File SDCardRoot = Environment.getDataDirectory();
                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(SDCardRoot,"/data/com.example.sienikam.myapplication/demo.xml");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                //progressDialog.setMax(totalSize);

                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //Log.e("size", String.valueOf(downloadedSize));
                }

                //close the output stream when done
                fileOutput.close();
                //catch some possible errors...
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            generate_ships_list();
            progressBar.setVisibility(View.GONE);
            Log.e("download", "stop");
            super.onPostExecute(o);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DownloadXML marinetraffic = new DownloadXML();
        marinetraffic.url_xml="http://arch.edu.pl/~k3/statki.xml";
        marinetraffic.execute();

        Button mapbutton = (Button) findViewById(R.id.button_map);
        Button databutton = (Button) findViewById(R.id.button_data);

        mapbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("map_type", "ALL");
                //Toast.makeText(getApplicationContext(), "om om om", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
        databutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DownloadXML marinetraffic = new DownloadXML();
                marinetraffic.url_xml="http://arch.edu.pl/~k3/statki.xml";
                marinetraffic.execute();
            }
        });
    }
}
