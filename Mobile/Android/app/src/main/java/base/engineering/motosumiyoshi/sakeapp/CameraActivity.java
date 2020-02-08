package base.engineering.motosumiyoshi.sakeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            ImageView ivCamera = findViewById(R.id.ivCamera);
            ivCamera.setImageBitmap(bitmap);
        }
    }

    public void onCameraImageClick(View view) {
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                200);
    }
}
