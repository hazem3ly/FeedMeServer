package com.neway.feedmeserver.bases;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.neway.feedmeserver.R;

public abstract class MapBaseActivity extends FragmentActivity implements BaseView
        , OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentResource());
        init(savedInstanceState);
        initMapView(getMapViewID());

    }

    private void initMapView(int mapViewID) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(mapViewID);
        mapFragment.getMapAsync(this);

    }

    protected abstract int getMapViewID();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        onMapGetReady(googleMap);
        googleMap.setOnMapLoadedCallback(this);
    }

    protected abstract void onMapGetReady(GoogleMap googleMap);
    protected abstract void onMapGetLoaded();

    /**
     * Layout resource to be inflated
     *
     * @return layout resource
     */
    @LayoutRes
    protected abstract int getContentResource();

    /**
     * Initialisations
     */
    protected abstract void init(@Nullable Bundle state);


    @Override
    public void onMapLoaded() {
        onMapGetLoaded();
    }


}
