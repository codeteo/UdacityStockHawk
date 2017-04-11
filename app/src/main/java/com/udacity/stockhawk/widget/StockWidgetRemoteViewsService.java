package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.Contract.Quote;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class that controls the data being shown in the Stock Widget.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StockWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor cursor = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (cursor != null) {
                    cursor.close();
                }


                final long identityToken = Binder.clearCallingIdentity();
                cursor = getContentResolver().query(Quote.URI,
                        Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null,
                        null,
                        Quote.COLUMN_SYMBOL + " ASC");
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount() {
                return cursor == null ? 0 : cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        cursor == null || !cursor.moveToPosition(position)) {
                    return null;
                }

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_list_item);

                String symbolId = cursor.getString(Contract.Quote.POSITION_SYMBOL);
                Float priceId = cursor.getFloat(Contract.Quote.POSITION_PRICE);
                Float changeId = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);

                DecimalFormat dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                DecimalFormat percentageFormat;

                dollarFormatWithPlus.setPositivePrefix("+$");
                percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
                percentageFormat.setMaximumFractionDigits(2);
                percentageFormat.setMinimumFractionDigits(2);
                percentageFormat.setPositivePrefix("+");

                @DrawableRes
                int backgroundResource;

                if (changeId > 0) {
                    backgroundResource = R.drawable.percent_change_pill_green;
                } else {
                    backgroundResource = R.drawable.percent_change_pill_red;
                }

                /* set views */
                remoteViews.setTextViewText(R.id.symbol, symbolId);
                remoteViews.setTextViewText(R.id.price, dollarFormat.format(priceId));
                remoteViews.setTextViewText(R.id.change, dollarFormatWithPlus.format(changeId));
                remoteViews.setInt(R.id.change, "setBackgroundResource", backgroundResource);

                final Intent fillInIntent = new Intent();
                Uri uri = Contract.Quote.makeUriForStock(symbolId);
                fillInIntent.setData(uri);
                remoteViews.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (cursor.moveToPosition(position))
                    return cursor.getLong(Quote.POSITION_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
