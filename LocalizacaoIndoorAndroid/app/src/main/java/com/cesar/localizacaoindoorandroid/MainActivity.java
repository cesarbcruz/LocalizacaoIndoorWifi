package com.cesar.localizacaoindoorandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cesar.com.localizacaoindoorapi.core.Finder;
import cesar.com.localizacaoindoorapi.model.Location;
import cesar.com.localizacaoindoorapi.model.Wifi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private EditText newLocation;
    private ResourceWifi resourceWifi;
    private ArrayAdapter<Location> arrayAdapterRecordedLocations;
    private List<Location> recordedLocations = new ArrayList<Location>();
    private Finder finder;
    private EditText currentLocation;
    private static int pos;
    private final int SAMPLE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(this);
        newLocation = (EditText) findViewById(R.id.location);
        resourceWifi = new ResourceWifi(this);

        select();
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

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                pos = position;
                showPopupMenu(view);
            }
        });

        currentLocation = (EditText) findViewById(R.id.currentLocation);
        finder = new Finder();
        createTrace();
    }

    public void showPopupMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    public boolean onMenuItemClick(MenuItem item){

        switch(item.getItemId()){
            case R.id.delete:
                recordedLocations.remove(pos);
                save();
                arrayAdapterRecordedLocations.notifyDataSetChanged();
                return true;
            case R.id.details:
                Location location = recordedLocations.get(pos);
                StringBuilder details = new StringBuilder();
                details.append(location.getName()).append("\n");
                for(Wifi wifi : location.getReferencesAp()){
                    details.append(wifi.getBssid()+" : "+wifi.calculateDistance()).append("\n");
                }
                Toast.makeText(getBaseContext(),details, Toast.LENGTH_LONG).show();
                return true;
            default: return false;
        }
    }

    public void onClick(View v) {
        if (this.newLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Informe a localização", Toast.LENGTH_LONG).show();
        } else {
            Location location = new Location();
            location.setName(this.newLocation.getText().toString());
            List<Wifi> references = new ArrayList<>();
            for (int i = 0; i < SAMPLE; i++) {
                references.addAll(resourceWifi.scan());
            }
            location.setReferencesAp(finder.average(references));
            if(recordedLocations.contains(location)){
                recordedLocations.remove(recordedLocations.indexOf(location));
            }
            recordedLocations.add(location);
            save();
            newLocation.setText("");
            arrayAdapterRecordedLocations.notifyDataSetChanged();
        }
    }

    private void save(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recordedLocations);
        prefsEditor.putString("recordedLocations", json);
        prefsEditor.commit();
    }

    private void select(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("recordedLocations", "");
        Type type = new TypeToken<List<Location>>(){}.getType();
        recordedLocations = gson.fromJson(json, type);
    }

    private void createTrace() {

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run()
            {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        List<Wifi> references = new ArrayList<>();
                        for (int i = 0; i < SAMPLE; i++) {
                            references.addAll(resourceWifi.scan());
                        }

                        Location newLocation = finder.meet(recordedLocations, finder.average(references));
                        if(newLocation!=null){
                            currentLocation.setText(newLocation.getName());
                        }else{
                            currentLocation.setText("");
                        }
                    }});
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
        select();
    }
}

