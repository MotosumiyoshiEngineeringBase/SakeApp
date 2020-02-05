package base.engineering.motosumiyoshi.sakeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import base.engineering.motosumiyoshi.sakeapp.httpclient.OpenPNEApiWrapper;

import static androidx.core.content.ContextCompat.startActivity;

public class GroupSNSActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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

        // トップページに戻るボタン
        Button returnButton = findViewById(R.id.to_foward);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getApplicationContext(), CommunityTimeLineActivity.class);
        // clickされたpositionのidを取得する。このIDはコミュニティIDに該当する。
        long communityId = parent.getAdapter().getItemId(position);
        intent.putExtra("CommunityId", communityId); //TODO 実は引数のidでよかったりする？
        startActivity(intent);
    }
}
