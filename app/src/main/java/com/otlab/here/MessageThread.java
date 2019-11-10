package com.otlab.here;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 생성자에 msg를 넣어주고 getMessage를 실행하거나
 * 객체를 생성 후 getMessage에 msg를 입력받아 서버로 부터 전송 받은 값 반환
 */
public class MessageThread extends AsyncTask {

    private ArrayList<String> sendMsgList;
    private String sendMsg = "";
    private String receiveMsg;
    private String address;
    private String readMsg;

    public MessageThread(ArrayList<String> sendMsgList, String receiveMsg, String address){
        this.sendMsgList = sendMsgList;
        this.receiveMsg = receiveMsg;
        this.address = address;

        if(sendMsgList.size()%2 == 0 && sendMsgList.size() >=2){
            sendMsg += sendMsgList.get(0);
            sendMsg += "=";
            sendMsg += sendMsgList.get(1);
            for(int i=2 ; i<sendMsgList.size() ; i+=2){
                sendMsg += "&";
                sendMsg += sendMsgList.get(i);
                sendMsg += "=";
                sendMsg += sendMsgList.get(i+1);
            }
        }
        else{
            sendMsg = "";
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = null;
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

                    StringBuffer buffer = new StringBuffer();
                    while ((readMsg = reader.readLine()) != null) {
                        buffer.append(readMsg);
                    }
                    receiveMsg = buffer.toString();
                } else {

                }
            } catch (Exception e) {
                //Log.d("why??????????", e.toString());
            }
        }
        return receiveMsg;
    }
}
