package com.udacity.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;

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
                cursor = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL + " ASC");
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public RemoteViews getViewAt(int i) {
                return null;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }

}
