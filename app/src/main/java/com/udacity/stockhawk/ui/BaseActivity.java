package com.udacity.stockhawk.ui;

import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.data.bus.BusProvider;

/**
 * Base Activity class, registers Otto event bus instance.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
