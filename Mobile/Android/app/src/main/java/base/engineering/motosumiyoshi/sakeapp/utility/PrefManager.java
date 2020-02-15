package base.engineering.motosumiyoshi.sakeapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

import base.engineering.motosumiyoshi.sakeapp.common.Constants;

public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
