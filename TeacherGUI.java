import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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

    String selectedCourse;

    JLabel courseOptions;
    JButton courseSelectButton;
    JButton courseCreateButton;
    JLabel courseSelection;
    JTextField courseTextField;

    JLabel quizOptions;
    JButton quizSelectButton;
    JButton quizDeleteButton;
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
        teacherGUI = new TeacherGUI();
        content.add(teacherGUI, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Buttons about the courses
        courseOptions = new JLabel("Math Science English");
        courseSelectButton = new JButton("Select");
        courseCreateButton = new JButton("Create");
        courseSelection = new JLabel("     Course: ");
        courseTextField = new JTextField("", 5);

        // Buttons about the quizzes
        quizOptions = new JLabel("Select a Course to View Quizzes");
        quizSelectButton = new JButton("Select");
        quizDeleteButton = new JButton("Delete");
        quizCreateButton = new JButton("Create");
        quizGradeButton = new JButton("Grade");
        quizSelection = new JLabel("     Quiz: ");
        quizTextField = new JTextField("", 5);

        // Button at the bottom
        editPasswordButton = new JButton("Edit Password");
        deleteAccountButton = new JButton("Delete Account");

        JPanel topPanel = new JPanel();
        content.add(topPanel, BorderLayout.NORTH);

        JPanel coursePanel = new JPanel();
        coursePanel.add(courseOptions);
        coursePanel.add(courseSelection);
        coursePanel.add(courseTextField);
        coursePanel.add(courseSelectButton);
        coursePanel.add(courseCreateButton);
        topPanel.add(coursePanel);

        JPanel quizPanel = new JPanel();
        quizPanel.add(quizOptions);
        quizPanel.add(quizSelection);
        quizPanel.add(quizTextField);
        quizPanel.add(quizSelectButton);
        quizPanel.add(quizDeleteButton);
        quizPanel.add(quizCreateButton);
        quizPanel.add(quizGradeButton);
        content.add(quizPanel, BorderLayout.EAST);

        JPanel editLeave = new JPanel();
        editLeave.add(editPasswordButton);
        editLeave.add(deleteAccountButton);
        content.add(editLeave, BorderLayout.SOUTH);

        courseSelectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        courseCreateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String courseName1 = courseTextField.getText();
                teacherGUI.createCourse(courseName1);
                JOptionPane.showMessageDialog(null, "Course Created",
                        null, JOptionPane.INFORMATION_MESSAGE);
            }
        });
        quizSelectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        quizDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        quizCreateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                        filename = JOptionPane.showInputDialog(null, "What is the name of the file?\\nThe first line should\" +\n" +
                                        "                            \"be the title of the quiz\\nAll remaining lines should be\" +\n" +
                                        "                            \"questions with answers on the same line, separated by commas",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((filename == null) || (filename.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Filename cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((filename == null) || (filename.isEmpty()));

                    t.writeQuizFromFile(selectedCourse, filename);
                } else {

                    do {
                        nameOfQuiz = JOptionPane.showInputDialog(null, "What is the name of the quiz you want to create?",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((nameOfQuiz == null) || (nameOfQuiz.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((nameOfQuiz == null) || (nameOfQuiz.isEmpty()));


                    String randomizedOrNot = JOptionPane.showInputDialog(null, "Would you like the quiz to be randomized?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);


                    ArrayList<String> newQuestions = new ArrayList<String>();
                    if (randomizedOrNot.equals(JOptionPane.YES_OPTION)) {
                        quizQuestions.add(String.valueOf(randomizedOrNot));
                    } else if (randomizedOrNot.equals(JOptionPane.NO_OPTION)) {
                        quizQuestions.add(String.valueOf(randomizedOrNot));
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
                        answerChoice = JOptionPane.showInputDialog(null, "What is the first answer choice?",
                                "Sign Up", JOptionPane.QUESTION_MESSAGE);
                        if ((answerChoice == null) || (answerChoice.isEmpty())) {
                            JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                    "University Card",
                                    JOptionPane.ERROR_MESSAGE);

                        } //end if

                    } while ((answerChoice == null) || (answerChoice.isEmpty()));

                    quizQuestions.add("/" + answerChoice);

                    String anotherAnswerChoice= JOptionPane.showInputDialog(null, "Is there another answer choice?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);

                    while (anotherAnswerChoice.equals(JOptionPane.YES_OPTION)) {
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
                        anotherAnswerChoice= JOptionPane.showInputDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                    }

                    String anotherQuestion= JOptionPane.showInputDialog(null, "Would you like to add another question?",
                            "Sign Up", JOptionPane.YES_NO_OPTION);
                    while (anotherQuestion.equals(JOptionPane.YES_OPTION)) {

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
                            answerChoice = JOptionPane.showInputDialog(null, "What is the first answer choice?",
                                    "Sign Up", JOptionPane.QUESTION_MESSAGE);
                            if ((answerChoice == null) || (answerChoice.isEmpty())) {
                                JOptionPane.showMessageDialog(null, "Name of Quiz cannot be empty!",
                                        "University Card",
                                        JOptionPane.ERROR_MESSAGE);

                            } //end if

                        } while ((answerChoice == null) || (answerChoice.isEmpty()));

                        quizQuestions.add("/" + answerChoice);

                        anotherAnswerChoice = JOptionPane.showInputDialog(null, "Is there another answer choice?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                        while (anotherAnswerChoice.equals(JOptionPane.YES_OPTION)) {
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

                            anotherAnswerChoice= JOptionPane.showInputDialog(null, "Is there another answer choice?",
                                    "Sign Up", JOptionPane.YES_NO_OPTION);
                        }
                        anotherQuestion = JOptionPane.showInputDialog(null, "Is there another question?",
                                "Sign Up", JOptionPane.YES_NO_OPTION);
                    }
                }
                t.createQuiz(selectedCourse, quizQuestions, nameOfQuiz);
            }
        });
        quizGradeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        editPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        deleteAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
    }


    public TeacherGUI() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // set oldX and oldY coordinates to beginning mouse press
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

    public void clearBoard() {

    }
    public void fillBoard() {

    }
    public void eraser() {

    }
    public void randomColor() {

    }
    public void getHexColor(Color hex) {
    }
    public void getRGBColor(Color rgb) {

    }

}

