import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A class that allows the user to draw.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Homework 11 -- Challenge</p>
 *
 * @author Katie Testin
 * @version April 5, 2022
 */

public class StudentGUI extends JComponent implements Runnable {

    Teacher t = new Teacher();
    Quiz quiz = new Quiz();

    String selectedCourse;
    String selectedQuiz;
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<String> totalQuizzes = new ArrayList<>();
    ArrayList<String> totalCourses = new ArrayList<>();
    ArrayList<String> studentSelectedQuestions = new ArrayList<>();
    String newPassword;
    File LOGINFILENAME = new File("logins.txt");
    String username;
    int role;

    JComboBox<String> courseOptions;
    JButton courseSelectButton;

    JComboBox<String> quizOptions;
    JButton quizSelectButton;
    JButton quizTakeButton;
    JButton quizViewGradeButton;
    JLabel center;


    JButton editPasswordButton;
    JButton deleteAccountButton;
    JButton logOutButton;
    JButton refreshButton;

    StudentGUI studentGUI;


    public void run() {

        JFrame frame = new JFrame();
        frame.setTitle("Student");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        Color color = new Color(255,127,80);
        studentGUI = new StudentGUI(username, role);
        content.add(studentGUI, BorderLayout.CENTER);
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

        // Buttons about the quizzes
        quizOptions = new JComboBox();
        quizOptions.addItem("Select Course First");
        quizTakeButton = new JButton("Take Quiz");
        quizViewGradeButton = new JButton("View Grade");
        center = new JLabel("                                      ");

        // Button at the bottom
        editPasswordButton = new JButton("Edit Password");
        deleteAccountButton = new JButton("Delete Account");
        logOutButton = new JButton("Log Out");
        refreshButton  = new JButton("Refresh");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(color);
        content.add(topPanel, BorderLayout.NORTH);

        JPanel coursePanel = new JPanel();
        coursePanel.setBackground(color);
        coursePanel.add(courseOptions);
        coursePanel.add(courseSelectButton);
        topPanel.add(coursePanel);

        JPanel quizPanel = new JPanel();
        quizPanel.setBackground(color);
        quizPanel.add(quizOptions);
        quizPanel.add(quizTakeButton);
        quizPanel.add(quizViewGradeButton);
        quizPanel.add(center);
        content.add(quizPanel, BorderLayout.EAST);

        JPanel editLeave = new JPanel();
        editLeave.add(editPasswordButton);
        editLeave.add(deleteAccountButton);
        editLeave.add(logOutButton);
        editLeave.add(refreshButton);
        content.add(editLeave, BorderLayout.SOUTH);

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

        courseSelectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedCourse = courseOptions.getSelectedItem().toString();
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

        // TAKE QUIZ
        quizTakeButton.addActionListener(new ActionListener() {
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
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                writer.write("takeQuiz");
                writer.println();
                writer.flush();

                selectedQuiz = quizOptions.getSelectedItem().toString();

                writer.write(selectedQuiz); // write the selected quiz to server
                writer.println();
                writer.flush();

                writer.write(selectedCourse);
                writer.println();
                writer.flush();

                String studentSelectedString = "";
                try {
                    studentSelectedString = reader.readLine(); //  read the questions from the server
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                String[] studentSelectedArray = studentSelectedString.split(">");
                System.out.println(studentSelectedString);

                int counter = 0;
                while (counter < studentSelectedArray.length) {
                    studentSelectedQuestions.add(studentSelectedArray[counter]);
                    counter++;
                }

                if (!((studentSelectedQuestions.get(0)).equals("None"))) {
                    String nameOfQuiz = "";
                    String[] options2 = new String[2];
                    options2[0] = "Manually";
                    options2[1] = "File";
                    String manuallyOrFromFile = (String) JOptionPane.showInputDialog(null,
                            "Choose an option to continue",
                            "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                            options2[0]);

                    writer.write(manuallyOrFromFile);
                    writer.println();
                    writer.flush();

                    String filename;
                    if (manuallyOrFromFile.equals("File")) {
                        do {
                            filename = JOptionPane.showInputDialog(null, "What is the name of the file? Each line should be the answer to a question.",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((filename == null) || (filename.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Filename cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((filename == null) || (filename.isEmpty()));

                        writer.write("filename");
                        writer.println();
                        writer.flush();

                        JOptionPane.showMessageDialog(null, "Quiz Submission Recorded",
                                null, JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int counter8 = 0;
                        ArrayList<String> studentAnswerAndQuestion = new ArrayList<String>();
                        String print = "";
                        ArrayList<String> answerChoices = new ArrayList<String>();

                        while (counter8 < studentSelectedQuestions.size()) {
                            String[] questionAndAnswerSplitUp =
                                    (studentSelectedQuestions.get(counter8)).split("/");
                            print = "Question: " + questionAndAnswerSplitUp[0];

                            // loop through all of the answer choices
                            int counter18 = 1;
                            while (counter18 < questionAndAnswerSplitUp.length) {
                                answerChoices.add(questionAndAnswerSplitUp[counter18]);
                                counter18++;
                            }
                            String[] options3 = new String[answerChoices.size()];
                            int counter3 = 0;
                            while (counter3 < answerChoices.size()) {
                                options3[counter3] = answerChoices.get(counter3);
                                counter3++;
                            }
                            String studentAnswer = (String) JOptionPane.showInputDialog(null,
                                    print,
                                    null, JOptionPane.QUESTION_MESSAGE, null, options3,
                                    options3[0]);

                            String x = studentSelectedQuestions.get(counter8) +
                                    "/Answer: " + studentAnswer;

                            writer.write(x);
                            writer.println();
                            writer.flush();

                            counter8++;
                            answerChoices.clear();
                        }
                        JOptionPane.showMessageDialog(null, "Quiz Submission Recorded",
                                null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        });

        logOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // VIEW GRADE
        quizViewGradeButton.addActionListener(new ActionListener() {
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
                writer.write("viewGrade");
                writer.println();
                writer.flush();

                String quizNameGrades = quizOptions.getSelectedItem().toString();

                writer.write(quizNameGrades);
                writer.println(); // write the name of quiz to view grades for
                writer.flush();

                String isThereSubmission = "";

                try {
                    isThereSubmission = reader.readLine(); // server tells if submission
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (isThereSubmission.equals("yes")) {
                    String print = null;
                    try {
                        print = reader.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, print,
                            null, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No graded submission",
                            null, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }


    public StudentGUI(String username, int role) {
        this.username = username;
        this.role = role;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            }
        });


    }
}