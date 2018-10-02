package convert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class PasswordManager {

    private static final String FILEPATH = System.getProperty("user.dir") + File.separator + "Chrome Passwords.csv";
    private GsonBuilder gsonBuilder;

    public PasswordManager() throws IllegalArgumentException {
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting();
    }

    public static int getNumberOfLines() {
        BufferedReader reader = null;
        int lines = 0;
        try {
            reader = new BufferedReader(new FileReader(FILEPATH));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return lines;
    }

    public void ReadFromCSV() throws Exception {
        Credentials[] credentials = new Credentials[getNumberOfLines()];
        Encryption encryption = new Encryption();
        CSVReader reader = new CSVReader(new FileReader(FILEPATH));
        String [] nextLine;
        String[] CSVArray = new String[4];
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            CSVArray[0] = nextLine[0];
            CSVArray[1] = nextLine[1];
            CSVArray[2] = nextLine[2];
            CSVArray[3] = encryption.encrypt(nextLine[3]);

            PrintCSV(CSVArray);
            ConvertToJson(CSVArray);
        }
    }

    private static void PrintCSV (String[] CSVArray) {
        Decryption decryption = new Decryption();
        System.out.println("================================================");
        System.out.println("Name : " + CSVArray[0]);
        System.out.println("URL : " + CSVArray[1]);
        System.out.println("Username : " + CSVArray[2]);
        System.out.println("Encrypted Password : " + CSVArray[3]);
        String encrypted = CSVArray[3];
        try {
            String decrypted = decryption.decrypt(encrypted);
            System.out.println("Decrypted : " + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ConvertToJson(String[] CSVArray) {

        String name = CSVArray[0];
        String url = CSVArray[1];
        String username = CSVArray[2];
        String password = CSVArray[3];

        Credentials credentials = new Credentials(name,url,username,password);

        Gson gson = this.gsonBuilder.setPrettyPrinting().create();

        //Print JSON
        System.out.println(gson.toJson(credentials));

        return gson.toJson(credentials);
    }

    public static void main(String[] args) throws Exception {

        PasswordManager passwordManager = new PasswordManager();
        try {
            passwordManager.ReadFromCSV();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Total number of records : " + getNumberOfLines());

        //To check if encryption and decryption works
//        Encryption en=new Encryption();
//        String encryptedWord=en.encrypt("password");
//        System.out.println("Encrypted word is : " + encryptedWord);
//        Decryption de =new Decryption();
//        System.out.println("Decrypted word is : " +    de.decrypt(encryptedWord));
    }
}