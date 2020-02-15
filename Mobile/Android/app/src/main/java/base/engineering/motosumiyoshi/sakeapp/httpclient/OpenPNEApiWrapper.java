package base.engineering.motosumiyoshi.sakeapp.httpclient;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.adapter.CommunityListAdapter;
import base.engineering.motosumiyoshi.sakeapp.adapter.CommunityTimeLineListAdapter;
import base.engineering.motosumiyoshi.sakeapp.chatkit.commons.ImageLoader;
import base.engineering.motosumiyoshi.sakeapp.chatkit.commons.models.Message;
import base.engineering.motosumiyoshi.sakeapp.chatkit.commons.models.User;
import base.engineering.motosumiyoshi.sakeapp.chatkit.messages.MessagesList;
import base.engineering.motosumiyoshi.sakeapp.chatkit.messages.MessagesListAdapter;
import base.engineering.motosumiyoshi.sakeapp.data.ShareData;
import base.engineering.motosumiyoshi.sakeapp.model.Community;
import base.engineering.motosumiyoshi.sakeapp.model.CommunityTimeLine;
import base.engineering.motosumiyoshi.sakeapp.utility.DateFormatter;

//OpenPNEのApiWrapperクラスです。
public class OpenPNEApiWrapper extends OkHttpCaller {

    private static String SCHEME;
    private static String DOMAIN;
    private static String APIKEY;

    static {
        ShareData data = ShareData.getInstance();
        SCHEME = data.getSCHEME();
        DOMAIN = data.getDOMAIN();
        APIKEY = data.getApiKey();
    }

    private Context context;

    public OpenPNEApiWrapper(Context context) {
        this.context = context;
    }

