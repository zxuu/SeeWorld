package com.iflytek.mscv5plusdemo;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TongxinDemo extends Activity implements View.OnClickListener{
    private EditText et_send;
    private Button bt_send;
    private TextView tv_recv;

    private String send_buff=null;
    private String recv_buff=null;

    private Handler handler = null;

    Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongxin_demo2);

        initView();

        handler = new Handler();
        //单开一个线程来进行socket通信
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    socket = new Socket("192.168.42.181" , 7654);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socket!=null) {
                        System.out.println("###################");
                        while (true) {      //循环进行收发
                            recv();
                        }
                    } else
                        System.out.println("socket is null");
            }
        }).start();
//        send();
//        mySend();
    }

    //不能在子线程中刷新UI，应为textView是主线程建立的
    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            tv_recv.append("\n"+recv_buff);
        }
    };

    private void send() {
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send_buff = et_send.getText().toString();
                        //向服务器端发送消息
                        System.out.println("------------------------");
                        OutputStream outputStream=null;
                        try {
                            outputStream = socket.getOutputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(outputStream!=null){
                            try {
                                outputStream.write(send_buff.getBytes());
                                System.out.println("1111111111111111111111");
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();

            }
        });
    }

    private void initView() {
        et_send = (EditText) findViewById(R.id.et_send2);
        findViewById(R.id.bt_send2).setOnClickListener(this);
        tv_recv = (TextView) findViewById(R.id.tv_recv2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send2:
                mySend();
                break;
        }
    }

    private void mySend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                send_buff = et_send.getText().toString();
                //向服务器端发送消息
                System.out.println("------------------------");
                OutputStream outputStream=null;
                try {
                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(outputStream!=null){
                    try {
                        outputStream.write(send_buff.getBytes());
                        System.out.println("1111111111111111111111");
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void recv() {

        //单开一个线程循环接收来自服务器端的消息
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputStream!=null){
            try {
                byte[] buffer = new byte[1024];
                int count = inputStream.read(buffer);//count是传输的字节数
                recv_buff = new String(buffer);//socket通信传输的是byte类型，需要转为String类型
                JSONObject jsonObject = new JSONObject(recv_buff);
                System.out.println(jsonObject.get("1"));
                recv_buff = (String) jsonObject.get("1");
                //Toast.makeText(this, (String) jsonObject.get("1"), Toast.LENGTH_SHORT).show();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //将受到的数据显示在TextView上
        if (recv_buff!=null){
            handler.post(runnableUi);

        }
    }
}
