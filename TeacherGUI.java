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
    ArrayList<String> totalCourses = new ArrayList<>();
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
    JButton logOutButton;
    JButton refreshButton;

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
        logOutButton = new JButton("Log Out");
        refreshButton = new JButton("Refresh");

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
        editLeave.add(logOutButton);
        editLeave.add(refreshButton);
        content.add(editLeave, BorderLayout.SOUTH);

        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
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
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                writer.write("refresh");
                writer.println();
                writer.flush();

                selectedCourse = courseOptions.getSelectedItem().toString();
                writer.write(selectedCourse);
                writer.println();
                writer.flush();

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

                //read in the new courses

                String totalCoursesString = "";
                try {
                    totalCoursesString = reader.readLine(); //  read the courses from the server
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String[] totalCoursesArray = totalCoursesString.split("/");

                totalCourses.clear();

                int counter2 = 0;
                while (counter2 < totalCoursesArray.length) {
                    totalCourses.add(totalCoursesArray[counter2]);
                    counter2++;
                }

                courseOptions.removeAllItems(); // take out the current quizzes
                int i2 = 0;
                while (i2 < totalCourses.size()) {
                    courseOptions.addItem(totalCourses.get(i2));
                    i2++;
                }
            }
        });

        // SELECT COURSE
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

                writer.write("createQuiz");
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

                writer.write(manuallyOrFromFile);
                writer.println();
                writer.flush();

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

                    writer.write(filename);
                    writer.println();
                    writer.flush();

                } else {
                    nameOfQuiz = quizTextField.getText();
                    writer.write(nameOfQuiz);
                    writer.println();
                    writer.flush();

                    int randomizedOrNot = JOptionPane.showConfirmDialog(null, "Would you like the quiz to be randomized?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    writer.write(String.valueOf(randomizedOrNot));
                    writer.println();
                    writer.flush();

                    ArrayList<String> newQuestions = new ArrayList<String>();

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

                    writer.write(quizQuestion);
                    writer.println();
                    writer.flush();

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

                    writer.write(answerChoice);
                    writer.println();
                    writer.flush();
                    //ended here
                    int anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    writer.write(String.valueOf(anotherAnswerChoice));
                    writer.println();
                    writer.flush();

                    while (anotherAnswerChoice == JOptionPane.YES_OPTION) {
                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        writer.write(answerChoice);
                        writer.println();
                        writer.flush();

                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);

                        writer.write(String.valueOf(anotherAnswerChoice));
                        writer.println();
                        writer.flush();
                    }

                    int anotherQuestion = JOptionPane.showConfirmDialog(null, "Would you like to add another question?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    writer.write(String.valueOf(anotherQuestion));
                    writer.println();
                    writer.flush();

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

                        writer.write(quizQuestion);
                        writer.println();
                        writer.flush();

                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the " +
                                            "answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        writer.write(answerChoice);
                        writer.println();
                        writer.flush();

                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        writer.write(String.valueOf(anotherAnswerChoice));
                        writer.println();
                        writer.flush();

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
                            writer.write(answerChoice);
                            writer.println();
                            writer.flush();

                            anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                    "Sign Up", JOptionPane.YES_NO_OPTION);

                            writer.write(String.valueOf(anotherAnswerChoice));
                            writer.println();
                            writer.flush();
                        }
                        anotherQuestion = JOptionPane.showConfirmDialog(null, "Is there another question?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        writer.write(String.valueOf(anotherQuestion));
                        writer.println();
                        writer.flush();
                    }
                }
                writer.write(selectedCourse);
                writer.println();
                writer.flush();

                quizOptions.addItem(nameOfQuiz);

                if (nameOfQuiz != "") {
                    JOptionPane.showMessageDialog(null, "Quiz Created!",
                            null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // GRADE QUIZ
        quizGradeButton.addActionListener(new ActionListener() {
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
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("gradeQuiz");
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

                writer.write(studentUsernameGrading);
                writer.println();
                writer.flush();

                String studentQuizNameGrading = quizOptions.getSelectedItem().toString();
                writer.write(studentQuizNameGrading);
                writer.println();
                writer.flush();

                String studentResponses = null;
                try {
                    studentResponses = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                int responseSize = 0;
                try {
                    responseSize = Integer.parseInt(reader.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                JOptionPane.showMessageDialog(null, studentResponses,
                        "Time of Submission", JOptionPane.INFORMATION_MESSAGE);

                if ((!(studentResponses == null )) || (!(studentResponses.equals("None")))) {
                    int counter14 = 1;
                    String print = "";
                    while (counter14 < responseSize) {
                        String print2 = "";
                        print = "";
                        try {
                            print2 = reader.readLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        String[] printString = print2.split(">");
                        int counter2 = 0;
                        while (counter2 < printString.length) {
                            print = print + "\n" + printString[counter2];
                            counter2++;
                        }

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
                        writer.write(initialGrade);
                        writer.println();
                        writer.flush();
                        counter14++;
                    }
                    JOptionPane.showMessageDialog(null, "Quiz Graded!",
                            null, JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });

        // EDIT QUIZ
        quizEditButton.addActionListener(new ActionListener() {
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
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                writer.write("editQuiz");
                writer.println();
                writer.flush();

                selectedQuiz = quizOptions.getSelectedItem().toString();

                writer.write(selectedQuiz);
                writer.println();
                writer.flush();

                ArrayList<String> allQuestions = new ArrayList<>();
                String questionsString = "";
                try {
                    questionsString = reader.readLine(); //  read the questions from the server
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String[] studentSelectedArray = questionsString.split(">");

                int counter = 0;
                while (counter < studentSelectedArray.length) {
                    allQuestions.add(studentSelectedArray[counter]);
                    counter++;
                }

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
                String nameOfQuiz = "";
                String[] options2 = new String[2];
                options2[0] = "Manually";
                options2[1] = "File";
                String manuallyOrFromFile = (String) JOptionPane.showInputDialog(null,
                        "Choose an option to continue",
                        "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                        options2[0]);
                String filename;

                writer.write(manuallyOrFromFile);
                writer.println();
                writer.flush();

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

                    writer.write(filename);
                    writer.println();
                    writer.flush();

                } else {
                    nameOfQuiz = quizOptions.getSelectedItem().toString();
                    writer.write(nameOfQuiz);
                    writer.println();
                    writer.flush();

                    int randomizedOrNot = JOptionPane.showConfirmDialog(null, "Would you like the quiz to be randomized?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    writer.write(String.valueOf(randomizedOrNot));
                    writer.println();
                    writer.flush();

                    ArrayList<String> newQuestions = new ArrayList<String>();

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

                    writer.write(quizQuestion);
                    writer.println();
                    writer.flush();

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

                    writer.write(answerChoice);
                    writer.println();
                    writer.flush();
                    //ended here
                    int anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    writer.write(String.valueOf(anotherAnswerChoice));
                    writer.println();
                    writer.flush();

                    while (anotherAnswerChoice == JOptionPane.YES_OPTION) {
                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        writer.write(answerChoice);
                        writer.println();
                        writer.flush();

                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);

                        writer.write(String.valueOf(anotherAnswerChoice));
                        writer.println();
                        writer.flush();
                    }

                    int anotherQuestion = JOptionPane.showConfirmDialog(null, "Would you like to add another question?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    writer.write(String.valueOf(anotherQuestion));
                    writer.println();
                    writer.flush();

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

                        writer.write(quizQuestion);
                        writer.println();
                        writer.flush();

                        do {
                            answerChoice = JOptionPane.showInputDialog(null, "What is the " +
                                            "answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        writer.write(answerChoice);
                        writer.println();
                        writer.flush();

                        anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        writer.write(String.valueOf(anotherAnswerChoice));
                        writer.println();
                        writer.flush();

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
                            writer.write(answerChoice);
                            writer.println();
                            writer.flush();

                            anotherAnswerChoice = JOptionPane.showConfirmDialog(null, "Is there another answer choice?",
                                    "Sign Up", JOptionPane.YES_NO_OPTION);

                            writer.write(String.valueOf(anotherAnswerChoice));
                            writer.println();
                            writer.flush();
                        }
                        anotherQuestion = JOptionPane.showConfirmDialog(null, "Is there another question?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        writer.write(String.valueOf(anotherQuestion));
                        writer.println();
                        writer.flush();
                    }
                }
                writer.write(selectedCourse);
                writer.println();
                writer.flush();

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