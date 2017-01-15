package com.cesar.localizacaoindoorandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cesar.com.localizacaoindoorapi.core.Finder;
import cesar.com.localizacaoindoorapi.model.Location;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText newLocation;
    private ResourceWifi resourceWifi;
    private ArrayAdapter<Location> arrayAdapterRecordedLocations;
    private List<Location> recordedLocations = new ArrayList<Location>();
    private List<Location> currentLocation = new ArrayList<Location>();
    private ArrayAdapter<Location> arrayAdapterCurrentLocation;
    private Finder finder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(this);
        newLocation = (EditText) findViewById(R.id.location);
        resourceWifi = new ResourceWifi(this);

        final ListView lv1 = (ListView) findViewById(R.id.recordedLocations);
        arrayAdapterRecordedLocations = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, recordedLocations);
        lv1.setAdapter(arrayAdapterRecordedLocations);

        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                newLocation.setText(lv1.getItemAtPosition(pos).toString());
                return true;
            }
        });

        ListView lv2 = (ListView) findViewById(R.id.currentLocation);
        arrayAdapterCurrentLocation = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, currentLocation);
        lv2.setAdapter(arrayAdapterCurrentLocation);

        finder = new Finder();
        createTrace();
    }

    public void onClick(View v) {
        if (this.newLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Informe a localização", Toast.LENGTH_LONG).show();
        } else {
            Location location = new Location();
            location.setName(this.newLocation.getText().toString());
            location.setReferencesAp(resourceWifi.scan());
            if(recordedLocations.contains(location)){
                recordedLocations.remove(recordedLocations.indexOf(location));
            }
            recordedLocations.add(location);
            newLocation.setText("");
            arrayAdapterRecordedLocations.notifyDataSetChanged();
        }
    }

    private void createTrace() {

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run()
            {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        currentLocation.clear();
                        int cont = 0;
                        Map<Location, Integer> locationsTemp = new HashMap<Location, Integer>();
                        while(cont < 5){
                            Location newLocation = finder.meet(recordedLocations, resourceWifi.scan());
                            if(newLocation!=null){
                                if(locationsTemp.containsKey(newLocation)){
                                    locationsTemp.put(newLocation, locationsTemp.get(newLocation)+1);
                                }else{
                                    locationsTemp.put(newLocation, 1);
                                }
                            }
                            cont++;
                        }

                        if (!locationsTemp.isEmpty()) {
                            currentLocation.add((Location) finder.sortByValueMaxToMin(locationsTemp).keySet().toArray()[0]);
                            arrayAdapterCurrentLocation.notifyDataSetChanged();
                            //System.out.println(currentLocation.toString());
                        }
                    }

                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}

