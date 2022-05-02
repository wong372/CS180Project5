import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
/**
 * A program that has student methods.
 *
 * <p>Purdue University -- CS18000 -- Spring 2022 -- Homework 02 -- Challenge</p>
 *
 * @author Katie Testin
 * @version April 11, 2021
 */
public class Teacher {
    private String username;

    public Teacher() {

    }

    public String getName() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void createQuiz(String course, ArrayList<String> quiz, String quizName) {
        File fileCourse = new File(course + ".txt");
        File fileQuiz = new File(course + "_" + quizName + ".txt");
        FileOutputStream fosCourse = null;
        try {
            fosCourse = new FileOutputStream(fileCourse, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileOutputStream fosQuiz = null;
        try {
            fosQuiz = new FileOutputStream(fileQuiz, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pwCourse = new PrintWriter(fosCourse);
        PrintWriter pwQuiz = new PrintWriter(fosQuiz);

        pwCourse.write(quizName + "\n");
        int counter3 = 0;
        while (counter3 < quiz.size()) {
            pwQuiz.write(quiz.get(counter3));
            counter3++;
        }

        pwCourse.close();
        pwQuiz.close();
    }

    public void deleteQuiz(String course, String quizName) {
        boolean isF = new File(course + ".txt").isFile();

        if (isF) {
            File f = new File(course + ".txt");

            try {
                FileInputStream fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileReader fr = null;
            try {
                fr = new FileReader(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line;

            ArrayList<String> allQuizzes = new ArrayList<String>();

            try {
                while (((line = bfr.readLine()) != null)) {
                    allQuizzes.add(line);
                }

                allQuizzes.remove(quizName);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                PrintWriter pw = new PrintWriter(fos);

                int counter4 = 0;
                while (counter4 < allQuizzes.size()) {
                    pw.write(allQuizzes.get(counter4) + "\n");
                    counter4++;
                }

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "This course does not exist.",
                    null, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void editQuizQuestions(String quizName, ArrayList<String> newQuestions, String course) {
        File f = new File(course + "_" + quizName + ".txt");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos);

        int counter6 = 0;
        while (counter6 < newQuestions.size()) {
            pw.write(newQuestions.get(counter6));
            counter6++;
        }
        pw.close();

    }

    public ArrayList<String> displayCourses() throws IOException {

        boolean fExists = new File("AllCourses.txt").isFile();
        ArrayList<String> allCourses = new ArrayList<String>();

        if (fExists) {
            File f = new File("AllCourses.txt");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter pw = new PrintWriter(fos);

            try {
                FileInputStream fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileReader fr = null;
            try {
                fr = new FileReader(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            if ((line) == null) {
                allCourses.add("None");
            } else {
                allCourses.add(line);
            }

            while ((line = bfr.readLine()) != null) {
                allCourses.add(line);
            }

            pw.close();
        } else {
            allCourses.add("None");
        }

        return allCourses;
    }

    public ArrayList<String> displayQuestions(String quizName, String course) {
        File f = new File(course + "_" + quizName + ".txt");
        ArrayList<String> allQuestions = new ArrayList<String>();
        try {
            if (f.createNewFile()) {
                JOptionPane.showMessageDialog(null, "There are no current questions.",
                        null, JOptionPane.INFORMATION_MESSAGE);
                allQuestions.add("None");
            } else {
                FileInputStream fis = new FileInputStream(f);
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line;

                line = bfr.readLine();

                // check if the order needs to be randomized
                if (line == null) {
                    JOptionPane.showMessageDialog(null, "File is empty.",
                            null, JOptionPane.INFORMATION_MESSAGE);
                    allQuestions.add("None");
                } else {
                    if (line.equals("1")) { //RANDOMIZATION
                        ArrayList<String> unrandomized = new ArrayList<>();
                        while ((line = bfr.readLine()) != null) {
                            unrandomized.add(line);
                        }


                        int counter10 = unrandomized.size();
                        int originalArraySize = unrandomized.size();
                        int counter11 = 0;

                        ArrayList<String> questionOrderRandomized = new ArrayList<String>();

                        while (counter11 < originalArraySize) {
                            Random rn = new Random();
                            int answer = rn.nextInt(counter10);
                            String[] elementsSplitUp = (unrandomized.get(answer)).split("/");

                            ArrayList<String> elementsSplitUpArray = new ArrayList<String>();

                            //convert the array into arraylist
                            int counter17 = 0;
                            while (counter17 < elementsSplitUp.length) {
                                elementsSplitUpArray.add(counter17, elementsSplitUp[counter17]);
                                counter17++;
                            }

                            String nextRandomizedElement = elementsSplitUpArray.get(0);
                            elementsSplitUpArray.remove(0);

                            // Mix up the question order
                            int arraySize = elementsSplitUpArray.size();
                            int counter18 = 0;
                            int counter19 = elementsSplitUpArray.size();
                            while (counter18 < arraySize) {
                                Random r = new Random();
                                int answer3 = r.nextInt(counter19);
                                nextRandomizedElement = nextRandomizedElement + "/" + elementsSplitUpArray.get(answer3);
                                elementsSplitUpArray.remove(answer3);
                                counter18++;
                                counter19--;
                            }

                            allQuestions.add(nextRandomizedElement);

                            unrandomized.remove(answer);
                            counter10--;
                            counter11++;
                        }

                    } else if (line.equals("2")) {
                        while ((line = bfr.readLine()) != null) {
                            allQuestions.add(line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allQuestions;
    }

    public ArrayList<String> displayQuizzes(String course) {
        File f = new File(course + ".txt");
        ArrayList<String> allQuizzes = new ArrayList<String>();

        try {
            if (f.createNewFile()) {
                JOptionPane.showMessageDialog(null, "There are no current quizzes.",
                        null, JOptionPane.INFORMATION_MESSAGE);
                allQuizzes.add("None");

            } else {
                FileOutputStream fos = new FileOutputStream(f, true);
                PrintWriter pw = new PrintWriter(fos);

                FileInputStream fis = new FileInputStream(f);
                FileReader fr = new FileReader(f);
                BufferedReader bfr = new BufferedReader(fr);
                String line = "";
                if ((line = bfr.readLine()) == null) {
                    allQuizzes.add("None");
                } else {
                    allQuizzes.add(line);
                }

                while ((line = bfr.readLine()) != null) {
                    allQuizzes.add(line);
                }

                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allQuizzes;
    }

    public ArrayList<String> displayStudentSubmission(String studentUsername, String quizName, String course) {
        boolean fExists = new File(course + "_" + studentUsername + "_" + quizName + ".txt").isFile();
        ArrayList<String> studentAnswers = new ArrayList<String>();

        if (fExists) {
            File fOriginal = new File(course + "_" + studentUsername + "_" + quizName + ".txt");

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
            System.out.println("There is no student submission for this quiz.");
            JOptionPane.showMessageDialog(null, "There is no student submission for this quiz.",
                    null, JOptionPane.INFORMATION_MESSAGE);
            studentAnswers.add("No submission");
        }

        return studentAnswers;
    }

    public void gradeSubmission(ArrayList<String> teacherGrades, String studentUsername, String quizName,
                                String course, int totalScore) {
        File fNew = new File(course + "_" + studentUsername + "_" + quizName + "_graded.txt");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fNew);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fos);

        int counter15 = 0;
        while (counter15 < teacherGrades.size()) {
            pw.write(teacherGrades.get(counter15));
            counter15++;
        }
        pw.write("/Total Score: " + totalScore + "\n");

        pw.close();
    }

    public String writeQuizFromFile(String selectedCourse, String fileName) {
        boolean fExists = new File(fileName).isFile();
        File fCourse = new File(selectedCourse + ".txt");
        ArrayList<String> teacherQuestions = new ArrayList<String>();

        String quizName = "";
        if (fExists) {
            File fileWithQuestions = new File(fileName);
            try {
                FileInputStream fis = new FileInputStream(fileWithQuestions);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileReader fr = null;
            try {
                fr = new FileReader(fileWithQuestions);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bfr = new BufferedReader(fr);
            String line;
            quizName = "";

            try {
                quizName = bfr.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                while (((line = bfr.readLine()) != null)) {
                    teacherQuestions.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            File fNew = new File(selectedCourse + "_" + quizName + ".txt");

            FileOutputStream fos2 = null;
            try {
                fos2 = new FileOutputStream(fCourse, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter pw2 = new PrintWriter(fos2);
            pw2.write(quizName + "\n");


            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fNew);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintWriter pw = new PrintWriter(fos);

            int counter15 = 0;
            while (counter15 < teacherQuestions.size()) {
                pw.write(teacherQuestions.get(counter15) + "\n");
                counter15++;
            }
            pw.close();

        } else {
            JOptionPane.showMessageDialog(null, "That file of questions did not exist",
                    "University Card", JOptionPane.INFORMATION_MESSAGE);
            teacherQuestions.add("None");
        }
        return quizName;
    }
}