package base.engineering.motosumiyoshi.sakeapp.httpclient;

import android.content.Context;
import android.content.Intent;
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

import base.engineering.motosumiyoshi.sakeapp.CommunityTimeLineActivity;
import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.adapter.CommunityListAdapter;
import base.engineering.motosumiyoshi.sakeapp.adapter.CommunityTimeLineListAdapter;
import base.engineering.motosumiyoshi.sakeapp.model.Community;
import base.engineering.motosumiyoshi.sakeapp.model.CommunityTimeLine;

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

    /** コミュニティを検索するメソッド。
     * @param word 検索キーワード。nullの場合は全コミュニティを検索する。
     * @param targetView 本メソッドの検索結果を表示するListView
     * */
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

    /**
     * @param community_id *必須	取得したいコミュニティタイムラインの、コミュニティIDを指定します。
     * @param searchMaxSize	取得したいタイムラインの、取得件数を指定します。デフォルトは20。
     * */
    public void searchCommunityTimeLine (long community_id, int searchMaxSize,
                                         ListView targetView) {
        String SEARCH_ACTIVITY_PATH = "/api.php/activity/community.json";
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(DOMAIN)
                .path(SEARCH_ACTIVITY_PATH)
                .appendQueryParameter("community_id", String.valueOf(community_id))
                .appendQueryParameter("count", String.valueOf(searchMaxSize))
                .appendQueryParameter("apiKey", APIKEY)
                .build();
        call(uri.toString(), targetView, "searchCommunityTimeLine");
    }

    /**
     * @param body *必須	タイムラインの本文を指定します。
     * @param public_flag    タイムラインの公開範囲を指定します。
     * @param in_reply_to_activity_id    タイムラインの返信先IDを指定します。
     * @param communityUri    タイムライン投稿元のURIを指定します。
     * @param target    タイムラインの種類を指定します。例えば、コミュニティタイムラインに投稿したい場合はcommunityを指定します。
     * @param target_id    タイムラインの種類のIDを指定します。targetが指定されている場合は*必須項目です。
     * */
    public void sendMessegeToCommunity (String body,
                                        int public_flag,
                                        int in_reply_to_activity_id,
                                        String communityUri,
                                        String target,
                                        int target_id,
                                        ListView targetView) {
        String SEARCH_ACTIVITY_PATH = "/api.php/activity/post.json";
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(DOMAIN)
                .path(SEARCH_ACTIVITY_PATH)
                .appendQueryParameter("body", body)
                .appendQueryParameter("public_flag", String.valueOf(public_flag))
                .appendQueryParameter("in_reply_to_activity_id", String.valueOf(in_reply_to_activity_id))
                .appendQueryParameter("uri", communityUri)
                .appendQueryParameter("target", target)
                .appendQueryParameter("target_id", String.valueOf(target_id))
                .appendQueryParameter("apiKey", APIKEY)
                .build();
        call(uri.toString(), targetView, "sendMessegeToCommunity");
    }

    /**
     * @param target *必須	string	topic､community､memberが指定可能です｡
     *        topic : 一件のトピックを取得します
     *        community : コミュニティのトピック一覧を取得します
     *        member : メンバーの所属するコミュニティのトピック一覧を取得します
     * @param target_id *必須	integer	targetがtopicの場合はtopic_idを､targetがcommunityの場合はcommunity_idを､targetがmemberの場合はmember_idを指定します｡
     * @param format    string	取得フォーマットを指定します｡miniとnormalが指定できます｡このフィールドがない場合はnormalフォーマットになります｡
     * @param max_id    integer	取得したいトピックIDの最大値を指定します。
     * @param since_id    integer	取得したいトピックIDの最小値を指定します。
     * @param searchMaxSize    integer	取得したいトピックの、取得件数を指定します。このパラメータを指定しなかった場合のデフォルトは15です。
     * */
    public void searchCommunityTopics (String target, long target_id, String format, int since_id,
                                       int max_id, int searchMaxSize,
                                         ListView targetView) {
        String SEARCH_ACTIVITY_PATH = "/api.php/topic/search.json";
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(DOMAIN)
                .path(SEARCH_ACTIVITY_PATH)
                .appendQueryParameter("target", target)
                .appendQueryParameter("target_id", String.valueOf(target_id))
                .appendQueryParameter("format", format)
                .appendQueryParameter("max_id", String.valueOf(max_id))
                .appendQueryParameter("since_id", String.valueOf(since_id))
                .appendQueryParameter("count", String.valueOf(searchMaxSize))
                .appendQueryParameter("apiKey", APIKEY)
                .build();
        call(uri.toString(), targetView, "searchCommunityTopics");
    }

    @Override
    public void onResponseReceived(String responseBody, View targetView, String methodName) {
        Gson gson = new Gson();
        //FIXME responseが401とかだと、JsonObjectのparseで落ちる。ので対応が必要。
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
            List<Community> communityList = gson.fromJson( jsonAry, new TypeToken<ArrayList<Community>>(){}.getType());
            ListView listview = (ListView) targetView;
            CommunityListAdapter adapter = new CommunityListAdapter(this.context, R.layout.community_list);
            adapter.setCommunityList(communityList);
            listview.setAdapter(adapter);
        } else if ("searchCommunityTimeLine".equals(methodName))  {  //特定コミュニティのタイムライン取得
            List<CommunityTimeLine> community = gson.fromJson( jsonAry, new TypeToken<ArrayList<CommunityTimeLine>>(){}.getType());
            ListView listview = (ListView) targetView;
            CommunityTimeLineListAdapter adapter = new CommunityTimeLineListAdapter(this.context, R.layout.timeline_list);
            adapter.setCommunityList(community);
            listview.setAdapter(adapter);
        }  else if ("sendMessegeToCommunity".equals(methodName))  {  //コミュニティへのメッセージ送信
            //
        } else if ("searchCommunityTopics".equals(methodName))  {  //特定コミュニティのトピックス取得
            List<CommunityTimeLine> community = gson.fromJson( jsonAry, new TypeToken<ArrayList<CommunityTimeLine>>(){}.getType());
            ListView listview = (ListView) targetView;
            CommunityTimeLineListAdapter adapter = new CommunityTimeLineListAdapter(this.context, R.layout.community_list);
            adapter.setCommunityList(community);
            listview.setAdapter(adapter);
        } else {
            //不明なメソッドの場合はエラーにすべし
        }
    }
}
