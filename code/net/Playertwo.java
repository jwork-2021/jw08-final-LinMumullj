package net;

public class Playertwo {
    public static void main(String[] args) {
        new Thread(new InfoSend(5555, "localhost", 8888)).start();
        //new Thread(new InfoReceive(9999)).start();
    }
}