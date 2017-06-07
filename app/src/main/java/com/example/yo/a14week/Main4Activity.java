package com.example.yo.a14week;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Main4Activity extends AppCompatActivity {
    EditText et1, et2;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        msg = (TextView) findViewById(R.id.msg);

    }

    public void onClick(View v) {

        if (et1.getText().toString().length() == 0 || et2.getText().toString().length() == 0) {
            et1.setText("");
            et2.setText("");
            Toast.makeText(Main4Activity.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            thread.start();
        }
    }


    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                String userid = et1.getText().toString();
                String password = et2.getText().toString();
                URL url = new URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                String postData = "userid=" + URLEncoder.encode(userid) + "&password=" + URLEncoder.encode(password);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                InputStream inputStream;
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();
                final String result = loginResult(inputStream);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("FAIL"))
                            msg.setText("로그인이 실패했습니다.");
                        else
                            msg.setText(result + "님 로그인 성공");
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String loginResult(InputStream inputStream) throws IOException {
            String str = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            char[] buff = new char[512];
            int len = -1;
            while ((len = br.read(buff)) != -1)
                str += new String(buff, 0, len);
            str = str.substring(4);
            br.close();
            return str;
        }


    };


}
