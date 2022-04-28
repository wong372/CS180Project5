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
        String selectedCourse = "";
        String selectedQuiz;

        ServerSocket serverSocket = new ServerSocket(4242);
        Socket socket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        do {
            // Asks if user wants to log in or sign up. Only continues if login is successful and user continues.
            signUpOrLogIn = reader.readLine(); // first receive

            if (signUpOrLogIn.equals("Sign Up")) {
                boolean userExists = true;
                username = reader.readLine(); // second receive
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
                username = reader.readLine(); // third receive

                password = reader.readLine(); // fourth receive
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

        } while (!loginSuccess); // end of finished code
        writer.close();
        reader.close();
        if (role1 == 1) { // loop for if the user is a teacher

            String chosenOption;
            boolean continue1 = true;
            Teacher t = new Teacher();

            serverSocket = new ServerSocket(4243);
            while (continue1) {
                socket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());

                chosenOption = reader.readLine();
                if (chosenOption.equals("courseSelect")) { // client has selected to select course
                    selectedCourse = reader.readLine();
                    ArrayList<String> totalQuizzes = t.displayQuizzes(selectedCourse);

                    int counter = 0;
                    String totalQuizzesString = "";
                    while (counter < totalQuizzes.size()) {
                        totalQuizzesString = totalQuizzesString  + totalQuizzes.get(counter) + "/";
                        counter++;
                    }
                    writer.write(totalQuizzesString);
                    writer.println();
                    writer.flush();

                } else if (chosenOption.equals("courseCreate")) {
                    String courseName1 = reader.readLine();
                    File f = new File("AllCourses.txt");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f, true);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    PrintWriter pw = new PrintWriter(fos);
                    pw.write(courseName1 + "\n"); // Add the course name to the list of courses file
                    pw.close();
                } else if (chosenOption.equals("deleteQuiz")) {
                    String c = reader.readLine(); // read in the course
                    String q = reader.readLine(); // read in the quiz

                    t.deleteQuiz(c, q);
                } else if (chosenOption.equals("editPassword")) {
                    newPassword = reader.readLine();
                    try {
                        quiz.rewriteFile(LOGINFILENAME, username);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    quiz.writeFileForSignUp(LOGINFILENAME, username, newPassword, role1); // write the new password to file
                } else if (chosenOption.equals("deleteAccount")) {
                    username = reader.readLine();
                    try {
                        boolean success = quiz.rewriteFile(String.valueOf(LOGINFILENAME), username);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    writer.close();
                    reader.close();
                }
            }
            writer.close();
            reader.close();

        } else if (role1 == 2) { // loop for if the user is student
            String chosenOption;
            boolean continue1 = true;
            serverSocket = new ServerSocket(4243);

            while(continue1) {
                socket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());

                chosenOption = reader.readLine();

                Teacher t = new Teacher(); // allow access to teacher methods

                if (chosenOption.equals("courseSelect")) { // course select button has been pressed
                    selectedCourse = reader.readLine();
                    ArrayList<String> totalQuizzes = t.displayQuizzes(selectedCourse);

                    int counter = 0;
                    String totalQuizzesString = "";
                    while (counter < totalQuizzes.size()) {
                        totalQuizzesString = totalQuizzesString  + totalQuizzes.get(counter) + "/";
                        counter++;
                    }
                    writer.write(totalQuizzesString);
                    writer.println();
                    writer.flush();

                } else if (chosenOption.equals("editPassword")) { // edit password button has been pressed
                    newPassword = reader.readLine();
                    try {
                        quiz.rewriteFile(LOGINFILENAME, username);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    quiz.writeFileForSignUp(LOGINFILENAME, username, newPassword, role1); // write the new password to file

                } else if (chosenOption.equals("deleteAccount")) { //delete account button has been selected
                    username = reader.readLine();
                    try {
                        boolean success = quiz.rewriteFile(String.valueOf(LOGINFILENAME), username);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    writer.close();
                    reader.close();


                } else if (chosenOption.equals("takeQuiz")) { // take quiz button was pressed
                    selectedQuiz = reader.readLine();
                    selectedCourse = reader.readLine();

                    Student s = new Student(username);

                    ArrayList<String> studentSelectedQuestions = t.displayQuestions(selectedQuiz, selectedCourse);

                    int counter = 0;
                    String studentSelectedString = "";
                    while (counter < studentSelectedQuestions.size()) {
                        studentSelectedString = studentSelectedString + studentSelectedQuestions.get(counter) + ">";
                        counter++;
                    }
                    writer.write(studentSelectedString);
                    writer.println();
                    writer.flush();

                    if (!((studentSelectedQuestions.get(0)).equals("None"))) {
                        String manuallyOrFromFile = reader.readLine();
                        String filename;
                        if (manuallyOrFromFile.equals("File")) {
                            filename = reader.readLine();

                            ArrayList<String> studentResponses =
                                    s.getStudentResponsesFromFile(filename);

                            if (!((studentResponses.get(0)).equals("None"))) {
                                int c8 = 0;
                                ArrayList<String> studentAnswerAndQuestion = new ArrayList<String>();

                                while (c8 < studentSelectedQuestions.size()) {
                                    String[] questionAndAnswerSplitUp =
                                            (studentSelectedQuestions.get(c8)).split("/");

                                    String studentAnswer = studentResponses.get(c8);
                                    studentAnswerAndQuestion.add(studentSelectedQuestions.get(c8) +
                                            "/Answer: " + studentAnswer + "\n");
                                    c8++;
                                }
                                System.out.println(studentAnswerAndQuestion.get(0));

                                LocalDateTime date = LocalDateTime.now();

                                s.createStudentSubmission(studentAnswerAndQuestion,
                                        selectedQuiz, date,
                                        selectedCourse);
                            }
                        } else {
                            int counter8 = 0;
                            ArrayList<String> studentAnswerAndQuestion = new ArrayList<String>();
                            String print = "";
                            ArrayList<String> answerChoices = new ArrayList<String>();

                            while (counter8 < studentSelectedQuestions.size()) {
                                studentAnswerAndQuestion.add(reader.readLine() + "\n");
                                counter8++;
                                answerChoices.clear();
                            }

                            LocalDateTime date = LocalDateTime.now();

                            s.createStudentSubmission(studentAnswerAndQuestion, selectedQuiz,
                                    date, selectedCourse);
                        }
                    }

                } else if (chosenOption.equals("viewGrade")) { // view grade button has been pressed
                    String quizNameGrades = reader.readLine();

                    Student s = new Student(username);

                    ArrayList<String> gradedSubmissions = s.viewGradedSubmission(quizNameGrades,
                            selectedCourse);

                    if (!(gradedSubmissions.get(0).equals("None"))) {
                        writer.write("yes");
                        writer.println();
                        writer.flush();

                        int counter21 = 0;
                        String print = "";

                        while (counter21 < gradedSubmissions.size()) {
                            String[] questionSplitUp =
                                    (gradedSubmissions.get(counter21)).split("/");
                            print = "Question: " + questionSplitUp[0] + "\n";

                            // loop through all of the answer choices
                            int counter22 = 1;
                            while (counter22 < questionSplitUp.length) {
                                print = print + questionSplitUp[counter22] + "\n";
                                counter22++;
                            }
                            counter21++;
                            writer.write(print);
                            writer.println();
                            writer.flush();
                        }
                    } else {
                        writer.write("no");
                        writer.println();
                        writer.flush();
                    }
                }
                }
            writer.close();
            reader.close();
        }
    }

}