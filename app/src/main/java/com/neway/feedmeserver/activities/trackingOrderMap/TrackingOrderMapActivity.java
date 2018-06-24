package com.neway.feedmeserver.activities.trackingOrderMap;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.neway.feedmeserver.R;
import com.neway.feedmeserver.bases.BaseActivity;
import com.neway.feedmeserver.bases.MapBaseActivity;
import com.neway.feedmeserver.model.Request;

public class TrackingOrderMapActivity extends MapBaseActivity {

    public static Request currentRequest = null;
    boolean mapLoaded = false;
    GoogleMap mMap = null;

    @Override
    protected int getMapViewID() {
        return R.id.map;
    }

    @Override
    protected void onMapGetReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    protected void onMapGetLoaded() {
        mapLoaded = true;
    }

    @Override
    protected int getContentResource() {
        return R.layout.tracking_order_map;
    }

    @Override
    protected void init(@Nullable Bundle state) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
