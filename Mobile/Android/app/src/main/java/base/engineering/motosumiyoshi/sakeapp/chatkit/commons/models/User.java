package base.engineering.motosumiyoshi.sakeapp.chatkit.commons.models;

/*
 * Created by troy379 on 04.04.17.
 */
public class User implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;
    private boolean itsMe;

    public User(String id, String name, String avatar, boolean online, boolean itsMe) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.itsMe = itsMe;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public boolean itsMe() {
        return this.itsMe;
    }

    public boolean isOnline() {
        return online;
    }
}
