package com.example.yo.a14week;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText etmsg;
    String SERVER_IP = "172.17.67.169";
    int SERVER_PORT = 200;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etmsg = (EditText) findViewById(R.id.etmsg);
    }
    public void onClick(View v){
        myThread.start();
    }

    Handler myHandler = new Handler();
    Thread myThread = new Thread() {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                String msg = "Client>> " + etmsg.getText().toString();
                outputStream.writeObject(msg);
                outputStream.flush();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                final String data = (String)inputStream.readObject();
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Server>> " + data, Toast.LENGTH_SHORT).show();
                    }
                });
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0,1,0," 실습2");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 1: intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
