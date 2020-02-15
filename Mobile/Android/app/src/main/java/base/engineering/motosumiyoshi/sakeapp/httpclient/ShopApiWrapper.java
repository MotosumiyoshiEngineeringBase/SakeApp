package base.engineering.motosumiyoshi.sakeapp.httpclient;

import android.net.Uri;

import okhttp3.Callback;

public class ShopApiWrapper extends OkHttp3Wrapper {

    private static final String SCHEME = "https";
    private static final String DOMAIN = "app.rakuten.co.jp";

    public ShopApiWrapper() {
    }

    public void searchSakeProduct(String keyword, Callback callback) {
        String SEARCH_ACTIVITY_PATH = "/services/api/IchibaItem/Search/20170706";
        String generId = "510915";
        Uri uri = new Uri.Builder().
                scheme(SCHEME).
                authority(DOMAIN).
                path(SEARCH_ACTIVITY_PATH).
                appendQueryParameter("applicationId", getAPIKey()).
                appendQueryParameter("generId", generId).
                appendQueryParameter("keyword", keyword).
                build();
        call(uri.toString(), callback);
    }

    private String getAPIKey() {
        return (new Object() {
            int t;
            public String toString() {
                byte[] buf = new byte[19];
                t = 1713560326;
                buf[0] = (byte) (t >>> 21);
                t = -1067347518;
                buf[1] = (byte) (t >>> 17);
                t = -615778106;
                buf[2] = (byte) (t >>> 2);
                t = 848073145;
                buf[3] = (byte) (t >>> 3);
                t = -1261704629;
                buf[4] = (byte) (t >>> 18);
                t = 705538034;
                buf[5] = (byte) (t >>> 14);
                t = 759807482;
                buf[6] = (byte) (t >>> 11);
                t = 479779125;
                buf[7] = (byte) (t >>> 10);
                t = -2120939766;
                buf[8] = (byte) (t >>> 5);
                t = 1784934680;
                buf[9] = (byte) (t >>> 17);
                t = 594379180;
                buf[10] = (byte) (t >>> 20);
                t = -949547121;
                buf[11] = (byte) (t >>> 13);
                t = 1438376778;
                buf[12] = (byte) (t >>> 4);
                t = 1442029542;
                buf[13] = (byte) (t >>> 12);
                t = -455206881;
                buf[14] = (byte) (t >>> 7);
                t = 476606694;
                buf[15] = (byte) (t >>> 2);
                t = 838911605;
                buf[16] = (byte) (t >>> 5);
                t = 1879774036;
                buf[17] = (byte) (t >>> 4);
                t = 542638691;
                buf[18] = (byte) (t >>> 1);
                return new String(buf);
            }
        }.toString());
    }
}
