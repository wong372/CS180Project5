ReadMe Document
Changes/Fixes to Project 4:
* It is no longer possible to select an incorrect answer due to the implementation of dropdown menus rather than the typing out of answers
* Added JavaDocs to the top of all of our classes
* Students are no longer able to take quizzes that have been deleted by a teacher
* Teachers can only grade the most recent quiz submission for each quiz by each student since only the latest submission is saved
How to Compile & Run Program:
To compile each class:
* Quiz.java: In the terminal, type “javac Quiz.java”
* QuizClient.java: In the terminal, type “javac QuizClient.java”
* QuizServer.java: In the terminal, type “javac QuizServer.java”
* QuizThread.java: In the terminal, type “javac QuizThread.java”
* Student.java: In the terminal, type “javac Student.java”
* StudentGUI.java: In the terminal, type “javac StudentGUI.java”
* Teacher.java: In the terminal, type “javac Teacher.java”
* TeacherGUI.java: In the terminal, type “javac TeacherGUI.java”


To run the server manually: In the terminal, type “java QuizServer”
To run the client(s) manually*: In the terminal, type “java QuizClient” and “java QuizClient2”


*Note: This is just to test the test cases we have provided in Tests.md. A different method is used to test more than two simultaneous clients.
________________


Part Submission:
Ashley Wong - Submitted Report on Brightspace.
Aaron Basiletti - Submitted Vocareum workspace.
Saahil Sanghi: Submitted Presentation video on Brightspace. 
________________


Classes:
Quiz.java
Description: This program contains methods for file processing for the login process.
Methods:
* readFileForLogin
   * Modifiers: public int
   * Parameters: String fileName, String username, String password
   * Description: Uses the user’s login credentials found in the login compilation file to determine what role they have: teacher or student.
* readFileForSignUp
   * Modifiers: public boolean
   * Parameters: String fileName, String username
   * Description: Checks login compilation file to see if there is already an account under the username that the new user wants to register their account under.
* writeFileForSignUp
   * Modifiers: public void
   * Parameters: String fileName, String username, String password, int role
   * Description: Writes the new user’s credentials to the login compilation file.
* rewriteFile
   * Modifiers: public boolean
   * Parameters: String filename, String username
   * Throws: IOException
   * Description: Rewrites files.
Teacher.java
        Description: This program has methods that can be called within other classes in order to         
        write and read from files.
        Methods
* createQuiz
   * Modifiers: public void
   * Parameters: String course, ArrayList<String>, String quizName
   * Description: Allows the user to write the name of a quiz to the file with the quizzes as well as create a new file with the quiz name that has the created questions in it.
* deleteQuiz
   * Modifiers: public void
   * Parameters: String course, String quizName
   * Description: Allows the user to delete a quiz by removing it from the list of quizzes in the course file.
* editQuizQuestions
   * Modifiers: public void
   * Parameters: String quizName, ArrayList<String>, String course
   * Description: Changes the questions in the quiz file to the new questions that the teacher has created.
* displayCourses
   * Modifiers: public ArrayList<String>
   * Parameters: none
   * Description: Reads all the current courses from the file and returns them to the user as an array list.
* displayQuestions
   * Modifiers: public ArrayList<String>
   * Parameters: String quizName, String course
   * Description: Reads all the current questions from the file and returns them to the user in the form of an array list.
* displayQuizzes
   * Modifiers: public ArrayList<String>
   * Parameters: String course
   * Description: Reads all the current quizzes from the file and returns them to the user in the form of an array list.
* displayStudentSubmission
   * Modifiers: public ArrayList<String>
   * Parameters: String username, String quizName, String course
   * Description: Reads in the student submission from a file and returns it to the user in the form of an array list.
* gradeSubmission
   * Modifiers: public void
   * Parameters: ArrayList<String> teacherGrades, String studentUsername, String quizName, String course, int totalScore
   * Description: Takes the grades submitted by the teacher and writes them to a file that can be viewed by the student in order to see their grades.
* writeQuizFromFile
   * Modifiers: public String
   * Parameters: String selectedCourse, String filename
   * Description: Writes the quiz from a filename that was inputted by the user and creates a new file for that quiz.
Student.java
        Description: This program allows the student to read and write from files.
        Methods:
* createStudentSubmission
   * Modifiers: public void 
   * Parameters: ArrayList<String> answer, String quizName, LocalDateTime time, String course
   * Description: Writes the student answers to a file along with the current time so that the teacher can grade it.
* viewGradedSubmission
   * Modifiers:ArrayList<String>
   * Parameters: String quizName, String course
   * Description: Reads the graded submission from a file so that the student can see their grade.
* getStudentResponsesFromFile
   * Modifiers: public ArrayList<String>
   * Parameters: String filename
   * Description: This allows the user to read in the student responses to questions from a file instead of inputting them manually.
TeacherGUI.java
        Description: A program that creates the GUI for the teacher to view and interact with.
        Extends: JComponent
        Implements: Runnable
        Methods:
* run
   * Modifiers: public void
   * Parameters: none
   * Description: Creates a visual aspect with a complex GUI for users with the ‘teacher’ role to interact with rather than have input/output come from the terminal.
* TeacherGUI
   * Modifiers: public
   * Parameters: String username, int role
   * Description: allows mouse/cursor response movements
StudentGUI.java
        Description: A program that creates the GUI for the student to view and interact with.
        Extends: JComponent
        Implements: Runnable
        Methods:
* run
   * Modifiers: public void
   * Parameters: none
   * Description: Creates a visual aspect with a complex GUI for users with the ‘student’ role to interact with rather than have input/output come from the terminal.
* StudentGUI
   * Modifiers: public
   * Parameters: String username, int role
   * Description: allows mouse/cursor response movements
QuizClient.java
Description: This program, the client class, takes in all the user inputs for the quiz program and sends them to the server for processing.
Methods:
* main
   * Modifiers: public static void
   * Parameters: String[] args
   * Throws: UnknownHostException, IOException, ClassNotFoundException
   * Description: Allows users to interact with the GUIs on the client side.
QuizServer,java
Description: This program, the server class, creates the thread to start the server and allow for concurrent users to connect.
Methods:
* main
   * Modifiers: public static void
   * Parameters: String[] args
   * Throws: UnknownHostException, IOException, ClassNotFoundException
   * Description: Allows users connect via socket.
QuizThread.java
        Description: This program contains the server functionality (file processing).
        Implements: Runnable
        Methods:
* QuizThread
   * Modifiers: public
   * Parameters: Socket socket
   * Description: sets threadSocket equal to socket
* run
   * Modifiers: public void
   * Parameters: none
   * Description: Runs through the entire process through file processing.