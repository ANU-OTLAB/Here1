package com.otlab.here;

/**생성자에 msg를 넣어주고 getMessage를 실행하거나
 * 객체를 생성 후 getMessage에 msg를 입력받아 서버로 부터 전송 받은 값 반환
 * */
public class MessageThread implements Runnable {

    private String sendMsg;
    private String receiveMsg;
    private String addr;

    public MessageThread(String addr){
        this.addr = addr;
    }
    public MessageThread(String msg, String addr){
        sendMsg = msg;
        this.addr = addr;
    }

    @Override
    public void run() {

    }

    public String getMessge(String msg){
        sendMsg = msg;
        run();
        return receiveMsg;
    }
    public String getMessge(){
        return receiveMsg;
    }
}
