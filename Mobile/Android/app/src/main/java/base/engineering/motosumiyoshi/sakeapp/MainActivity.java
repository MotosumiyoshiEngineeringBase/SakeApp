package base.engineering.motosumiyoshi.sakeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;

import base.engineering.motosumiyoshi.sakeapp.activities.community.CommunicationActivity;
import base.engineering.motosumiyoshi.sakeapp.activities.community.LoginActivity;
import base.engineering.motosumiyoshi.sakeapp.activities.videobroadcast.VideoBroadcastActivity;
import base.engineering.motosumiyoshi.sakeapp.viewhelper.BottomNavigationViewHelper;

import static base.engineering.motosumiyoshi.sakeapp.R.id.to_broadcast;
import static base.engineering.motosumiyoshi.sakeapp.R.id.to_camera;
import static base.engineering.motosumiyoshi.sakeapp.R.id.to_group;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String topPageUrl = "https://www.saketime.jp/ranking/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボトムナビゲーション
        BottomNavigationView bottomavigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.disableShiftMode(bottomavigation);
        bottomavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case to_group:
                        startActivity(
                                new Intent(getApplicationContext(), LoginActivity.class));
                        return true;
                    case to_camera:
                        startActivity(
                                new Intent(getApplicationContext(), CameraActivity.class));
                        return true;
                    case to_broadcast:
                        startActivity(
                                new Intent(getApplicationContext(), VideoBroadcastActivity.class));
                        return true;
                }
                return false;
            }
        });

        //WebView
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        webView.loadUrl(topPageUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case to_group:
                startActivity(
                        new Intent(getApplicationContext(), CommunicationActivity.class));
                return true;
            case to_camera:
                startActivity(
                        new Intent(getApplicationContext(), CameraActivity.class));
                return true;
            case to_broadcast:
                startActivity(
                        new Intent(getApplicationContext(), VideoBroadcastActivity.class));
                return true;
        }
        return false;
    }
}