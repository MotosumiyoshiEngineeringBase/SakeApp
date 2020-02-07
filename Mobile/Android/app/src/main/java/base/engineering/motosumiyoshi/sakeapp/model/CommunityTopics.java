package base.engineering.motosumiyoshi.sakeapp.model;

import com.google.gson.annotations.SerializedName;

public class CommunityTopics {
    //トピックID	○	○
    @SerializedName("id")
    private String id;

    //コミュニティID	○	○
    @SerializedName("community_id")
    private String idcommunity_id;

    //コミュニティの名前	○	○
    @SerializedName("community_name")
    private String idcommunity_name;

    //トピック所有者メンバーの情報(詳しくは レスポンスの共通仕様 を参照)	○
    @SerializedName("member")
    private Member member;

    //トピックのタイトル	○	○
    @SerializedName("name")
    private String name;

    //ピックの本文	○	○
    @SerializedName("body")
    private String body;

    //トピックの作成時間	○
    @SerializedName("created_at")
    private String created_at;

    //トピックに添付された画像のリスト	○
    @SerializedName("images")
    private String[] images;

    //トピックが編集可能どうかのフラグ	○
    @SerializedName("editable")
    private Boolean editable;

    //トピック最新コメント		○
    @SerializedName("latest_comment")
    private String latest_comment;

    //トピックの最新コメントのID
    @SerializedName("latest_commnet_id")
    private String latest_commnet_id;
}
