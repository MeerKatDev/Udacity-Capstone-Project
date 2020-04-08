package org.meerkatdev.redditroulette.utils;

import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

public class Tags {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String LIFECYCLE = "LIFECYCLE";
    public static final String ACCESS_TYPE = "access_type";
    public static final String OAUTH_DATA = "oauth_data";
    public static final String SUBREDDIT_ID = "subreddit_id";
    public static final String SUBREDDIT_NAME = "subreddit_name";

    public static final String POST = "post";


    public static void logBundle(Bundle b) {
        if (b != null) {
            StringBuilder str = new StringBuilder();
            Set<String> keys = b.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                str.append(key);
                str.append(":");
                str.append(b.get(key));
                str.append("\n\r");
            }
            Log.d("BUNDLE_LOGGING", str.toString());
        }
    }
}
