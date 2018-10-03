package com.iflytek.speech.util;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.mscv5plusdemo.SendCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class HandleSend {
    RecognizerResult results = null;
    String text = null;
    String sn = null;
    String destination = null;
    int sendMsg;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    public HandleSend(RecognizerResult results) {
        this.results = results;
        text = JsonParser.parseIatResult(results.getResultString());

        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        destination = resultBuffer.toString();
        selectResulrBuf(destination);
    }

    private void selectResulrBuf(String result) {
        if (result.contains("位置")) {
            sendMsg = 1;
        } else if (result.contains("导航去")) {
            sendMsg = 2;
        } else if (result.contains("正前方")) {
            sendMsg = 3;
        } else if (result.contains("指示")) {
            sendMsg = 4;
        } else if (result.contains("红灯绿灯")) {
            sendMsg = 5;
        } else if (result.contains("几点")) {
            sendMsg = 6;
        }
    }

    public void handleSendInfo(SendCallBack sendCallBack) {
        sendCallBack.handleSend(sendMsg);
    }

    public String getDestination() {

        return destination.replace("导航去", "");
    }
}
