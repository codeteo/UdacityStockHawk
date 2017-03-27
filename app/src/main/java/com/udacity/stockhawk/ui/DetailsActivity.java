package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.stockhawk.R;

import butterknife.ButterKnife;

/**
 * Activity to display graph of the stock.
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String INTENT_SYMBOL = "symbol";
    private static final String INTENT_HISTORY = "history";

    String history, symbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            symbol = getIntent().getStringExtra(INTENT_SYMBOL);
            history = getIntent().getStringExtra(INTENT_HISTORY);
        }

        setUpToolbar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(symbol);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

}