    public void getTimeLine(TextView targetView) {
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

    /**
     * コミュニティを検索するメソッド。
     *
     * @param word       検索キーワード。nullの場合は全コミュニティを検索する。
     * @param targetView 本メソッドの検索結果を表示するListView
     */
    public void searchCommunity(String word, ListView targetView) {
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
     * @param community_id  *必須	取得したいコミュニティタイムラインの、コミュニティIDを指定します。
     * @param searchMaxSize 取得したいタイムラインの、取得件数を指定します。デフォルトは20。
     */
    public void searchCommunityTimeLine(long community_id, int searchMaxSize,
                                        MessagesList targetView) {
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
     * @param body                    *必須	タイムラインの本文を指定します。
     * @param public_flag             タイムラインの公開範囲を指定します。
     * @param in_reply_to_activity_id タイムラインの返信先IDを指定します。
     * @param communityUri            タイムライン投稿元のURIを指定します。
     * @param target                  タイムラインの種類を指定します。例えば、コミュニティタイムラインに投稿したい場合はcommunityを指定します。
     * @param target_id               タイムラインの種類のIDを指定します。targetが指定されている場合は*必須項目です。
     */
    public void postMessegeToCommunity(String body,
                                       int public_flag,
                                       int in_reply_to_activity_id,
                                       String communityUri,
                                       String target,
                                       long target_id,
                                       MessagesList targetView) {
        String POST_MESSAGE_PATH = "/api.php/activity/post.json";
        Uri uri = new Uri.Builder()
                .scheme(SCHEME)
                .authority(DOMAIN)
                .path(POST_MESSAGE_PATH)
                .appendQueryParameter("body", body)
                .appendQueryParameter("public_flag", String.valueOf(public_flag))
//                .appendQueryParameter("in_reply_to_activity_id", String.valueOf(in_reply_to_activity_id))
                .appendQueryParameter("uri", communityUri)
                .appendQueryParameter("target", target)
                .appendQueryParameter("target_id", String.valueOf(target_id))
                .appendQueryParameter("apiKey", APIKEY)
                .build();
        call(uri.toString(), targetView, "postMessageToCommunity");
    }

    /**
     * @param target        *必須	string	topic､community､memberが指定可能です｡
     *                      topic : 一件のトピックを取得します
     *                      community : コミュニティのトピック一覧を取得します
     *                      member : メンバーの所属するコミュニティのトピック一覧を取得します
     * @param target_id     *必須	integer	targetがtopicの場合はtopic_idを､targetがcommunityの場合はcommunity_idを､targetがmemberの場合はmember_idを指定します｡
     * @param format        string	取得フォーマットを指定します｡miniとnormalが指定できます｡このフィールドがない場合はnormalフォーマットになります｡
     * @param max_id        integer	取得したいトピックIDの最大値を指定します。
     * @param since_id      integer	取得したいトピックIDの最小値を指定します。
     * @param searchMaxSize integer	取得したいトピックの、取得件数を指定します。このパラメータを指定しなかった場合のデフォルトは15です。
     */
    public void searchCommunityTopics(String target, long target_id, String format, int since_id,
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
        //FIXME 権限のない操作をした場合は403がリプライされる。
        if (responseBody.contains("403")) {
            //TODO エラーを返してあげよう
            return;
        }

        Gson gson = new Gson();
        //FIXME responseが401とかだと、JsonObjectのparseで落ちる。ので対応が必要。
        JsonObject jsonObj = (JsonObject) new Gson().fromJson(responseBody, JsonObject.class);

        JsonElement responseStatus = jsonObj.get("status");
        if ("error".equals(responseStatus.getAsString())) {
            //setText("error");
            return;
        }

        //TODO もっときれいにやる方法を考える。やりたいことはメソッド事に表示するViewが違うから、そこをうまくハンドリングできるようにする
        if ("getTimeLine".equals(methodName)) { //タイムライン取得の場合
        } else if ("searchCommunity".equals(methodName)) { //コミュニティ検索の場合
            JsonArray jsonAry = jsonObj.get("data").getAsJsonArray();
            List<Community> communityList = gson.fromJson(jsonAry, new TypeToken<ArrayList<Community>>() {
            }.getType());
            ListView listview = (ListView) targetView;
            CommunityListAdapter adapter = new CommunityListAdapter(this.context, R.layout.community_list);
            adapter.setCommunityList(communityList);
            listview.setAdapter(adapter);
        } else if ("searchCommunityTimeLine".equals(methodName)) {  //特定コミュニティのタイムライン取得
            JsonArray jsonAry = jsonObj.get("data").getAsJsonArray();
            List<CommunityTimeLine> community = gson.fromJson(jsonAry, new TypeToken<ArrayList<CommunityTimeLine>>() {
            }.getType());
            ArrayList<Message> messages = new ArrayList<>();
            if (community != null && !community.isEmpty()) {
                for (CommunityTimeLine timeLine : community) {
                    User user = new User(timeLine.getMember().getName(), timeLine.getMember().getName(), timeLine.getMember().getProfileImage(), false, timeLine.getMember().isSelf());
                    messages.add(new Message(
                            String.valueOf(timeLine.getId()),
                            user,
                            timeLine.getBody(),
                            DateFormatter.stringToDate(timeLine.getCreatedDate(), "EEE, dd MMM yyyy HH:mm:ss Z"))
                    );
                }
                //サンプルデータ
//                User user = new User("test01", "T.Test", "http://i.imgur.com/R3Jm1CL.png", false);
//                messages.add(new Message(
//                        "hoge0001",
//                        user,
//                        "手入力したテストメッセージだよ",
//                        DateFormatter.stringToDate("Wed, 01 Jan 2020 23:29:13 +0900", "EEE, dd MMM yyyy HH:mm:ss Z")));
            }

            String senderId = "管理者アカウント"; //TODO ログインユーザーのユーザーIDをセットする。そうすることでタイムラインの右側にメッセージ表示されるようになる。
            ImageLoader imageLoader = new ImageLoader() {
                @Override
                public void loadImage(ImageView imageView, String url, Object payload) {
                    Picasso.get().load(url).into(imageView);
                }
            };
            MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(senderId, imageLoader);
            adapter.addToEnd(messages, false);

            MessagesList messagesList = (MessagesList) targetView;
            messagesList.setAdapter(adapter);
        } else if ("postMessageToCommunity".equals(methodName)) {  //特定コミュニティのタイムライン取得
            MessagesList messagesList = (MessagesList) targetView;
            MessagesListAdapter<Message> adapter = (MessagesListAdapter<Message>) messagesList.getAdapter();

            JsonElement json = jsonObj.get("data");
            CommunityTimeLine response = gson.fromJson(json, new TypeToken<CommunityTimeLine>() {
            }.getType());
            User user = new User(response.getMember().getName(), response.getMember().getName(), response.getMember().getProfileImage(), false, response.getMember().isSelf());
            Message sendedMessage = new Message(
                    String.valueOf(response.getId()),
                    user,
                    response.getBody(),
                    DateFormatter.stringToDate(response.getCreatedDate(), "EEE, dd MMM yyyy HH:mm:ss Z"));
            adapter.addToStart(sendedMessage, true);
            messagesList.setAdapter(adapter);

        } else if ("searchCommunityTopics".equals(methodName)) {  //特定コミュニティのトピックス取得
            JsonArray jsonAry = jsonObj.get("data").getAsJsonArray();
            List<CommunityTimeLine> community = gson.fromJson(jsonAry, new TypeToken<ArrayList<CommunityTimeLine>>() {
            }.getType());
            ListView listview = (ListView) targetView;
            CommunityTimeLineListAdapter adapter = new CommunityTimeLineListAdapter(this.context, R.layout.community_list);
            adapter.setCommunityList(community);
            listview.setAdapter(adapter);
        } else {
            //不明なメソッドの場合はエラーにすべし
        }
    }
}
