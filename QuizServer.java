import java.io.*;
import java.net.*;
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
        Quiz quiz = new Quiz();

        System.out.println("Waiting for the client to connect...");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        String usernameClient = reader.readLine();
        String passwordClient = reader.readLine();
        String roleClient = reader.readLine();

        quiz.writeFileForSignUp("logins.txt", usernameClient, passwordClient, roleClient);

        System.out.println("Account created!");

        String response = usernameClient.replaceAll(" ",",");

        writer.write(response);
        writer.println();
        writer.flush();
        System.out.printf("Sent to client:\n%s\n", response);

        writer.close();
        reader.close();
    }
}

