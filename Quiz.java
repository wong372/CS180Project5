import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Quiz
 *
 * This program contains methods for file processing for the login process of Project 5.
 *
 * @author Katie Testin, Aaron Basiletti, Ashley Wong, Saahil Sanghi, L21
 *
 * @version May 02, 2022
 *
 */
public class Quiz {

    private static final String LOGINFILENAME = "logins.txt";

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
