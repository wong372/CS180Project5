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
 * A server class that creates the thread to start the server and allow for concurrent users to connect.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Project 5 </p>
 *
 * @author Katie Testin, Aaron Basiletti, Ashley Wong, Saahil Sanghi, L21
 * @version May 1, 2022
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
