package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {
    private EditText data1, data2;
    private Button getBtn;
    private TextView result;
    private Button btnStart, btnEnd;
    private Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data1 = (EditText)findViewById(R.id.editText);
        data2 = (EditText)findViewById(R.id.editText2);
        btnStart = (Button)findViewById(R.id.btn_start);
        btnEnd = (Button)findViewById(R.id.btn_end);
        NetworkUtil.setNetworkPolicy();
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PHPRequest request = new PHPRequest("http://192.168.43.196:80/PHP_connection.php");

                    String res2 = request.PhPtest(String.valueOf(data1.getText()),String.valueOf(data2.getText()));
                    Log.i("!!!!!!!!!!!!!!!!!!","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                    if(result.equals("1")){
//                        Toast.makeText(getApplication(),"들어감",Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(getApplication(),"안 들어감",Toast.LENGTH_SHORT).show();
//                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });
        String test = "http://172.20.10.10/MediumServer/SelectAllPost.php";

        URLConnector task = new URLConnector(test);



        task.start();



        try{

            task.join();

            System.out.println("waiting... for result");

        }

        catch(InterruptedException e){



        }



        String result1 = task.getResult();



        System.out.println(result1);
        result = (TextView)findViewById(R.id.result);



        getBtn = (Button)findViewById(R.id.getBtn);
        getBtn.setOnClickListener((view) -> { getWebsite(); });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                intent.putExtra("result", result.getText().toString());
                Log.d("@@@@@@@@@@@@@@@", result.getText().toString());
                startService(intent);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
            }
        });





    }

    private void getWebsite() {
        new Thread((Runnable) () -> {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect("http://192.168.43.194:5004").get();
                    String title = doc.title();
                    String links = doc.select("h4").text();
                    builder.append(links);
                } catch (IOException e) {
                    builder.append("Error");
            }

            runOnUiThread(() -> {
                result.setText(builder.toString());
//                Intent intent1 = new Intent(MainActivity.this,MyService.class);
//                intent1.putExtra("result", result.toString());
            });
        }).start();
    }
}
