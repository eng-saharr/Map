package com.example.mapapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.List;

public class MyLocationProvieder {


    private static final long TIME_BETWEEN_UPDATE =20000 ;
    private static final float DISTANCE_BETWEEN_UPDATE =10 ;
    private Location location;
    private LocationManager locationManager;

    public MyLocationProvieder(Context context) {
        locationManager =(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        location=null;

    }
    private boolean isProviderEnabled(){
        boolean gps=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }
    @SuppressLint("MissingPermission")
    public Location getCurrentLocation(LocationListener locationListener){
        if (!isProviderEnabled())
            return null;

        String provider = LocationManager.GPS_PROVIDER;
        if(!locationManager.isProviderEnabled(provider))
            provider=LocationManager.NETWORK_PROVIDER;
        location=locationManager.getLastKnownLocation(provider);
        if (location==null)
            location=getBestLocation();
        if(locationListener != null){
            locationManager.
                    requestLocationUpdates(provider,TIME_BETWEEN_UPDATE,DISTANCE_BETWEEN_UPDATE,
                            locationListener);
        }
             return location;


    }

    private Location getBestLocation() {
        List<String>providers=locationManager.getAllProviders();
        Location bestLocation=null;
        for (String provider:providers){
            @SuppressLint("MissingPermission")
            Location temp=locationManager.getLastKnownLocation(provider);
            if (temp==null)
                continue;
            if (bestLocation==null)
                bestLocation=temp;
            else {
                if (temp.getAccuracy()>bestLocation.getAccuracy())
                    bestLocation=temp;
            }
        }
      return  bestLocation;

    }

}
