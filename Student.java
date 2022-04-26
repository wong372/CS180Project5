import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * A program that has student methods.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Homework 02 -- Challenge</p>
 *
 * @author Katie Testin
 * @version April 11, 2021
 */
public class Student {
    private String username;

    public Student(String username) {
        this.username = username;
    }

    public void createStudentSubmission(ArrayList<String> answer, String quizName, LocalDateTime time, String course) {

        File f = new File(course + "_" + username + "_" + quizName + ".txt");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos);
        pw.write("Student Submission Time: " + time + "\n");

        int counter9 = 0;
        while (counter9 < answer.size()) {
            pw.write(answer.get(counter9));
            counter9++;
        }
        pw.close();
    }

    public ArrayList<String> viewGradedSubmission(String quizName, String course) {
        boolean fExists = new File(course + "_" + username + "_" + quizName + "_graded.txt").isFile();
        ArrayList<String> studentAnswers = new ArrayList<String>();

        if (fExists) {
            File fOriginal = new File(course + "_" + username + "_" + quizName + "_graded.txt");

            try {
                FileInputStream fis = new FileInputStream(fOriginal);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
            FileReader fr = null;
            try {
                fr = new FileReader(fOriginal);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line;

            try {
                while (((line = bfr.readLine()) != null)) {
                    studentAnswers.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "There is no graded submission available.",
                    null, JOptionPane.INFORMATION_MESSAGE);
            studentAnswers.add("None");
        }

        return studentAnswers;
    }

    public ArrayList<String> getStudentResponsesFromFile(String fileName) {
        boolean fExists = new File(fileName).isFile();
        ArrayList<String> studentResponses = new ArrayList<String>();

        if (fExists) {
            File fileWithResponses = new File(fileName);
            try {
                FileInputStream fis = new FileInputStream(fileWithResponses);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileReader fr = null;
            try {
                fr = new FileReader(fileWithResponses);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line;
            try {
                while (((line = bfr.readLine()) != null)) {
                    studentResponses.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "File with responses did not exist.",
                    null, JOptionPane.INFORMATION_MESSAGE);
            studentResponses.add("None");
        }
        return studentResponses;
    }
}
