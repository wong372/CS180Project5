import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * QuizThread
 *
 * A program containing the server functionality (file processing) of Project 5.
 *
 * @author Katie Testin, Aaron Basiletti, Ashley Wong, Saahil Sanghi, L21
 *
 * @version 5/1/22
 *
 */

public class QuizThread implements Runnable {
    Socket threadSocket;
    public QuizThread(Socket socket) {
        threadSocket = socket;
    }
    public void run() {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new PrintWriter(threadSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));

            while (true) {
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
                String LOGINFILENAME = "logins.txt";
                File logins = new File(LOGINFILENAME);
                logins.createNewFile();
                String selectedCourse = "";
                String selectedQuiz;


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

                    ServerSocket serverSocket = new ServerSocket(4243);
                    while (continue1) {
                        Socket socket = serverSocket.accept();
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer = new PrintWriter(socket.getOutputStream());

                        chosenOption = reader.readLine();
                        if (chosenOption.equals("courseSelect")) { // client has selected to select course
                            selectedCourse = reader.readLine();
                            ArrayList<String> totalQuizzes = t.displayQuizzes(selectedCourse);

                            int counter = 0;
                            String totalQuizzesString = "";
                            while (counter < totalQuizzes.size()) {
                                totalQuizzesString = totalQuizzesString + totalQuizzes.get(counter) + "/";
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
                            quiz.writeFileForSignUp(LOGINFILENAME, username, newPassword, role1); // write new password to file
                        } else if (chosenOption.equals("deleteAccount")) {
                            username = reader.readLine();
                            try {
                                boolean success = quiz.rewriteFile(String.valueOf(LOGINFILENAME), username);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            writer.close();
                            reader.close();
                        } else if (chosenOption.equals("createQuiz")) {
                            String manualOrFile = reader.readLine();
                            String nameOfQuiz = "";
                            ArrayList<String> quizQuestions = new ArrayList<String>();

                            if (manualOrFile.equals("File")) {
                                String filename = reader.readLine();

                                t.writeQuizFromFile(selectedCourse, filename);
                            } else {
                                nameOfQuiz = reader.readLine();
                                String randomizedOrNot = reader.readLine();
                                System.out.println("r" + randomizedOrNot);

                                ArrayList<String> newQuestions = new ArrayList<String>();
                                if (randomizedOrNot.equals("0")) {
                                    quizQuestions.add("1");
                                } else if (randomizedOrNot.equals("1")) {
                                    quizQuestions.add("2");
                                }

                                String quizQuestion = reader.readLine();

                                quizQuestions.add("\n" + quizQuestion);

                                String answerChoice = reader.readLine();

                                quizQuestions.add("/" + answerChoice);
                                //ended here

                                String anotherAnswerChoice = reader.readLine();

                                while (anotherAnswerChoice.equals("0")) {
                                    answerChoice = reader.readLine();

                                    quizQuestions.add("/" + answerChoice);
                                    anotherAnswerChoice = reader.readLine();
                                }

                                String anotherQuestion = reader.readLine();

                                while (anotherQuestion.equals("0")) { // if another question is yes
                                    quizQuestion = reader.readLine();

                                    quizQuestions.add("\n" + quizQuestion);

                                    answerChoice = reader.readLine();

                                    quizQuestions.add("/" + answerChoice);

                                    anotherAnswerChoice = reader.readLine();

                                    while (anotherAnswerChoice.equals("0")) {
                                        answerChoice = reader.readLine();

                                        quizQuestions.add("/" + answerChoice);

                                        anotherAnswerChoice = reader.readLine();
                                    }
                                    anotherQuestion = reader.readLine();
                                }
                            }
                            selectedCourse = reader.readLine();
                            t.createQuiz(selectedCourse, quizQuestions, nameOfQuiz);
                        } else if (chosenOption.equals("editQuiz")) {
                            selectedQuiz = reader.readLine();

                            ArrayList<String> allQuestions = t.displayQuestions(selectedQuiz, selectedCourse);

                            int counter2 = 0;
                            String allQString = "";
                            while (counter2 < allQuestions.size()) {
                                allQString = allQString + allQuestions.get(counter2) + ">";
                                counter2++;
                            }
                            writer.write(allQString);
                            writer.println();
                            writer.flush();

                            // now create new quiz
                            ArrayList<String> newQuestions = new ArrayList<String>();

                            String manualOrFile = reader.readLine();
                            String nameOfQuiz = "";

                            if (manualOrFile.equals("File")) {
                                String filename = reader.readLine();

                                t.writeQuizFromFile(selectedCourse, filename);
                            } else {
                                nameOfQuiz = reader.readLine();
                                String randomizedOrNot = reader.readLine();
                                System.out.println("r" + randomizedOrNot);

                                if (randomizedOrNot.equals("0")) {
                                    newQuestions.add("1");
                                } else if (randomizedOrNot.equals("1")) {
                                    newQuestions.add("2");
                                }

                                String quizQuestion = reader.readLine();

                                newQuestions.add("\n" + quizQuestion);

                                String answerChoice = reader.readLine();

                                newQuestions.add("/" + answerChoice);
                                //ended here

                                String anotherAnswerChoice = reader.readLine();

                                while (anotherAnswerChoice.equals("0")) {
                                    answerChoice = reader.readLine();

                                    newQuestions.add("/" + answerChoice);
                                    anotherAnswerChoice = reader.readLine();
                                }

                                String anotherQuestion = reader.readLine();

                                while (anotherQuestion.equals("0")) { // if another question is yes
                                    quizQuestion = reader.readLine();

                                    newQuestions.add("\n" + quizQuestion);

                                    answerChoice = reader.readLine();

                                    newQuestions.add("/" + answerChoice);

                                    anotherAnswerChoice = reader.readLine();

                                    while (anotherAnswerChoice.equals("0")) {
                                        answerChoice = reader.readLine();

                                        newQuestions.add("/" + answerChoice);

                                        anotherAnswerChoice = reader.readLine();
                                    }
                                    anotherQuestion = reader.readLine();
                                }
                            }
                            selectedCourse = reader.readLine();
                            t.editQuizQuestions(nameOfQuiz, newQuestions, selectedCourse);

                        } else if (chosenOption.equals("gradeQuiz")) {
                            String studentUsername = reader.readLine();
                            System.out.println(studentUsername);

                            String studentQuizName = reader.readLine();
                            System.out.println(studentQuizName);
                            int grade = 0;

                            ArrayList<String> studentResponses =
                                    t.displayStudentSubmission(studentUsername,
                                            studentQuizName, selectedCourse);
                            writer.write(studentResponses.get(0));
                            writer.println();
                            writer.flush();
                            System.out.println(studentResponses.get(0));
                            System.out.println(studentResponses.get(1));
                            System.out.println(studentResponses.get(2));
                            System.out.println(studentResponses.get(3));

                            writer.write(String.valueOf(studentResponses.size()));
                            writer.println();
                            writer.flush();

                            if (!(studentResponses.get(0).equals("None"))) {

                                int counter14 = 1;
                                int totalGrade = 0;
                                int numberQuestions = 0;

                                ArrayList<String> gradedResponses = new ArrayList<String>();
                                String print = "";
                                while (counter14 < studentResponses.size()) {
                                    String[] splitStudentResponses =
                                            (studentResponses.get(counter14)).split("/");
                                    print = print + "Question: " + counter14 + splitStudentResponses[0] + ">";

                                    int counter23 = 1;
                                    while (counter23 < splitStudentResponses.length) {
                                        print = print + splitStudentResponses[counter23] + ">";
                                        counter23++;
                                    }

                                    print = print + "What grade would you like to " +
                                            "give this question " +
                                            "between 0 and 100?";
                                    System.out.println(print);


                                    String initialGrade = "";

                                    writer.write(print);
                                    writer.println();
                                    writer.flush();

                                    String gradeString = reader.readLine();
                                    grade = Integer.parseInt(gradeString);
                                    print = "";
                                    totalGrade = totalGrade + grade;
                                    gradedResponses.add(studentResponses.get(counter14) + "/Grade: " +
                                            grade + "/");
                                    counter14++;
                                }

                                int totalGradedScore = totalGrade / numberQuestions;

                                t.gradeSubmission(gradedResponses, studentUsername,
                                        studentQuizName, selectedCourse, totalGradedScore);
                            }
                        }
                    }
                    writer.close();
                    reader.close();

                } else if (role1 == 2) { // loop for if the user is student
                    String chosenOption;
                    boolean continue1 = true;
                    ServerSocket serverSocket = new ServerSocket(4243);

                    while (continue1) {
                        Socket socket = serverSocket.accept();
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
                                totalQuizzesString = totalQuizzesString + totalQuizzes.get(counter) + "/";
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
        } catch (IOException exception) {

        }
    }

}
