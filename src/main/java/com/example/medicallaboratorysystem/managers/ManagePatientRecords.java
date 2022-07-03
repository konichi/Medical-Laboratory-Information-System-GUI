package com.example.medicallaboratorysystem.managers;

import com.example.medicallaboratorysystem.helpers.ReadFile;
import com.example.medicallaboratorysystem.helpers.WriteToFile;
import com.example.medicallaboratorysystem.models.Patient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ManagePatientRecords {
    //private ArrayList<Patient> patients;
    private final String FILENAME = "Patients.txt";
    private int count = 0;
    private int line = 0;
    private String[][] patients;

    //private ManageLaboratoryRequest mlr;
    private WriteToFile wtf;
    private ReadFile rf;
//    private MainMenu mm;

    public ManagePatientRecords() {
        //patients = new ArrayList<>();
        wtf = new WriteToFile();

        //TODO: call readRecords() here to store the records in patients[][]
        readRecords();
    }

    public int addPatient(Patient patient) {
        //patients.add(patient);

        int error = wtf.writeToPatients(FILENAME, patient);
        return error;
    }

    public ArrayList<Patient> searchRecordbyUID(String id) {
        ArrayList<Patient> foundRecords = new ArrayList<>();
        //TODO
        //convert to id to uppercase
        id = id.toUpperCase(Locale.ROOT);
        //put logic of search record by UID
        for (int i = 0; i < count; i++)
            if(Objects.equals(patients[i][0], id) && !Objects.equals(patients[i][9], "D")) {
                line = i;
                String patientCodeIdentifier = patients[i][0];
                String firstName = patients[i][1];
                String lastName = patients[i][2];
                String middleName = patients[i][3];
                String birthday = patients[i][4];
                String gender = patients[i][5];
                String address = patients[i][6];
                String phoneNo = patients[i][7];
                long tempNationalID = Long.parseLong(patients[i][8]);
                Patient foundRecord = new Patient(patientCodeIdentifier, lastName, firstName, middleName, birthday, gender, address, phoneNo, tempNationalID);
                foundRecords.add(foundRecord);
                return foundRecords;
            }
        return null;
    }

    public ArrayList<Patient> searchRecordbyNationalId(String id) {
        ArrayList<Patient> foundRecords = new ArrayList<>();
        //TODO
        //convert to id to uppercase
        id = id.toUpperCase(Locale.ROOT);
        for (int i = 0; i < count; i++)
            if (Objects.equals(patients[i][8], id) && !Objects.equals(patients[i][9], "D")) {
                line = i;
                String patientCodeIdentifier = patients[i][0];
                String firstName = patients[i][1];
                String lastName = patients[i][2];
                String middleName = patients[i][3];
                String birthday = patients[i][4];
                String gender = patients[i][5];
                String address = patients[i][6];
                String phoneNo = patients[i][7];
                long tempNationalID = Long.parseLong(patients[i][8]);
                Patient foundRecord = new Patient(patientCodeIdentifier, lastName, firstName, middleName, birthday, gender, address, phoneNo, tempNationalID);
                foundRecords.add(foundRecord);
                return foundRecords;
            }
        return null;
    }

    public ArrayList<Patient> searchRecord(String lastName, String firstName, String birthday) {
        ArrayList<Patient> foundRecords = new ArrayList<>();
        //TODO
        //put logic of search record by lastName, firstName, and birthday
        int searched = 0;
        for (int i = 0; i < count; i++) {
            try {
                if (!Objects.equals(patients[i][9], "D") && patients[i][1].equalsIgnoreCase(lastName) && patients[i][2].equalsIgnoreCase(firstName) && patients[i][4].equalsIgnoreCase(birthday)) {
                    String patientCodeIdentifier = patients[i][0];
                    String firstName1 = patients[i][1];
                    String lastName1 = patients[i][2];
                    String middleName = patients[i][3];
                    String birthday1 = patients[i][4];
                    String gender = patients[i][5];
                    String address = patients[i][6];
                    String phoneNo = patients[i][7];
                    long tempNationalID = Long.parseLong(patients[i][8]);
                    Patient foundRecord = new Patient(patientCodeIdentifier, lastName1, firstName1, middleName, birthday1, gender, address, phoneNo, tempNationalID);
                    foundRecords.add(foundRecord);
                    searched++;
                }
            } catch (NullPointerException ignored) {}
        }
        if (searched > 1);
            // go to new method
        else if (searched == 1)
            return foundRecords;
        else
            return null;
    }

    public int deleteRecord(String reason) {
        String D = "D;";
        String newLine = String.join("", D, reason);

        //TODO
        try {
            File file = new File(FILENAME);
            Scanner scannerFile = new Scanner(file);

            String tempLine = Files.readAllLines(Paths.get(FILENAME)).get(line);
            StringBuilder buffer = new StringBuilder();
            while (scannerFile.hasNextLine()) {
                buffer.append(scannerFile.nextLine()).append(System.lineSeparator());
            }
            String fileContents = buffer.toString();
            scannerFile.close();

            String line1 = String.join("", tempLine, newLine, ";");
            fileContents = fileContents.replaceAll(tempLine, line1);
            FileWriter fw = new FileWriter(FILENAME);
            fw.append(fileContents);
            fw.flush();

            String[] splitLine = tempLine.split(";");
            String UID = splitLine[0];

            System.out.println("Data of patient " + UID + " has been deleted.");
        } catch(IOException e) {
            System.out.println("Error occurred. Please try again.\n");
            return 1;
        }
        return 0; //return 0 if successful
    }

    public int editRecord(int update, String value) {
        //TODO
        try {
            File file = new File(FILENAME);
            Scanner scannerFile = new Scanner(file);

            String tempLine = Files.readAllLines(Paths.get(FILENAME)).get(line);
            StringBuilder buffer = new StringBuilder();
            while(scannerFile.hasNextLine()) {
                buffer.append(scannerFile.nextLine()).append(System.lineSeparator());
            }
            String fileContents = buffer.toString();
            scannerFile.close();

            String[] splitLine = tempLine.split(";");
            if(update==1)
                splitLine[6] = value;
            else
                splitLine[7] = value;

            String line1 = String.join(";", splitLine);
            line1 = String.join("", line1, ";");

            fileContents = fileContents.replaceAll(tempLine,line1);
            FileWriter fw = new FileWriter(FILENAME);
            fw.append(fileContents);
            fw.flush();

            String UID = splitLine[0];
            System.out.println("The Address/Phone Number of patient " + UID + " has been updated.");
        } catch (IOException e) {
            System.out.println("Error occurred. Please try again.\n");
            return 1;
        }
        return 0; //return 0 if successful
    }


    public void readRecords() {
        // get all lines in Patients.txt and save to String[][] patients
        String fileName = "Patients.txt";
        int error = rf.readPatients(fileName);
        if(error==1) {

        }

        patients = rf.getTempSearch();

        // count total non-null entries in String[][] patients
        for (String[] patient : patients) {
            if (patient[0] != null) {
                count++;
            }
        }


        int searched = 0;
        int[] lines = new int[256];
        String searchLast = null;
        String searchFirst = null;
        String searchBirthday = null;
    }

    //    generates Patient UID
    public String generateUID() {
        rf = new ReadFile();

        String[] tempUID = new String[7];
        tempUID[0] = "P";

        int B = Calendar.getInstance().get(Calendar.YEAR);
        String temp = String.valueOf(B);
        char[] cTemp = new char[temp.length()];
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++)
            tempUID[i + 1] = String.valueOf(cTemp[i]);

        int C = Calendar.getInstance().get(Calendar.MONTH)+1;
        temp = String.valueOf(C);
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++) {
            if (C > 9)
                tempUID[i + 5] = String.valueOf(cTemp[i]);
            else {
                tempUID[5] = "0";
                tempUID[6] = String.valueOf(cTemp[i]);
            }
        }

        //GET UID FROM PATIENTS.TXT
        int isFirst = rf.readUID(FILENAME);
        String prevUID = rf.getUID();

        String D;
        String E;
        String newUID;

        //CHECK IF FIRST UID TO BE GENERATED
        if(isFirst==1){
            newUID = "AAA00";
            String str = String.join("", tempUID);
            return String.join("",str, newUID);
        }
        else {
            D = prevUID.substring(7, 10);
            E = prevUID.substring(prevUID.length()-2);
        }

        //CHECK DDD - AAA/ZZZ and EE - 00/99
        int num = Integer.parseInt(E);
        char strTemp;
        int numTemp;
        if(num==99){
            if(D.charAt(2)!='Z' && D.charAt(2)<='Z') {
                strTemp = D.charAt(2);
                strTemp++;
                D = D.substring(0, D.length()-1);
                D = String.join("", D, String.valueOf(strTemp));
            } else if (D.charAt(1)!='Z' && D.charAt(1)<='Z') {
                strTemp = D.charAt(1);
                strTemp++;
                D = D.substring(0, D.length()-2);
                D = String.join("", D, String.valueOf(strTemp), "Z");
            } else {
                strTemp = D.charAt(0);
                strTemp++;
                D = String.join("", String.valueOf(strTemp), "ZZ");
            }
            E = "00";
        } else {
            numTemp = num;
            if(numTemp<9) {
                numTemp++;
                strTemp = '0';
                E = String.join("", String.valueOf(strTemp), String.valueOf(numTemp));
            } else {
                numTemp++;
                E = String.valueOf(numTemp);
            }
        }
        newUID = String.join("", D, E);

        String str = String.join("", tempUID);
        return String.join("", str, newUID);

    }
}

