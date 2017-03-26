package com.udacity.stockhawk;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Possible states of server
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({ServerStatus.OK, ServerStatus.DOWN, ServerStatus.UNKNOWN})
public @interface ServerStatus {

    int OK = 0;
    int DOWN = 1;
    int UNKNOWN = 2;

}
