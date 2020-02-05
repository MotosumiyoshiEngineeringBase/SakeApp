package base.engineering.motosumiyoshi.sakeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import base.engineering.motosumiyoshi.sakeapp.httpclient.OpenPNEApiWrapper;

public class GroupSNSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sns);

        // 全コミュニティ(引数指定なし)を取得して表示する
        ListView listView = (ListView)findViewById(R.id.communityGroup);
        OpenPNEApiWrapper openPne = new OpenPNEApiWrapper(getApplicationContext());
        openPne.searchCommunity("", listView);

        // トップページに戻るボタン
        Button returnButton = findViewById(R.id.to_top);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
