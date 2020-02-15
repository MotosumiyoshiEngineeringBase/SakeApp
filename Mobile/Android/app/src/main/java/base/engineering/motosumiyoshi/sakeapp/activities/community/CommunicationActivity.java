package base.engineering.motosumiyoshi.sakeapp.activities.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.httpclient.OpenPNEApiWrapper;

public class CommunicationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sns);

        // 全コミュニティ(引数指定なし)を取得して表示する
        ListView listView = (ListView)findViewById(R.id.communityGroup);
        OpenPNEApiWrapper openPne = new OpenPNEApiWrapper(getApplicationContext());
        openPne.searchCommunity("", listView);
        // コミュニティをクリックすると、タイムラインを表示します。
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getApplicationContext(), TimelineMessagesActivity.class);
        // clickされたpositionのidを取得する。このIDはコミュニティIDに該当する。
        long communityId = parent.getAdapter().getItemId(position);
        intent.putExtra("CommunityId", communityId);
        startActivity(intent);
    }
}
