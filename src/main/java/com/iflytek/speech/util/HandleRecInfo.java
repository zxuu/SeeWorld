package com.iflytek.speech.util;

import com.iflytek.mscv5plusdemo.ReceiveBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HandleRecInfo {
    String recv_buff = null;
    int msg;

    public HandleRecInfo(Socket socket) {
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

                msg = (int) jsonObject.get("1");
                //Toast.makeText(this, (String) jsonObject.get("1"), Toast.LENGTH_SHORT).show();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleInfo(ReceiveBack receiveBack) {
        receiveBack.receiveSelect(msg);
    }
}
