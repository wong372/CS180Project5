import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * A client class the takes in all the user inputs for the quiz program and sends them to the server for processing.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Project 5 </p>
 *
 * @author Katie Testin, Aaron Basiletti, Ashley Wong, Saahil Sanghi, L21
 * @version May 1, 2022
 */
public class QuizClient {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        // set up the client

        Socket socket = new Socket("localhost", 4242);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        PrintWriter writer = new PrintWriter(socket.getOutputStream());

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


        //welcome, sign up or log in
        JOptionPane.showMessageDialog(null, "Welcome!",
                null, JOptionPane.INFORMATION_MESSAGE);

        do {
            // Asks if user wants to log in or sign up. Only continues if login is successful and user continues.
            String[] options1 = new String[2];
            options1[0] = "Sign Up";
            options1[1] = "Log in";
            signUpOrLogIn = (String) JOptionPane.showInputDialog(null,
                    "Choose an option to continue",
                    "University Card", JOptionPane.QUESTION_MESSAGE, null, options1,
                    options1[1]);

            writer.write(signUpOrLogIn); // first send
            writer.println();
            writer.flush();

            if (signUpOrLogIn.equals("Sign Up")) {
                do {
                    username = JOptionPane.showInputDialog(null, "What do you want your " +
                                    "username to be?",
                            "Sign Up", JOptionPane.QUESTION_MESSAGE);
                    if ((username == null) || (username.isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Username cannot be empty!",
                                "University Card",
                                JOptionPane.ERROR_MESSAGE);

                    } //end if

                } while ((username == null) || (username.isEmpty()));

                writer.write(username); // second send
                writer.println();
                writer.flush();

                boolean userExists = Boolean.parseBoolean(reader.readLine());

                if (userExists) {
                    JOptionPane.showMessageDialog(null, "This user already exists!",
                            null, JOptionPane.INFORMATION_MESSAGE);
                    accountContinue = false;
                } else {
                    do {
                        do {
                            password = JOptionPane.showInputDialog(null, "What do you want your " +
                                            "password to be?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((password == null) || (password.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Password cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((password == null) || (password.isEmpty()));

                        if (password.contains(",")) {
                            JOptionPane.showMessageDialog(null, "Password cannot contain a" +
                                            "comm!\nPlease enter again.",
                                    null, JOptionPane.ERROR_MESSAGE);
                        }
                    } while (password.contains(","));

                    writer.write(password); // third send
                    writer.println(); // write the password to the server
                    writer.flush();

                    String[] options2 = new String[2];
                    options2[0] = "Teacher";
                    options2[1] = "Student";
                    role = (String) JOptionPane.showInputDialog(null,
                            "Choose an option to continue",
                            "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                            options2[1]);
                    // Writing of credentials to the LOGINFILENAME file.

                    writer.write(role); // fourth send
                    writer.println(); // write the role to the server
                    writer.flush();

                    accountContinue = false;
                    loginSuccess = false;
                }

            } else if (signUpOrLogIn.equals("Log in")) {
                do {
                    username = JOptionPane.showInputDialog(null, "What is your " +
                                    "username?",
                            "Log in", JOptionPane.QUESTION_MESSAGE);
                    if ((username == null) || (username.isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Username cannot be empty!",
                                "University Card",
                                JOptionPane.ERROR_MESSAGE);
                    } //end if
                } while ((username == null) || (username.isEmpty()));

                username = username.toLowerCase();

                writer.write(username); // fifth send
                writer.println(); // write the username to the server
                writer.flush();

                do {
                    password = JOptionPane.showInputDialog(null, "What is your " +
                                    "password?",
                            "Log in", JOptionPane.QUESTION_MESSAGE);
                    if ((password == null) || (password.isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty!",
                                "University Card",
                                JOptionPane.ERROR_MESSAGE);

                    } //end if

                } while ((password == null) || (password.isEmpty()));

                writer.write(password); // sixth send
                writer.println(); // write the password to the server
                writer.flush();

                String r = reader.readLine();
                System.out.println(r);
                role1 = Integer.parseInt(r);

                if (role1 == 0) {
                    JOptionPane.showMessageDialog(null, "Username or password is incorrect",
                            "Log in", JOptionPane.ERROR_MESSAGE);
                    loginSuccess = false;
                } else {
                    loginSuccess = true;
                    accountContinue = true;
                }
            }

    /*
    Once logged in, user is prompted to continue or edit/delete account. Only continues if they select continue.
     */

        } while (!loginSuccess) ; // end of finished code
        writer.close();
        reader.close();
        if (role1 == 1) {
            SwingUtilities.invokeLater(new TeacherGUI(username, role1)); // Teacher GUI
        } else if (role1 == 2) {
            SwingUtilities.invokeLater(new StudentGUI(username, role1)); //Student GUI
        }

    }
}
