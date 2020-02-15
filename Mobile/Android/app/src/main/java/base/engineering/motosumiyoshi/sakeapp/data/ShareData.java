package base.engineering.motosumiyoshi.sakeapp.data;

import android.app.Application;

/**
 * アクティビティ間で共有する変数クラスです。
 * */
public class ShareData extends Application {
    private static ShareData sInstance = new ShareData();
    private ShareData(){};

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static ShareData getInstance() {
        return sInstance;
    }

    //認証後のAPIキー
    private String apiKey = "";
    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    //外部システム接続情報
    private static final String SCHEME = "http";
    public String getSCHEME() {
        return SCHEME;
    }
    private static final String SCHEME_SSL = "https";
    public String getSCHEME_SSL() {
        return SCHEME_SSL;
    }
    private static final String DOMAIN = "motosumiengineer.pne.jp";
    public String getDOMAIN() {
        return DOMAIN;
    }
}

