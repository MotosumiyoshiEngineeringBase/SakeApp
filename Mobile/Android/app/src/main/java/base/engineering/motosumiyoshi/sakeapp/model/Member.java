package base.engineering.motosumiyoshi.sakeapp.model;

import com.google.gson.annotations.SerializedName;

public class Member {
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
    public String getName() {return this.name; }

    @SerializedName("profile_image")
    private String profile_image;
    public String getProfileImage() {return this.profile_image; }

    @SerializedName("profile_url")
    private String profile_url;

    @SerializedName("screen_name")
    private String screen_name;

    @SerializedName("self")
    private Boolean self;
    public Boolean isSelf() {return this.self; }

    @SerializedName("self_introduction")
    private Boolean self_introduction;

}
