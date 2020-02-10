package base.engineering.motosumiyoshi.sakeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button snsButton = findViewById(R.id.to_group);
        snsButton.setOnClickListener(v -> {
            startActivity(
                new Intent(getApplicationContext(), CommunicationActivity.class));
            });

        Button cameraButton = findViewById(R.id.to_camera);
        cameraButton.setOnClickListener(v -> {
            startActivity(
                    new Intent(getApplicationContext(), CameraActivity.class));
        });
    }
}