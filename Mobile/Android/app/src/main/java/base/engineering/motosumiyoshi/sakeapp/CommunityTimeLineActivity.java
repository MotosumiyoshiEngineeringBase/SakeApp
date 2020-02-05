package base.engineering.motosumiyoshi.sakeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import base.engineering.motosumiyoshi.sakeapp.httpclient.OpenPNEApiWrapper;

/**
 * コミュニティ単位のタイムラインを表示するアクティビティクラスです。
 * */
public class CommunityTimeLineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_timeline);

        Intent intent = getIntent();
        long communityId = intent.getLongExtra("CommunityId", -1);

        // コミュニティIDを指定して、タイムラインを取得して表示する
        ListView listView = (ListView)findViewById(R.id.communityTimeLine);
        OpenPNEApiWrapper openPne = new OpenPNEApiWrapper(getApplicationContext());
        openPne.searchCommunityTimeLine(communityId, 20, listView);  //TODO 取得変数は可変にできるようにする

        // 戻るボタン
        Button returnButton = findViewById(R.id.to_foward);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
