package base.engineering.motosumiyoshi.sakeapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.adapter.ProductAdapter;
import base.engineering.motosumiyoshi.sakeapp.httpclient.ShopApiWrapper;
import base.engineering.motosumiyoshi.sakeapp.httpclient.VisionApiWrapper;
import base.engineering.motosumiyoshi.sakeapp.model.OCRBox;
import base.engineering.motosumiyoshi.sakeapp.model.Product;
import base.engineering.motosumiyoshi.sakeapp.view.CanvasImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // 戻るボタン押下時
        Button returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        });

        // Vision APIを利用して、画像から酒ラベルを特定
        executeAnalyze();
    }

    /**
     * 撮影画像から商品のラベルを抽出
     */
    private void executeAnalyze() {

        // 画像を表示
        Intent intent = getIntent();
        CanvasImageView imageView = findViewById(R.id.captured_image);
        byte[] cameraImage = intent.getByteArrayExtra("CAPTURED_IMAGE");
        imageView.setImageBitmap(cameraImage);

        // 画像からテキストを抽出
        VisionApiWrapper visionApi = new VisionApiWrapper();
        TextView capturedImageLabel = findViewById(R.id.label_captured_img);
        visionApi.execOCR(cameraImage, new VisionApiWrapper.VisionApiCallback() {
            @Override
            public void onSuccess(List<OCRBox> result) {
                imageView.setLineBoxList(result);
                List<String> keywords = new ArrayList<>();
                StringBuilder text = new StringBuilder();
                for (OCRBox box : result) {
                    keywords.add(box.getText());
                    text.append(box.getText() + " ");
                }
                capturedImageLabel.setText(text.toString());
                showProduct(keywords);
            }

            @Override
            public void onFail() {
                capturedImageLabel.setText(getString(R.string.not_detect_ocr_text));
            }
        });
    }

    /**
     * 商品検索結果を表示
     */
    private void showProduct(List<String> keywords) {
        ShopApiWrapper showApi = new ShopApiWrapper();
        showApi.searchSakeProduct(keywords, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                Gson gson = new Gson();
                List<Product> productList = new ArrayList<>();
                JsonObject jsonObj = gson.fromJson(responseBody, JsonObject.class);
                JsonArray jsonArray = jsonObj.get("Items").getAsJsonArray();
                if (jsonArray.size() > 0) {
                    for (JsonElement elem : jsonArray) {
                        JsonObject item = elem.getAsJsonObject().get("Item").getAsJsonObject();
                        String itemName = item.get("itemName").getAsString();
                        String price = item.get("itemPrice").getAsString();
                        String imgUrl = null;
                        for (JsonElement imgElem : item.get("smallImageUrls").getAsJsonArray()) {
                            imgUrl = imgElem.getAsJsonObject().get("imageUrl").getAsString();
                        }
                        String itemUrl = item.get("itemUrl").getAsString();
                        productList.add(new Product(itemName, price, imgUrl, itemUrl));
                        final Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(() -> {
                            ListView productListView = findViewById(R.id.product_view);
                            productListView.setAdapter(new ProductAdapter(
                                    getApplicationContext(), R.layout.product_list, productList));
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(
                                getApplicationContext(),
                                getString(R.string.not_hit_product),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
