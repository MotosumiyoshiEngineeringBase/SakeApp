package base.engineering.motosumiyoshi.sakeapp.model;

import com.google.gson.annotations.SerializedName;

public class CommunityTimeLine {
    //タイムラインのID（連番）
    @SerializedName("id")
    private String id;
    public long getId() {
        return Long.parseLong(this.id);
    }

    //メンバーの情報（詳しくは レスポンスの共通仕様 を参照）
    @SerializedName("member")
    private Member member;
    public Member getMember() {
        return this.member;
    }

    //タイムラインの本文
    @SerializedName("body")
    private String body;
    public String getBody() {
        return this.body;
    }

    //タイムラインの本文（HTML版）
    @SerializedName("body_html")
    private String body_html;

    //タイムラインの参照URI
    @SerializedName("uri")
    private String uri;

    //タイムラインの投稿元の名前
    @SerializedName("source")
    private String source;

    //タイムラインの投稿元のURL
    @SerializedName("source_uri")
    private String source_uri;

    //投稿時間
    @SerializedName("created_at")
    private String created_at;
    public String getCreatedDate() {return this.created_at; }
}
