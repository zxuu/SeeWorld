package com.iflytek.speech.util;

import com.iflytek.mscv5plusdemo.ReceiveBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Iterator;

public class HandleRecInfo {
    JSONObject jsonObject;
    String recv_buff = null;
    int msg;
    String thing;
    String[] things;
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
                jsonObject = new JSONObject(recv_buff);

                msg = (int) jsonObject.get("1");
                thing = (String) jsonObject.get("2");
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

    public String getThing() {
        return thing;
    }

    public String getThings() {
        Iterator it = jsonObject.keys();
        int i = 0;
        int length = 0;
        while (it.hasNext()) {
            length++;
        }
        things = new String[length-1];
        for (int j = 0; j < length; j++) {
            try {
                things[j] = (String) jsonObject.get(j + 2 + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String d = "";
        for (int j = 0; j < things.length; j++) {
            if (j == things.length) {
                d = d + things[j];
            } else
                d = d + things[j] + ",";
        }

        return d;
    }
}
