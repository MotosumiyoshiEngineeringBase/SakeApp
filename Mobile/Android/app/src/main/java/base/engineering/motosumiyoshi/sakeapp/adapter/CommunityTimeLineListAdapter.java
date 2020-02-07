package base.engineering.motosumiyoshi.sakeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.model.Community;
import base.engineering.motosumiyoshi.sakeapp.model.CommunityTimeLine;

public class CommunityTimeLineListAdapter extends BaseAdapter {

    //表示するタイムラインオブジェクト
    static class ViewHolder {
        ImageView img;
        TextView createdDate;
        TextView username;
        TextView body;
    }

    Context context;
    LayoutInflater layoutInflater = null;
    List<CommunityTimeLine> communityTimeLineList = new ArrayList<CommunityTimeLine>();
    private int layoutID;

    public CommunityTimeLineListAdapter(Context context, int itemLayoutId) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutID = itemLayoutId;
    }

    public void setCommunityList(List<CommunityTimeLine> communityTimeLineList) {
        this.communityTimeLineList = communityTimeLineList;
    }

    @Override
    public int getCount() {
        return communityTimeLineList.size();
    }

    @Override
    public Object getItem(int position) {
        return communityTimeLineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return communityTimeLineList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(this.layoutID, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.img_item);
            holder.username = convertView.findViewById(R.id.text_user_name);
            holder.body = convertView.findViewById(R.id.text_body);
            holder.createdDate = convertView.findViewById(R.id.text_created_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.createdDate.setText(communityTimeLineList.get(position).getCreatedDate());
        if (communityTimeLineList.get(position).getMember() != null) {
            Picasso.get().load(communityTimeLineList.get(position).getMember().getProfileImage()).resize(100, 100).into(holder.img);
        }
        holder.username.setText(communityTimeLineList.get(position).getMember().getName());
        holder.body.setText(communityTimeLineList.get(position).getBody());

        return convertView;
    }
}
