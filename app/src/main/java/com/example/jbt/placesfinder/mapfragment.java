package com.example.jbt.placesfinder;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Created by jbt on 2/7/2017.
 */

public class mapfragment extends Fragment{
    private MapView mapView;
    private GoogleMap googleMap;
    private TextView title;
    private Marker marker;
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabletmaps, container, false);
        sp = getActivity().getSharedPreferences("DATA",Context.MODE_PRIVATE);
        mapView = (MapView) rootView.findViewById(R.id.map2);
        mapView.onCreate(null);
        mapView.onResume();



        try {
            MapsInitializer.initialize(getActivity());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                try {
                    googleMap.setMyLocationEnabled(true);
                }catch (SecurityException e){

                }
                double lat = (double) sp.getFloat("lat", 31);
                double lng = (double) sp.getFloat("lng", 35);
                String title=sp.getString("title","location");
                String desc= sp.getString("desc","description");
                //CREATE A MARKER
                LatLng jerusalem = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(jerusalem).title(title).snippet(desc));

                // ZOOMING FOR THE LOCATION
                CameraPosition cameraPosition = new CameraPosition.Builder().target(jerusalem).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }


        });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }



}
