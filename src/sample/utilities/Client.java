package sample.utilities;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private static int port = 8080;
    private static String ipAddress = "localhost";
    private static boolean isOn = false;
    private static TextArea memoTextArea;
    private static TextArea chatTextArea;
    private static ClientPOJO clientPOJO;

    public static String username;

    public static void start(String ipAddress, int port, TextArea memoTextArea, TextArea chatTextArea) {
        isOn = true;
        Client.port = 8080;
        Client.ipAddress = ipAddress;
        Client.memoTextArea = memoTextArea;
        Client.chatTextArea = chatTextArea;
        new Thread(new ReceiveMessage()).start();
    }

    public static void stop() {
        if (isOn) {
            try {
                isOn = false;
                clientPOJO.getSocket().close();
            } catch (IOException ex) {}
        }
    }

    public static void sendMessage(String message) {
        try {
            Client.clientPOJO.getOut().writeUTF(message);
        } catch (SocketException ex) {
            System.out.println("Something is wrong: Server is probably offline");
            System.out.println("Make sure that the server is on then restart the app and try again");
        } catch (IOException ex) {ex.printStackTrace();}
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

                String message;
                char prefix;
                while(isOn) {
                    try {
                        message = in.readUTF();
                        prefix = message.charAt(0);
                        switch (prefix) {
                            case 'm':
                                memoTextArea.setText(message.substring(1, message.length()));
                                break;
                            case 'c':
                                chatTextArea.setText(chatTextArea.getText() + message.substring(1, message.length()) + "\n");
                                break;
                        }
                    } catch (SocketException ex) { }  // closing
                }
                sc.close();
            } catch (EOFException ex) {
                System.out.println("Server is deactivated");
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
