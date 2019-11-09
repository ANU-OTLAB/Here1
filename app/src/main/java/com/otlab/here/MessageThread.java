package com.otlab.here;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 생성자에 msg를 넣어주고 getMessage를 실행하거나
 * 객체를 생성 후 getMessage에 msg를 입력받아 서버로 부터 전송 받은 값 반환
 */
public class MessageThread implements Runnable {

    private ArrayList<String> sendMsgList;
    private String sendMsg = "id=by&pw=12345678";
    private String receiveMsg;
    private String address;
    private String str;

    public MessageThread(String address) {
        this.address = address;
    }

    public MessageThread(ArrayList<String> msgList, String address) {
        sendMsgList = msgList;
        this.address = address;
    }

    public void setMessage(ArrayList<String> msgList) {
        sendMsgList = msgList;
    }

    @Override
    public void run() {
        try {
            address = "http://iclab.andong.ac.kr/here/login.jsp";
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(sendMsg);
            osw.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF_8"));
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {

            }
        } catch (Exception e) {
            Log.d("why??????????", e.toString());
        }
    }

    public String getMessge(ArrayList<String> msgList) {
        sendMsgList = msgList;
        run();
        return receiveMsg;
    }

    public String getMessge() {
        run();
        return receiveMsg+"?";
    }
}
