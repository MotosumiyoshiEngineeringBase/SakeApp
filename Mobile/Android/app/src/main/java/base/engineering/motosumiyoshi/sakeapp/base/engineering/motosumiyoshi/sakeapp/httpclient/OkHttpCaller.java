package base.engineering.motosumiyoshi.sakeapp.base.engineering.motosumiyoshi.sakeapp.httpclient;

import android.os.Handler;
import android.view.View;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class OkHttpCaller {
    private Handler mHandler = null;

    OkHttpCaller () {
        mHandler = new Handler();
    }

    public void call (String url, View targetView, String methodName) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO エラーハンドリングの方法を標準化する
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResponseReceived(e.getMessage(), targetView, methodName);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResponseReceived(responseBody, targetView, methodName);
                    }
                });
            }
        });
    }

    public abstract void onResponseReceived (String responseBody, View targetView, String methodName);
}
