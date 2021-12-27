package net;

public class Playerone {
    public static void main(String[] args) {
        //线程版UDP收发信息
        new Thread(new InfoSend(7777, "localhost", 9999)).start();
        //new Thread(new InfoReceive(8888)).start();
    }
}