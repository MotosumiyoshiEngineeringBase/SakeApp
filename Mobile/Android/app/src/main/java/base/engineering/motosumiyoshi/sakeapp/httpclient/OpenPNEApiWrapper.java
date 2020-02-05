package base.engineering.motosumiyoshi.sakeapp.httpclient;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.adapter.CommunityListAdapter;
import base.engineering.motosumiyoshi.sakeapp.model.Community;

//OpenPNEのApiWrapperクラスです。
public class OpenPNEApiWrapper extends OkHttpCaller {

    private String SCHEME = "http";
    private String DOMAIN = "motosumiengineer.pne.jp";
    private String APIKEY = "8b4dfc3a01fe1629c8fa66913d4450c99cc8e674773db935e5cbc91abbab9940";

    private Context context;

    public OpenPNEApiWrapper (Context context) {
        this.context = context;
    }

    public void getTimeLine (TextView targetView) {
        String SEARCH_ACTIVITY_PATH = "/api.php/activity/search.json";
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(DOMAIN)
                .path(SEARCH_ACTIVITY_PATH)
                .appendQueryParameter("since_id", "1")
                .appendQueryParameter("max_id", "50")
                .build();
        call(uri.toString(), targetView, "getTimeLine");
    }

    public void searchCommunity (String word, ListView targetView) {
        String SEARCH_ACTIVITY_PATH = "/api.php/community/search.json";
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(DOMAIN)
                .path(SEARCH_ACTIVITY_PATH)
                .appendQueryParameter("keyword", word == null ? "" : word)
                .appendQueryParameter("apiKey", APIKEY)
                .build();
        call(uri.toString(), targetView, "searchCommunity");
    }

    @Override
    public void onResponseReceived(String responseBody, View targetView, String methodName) {
        Gson gson = new Gson();
        JsonObject jsonObj = (JsonObject) new Gson().fromJson(responseBody, JsonObject.class);

        JsonElement responseStatus = jsonObj.get("status");
        if("error".equals(responseStatus.getAsString())){
            //setText("error");
            return;
        }

        JsonArray jsonAry = jsonObj.get("data").getAsJsonArray();

        //TODO もっときれいにやる方法を考える。やりたいことはメソッド事に表示するViewが違うから、そこをうまくハンドリングできるようにする
        if ("getTimeLine".equals(methodName)) { //タイムライン取得の場合

        } else if ("searchCommunity".equals(methodName)) { //コミュニティ検索の場合
            List<Community> community = gson.fromJson( jsonAry, new TypeToken<ArrayList<Community>>(){}.getType());
            ListView listview = (ListView) targetView;
            CommunityListAdapter adapter = new CommunityListAdapter(this.context, R.layout.community_list);
            adapter.setCommunityList(community);
            listview.setAdapter(adapter);

            //リスト項目が選択された時のイベントを追加
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO
                }
            });
        } else {
            //不明なメソッドの場合はエラーにすべし
        }
    }
}
