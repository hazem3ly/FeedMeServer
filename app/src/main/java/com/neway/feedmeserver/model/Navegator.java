package com.neway.feedmeserver.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Hazem Ali
 * On 5/7/2018.
 */
public class Navegator {

    public static final String BUNDLE_DATA = "bundle_data";

    public static void navigateToActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    public static void navigateToActivity(Context context, Class<?> clazz, Bundle data) {
        context.startActivity(new Intent(context, clazz).putExtra(BUNDLE_DATA, data));
    }

}
