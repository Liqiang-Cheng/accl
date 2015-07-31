/*
 * Copyright Medtronic, Inc. 2015
 *
 *  MEDTRONIC CONFIDENTIAL - This document is the property of Medtronic,
 *  Inc.,and must be accounted for. Information herein is confidential. Do
 *  not reproduce it, reveal it to unauthorized persons, or send it outside
 *  Medtronic without proper authorization.
 */

package com.medtronic.neuro.acclmobile.app;

import android.content.Context;
import android.widget.ArrayAdapter;

public class AcclListAdapter extends ArrayAdapter<AcclData> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public AcclListAdapter(Context context, int resource) {
        super(context, resource);
    }
}
