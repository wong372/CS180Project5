import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
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
        String LOGINFILENAME = "logins.txt";

        Quiz quiz = new Quiz();

        //instantiating variables
        //Scanner scan = new Scanner(System.in);
        String signUpOrLogIn = "";
        String role = "";
        int role1 = 0;
        String username = null;
        String password;
        String newPassword;
        int editDeleteContinue = 0;
        boolean accountContinue = false;
        boolean courseContinue = false;
        boolean validNum = false;
        boolean loginSuccess = false;
        int selectOrCreate = 0;
        File logins = new File(LOGINFILENAME);
        logins.createNewFile();
        int createDeleteEditQuiz = 0;
        int courseSelectAddQuit = 0;
        int continueToAnotherQuestion = 0;
        int anotherAnswerChoice = 0;
        int randomizedOrNot = 0;
        int anotherQuestion = 0;
        int selectOrCreate1 = 0;
        int manuallyOrFromFile = 0;

        ServerSocket serverSocket = new ServerSocket(4242);

        Socket socket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        do {
            // Asks if user wants to log in or sign up. Only continues if login is successful and user continues.
            signUpOrLogIn = reader.readLine();

            if (signUpOrLogIn.equals("Sign Up")) {

                boolean userExists = true;
                username = reader.readLine();
                // Checking if user already exists in LOGINFILENAME file.
                userExists = quiz.readFileForSignUp(LOGINFILENAME, username);

                writer.write(String.valueOf(userExists));
                writer.println(); // send the value back to the client
                writer.flush();

                if (userExists) {
                    accountContinue = false;
                } else {
                    password = reader.readLine();

                    role = reader.readLine();
                    if (role.equals("Student")) {
                        role1 = 2;
                    } else if (role.equals("Teacher")) {
                        role1 = 1;
                    }
                    // Writing of credentials to the LOGINFILENAME file.

                    quiz.writeFileForSignUp(LOGINFILENAME, username, password, role1);
                    accountContinue = false;
                    loginSuccess = false;

                    System.out.println("Account created!");
                }

            } else if (signUpOrLogIn.equals("Log in")) {
                username = reader.readLine();

                password = reader.readLine();
                role1 = quiz.readFileForLogin(LOGINFILENAME, username, password);
                role = String.valueOf(role1);

                writer.write(role);
                writer.println(); // send the role back to the client
                writer.flush();

                if (role1 == 0) {
                    loginSuccess = false;
                } else {
                    loginSuccess = true;
                }
            }

            /*
            Once logged in, user is prompted to continue or edit/delete account. Only continues if they select continue.
             */
            if (loginSuccess) {
                String editDeleteContinueString = reader.readLine();

                if (editDeleteContinueString.equals("Continue")) {
                    editDeleteContinue = 1;
                } else if (editDeleteContinueString.equals("Edit")) {
                    editDeleteContinue = 2;
                } else if (editDeleteContinueString.equals("Delete")) {
                    editDeleteContinue = 3;
                }

                writer.write(editDeleteContinue);
                writer.println();
                writer.flush();

                if (editDeleteContinue == 1) {
                    accountContinue = true;
                } else if (editDeleteContinue == 2) { // If user wants to edit account
                    newPassword = reader.readLine();

                    quiz.rewriteFile(LOGINFILENAME, username);
                    quiz.writeFileForSignUp(LOGINFILENAME, username, newPassword, role1);


                    accountContinue = false;
                } else if (editDeleteContinue == 3) {
                    boolean success = quiz.rewriteFile(LOGINFILENAME, username);

                    JOptionPane.showMessageDialog(null, "Account was successfully deleted!",
                            "Account Settings", JOptionPane.INFORMATION_MESSAGE);

                    accountContinue = false;
                }
            }
        } while (!accountContinue); // end of finished code
    }
}