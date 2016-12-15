package net.feheren_fekete.kimuka;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private static final String ACTIVE_FILTER = "ActiveFilter";
    private static final String ACTIVE_FILTER_DEFAULT = "";

    private SharedPreferences mSharedPreferences;

    public AppPreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
    }

    public String getActiveFilter() {
        return mSharedPreferences.getString(ACTIVE_FILTER, ACTIVE_FILTER_DEFAULT);
    }

    public void setActiveFilter(String activeFilter) {
        mSharedPreferences
                .edit()
                .putString(ACTIVE_FILTER, activeFilter)
                .apply();
    }

}
