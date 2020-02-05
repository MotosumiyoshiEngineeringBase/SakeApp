package base.engineering.motosumiyoshi.sakeapp.model;

import com.google.gson.annotations.SerializedName;

public class AdminMember {
    @SerializedName("blocking")
    private Boolean blocking;

    @SerializedName("friend")
    private Boolean friend;

    @SerializedName("friends_count")
    private int friends_count;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("profile_url")
    private String profile_url;

    @SerializedName("screen_name")
    private String screen_name;

    @SerializedName("self")
    private Boolean self;

    @SerializedName("self_introduction")
    private Boolean self_introduction;

}
