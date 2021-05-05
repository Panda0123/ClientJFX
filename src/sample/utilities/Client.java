package sample.utilities;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;

public class Client {
    private static int port = 8080;
    private static String ipAddress = "localhost";
    private static Thread receiveThread = null;
    private static TextArea memoTextArea;
    private static TextArea chatTextArea;
    private static ClientPOJO clientPOJO;

    public static String username;

    public static void start(String ipAddress, int port, TextArea memoTextArea, TextArea chatTextArea) {
        Client.port = 8080;
        Client.ipAddress = ipAddress;
        Client.memoTextArea = memoTextArea;
        Client.chatTextArea = chatTextArea;
        receiveThread = new Thread(new ReceiveMessage());
        receiveThread.start();
    }

    public static void stop() {
        Thread tempThread = receiveThread;
        receiveThread = null;
        tempThread.interrupt();
    }

    public static void sendMessage(String message) throws IOException{
        Client.clientPOJO.getOut().writeUTF(message);
    }

    private static class ReceiveMessage implements Runnable {

        @Override
        public void run() {
            try {
                Socket sc = new Socket(ipAddress, port);
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                out.writeUTF(Client.username);
                Client.clientPOJO = new ClientPOJO(sc, out, in, Client.username);

                Thread thisThread = Thread.currentThread();
                String message;
                char prepend;
                while(thisThread == receiveThread) {
                    try {
                        message = in.readUTF();
                        prepend = message.charAt(0);
                        switch (prepend) {
                            case 'm':
                                memoTextArea.setText(message.substring(1, message.length()));
                                break;
                            case 'c':
                                chatTextArea.setText(chatTextArea.getText() + message.substring(1, message.length()) + "\n");
                                break;
                        }
                    } catch (InterruptedIOException ex) { }
                }
                sc.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static boolean accountExist(String username, String password) {
        // temporary
        return false;
    }
}
