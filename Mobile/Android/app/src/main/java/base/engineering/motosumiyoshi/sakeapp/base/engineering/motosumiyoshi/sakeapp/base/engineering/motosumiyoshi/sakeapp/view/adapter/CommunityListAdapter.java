package base.engineering.motosumiyoshi.sakeapp.base.engineering.motosumiyoshi.sakeapp.base.engineering.motosumiyoshi.sakeapp.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.base.engineering.motosumiyoshi.sakeapp.model.Community;

public class CommunityListAdapter extends BaseAdapter {

    //表示するコミュニティViewオブジェクト
    static class ViewHolder {
        ImageView img;
        TextView communityName;
        TextView description;
        TextView members;
    }

    Context context;
    LayoutInflater layoutInflater = null;
    List<Community> communityList = new ArrayList<Community>();
    private int layoutID;

    public CommunityListAdapter(Context context, int itemLayoutId) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutID = itemLayoutId;
    }

    public void setCommunityList(List<Community> communityList) {
        this.communityList = communityList;
    }

    @Override
    public int getCount() {
        return communityList.size();
    }

    @Override
    public Object getItem(int position) {
        return communityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return communityList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(this.layoutID, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.img_item);
            holder.communityName = convertView.findViewById(R.id.text_community_name);
            holder.description = convertView.findViewById(R.id.text_community_description);
            holder.members = convertView.findViewById(R.id.text_community_members);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(communityList.get(position).getImage()).resize(100, 100).into(holder.img);
        holder.communityName.setText(communityList.get(position).getName());
        holder.description.setText(communityList.get(position).getDescription());
        holder.members.setText(this.context.getString(R.string.registeredMemberCounts) + communityList.get(position).getMemberCount());

        return convertView;
    }
}
