import javax.swing.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * A Simple Client.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Homework 11 -- Walkthrough</p>
 *
 * @author Purdue CS
 * @version January 10, 2022
 */
public class QuizClient {
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        // set up the client
        Scanner scan = new Scanner(System.in);

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
                    options1[0]);


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

                boolean userExists = true;

                username = username.toLowerCase();

                // Checking if user already exists in LOGINFILENAME file.
                userExists = quiz.readFileForSignUp(LOGINFILENAME, username);
                if (userExists) {
                    JOptionPane.showMessageDialog(null, "This user already exists!",
                            null, JOptionPane.INFORMATION_MESSAGE);
                    accountContinue = false;
                } else {
                    writer.write(username);
                    writer.println();
                    writer.flush();
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
                    writer.write(password);
                    writer.println();
                    writer.flush();

                    String[] options2 = new String[2];
                    options2[0] = "Teacher";
                    options2[1] = "Student";
                    role = (String) JOptionPane.showInputDialog(null,
                            "Choose an option to continue",
                            "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                            options2[0]);
                    // Writing of credentials to the LOGINFILENAME file.
                    writer.write(role);
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

                role1 = quiz.readFileForLogin(LOGINFILENAME, username, password);
                if (role1 == 0) {
                    System.out.println("Username or password is incorrect.");
                    loginSuccess = false;
                } else {
                    loginSuccess = true;
                    writer.write(username);
                    writer.println(); // send the username to server
                    writer.flush();

                    writer.write(password);
                    writer.println(); // send the password to server
                    writer.flush();
                }
            }

    /*
    Once logged in, user is prompted to continue or edit/delete account. Only continues if they select continue.
     */
            if (loginSuccess) {
                do {
                    try {
                        System.out.println("Would you like to edit or delete your account?\n1.Continue\n" +
                                "2.Edit Your Account\n" +
                                "3.Delete Your Account");
                        editDeleteContinue = scan.nextInt();

                        if (editDeleteContinue != 1 && editDeleteContinue != 2 && editDeleteContinue != 3) {
                            System.out.println("Please enter a valid input.");
                            validNum = false;
                        } else {
                            validNum = true;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid input.");
                        validNum = false;
                    }

                    scan.nextLine();

                } while (!validNum);

                if (editDeleteContinue == 1) {
                    accountContinue = true;
                } else if (editDeleteContinue == 2) { // If user wants to edit account
                    do {
                        System.out.println("Enter new password");
                        newPassword = scan.nextLine();

                        if (newPassword.contains(",")) {
                            System.out.println("Password cannot contain a comma.\nPlease try again.");
                        }
                    } while (newPassword.contains(","));

                    //quiz.rewriteFile(LOGINFILENAME, username);
                    //quiz.writeFileForSignUp(LOGINFILENAME, username, newPassword, role);

                    System.out.println("New Password: " + newPassword);

                    accountContinue = false;
                } else if (editDeleteContinue == 3) {
                    //boolean success = quiz.rewriteFile(LOGINFILENAME, username);
                    System.out.println("Account was successfully deleted!");
                    accountContinue = false;
                }
            }
        } while (!accountContinue);

        if (accountContinue) {
            if (role1 == 1) { // IF THE USER IS A TEACHER
                try {
                    do {
                        System.out.println("What would you like to do?\n1.Create Course\n2.Select Course\n3.Exit");
                        try {
                            selectOrCreate1 = scan.nextInt();
                            if (selectOrCreate1 != 1 && selectOrCreate1 != 2 && selectOrCreate != 3) {
                                System.out.println("Please choose a valid option.");
                                validNum = false;
                            } else {
                                validNum = true;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please choose a valid option.");
                            validNum = false;
                        }
                        scan.nextLine();
                    } while (!validNum);

                    while (selectOrCreate1 == 1 || selectOrCreate1 == 2 || selectOrCreate1 == 3) {
                        if (selectOrCreate1 == 1) { //Teacher has picked to create course
                            System.out.println("What is the name of the course you want to create?");
                            String courseName = scan.nextLine();
                            File f = new File("AllCourses.txt");
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(f, true);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            PrintWriter pw = new PrintWriter(fos);
                            pw.write(courseName + "\n"); // Add the course name to the list of courses file
                            pw.close();

                            do {
                                System.out.println("What would you like to do?\n1.Create Course\n2.Select Course" +
                                        "\n3.Exit");
                                try {
                                    selectOrCreate1 = scan.nextInt();
                                    if (selectOrCreate1 != 1 && selectOrCreate1 != 2 && selectOrCreate1 != 3) {
                                        System.out.println("Please choose a valid option.");
                                        validNum = false;
                                    } else {
                                        validNum = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please choose a valid option.");
                                    validNum = false;
                                }
                                scan.nextLine();
                            } while (!validNum);

                        } else if (selectOrCreate1 == 2) { // Teacher has picked to select course
                            Teacher t = new Teacher();
                            ArrayList<String> totalCourses = t.displayCourses(); // display all the current
                            // courses to select
                            if (!(totalCourses.get(0).equals("None"))) {
                                int counter1 = 0;
                                System.out.println("All the current courses: ");
                                while (counter1 < totalCourses.size()) {
                                    System.out.println(totalCourses.get(counter1));
                                    counter1++;
                                }
                                System.out.println("Which course would you like to select?");
                                String selectedCourse = scan.nextLine();

                                if (totalCourses.contains(selectedCourse)) {
                                    do {
                                        System.out.println("What would you like to do in this course?\n" +
                                                "1.Create Quiz\n" +
                                                "2.Delete Quiz\n3.Edit Quiz\n4.View and Grade Student Submissions");

                                        try {
                                            createDeleteEditQuiz = scan.nextInt();
                                            if (createDeleteEditQuiz != 1 && createDeleteEditQuiz != 2 &&
                                                    createDeleteEditQuiz != 3 && createDeleteEditQuiz != 4) {
                                                System.out.println("Please choose a valid option.");
                                                validNum = false;
                                            } else {
                                                validNum = true;
                                            }
                                        } catch (InputMismatchException e) {
                                            System.out.println("Please choose a valid option.");
                                            validNum = false;
                                        }
                                        scan.nextLine();
                                    } while (!validNum);

                                    if (createDeleteEditQuiz == 1) { //Teacher has selected to create a quiz
                                        ArrayList<String> quizQuestions = new ArrayList<String>();

                                        String nameOfQuiz = "";

                                        do {
                                            System.out.println("Would you like to enter quiz manually or " +
                                                    "import from file?" +
                                                    "\n1.File\n2.Manually");

                                            try {
                                                manuallyOrFromFile = scan.nextInt();
                                                if (manuallyOrFromFile != 1 && manuallyOrFromFile != 2) {
                                                    System.out.println("Please choose a valid option.");
                                                    validNum = false;
                                                } else {
                                                    validNum = true;
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println("Please choose a valid option.");
                                                validNum = false;
                                            }
                                            scan.nextLine();
                                        } while (!validNum);

                                        if (manuallyOrFromFile == 1) {
                                            System.out.println("What is the name of the file?\nThe first line should" +
                                                    "be the title of the quiz\nAll remaining lines should be" +
                                                    "questions with answers on the same line, separated by commas");
                                            String filename = scan.nextLine();
                                            t.writeQuizFromFile(selectedCourse, filename);
                                        } else {
                                            System.out.println("What is the name of the quiz you want to create?");
                                            nameOfQuiz = scan.nextLine();

                                            do {
                                                System.out.println("Would you like the quiz to be randomized?" +
                                                        "\n1.Yes\n2.No");
                                                try {
                                                    randomizedOrNot = scan.nextInt();
                                                    if (randomizedOrNot != 1 && randomizedOrNot != 2) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    } else {
                                                        validNum = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please choose a valid option.");
                                                    validNum = false;
                                                }
                                                scan.nextLine();
                                            } while (!validNum);


                                            ArrayList<String> newQuestions = new ArrayList<String>();
                                            if (randomizedOrNot == 1) {
                                                quizQuestions.add(String.valueOf(randomizedOrNot));
                                            } else if (randomizedOrNot == 2) {
                                                quizQuestions.add(String.valueOf(randomizedOrNot));
                                            }

                                            System.out.println("What is the first question on the quiz?");
                                            String quizQuestion = scan.nextLine();
                                            quizQuestions.add("\n" + quizQuestion);
                                            System.out.println("What is the first answer choice?");
                                            String answerChoice = scan.nextLine();
                                            quizQuestions.add("/" + answerChoice);

                                            do {
                                                System.out.println("Is there another answer choice?\n1.Yes\n2.No");
                                                try {
                                                    anotherAnswerChoice = scan.nextInt();
                                                    if (anotherAnswerChoice != 1 && anotherAnswerChoice != 2) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    } else {
                                                        validNum = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please choose a valid option.");
                                                    validNum = false;
                                                }
                                                scan.nextLine();
                                            } while (!validNum);


                                            while (anotherAnswerChoice == 1) {
                                                System.out.println("What is the next answer choice?");
                                                answerChoice = scan.nextLine();
                                                quizQuestions.add("/" + answerChoice);
                                                do {
                                                    System.out.println("Is there another answer choice?\n1.Yes\n2.No");
                                                    try {
                                                        anotherAnswerChoice = scan.nextInt();
                                                        if (anotherAnswerChoice != 1 && anotherAnswerChoice != 2) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        } else {
                                                            validNum = true;
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    }
                                                    scan.nextLine();
                                                } while (!validNum);
                                            }

                                            do {
                                                System.out.println("Would you like to add another " +
                                                        "question?\n1.Yes\n2.No");
                                                try {
                                                    anotherQuestion = scan.nextInt();
                                                    if (anotherQuestion != 1 && anotherQuestion != 2) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    } else {
                                                        validNum = true;
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Please choose a valid option.");
                                                    validNum = false;
                                                }
                                                scan.nextLine();
                                            } while (!validNum);

                                            while (anotherQuestion == 1) {
                                                System.out.println("What is the next question on the quiz?");
                                                quizQuestion = scan.nextLine();
                                                quizQuestions.add("\n" + quizQuestion);
                                                System.out.println("What is the first answer choice?");
                                                answerChoice = scan.nextLine();
                                                quizQuestions.add("/" + answerChoice);

                                                do {
                                                    System.out.println("Is there another answer choice?\n1.Yes\n2.No");
                                                    try {
                                                        anotherAnswerChoice = scan.nextInt();
                                                        if (anotherAnswerChoice != 1 && anotherAnswerChoice != 2) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        } else {
                                                            validNum = true;
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    }
                                                    scan.nextLine();
                                                } while (!validNum);

                                                while (anotherAnswerChoice == 1) {
                                                    System.out.println("What is the next answer choice?");
                                                    answerChoice = scan.nextLine();
                                                    quizQuestions.add("/" + answerChoice);

                                                    do {
                                                        System.out.println("Is there another answer " +
                                                                "choice?\n1.Yes\n2.No");
                                                        try {
                                                            anotherAnswerChoice = scan.nextInt();
                                                            if (anotherAnswerChoice != 1 && anotherAnswerChoice != 2) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            } else {
                                                                validNum = true;
                                                            }
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        }
                                                        scan.nextLine();
                                                    } while (!validNum);
                                                }
                                                do {
                                                    System.out.println("Would you like to add another question?" +
                                                            "\n1.Yes\n2.No");
                                                    try {
                                                        anotherQuestion = scan.nextInt();
                                                        if (anotherQuestion != 1 && anotherQuestion != 2) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        } else {
                                                            validNum = true;
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    }
                                                    scan.nextLine();
                                                } while (!validNum);
                                            }
                                        }
                                        t.createQuiz(selectedCourse, quizQuestions, nameOfQuiz);
                                    } else if (createDeleteEditQuiz == 2) { // Teacher has selected to delete quiz

                                        ArrayList<String> totalQuizzes = t.displayQuizzes(selectedCourse);
                                        // display all the current quizzes to select
                                        if (!(totalQuizzes.get(0).equals("None"))) {
                                            int counter5 = 0;
                                            System.out.println("All the current quizzes: ");
                                            while (counter5 < totalQuizzes.size()) {
                                                System.out.println(totalQuizzes.get(counter5));
                                                counter5++;
                                            }
                                            System.out.println("Which quiz would you like to delete?");
                                            String selectedQuiz = scan.nextLine();

                                            t.deleteQuiz(selectedCourse, selectedQuiz);
                                        } else {
                                            System.out.println("There are no current quizzes.");
                                        }
                                    } else if (createDeleteEditQuiz == 3) { // Teacher has selected to edit quiz
                                        ArrayList<String> totalQuizzes = t.displayQuizzes(selectedCourse);
                                        // display all the current quizzes to select
                                        if (!(totalQuizzes.get(0).equals("None"))) {
                                            int counter5 = 0;
                                            System.out.println("All the current quizzes: ");
                                            while (counter5 < totalQuizzes.size()) {
                                                System.out.println(totalQuizzes.get(counter5));
                                                counter5++;
                                            }
                                            System.out.println("Which quiz would you like to edit?");
                                            String selectedQuiz = scan.nextLine();

                                            if (totalQuizzes.contains(selectedQuiz)) {

                                                ArrayList<String> allQuestions =
                                                        t.displayQuestions(selectedQuiz, selectedCourse);

                                                System.out.println("The current questions are: ");

                                                int counter19 = 0;

                                                while (counter19 < allQuestions.size()) {
                                                    String[] questionAndAnswerSplitUp2 =
                                                            (allQuestions.get(counter19)).split("/");
                                                    System.out.println("Question: " + questionAndAnswerSplitUp2[0]);

                                                    // loop through all of the answer choices
                                                    int counter20 = 1;
                                                    while (counter20 < questionAndAnswerSplitUp2.length) {
                                                        System.out.println(counter20 + ") " +
                                                                questionAndAnswerSplitUp2[counter20]);
                                                        counter20++;
                                                    }
                                                    counter19++;
                                                }

                                                do {
                                                    System.out.println("Would you like to enter quiz manually or " +
                                                            "import from file?" +
                                                            "\n1.File\n2.Manually");

                                                    try {
                                                        manuallyOrFromFile = scan.nextInt();
                                                        if (manuallyOrFromFile != 1 && manuallyOrFromFile != 2) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        } else {
                                                            validNum = true;
                                                        }
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Please choose a valid option.");
                                                        validNum = false;
                                                    }
                                                    scan.nextLine();
                                                } while (!validNum);

                                                if (manuallyOrFromFile == 1) {
                                                    System.out.println("What is the name of the file?\nThe first " +
                                                            "line should" +
                                                            "be the title of the quiz\nAll remaining " +
                                                            "lines should be " +
                                                            "questions with answers on the same line, separated " +
                                                            "by commas");
                                                    String filename = scan.nextLine();
                                                    t.writeQuizFromFile(selectedCourse, filename);
                                                } else {

                                                    ArrayList<String> newQuestions = new ArrayList<String>();

                                                    do {
                                                        System.out.println("Would you " +
                                                                "like the quiz to be randomized?\n1.Yes\n2.No");
                                                        try {
                                                            randomizedOrNot = scan.nextInt();
                                                            if (randomizedOrNot != 1 && randomizedOrNot != 2) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            } else {
                                                                validNum = true;
                                                            }
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        }
                                                        scan.nextLine();
                                                    } while (!validNum);

                                                    if (randomizedOrNot == 1) {
                                                        newQuestions.add(String.valueOf(randomizedOrNot));
                                                    } else if (randomizedOrNot == 2) {
                                                        newQuestions.add(String.valueOf(randomizedOrNot));
                                                    }

                                                    System.out.println("What do you want the new " +
                                                            "first question to be?");
                                                    String newQuestion = scan.nextLine();
                                                    newQuestions.add("\n" + newQuestion);
                                                    System.out.println("What is the first answer choice?");
                                                    String answerChoice = scan.nextLine();
                                                    newQuestions.add("/" + answerChoice);

                                                    do {
                                                        System.out.println("Is there another answer " +
                                                                "choice?\n1.Yes\n2.No");
                                                        try {
                                                            anotherAnswerChoice = scan.nextInt();
                                                            if (anotherAnswerChoice != 1 && anotherAnswerChoice != 2) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            } else {
                                                                validNum = true;
                                                            }
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        }
                                                        scan.nextLine();
                                                    } while (!validNum);

                                                    while (anotherAnswerChoice == 1) {
                                                        System.out.println("What is the next answer choice?");
                                                        answerChoice = scan.nextLine();
                                                        newQuestions.add("/" + answerChoice);
                                                        do {
                                                            System.out.println("Is there another answer " +
                                                                    "choice?\n1.Yes\n2.No");
                                                            try {
                                                                anotherAnswerChoice = scan.nextInt();
                                                                if (anotherAnswerChoice != 1 &&
                                                                        anotherAnswerChoice != 2) {
                                                                    System.out.println("Please choose a valid option.");
                                                                    validNum = false;
                                                                } else {
                                                                    validNum = true;
                                                                }
                                                            } catch (InputMismatchException e) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            }
                                                            scan.nextLine();
                                                        } while (!validNum);
                                                    }
                                                    do {
                                                        System.out.println("Would you like to add another question?" +
                                                                "\n1.Yes\n2.No");
                                                        try {
                                                            continueToAnotherQuestion = scan.nextInt();
                                                            if (continueToAnotherQuestion != 1 &&
                                                                    continueToAnotherQuestion != 2) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            } else {
                                                                validNum = true;
                                                            }
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Please choose a valid option.");
                                                            validNum = false;
                                                        }
                                                        scan.nextLine();
                                                    } while (!validNum);
                                                    while (continueToAnotherQuestion == 1) {
                                                        System.out.println("What do you want the next question to be?");
                                                        newQuestion = scan.nextLine();
                                                        newQuestions.add("\n" + newQuestion);
                                                        System.out.println("What is the first answer choice?");
                                                        answerChoice = scan.nextLine();
                                                        newQuestions.add("/" + answerChoice);

                                                        do {
                                                            System.out.println("Is there another " +
                                                                    "answer choice?\n1.Yes\n2.No");
                                                            try {
                                                                anotherAnswerChoice = scan.nextInt();
                                                                if (anotherAnswerChoice != 1 &&
                                                                        anotherAnswerChoice != 2) {
                                                                    System.out.println("Please choose a valid option.");
                                                                    validNum = false;
                                                                } else {
                                                                    validNum = true;
                                                                }
                                                            } catch (InputMismatchException e) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            }
                                                            scan.nextLine();
                                                        } while (!validNum);

                                                        while (anotherAnswerChoice == 1) {
                                                            System.out.println("What is the next answer choice?");
                                                            answerChoice = scan.nextLine();
                                                            newQuestions.add("/" + answerChoice);

                                                            do {
                                                                System.out.println("Is there another answer " +
                                                                        "choice?\n" +
                                                                        "1.Yes\n2.No");
                                                                try {
                                                                    anotherAnswerChoice = scan.nextInt();
                                                                    if (anotherAnswerChoice != 1 &&
                                                                            anotherAnswerChoice != 2) {
                                                                        System.out.println("Please " +
                                                                                "choose a valid option.");
                                                                        validNum = false;
                                                                    } else {
                                                                        validNum = true;
                                                                    }
                                                                } catch (InputMismatchException e) {
                                                                    System.out.println("Please choose a valid option.");
                                                                    validNum = false;
                                                                }
                                                                scan.nextLine();
                                                            } while (!validNum);
                                                        }
                                                        do {
                                                            System.out.println("Would you like to " +
                                                                    "add another question?\n1.Yes\n2.No");
                                                            try {
                                                                continueToAnotherQuestion = scan.nextInt();
                                                                if (continueToAnotherQuestion != 1 &&
                                                                        continueToAnotherQuestion != 2) {
                                                                    System.out.println("Please choose a valid option.");
                                                                    validNum = false;
                                                                } else {
                                                                    validNum = true;
                                                                }
                                                            } catch (InputMismatchException e) {
                                                                System.out.println("Please choose a valid option.");
                                                                validNum = false;
                                                            }
                                                            scan.nextLine();
                                                        } while (!validNum);
                                                    }
                                                    t.editQuizQuestions(selectedQuiz, newQuestions, selectedCourse);
                                                }
                                            } else {
                                                System.out.println("Input was not a valid quiz.");
                                            }
                                        } else {
                                            System.out.println("There are no current quizzes.");
                                        }
                                    } else if (createDeleteEditQuiz == 4) { // Teacher has selected to grade submission
                                        System.out.println("What is the username of the student you would " +
                                                "like to grade?");
                                        String studentUsernameGrading = scan.nextLine();

                                        System.out.println("What is the name of the quiz you would like to grade?");
                                        String studentQuizNameGrading = scan.nextLine();

                                        ArrayList<String> studentResponses =
                                                t.displayStudentSubmission(studentUsernameGrading,
                                                        studentQuizNameGrading, selectedCourse);

                                        if (!(studentResponses.get(0).equals("None"))) {

                                            int counter14 = 0;
                                            int totalGrade = 0;
                                            int numberQuestions = 0;


                                            ArrayList<String> gradedResponses = new ArrayList<String>();
                                            while (counter14 < studentResponses.size()) {
                                                String[] splitStudentResponses =
                                                        (studentResponses.get(counter14)).split("/");
                                                String[] splitBySpaces =
                                                        (studentResponses.get(counter14)).split(" ");
                                                if (splitBySpaces[0].equals("Student")) {
                                                    System.out.println(studentResponses.get(counter14));
                                                } else {
                                                    System.out.println("Question: " + splitStudentResponses[0]);

                                                    int counter23 = 1;
                                                    while (counter23 < splitStudentResponses.length) {
                                                        System.out.println(splitStudentResponses[counter23]);
                                                        counter23++;
                                                    }

                                                    System.out.println("What grade would you like to " +
                                                            "give this question " +
                                                            "between 0 and 100?");
                                                    String initialGrade = scan.nextLine();
                                                    numberQuestions++;

                                                    boolean continueOn = true;
                                                    int grade = 0;

                                                    while (continueOn) {
                                                        try {
                                                            grade = Integer.parseInt(initialGrade);
                                                            continueOn = false;
                                                        } catch (NumberFormatException e) {
                                                            System.out.println("Grade must be an integer " +
                                                                    "from 0 to 100");
                                                            System.out.println("What grade would you like to give " +
                                                                    "this question " +
                                                                    "between 0 and 100?");
                                                            initialGrade = scan.nextLine();
                                                        }
                                                    }

                                                    totalGrade = totalGrade + grade;

                                                    gradedResponses.add(studentResponses.get(counter14) + "/Grade: " +
                                                            grade);
                                                }
                                                counter14++;
                                            }
                                            int totalGradedScore = totalGrade / numberQuestions;

                                            t.gradeSubmission(gradedResponses, studentUsernameGrading,
                                                    studentQuizNameGrading, selectedCourse, totalGradedScore);
                                        }
                                    }
                                } else {
                                    System.out.println("This was not a course option.");
                                }
                            }
                            do {
                                System.out.println("What would you like to do?\n1.Create Course\n2.Select " +
                                        "Course " +
                                        "\n3.Exit");
                                try {
                                    selectOrCreate1 = scan.nextInt();
                                    if (selectOrCreate1 != 1 && selectOrCreate1 != 2 && selectOrCreate1 != 3) {
                                        System.out.println("Please choose a valid option.");
                                        validNum = false;
                                    } else {
                                        validNum = true;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Please choose a valid option.");
                                    validNum = false;
                                }
                                scan.nextLine();
                            } while (!validNum);
                        } else if (selectOrCreate1 == 3) {
                            selectOrCreate1 = 4; // exit the loop
                        } else {
                            System.out.println("Invalid input. Type '1' or '2'.");
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Type '1' or '2'.");
                } catch (InputMismatchException e) {
                    System.out.println("Please enter valid input.");
                } catch (Exception e) {
                    System.out.println("Please enter valid input");
                }
            } else if (role1 == 2) { // If user is student
                do {
                    try {
                        do {
                            System.out.println("Would you like to take a quiz, view grades, or exit the program?" +
                                    "\n1.Quiz\n2.Grades\n3.Exit");
                            try {
                                courseSelectAddQuit = scan.nextInt();
                                if (courseSelectAddQuit != 1 && courseSelectAddQuit != 2 && courseSelectAddQuit != 3) {
                                    System.out.println("Please choose a valid option.");
                                    validNum = false;
                                } else {
                                    validNum = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Please choose a valid option.");
                                validNum = false;
                            }
                            scan.nextLine();
                        } while (!validNum);
                        Teacher t2 = new Teacher();
                        Student s = new Student(username);

                        if (courseSelectAddQuit == 1) { // take a quiz

                            ArrayList<String> courseOptionsStudent = t2.displayCourses();

                            if (!(courseOptionsStudent.get(0)).equals("None")) {
                                System.out.println("Which course would you like to take a quiz in?");

                                int counter13 = 0;
                                while (counter13 < courseOptionsStudent.size()) {
                                    System.out.println(courseOptionsStudent.get(counter13));
                                    counter13++;
                                }

                                String studentSelectedCourse = scan.nextLine();

                                ArrayList<String> quizOptions = t2.displayQuizzes(studentSelectedCourse);

                                if (!(quizOptions.get(0)).equals("None")) {
                                    System.out.println("Which quiz in this course do you want to take?");

                                    int counter12 = 0;
                                    while (counter12 < quizOptions.size()) {
                                        System.out.println(quizOptions.get(counter12));
                                        counter12++;
                                    }

                                    String studentSelectedQuiz = scan.nextLine();

                                    ArrayList<String> studentSelectedQuestions =
                                            t2.displayQuestions(studentSelectedQuiz, studentSelectedCourse);

                                    if (!((studentSelectedQuestions.get(0)).equals("None"))) {

                                        do {
                                            System.out.println("Would you like to enter quiz manually or " +
                                                    "import from file?" +
                                                    "\n1.File\n2.Manually");

                                            try {
                                                manuallyOrFromFile = scan.nextInt();
                                                if (manuallyOrFromFile != 1 && manuallyOrFromFile != 2) {
                                                    System.out.println("Please choose a valid option.");
                                                    validNum = false;
                                                } else {
                                                    validNum = true;
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println("Please choose a valid option.");
                                                validNum = false;
                                            }
                                            scan.nextLine();
                                        } while (!validNum);

                                        if (manuallyOrFromFile == 1) {
                                            System.out.println("What is the name of the file?\nEach line should " +
                                                    "be the " +
                                                    "answer to a question.");
                                            String filename = scan.nextLine();
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

                                                LocalDateTime date = LocalDateTime.now();

                                                s.createStudentSubmission(studentAnswerAndQuestion,
                                                        studentSelectedQuiz, date,
                                                        studentSelectedCourse);
                                            }
                                        } else if (manuallyOrFromFile == 2) {
                                            int counter8 = 0;
                                            ArrayList<String> studentAnswerAndQuestion = new ArrayList<String>();

                                            while (counter8 < studentSelectedQuestions.size()) {
                                                String[] questionAndAnswerSplitUp =
                                                        (studentSelectedQuestions.get(counter8)).split("/");

                                                System.out.println("Question: " + questionAndAnswerSplitUp[0]);

                                                // loop through all of the answer choices
                                                int counter18 = 1;
                                                while (counter18 < questionAndAnswerSplitUp.length) {
                                                    System.out.println(counter18 + ") " +
                                                            questionAndAnswerSplitUp[counter18]);
                                                    counter18++;
                                                }

                                                String studentAnswer = scan.nextLine();
                                                studentAnswerAndQuestion.add(studentSelectedQuestions.get(counter8) +
                                                        "/Answer: " + studentAnswer + "\n");
                                                counter8++;
                                            }

                                            LocalDateTime date = LocalDateTime.now();

                                            s.createStudentSubmission(studentAnswerAndQuestion, studentSelectedQuiz,
                                                    date, studentSelectedCourse);
                                        }
                                    }
                                } else {
                                    System.out.println("There are no current quizzes.");
                                }
                            } else {
                                System.out.println("There are no current courses.");
                            }

                            courseContinue = false; // will loop back to course select menu

                        } else if (courseSelectAddQuit == 2) { // student has selected to view grades
                            ArrayList<String> courseOptionsStudent = t2.displayCourses();

                            if (!(courseOptionsStudent.get(0).equals("None"))) {
                                System.out.println("Which course would you like view a grade for?");

                                int counter13 = 0;
                                while (counter13 < courseOptionsStudent.size()) {
                                    System.out.println(courseOptionsStudent.get(counter13));
                                    counter13++;
                                }

                                String studentSelectedCourse = scan.nextLine();

                                ArrayList<String> quizOptions = t2.displayQuizzes(studentSelectedCourse);

                                if (!(quizOptions.get(0)).equals("None")) {
                                    System.out.println("Which quiz in this course do you want to see grades?");

                                    int counter12 = 0;
                                    while (counter12 < quizOptions.size()) {
                                        System.out.println(quizOptions.get(counter12));
                                        counter12++;
                                    }

                                    String quizNameGrades = scan.nextLine();
                                    ArrayList<String> gradedSubmissions = s.viewGradedSubmission(quizNameGrades,
                                            studentSelectedCourse);

                                    if (!(gradedSubmissions.get(0).equals("None"))) {
                                        int counter21 = 0;

                                        while (counter21 < gradedSubmissions.size()) {
                                            String[] questionSplitUp =
                                                    (gradedSubmissions.get(counter21)).split("/");
                                            System.out.println("Question: " + questionSplitUp[0]);

                                            // loop through all of the answer choices
                                            int counter22 = 1;
                                            while (counter22 < questionSplitUp.length) {
                                                System.out.println(questionSplitUp[counter22]);
                                                counter22++;
                                            }
                                            counter21++;
                                        }
                                    }
                                } else {
                                    System.out.println("There are no current quizzes.");
                                }
                            } else {
                                System.out.println("There are no current courses.");
                            }
                        } else if (courseSelectAddQuit == 3) { // quit
                            courseContinue = true; // will quit
                        } else { // invalid input
                            System.out.println("Invalid input. Type '1' or '2'.");
                            courseContinue = false; // loop back to course select menu
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Type '1', '2' or '3'.");
                        courseContinue = false; // loop back to course select menu
                    }
                } while (!courseContinue);
            } // end teacher/student pathway
        }
    } // end main

    public int readFileForLogin(String fileName, String username, String password) {
        int userRole = 0;
        boolean userFound = false;
        String line = "";
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            while (((line = br.readLine()) != null) && (!userFound)) {
                if (line.startsWith(username + ",")) {
                    if (line.contains("," + password + ",")) {
                        userFound = true;
                        userRole = Integer.parseInt(line.substring(line.length() - 1));
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userRole;
    }

    public boolean readFileForSignUp(String fileName, String username) {
        boolean userExists = false;
        String line = "";
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            while (((line = br.readLine()) != null) && (!userExists)) {
                if (line.startsWith(username + ",")) {
                    userExists = true;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userExists;
    }

    public void writeFileForSignUp(String fileName, String username, String password, int role) {
        try {
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(username + "," + password + "," + role + "\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean rewriteFile(String filename, String username) throws IOException {
        File inputFile = new File(filename);
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = username;
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if (trimmedLine.startsWith(lineToRemove + ",")) continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        inputFile.delete();
        boolean successful = tempFile.renameTo(inputFile);
        return successful;
    }
}






    //System.out.println("What do you want to send to the server?");
    //String response = scan.nextLine();

    //writer.write(response);
    //writer.println();
    //writer.flush();
    //System.out.printf("Sent to server: \n%s\n", response);

    //String s1 = reader.readLine();

    //System.out.printf("Received from server: \n%s\n", s1);

//TODO CLOSE THESE
    //writer.close();
    //reader.close();

//}
//}

