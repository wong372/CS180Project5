import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A Simple Server
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Homework 11 -- Walkthrough</p>
 *
 * @author Purdue CS
 * @version January 10, 2022
 */
public class QuizServer {
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(4242);
        serverSocket.setReuseAddress(true);
        Socket socket;
        String LOGINFILENAME = "logins.txt";
        while(true) {
            // wait for a client to connect
            socket = serverSocket.accept();
            QuizThread qc = new QuizThread(socket);

            new Thread(qc).start();
        }
    }

}