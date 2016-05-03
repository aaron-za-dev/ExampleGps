package com.aaronzuniga.examplegps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txt1, txt2, txt3, txt4, txt5;
    private Button activar, desactivar;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         txt1 = (TextView) findViewById(R.id.latitud);
         txt2 = (TextView) findViewById(R.id.longitud);
         txt3 = (TextView) findViewById(R.id.precision);
         txt4 = (TextView) findViewById(R.id.direccionActual);
         txt5 = (TextView) findViewById(R.id.estadoProveedor);

         activar = (Button) findViewById(R.id.activar);
         activar.setOnClickListener(this);

         desactivar = (Button) findViewById(R.id.desactivar);
         desactivar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activar:
                 actualizarPosicion();
                break;
            case R.id.desactivar:
                locationManager.removeUpdates(locationListener);
                break;
        }

    }

    private void actualizarPosicion() {
        //Obtenemos una referencia al LocationManager
        locationManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la última posición conocida
        Location location =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //Mostramos la última posición conocida
        muestraPosicion(location);


        //Mostramos la ultima direccion conocida
        muestraDireccion(location);

        //Nos registramos para recibir actualizaciones de la posición
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                muestraPosicion(location);
                muestraDireccion(location);
            }
            public void onProviderDisabled(String provider){
                txt5.setText("Provider OFF");
            }
            public void onProviderEnabled(String provider){
                txt5.setText("Provider ON");
            }
            public void onStatusChanged(String provider, int status, Bundle extras){
                Log.i("LocAndroid", "Provider Status: " + status);
                txt5.setText("Provider Status: " + status);
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }


    private void muestraPosicion(Location loc) {
        if(loc != null)
        {
            txt1.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            txt2.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            txt3.setText("Precision: " + String.valueOf(loc.getAccuracy()));
            Log.i("LocAndroid", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
        }
        else
        {
            txt1.setText("Latitud: (sin_datos)");
            txt2.setText("Longitud: (sin_datos)");
            txt3.setText("Precision: (sin_datos)");
        }
    }


    private void muestraDireccion(Location loc){
        if(loc != null){
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation( loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    txt4.setText("Mi direccion es:"+ address.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            txt4.setText("Direccion: (Sin informacion)");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}







