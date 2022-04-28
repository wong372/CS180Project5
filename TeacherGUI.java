import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A class that allows the user to draw.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Homework 11 -- Challenge</p>
 *
 * @author Katie Testin
 * @version April 5, 2022
 */

public class TeacherGUI extends JComponent implements Runnable {

    Teacher t = new Teacher();
    Quiz quiz = new Quiz();

    String selectedCourse;
    String selectedQuiz;
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<String> totalQuizzes = new ArrayList<>();
    String newPassword;
    File LOGINFILENAME = new File("logins.txt");
    String username;
    int role;

    JComboBox<String> courseOptions;
    JButton courseSelectButton;
    JButton courseCreateButton;
    JLabel courseSelection;
    JTextField courseTextField;

    JComboBox<String> quizOptions;
    JButton quizSelectButton;
    JButton quizDeleteButton;
    JButton quizEditButton;
    JButton quizCreateButton;
    JButton quizGradeButton;
    JLabel quizSelection;
    JTextField quizTextField;

    JButton editPasswordButton;
    JButton deleteAccountButton;

    TeacherGUI teacherGUI;


    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Teacher");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        Color color = new Color(103,205,203);
        teacherGUI = new TeacherGUI(username, role);
        content.add(teacherGUI, BorderLayout.CENTER);
        content.setBackground(color);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Buttons about the courses
        courseOptions = new JComboBox();
        try {
            courses = t.displayCourses();
            int i = 0;
            while (i < courses.size()) {
                courseOptions.addItem(courses.get(i));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        courseSelectButton = new JButton("Select");
        courseCreateButton = new JButton("Create");
        courseSelection = new JLabel("  New Course: ");
        courseTextField = new JTextField("", 5);

        // Buttons about the quizzes
        quizOptions = new JComboBox();
        quizOptions.addItem("Select Course First");
        quizDeleteButton = new JButton("Delete");
        quizCreateButton = new JButton("Create");
        quizEditButton = new JButton("Edit");
        quizGradeButton = new JButton("Grade");
        quizSelection = new JLabel("New Quiz: ");
        quizTextField = new JTextField("", 5);

        // Button at the bottom
        editPasswordButton = new JButton("Edit Password");
        deleteAccountButton = new JButton("Delete Account");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(color);
        content.add(topPanel, BorderLayout.NORTH);

        JPanel coursePanel = new JPanel();
        coursePanel.setBackground(color);
        coursePanel.add(courseOptions);
        coursePanel.add(courseSelectButton);
        coursePanel.add(courseSelection);
        coursePanel.add(courseTextField);
        coursePanel.add(courseCreateButton);
        topPanel.add(coursePanel);

        JPanel quizPanel = new JPanel();
        quizPanel.setBackground(color);
        quizPanel.add(quizOptions);
        quizPanel.add(quizDeleteButton);
        quizPanel.add(quizGradeButton);
        quizPanel.add(quizEditButton);
        quizPanel.add(quizSelection);
        quizPanel.add(quizTextField);
        quizPanel.add(quizCreateButton);
        content.add(quizPanel, BorderLayout.EAST);

        JPanel editLeave = new JPanel();
        editLeave.add(editPasswordButton);
        editLeave.add(deleteAccountButton);
        content.add(editLeave, BorderLayout.SOUTH);

        courseSelectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedCourse = courseOptions.getSelectedItem().toString();
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                writer.write("courseSelect");
                writer.println();
                writer.flush();

                writer.write(selectedCourse);
                writer.println();
                writer.flush(); // write the selected course to server


                String totalQuizzesString = "";
                try {
                    totalQuizzesString = reader.readLine(); //  read the quizzes from the server
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String[] totalQuizzesArray = totalQuizzesString.split("/");

                totalQuizzes.clear();

                int counter = 0;
                while (counter < totalQuizzesArray.length) {
                    totalQuizzes.add(totalQuizzesArray[counter]);
                    counter++;
                }

                quizOptions.removeAllItems(); // take out the current quizzes
                int i = 0;
                while (i < totalQuizzes.size()) {
                    quizOptions.addItem(totalQuizzes.get(i));
                    i++;
                }
            }
        });

        //CREATE COURSE
        courseCreateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("courseCreate");
                writer.println();
                writer.flush();

