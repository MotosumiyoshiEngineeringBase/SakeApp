package base.engineering.motosumiyoshi.sakeapp.activities.community;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import base.engineering.motosumiyoshi.sakeapp.R;
import base.engineering.motosumiyoshi.sakeapp.data.ShareData;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private String inputUserName;
    private String inputUserPass;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShareData data = ShareData.getInstance();
        if (data.getApiKey() == null || data.getApiKey().isEmpty()) {
            setContentView(R.layout.activity_login);
            username = findViewById(R.id.username);
            password = findViewById(R.id.password);
            loginButton = findViewById(R.id.login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputUserName = username.getText().toString();
                    inputUserPass = password.getText().toString();
                    if (inputUserName == null || inputUserName.isEmpty()
                            || inputUserPass == null || inputUserPass.isEmpty()) {
                        return;
                    }

                    //ShareData data = ShareData.getInstance();
                    StringBuilder urlBuilder = new StringBuilder();
                    urlBuilder.append("https://motosumiengineer.pne.jp/member/login/authMode/MailAddress/?");
                    urlBuilder.append("authMailAddress[mail_address]=");
                    urlBuilder.append(inputUserName);
                    urlBuilder.append("&");
                    urlBuilder.append("authMailAddress[password]=");
                    urlBuilder.append(inputUserPass);

                    AsyncHttpRequest task = new AsyncHttpRequest(this);
                    task.setOnCallback(new AsyncHttpRequest.CallBackTask() {
                        @Override
                        public void CallBack(String result) {
                            super.CallBack(result);
                            // resultにはdoInBackgroundの返り値が入る。
                            // ここからAsyncTask処理後の処理を記述します。
                            if (result == null || result.isEmpty()) {
                                return;
                            }

                            if ("UNAUTHORIZED".equals(result)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setTitle("認証エラー");
                                builder.setMessage("メールアドレスもしくはパスワードが正しくありません。");
                                builder.setNeutralButton("閉じる", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }

                            //apiKeyをクラス間で参照できるようにセット
                            ShareData data = ShareData.getInstance();
                            data.setApiKey(result);

                            startActivity(
                                    new Intent(getApplicationContext(), CommunicationActivity.class));
                        }
                    });
                    task.execute(urlBuilder.toString());
                }
            });
        } else {
            // 認証情報が取得できてるのであればコミュニティ画面に繊維
            startActivity(
                    new Intent(getApplicationContext(), CommunicationActivity.class));
        }
    }
}

class AsyncHttpRequest extends AsyncTask<String, Void, String> {
    private View.OnClickListener mainActivity;
    private CallBackTask callbacktask;

    public AsyncHttpRequest(View.OnClickListener activity) {
        this.mainActivity = activity;
    }

    // このメソッドは必ずオーバーライドする必要があるよ
    @Override
    protected String doInBackground(String... url) {
        String strData = null;
        try {
            Document document = Jsoup.connect(url[0]).get();
            String html = Jsoup.parse(document.html()).select("script").get(0).html();
            html = html.substring(html.indexOf("{"), html.indexOf("}") + 1);
            JSONObject json = new JSONObject(html);
            strData = json.getString("apiKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);
    }

    // CallbackTaskの関数をオーバーライドするための関数
    public void setOnCallback(CallBackTask call) {
        callbacktask = call;
    }

    static class CallBackTask {
        protected void CallBack(String result) {
        }
    }
}
