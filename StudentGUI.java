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
    ArrayList<String> courses;
    ArrayList<String> totalQuizzes;
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
        content.add(editLeave, BorderLayout.SOUTH);
        Socket socket = null;
        try {
            socket = new Socket("localhost", 4242);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter writer2 = null;
        try {
            writer2 = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter finalWriter = writer2;
        courseSelectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finalWriter.write("courseSelect"); // write the chosen path to server
                selectedCourse = courseOptions.getSelectedItem().toString();
                finalWriter.write(selectedCourse); // write the selected course to server
                totalQuizzes = t.displayQuizzes(selectedCourse);
                quizOptions.removeAllItems();
                int i = 0;
                while (i < totalQuizzes.size()) {
                    quizOptions.addItem(totalQuizzes.get(i));
                    i++;
                }
            }
        });
        editPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

                try {
                    quiz.rewriteFile(String.valueOf(LOGINFILENAME), username);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                quiz.writeFileForSignUp(String.valueOf(LOGINFILENAME), username, newPassword, role);

                JOptionPane.showMessageDialog(null, "Password Changed!",
                        null, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean success = quiz.rewriteFile(String.valueOf(LOGINFILENAME), username);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Account was successfully deleted!",
                        null, JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            }
        });
        quizTakeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedQuiz = quizOptions.getSelectedItem().toString();
                Student s = new Student(username);


                ArrayList<String> studentSelectedQuestions = t.displayQuestions(selectedQuiz, selectedCourse);

                if (!((studentSelectedQuestions.get(0)).equals("None"))) {
                    String nameOfQuiz = "";
                    String[] options2 = new String[2];
                    options2[0] = "Manually";
                    options2[1] = "File";
                    String manuallyOrFromFile = (String) JOptionPane.showInputDialog(null,
                            "Choose an option to continue",
                            "Sign Up", JOptionPane.QUESTION_MESSAGE, null, options2,
                            options2[0]);
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
                                    selectedQuiz, date,
                                    selectedCourse);
                            JOptionPane.showMessageDialog(null, "Quiz Submission Recorded",
                                    null, JOptionPane.INFORMATION_MESSAGE);
                        }
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
                            int counter = 0;
                            while (counter < answerChoices.size()) {
                                options3[counter] = answerChoices.get(counter);
                                counter++;
                            }
                            String studentAnswer = (String) JOptionPane.showInputDialog(null,
                                    print,
                                    null, JOptionPane.QUESTION_MESSAGE, null, options3,
                                    options3[0]);

                            studentAnswerAndQuestion.add(studentSelectedQuestions.get(counter8) +
                                    "/Answer: " + studentAnswer + "\n");
                            counter8++;
                            answerChoices.clear();
                        }

                        LocalDateTime date = LocalDateTime.now();

                        s.createStudentSubmission(studentAnswerAndQuestion, selectedQuiz,
                                date, selectedCourse);
                        JOptionPane.showMessageDialog(null, "Quiz Submission Recorded",
                                null, JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        });
        quizViewGradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Student s = new Student(username);

                String quizNameGrades = quizOptions.getSelectedItem().toString();
                ArrayList<String> gradedSubmissions = s.viewGradedSubmission(quizNameGrades,
                                selectedCourse);

                if (!(gradedSubmissions.get(0).equals("None"))) {
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
                        JOptionPane.showMessageDialog(null, print,
                                null, JOptionPane.INFORMATION_MESSAGE);
                    }
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

    public void createCourse(String courseName) {
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
    }
}