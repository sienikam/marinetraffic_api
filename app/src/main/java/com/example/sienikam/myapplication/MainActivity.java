package com.example.sienikam.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button mapbutton = (Button) findViewById(R.id.button_map);
        Button databutton = (Button) findViewById(R.id.button_data);

        mapbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "om om om", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
        databutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                XMLParser test2 = new XMLParser();
                test2.xml_data_url="http://arch.edu.pl/~k3/statki.xml";

                //Log.e("test", String.valueOf(test.getXmlFromUrl(test.xml_data_url).getLength()));

                //Toast.makeText(getApplicationContext(), "om om om", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
    }
}
