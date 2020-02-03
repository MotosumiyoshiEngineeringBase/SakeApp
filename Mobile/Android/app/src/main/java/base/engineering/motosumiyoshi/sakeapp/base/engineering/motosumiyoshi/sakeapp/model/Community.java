package base.engineering.motosumiyoshi.sakeapp.base.engineering.motosumiyoshi.sakeapp.model;

import com.google.gson.annotations.SerializedName;

public class Community {
    //id	コミュニティID
    @SerializedName("id")
    private String id;
    public long getId() {
        return Long.parseLong(this.id);
    }

    //name コミュニティ名
    @SerializedName("name")
    private String name;
    public String getName() {
        return this.name;
    }

    //コミュニティの説明
    @SerializedName("description")
    private String description;
    public String getDescription() {
        return this.description;
    }

    //category	コミュニティのカテゴリ
    @SerializedName("category")
    private String category;

    //community_url	コミュニティTOPのURL
    @SerializedName("community_url")
    private String community_url;

    //community_image_url	コミュニティイメージ画像のURL
    @SerializedName("community_image_url")
    private String community_image_url;
    public String getImage() {
        return this.community_image_url;
    }

    //joining	このコミュニティに参加していればtrue, そうでなければfalseを返します。
    @SerializedName("joining")
    private Boolean joining;

    //admin	このコミュニティの管理人であればtrue, そうでなければfalseを返します。
    @SerializedName("admin")
    private Boolean admin;

    //sub_admin	このコミュニティの副管理人であればtrue, そうでなければfalseを返します。
    @SerializedName("sub_admin")
    private Boolean sub_admin;

    //created_at	コミュニティが作成された日付
    @SerializedName("created_at")
    private String created_at;

    //admin_member	コミュニティ管理人のメンバー情報（詳しくは レスポンスの共通仕様 を参照）
    @SerializedName("admin_member")
    private AdminMember admin_member;

    //member_count	コミュニティ参加者の数
    @SerializedName("member_count")
    private int member_count;
    public int getMemberCount() {
        return this.member_count;
    }

    //public_flag	コミュニティの公開範囲
    @SerializedName("public_flag")
    private Boolean public_flag;

    //register_policy	コミュニティの参加条件
    @SerializedName("register_policy")
    private String register_policy;
}