                String courseName1 = courseTextField.getText();
                writer.write(courseName1);
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Course Created",
                        null, JOptionPane.INFORMATION_MESSAGE);
                courseOptions.addItem(courseName1);
            }
        });

        //DELETE QUIZ
        quizDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("deleteQuiz");
                writer.println();
                writer.flush();

                String selectedQuiz = quizOptions.getSelectedItem().toString();

                writer.write(selectedCourse);
                writer.println();
                writer.flush();

                writer.write(selectedQuiz);
                writer.println();
                writer.flush();

                quizOptions.removeItem(selectedQuiz); // clear that quiz from list
            }
        });

        // CREATE QUIZ
        quizCreateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("courseSelect");
                writer.println();
                writer.flush();
                ArrayList<String> quizQuestions = new ArrayList<String>();

                String nameOfQuiz = "";
                String[] options2 = new String[2];
                options2[0] = "Manually";
                options2[1] = "File";
                String manuallyOrFromFile = (String) JOptionPane.showInputDialog(null,
                        "Choose an option to continue",
                        "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                        options2[0]);
                String filename;
                Teacher t = new Teacher();
                if (manuallyOrFromFile.equals("File")) {
                    do {
                        filename = JOptionPane.showInputDialog(null, "What is the name of the file? The first line should be the title of the quizAll remaining lines should be questions with answers on the same line, separated by commas",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((filename == null) || (filename.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Filename cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((filename == null) || (filename.isEmpty()));

                    t.writeQuizFromFile(selectedCourse, filename);
                } else {
                    nameOfQuiz = quizTextField.getText();

                    int randomizedOrNot = JOptionPane.showConfirmDialog(null, "Would you like the quiz to be randomized?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);


                    ArrayList<String> newQuestions = new ArrayList<String>();
                    if (randomizedOrNot == JOptionPane.YES_OPTION) {
                        quizQuestions.add(String.valueOf(1));
                    } else if (randomizedOrNot == JOptionPane.NO_OPTION) {
                        quizQuestions.add(String.valueOf(2));
                    }

                    String quizQuestion;

                    do {
                        quizQuestion = JOptionPane.showInputDialog(null, "What is the first question on the quiz?",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((quizQuestion == null) || (quizQuestion.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((quizQuestion == null) || (quizQuestion.isEmpty()));

                    quizQuestions.add("\n" + quizQuestion);

                    String answerChoice;

                    do {
                        answerChoice = JOptionPane.showInputDialog(null, "What is the next answer choice?",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((answerChoice == null) || (answerChoice.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((answerChoice == null) || (answerChoice.isEmpty()));

                    quizQuestions.add("/" + answerChoice);

                    int anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    while (anotherAnswerChoice == JOptionPane.YES_OPTION) {
                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the first answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));


                        quizQuestions.add("/" + answerChoice);
                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                    }

                    int anotherQuestion = JOptionPane.showConfirmDialog(null, "Would you like to add another question?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);
                    while (anotherQuestion == JOptionPane.YES_OPTION) {

                        do {
                            quizQuestion = JOptionPane.showInputDialog(null, "What is the next question on the quiz?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((quizQuestion == null) || (quizQuestion.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((quizQuestion == null) || (quizQuestion.isEmpty()));

                        quizQuestions.add("\n" + quizQuestion);
                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the first " +
                                            "answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        quizQuestions.add("/" + answerChoice);

                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        while (anotherAnswerChoice == JOptionPane.YES_OPTION) {
                            do {
                                answerChoice = JOptionPane.showInputDialog(null, "What is the next answer choice?",
                                        "Sign Up", JOptionPane.QUESTION_MESSAGE);
                                if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                    JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                            "University Card",
                                            JOptionPane.ERROR_MESSAGE);

                                } //end if

                            } while ((answerChoice == null) || (answerChoice.isEmpty()));

                            quizQuestions.add("/" + answerChoice);

                            anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                    "Sign Up", JOptionPane.YES_NO_OPTION);
                        }
                        anotherQuestion = JOptionPane.showConfirmDialog(null, "Is there another question?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                    }
                }
                t.createQuiz(selectedCourse, quizQuestions, nameOfQuiz);
                quizOptions.addItem(nameOfQuiz);

                if (nameOfQuiz != "") {
                    JOptionPane.showMessageDialog(null, "Quiz Created!",
                            null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        quizGradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("courseSelect");
                writer.println();
                writer.flush();
                String studentUsernameGrading = "";
                do {
                    studentUsernameGrading = JOptionPane.showInputDialog(null, "What is the username of the student you want to grade",
                            "Log in", JOptionPane.QUESTION_MESSAGE);
                    if ((studentUsernameGrading == null) || (studentUsernameGrading.isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Username cannot be empty!",
                                "University Card",
                                JOptionPane.ERROR_MESSAGE);

                    } //end if

                } while ((studentUsernameGrading == null) || (studentUsernameGrading.isEmpty()));

                String studentQuizNameGrading = quizOptions.getSelectedItem().toString();

                ArrayList<String> studentResponses =
                        t.displayStudentSubmission(studentUsernameGrading,
                                studentQuizNameGrading, selectedCourse);

                if (!(studentResponses.get(0).equals("None"))) {

                    int counter14 = 0;
                    int totalGrade = 0;
                    int numberQuestions = 0;


                    ArrayList<String> gradedResponses = new ArrayList<String>();
                    String print = "";
                    while (counter14 < studentResponses.size()) {
                        String[] splitStudentResponses =
                                (studentResponses.get(counter14)).split("/");
                        String[] splitBySpaces =
                                (studentResponses.get(counter14)).split(" ");
                        if (splitBySpaces[0].equals("Student")) {
                            print = print + studentResponses.get(counter14) + "\n";
                        } else {
                            print = print + "Question: " + splitStudentResponses[0] + "\n";

                            int counter23 = 1;
                            while (counter23 < splitStudentResponses.length) {
                                print = print + splitStudentResponses[counter23] + "\n";
                                counter23++;
                            }

                            print = print + "What grade would you like to " +
                                    "give this question " +
                                    "between 0 and 100?";

                            String initialGrade = "";

                            do {
                                initialGrade = JOptionPane.showInputDialog(null, print,
                                        null, JOptionPane.QUESTION_MESSAGE);
                                if ((initialGrade == null) || (initialGrade.isEmpty())) {
                                    JOptionPane.showMessageDialog(null, "Grade cannot be empty!",
                                            null,
                                            JOptionPane.ERROR_MESSAGE);

                                } //end if

                            } while ((initialGrade == null) || (initialGrade.isEmpty()));
                            print = "";

                            numberQuestions++;

                            boolean continueOn = true;
                            int grade = 0;

                            while (continueOn) {
                                try {
                                    grade = Integer.parseInt(initialGrade);
                                    continueOn = false;
                                } catch (NumberFormatException e1) {
                                    JOptionPane.showMessageDialog(null, "Grade must be integer between 0 and 100",
                                            null, JOptionPane.ERROR_MESSAGE);

                                    do {
                                        initialGrade = JOptionPane.showInputDialog(null, "What grade" +
                                                        "would you like to give this question between 1 and 100",
                                                null, JOptionPane.QUESTION_MESSAGE);
                                        if ((initialGrade == null) || (initialGrade.isEmpty())) {
                                            JOptionPane.showMessageDialog(null, "Grade cannot be empty!",
                                                    null,
                                                    JOptionPane.ERROR_MESSAGE);

                                        } //end if

                                    } while ((initialGrade == null) || (initialGrade.isEmpty()));
                                }
                            }

                            totalGrade = totalGrade + grade;

                            gradedResponses.add(studentResponses.get(counter14) + "/Grade: " +
                                    grade + "/");
                        }
                        counter14++;
                    }
                    int totalGradedScore = totalGrade / numberQuestions;

                    t.gradeSubmission(gradedResponses, studentUsernameGrading,
                            studentQuizNameGrading, selectedCourse, totalGradedScore);
                }
                JOptionPane.showMessageDialog(null, "Quiz Graded!",
                        null, JOptionPane.INFORMATION_MESSAGE);
            }

        });
        quizEditButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("courseSelect");
                writer.println();
                writer.flush();
                selectedQuiz = quizOptions.getSelectedItem().toString();

                ArrayList<String> allQuestions = t.displayQuestions(selectedQuiz, selectedCourse);


                int counter19 = 0;
                String print = "";


                while (counter19 < allQuestions.size()) {
                    String[] questionAndAnswerSplitUp2 = (allQuestions.get(counter19)).split("/");
                    print = print + "Question: " + questionAndAnswerSplitUp2[0] + "\n";

                    // loop through all of the answer choices
                    int counter20 = 1;
                    while (counter20 < questionAndAnswerSplitUp2.length) {
                        print = print + counter20 + ") " +
                                questionAndAnswerSplitUp2[counter20] + "\n";
                        counter20++;
                    }
                    counter19++;
                }
                JOptionPane.showMessageDialog(null, "The current questions are: \n " + print,
                        null, JOptionPane.INFORMATION_MESSAGE);

                JOptionPane.showMessageDialog(null, "Now create new questions",
                        null, JOptionPane.INFORMATION_MESSAGE);

                // now create new quiz
                ArrayList<String> quizQuestions = new ArrayList<String>();

                String nameOfQuiz = "";
                String[] options2 = new String[2];
                options2[0] = "Manually";
                options2[1] = "File";
                String manuallyOrFromFile = (String) JOptionPane.showInputDialog(null,
                        "Choose an option to continue",
                        "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                        options2[0]);
                String filename;
                Teacher t = new Teacher();
                if (manuallyOrFromFile.equals("File")) {
                    do {
                        filename = JOptionPane.showInputDialog(null, "What is the name of the file? The first line should be the title of the quizAll remaining lines should be questions with answers on the same line, separated by commas",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((filename == null) || (filename.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Filename cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((filename == null) || (filename.isEmpty()));

                    t.writeQuizFromFile(selectedCourse, filename);
                } else {
                    nameOfQuiz = quizTextField.getText();

                    int randomizedOrNot = JOptionPane.showConfirmDialog(null, "Would you like the quiz to be randomized?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    ArrayList<String> newQuestions = new ArrayList<String>();
                    if (randomizedOrNot == JOptionPane.YES_OPTION) {
                        quizQuestions.add(String.valueOf(1));
                    } else if (randomizedOrNot == JOptionPane.NO_OPTION) {
                        quizQuestions.add(String.valueOf(2));
                    }

                    String quizQuestion;

                    do {
                        quizQuestion = JOptionPane.showInputDialog(null, "What is the first question on the quiz?",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((quizQuestion == null) || (quizQuestion.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((quizQuestion == null) || (quizQuestion.isEmpty()));

                    quizQuestions.add("\n" + quizQuestion);

                    String answerChoice;

                    do {
                        answerChoice = JOptionPane.showInputDialog(null, "What is the next answer choice?",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((answerChoice == null) || (answerChoice.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((answerChoice == null) || (answerChoice.isEmpty()));

                    quizQuestions.add("/" + answerChoice);

                    int anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    while (anotherAnswerChoice == JOptionPane.YES_OPTION) {
                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the first answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));


                        quizQuestions.add("/" + answerChoice);
                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                    }

                    int anotherQuestion = JOptionPane.showConfirmDialog(null, "Would you like to add another question?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);
                    while (anotherQuestion == JOptionPane.YES_OPTION) {

                        do {
                            quizQuestion = JOptionPane.showInputDialog(null, "What is the next question on the quiz?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((quizQuestion == null) || (quizQuestion.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((quizQuestion == null) || (quizQuestion.isEmpty()));

                        quizQuestions.add("\n" + quizQuestion);
                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the first " +
                                            "answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        quizQuestions.add("/" + answerChoice);

                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        while (anotherAnswerChoice == JOptionPane.YES_OPTION) {
                            do {
                                answerChoice = JOptionPane.showInputDialog(null, "What is the next answer choice?",
                                        "Sign Up", JOptionPane.QUESTION_MESSAGE);
                                if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                    JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                            "University Card",
                                            JOptionPane.ERROR_MESSAGE);

                                } //end if

                            } while ((answerChoice == null) || (answerChoice.isEmpty()));

                            quizQuestions.add("/" + answerChoice);

                            anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                    "Sign Up", JOptionPane.YES_NO_OPTION);
                        }
                        anotherQuestion = JOptionPane.showConfirmDialog(null, "Is there another question?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                    }
                }
                t.createQuiz(selectedCourse, quizQuestions, nameOfQuiz);
                quizOptions.addItem(nameOfQuiz);

                if (nameOfQuiz != "") {
                    JOptionPane.showMessageDialog(null, "Quiz Created!",
                            null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //EDIT PASSWORD
        editPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                writer.write("editPassword");
                writer.println();
                writer.flush();

                do {
                    newPassword = JOptionPane.showInputDialog(null, "What is your new" +
                                    "password?",
                            "Log in", JOptionPane.QUESTION_MESSAGE);
                    if ((newPassword == null) || (newPassword.isEmpty())) {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty!",
                                "University Card",
                                JOptionPane.ERROR_MESSAGE);

                    } //end if

                } while ((newPassword == null) || (newPassword.isEmpty()));

                writer.write(newPassword);
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Password Changed!",
                        null, JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // DELETE ACCOUNT
        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4243);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                writer.write("deleteAccount");
                writer.println();
                writer.flush();

                writer.write(username); // write the username to server
                writer.println();
                writer.flush();

                JOptionPane.showMessageDialog(null, "Account was successfully deleted!",
                        null, JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            }
        });
    }


    public TeacherGUI(String username, int role) {
        this.username = username;
        this.role = role;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            }
        });


    }


}