/*
* Class: ManagePatientRecords
* Manages Patient Records such as
*   adding a new patient record,
*   searching for a patient record,
*   deleting a patient record,
*   and editing a patient record.
* */

import com.toedter.calendar.JDateChooser;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ManagePatientRecords implements ActionListener {
    private ArrayList<Patient> patients;

    private ManageLaboratoryRequest mlr;
    private WriteToFile wtf;
    private ReadFile rf;
    private MainMenu mm;

    private JFrame frame;
    private JPanel panel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton returnButton;

    long x;
    int searched = 0;

    public void managePatientRecords() {
        mm = new MainMenu();
        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(960, 540);
        frame.setTitle("Patient Records");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

        JLabel selectLabel = new JLabel("Select a transaction: ");
        selectLabel.setBounds(10, 10, 250, 25);
        panel.add(selectLabel);

        addButton = new JButton("Add New Patient");
        addButton.addActionListener(this);
        addButton.setBounds(10, 10, 500, 25);
        panel.add(addButton);

        editButton = new JButton("Edit Patient Record");
        editButton.addActionListener(this);
        addButton.setBounds(10, 30, 500, 25);
        panel.add(addButton);

        deleteButton = new JButton("Delete Patient Record");
        deleteButton.addActionListener(this);
        deleteButton.setBounds(10, 50, 500, 25);
        panel.add(deleteButton);

        searchButton = new JButton("Search Patient Record");
        searchButton.addActionListener(this);
        searchButton.setBounds(10, 70, 500, 25);
        panel.add(searchButton);

        returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(this);
        returnButton.setBounds(10, 90, 500, 25);
        panel.add(returnButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        if (e.getSource() == addButton)
            addNewPatient();
        else if (e.getSource() == editButton)
            editPatientRecord();
        else if (e.getSource() == deleteButton)
            deletePatientRecord();
        else if (e.getSource() == searchButton)
            searchPatientRecord();
        else if (e.getSource() == returnButton)
            mm.mainMenu();
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
        String fileName = "Patients.txt";
        int isFirst = rf.readUID(fileName);
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

//    adds a new patient record
    public void addNewPatient() {
        patients = new ArrayList<>();
        wtf = new WriteToFile();
        mm = new MainMenu();

        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(960, 540);
        frame.setTitle("Add New Patient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setLayout(null);

        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameLabel.setBounds(10, 10, 80, 20);
        panel.add(firstNameLabel);

        JTextField firstNameText = new JTextField();
        firstNameText.setBounds(100, 10, 250, 25);
        panel.add(firstNameText);

        JLabel lastNameLabel = new JLabel("Last Name: ");
        lastNameLabel.setBounds(10, 40, 80, 20);
        panel.add(lastNameLabel);

        JTextField lastNameText = new JTextField();
        lastNameText.setBounds(100, 40, 250, 25);
        panel.add(lastNameText);

        JLabel middleNameLabel = new JLabel("Middle Name: ");
        middleNameLabel.setBounds(10, 70, 80, 20);
        panel.add(middleNameLabel);

        JTextField middleNameText = new JTextField();
        middleNameText.setBounds(100, 70, 250, 25);
        panel.add(middleNameText);

        JLabel birthdayLabel = new JLabel("Birthday: ");
        birthdayLabel.setBounds(10, 100, 80, 20);
        panel.add(birthdayLabel);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(100, 100, 250, 25);
        dateChooser.setDateFormatString("yyyyMMdd");
//        String tempBirthday = ((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
        frame.getContentPane().add(dateChooser);
        panel.add(dateChooser);

        JLabel genderLabel = new JLabel("Gender: ");
        genderLabel.setBounds(10, 130, 80, 20);
        panel.add(genderLabel);

        JRadioButton femaleButton = new JRadioButton();
        femaleButton.setText("Female");
        femaleButton.setBounds(100, 130, 100, 25);
        panel.add(femaleButton);

        JRadioButton maleButton = new JRadioButton();
        maleButton.setText("Male");
        maleButton.setBounds(200, 130, 250, 25);
        panel.add(maleButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(femaleButton);
        buttonGroup.add(maleButton);

        JLabel addressLabel = new JLabel("Address: ");
        addressLabel.setBounds(10, 160, 80, 20);
        panel.add(addressLabel);

        JTextField addressText = new JTextField();
        addressText.setBounds(100, 160, 250, 25);
        panel.add(addressText);

        JLabel phoneNoLabel = new JLabel("Phone No.: ");
        phoneNoLabel.setBounds(10, 190, 80, 20);
        panel.add(phoneNoLabel);

        JTextField phoneNoText = new JTextField();
        phoneNoText.setBounds(100, 190, 250, 25);
        phoneNoText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    x = Long.parseLong(phoneNoText.getText());
                } catch (NumberFormatException nfe) {
                    String temp = phoneNoText.getText();
                    temp = temp.substring(0, temp.length()-1);
                    phoneNoText.setText(temp);
                }
            }
        });
        panel.add(phoneNoText);

        JLabel nationalIdLabel = new JLabel("National ID No.: ");
        nationalIdLabel.setBounds(10, 220, 100, 20);
        panel.add(nationalIdLabel);

        JTextField nationalIdText = new JTextField();
        nationalIdText.setBounds(100, 220, 250, 25);
        nationalIdText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    x = Long.parseLong(nationalIdText.getText());
                } catch (NumberFormatException nfe) {
                    String temp = nationalIdText.getText();
                    temp = temp.substring(0, temp.length()-1);
                    nationalIdText.setText(temp);
                }
            }
        });
        panel.add(nationalIdText);

        JLabel saveLabel = new JLabel("Save Patient Record?");
        saveLabel.setBounds(10, 280, 500, 20);
        panel.add(saveLabel);

        JButton yesButton = new JButton("YES");
        yesButton.setBounds(10, 300, 80, 25);
//        if (firstNameText.getText().isEmpty() || lastNameText.getText().isEmpty() || middleNameText.getText().isEmpty() || birthdayText.getText().isEmpty() || (femaleButton.isSelected() || maleButton.isSelected()) || addressText.getText().isEmpty() || phoneNoText.getText().isEmpty() || nationalIdText.getText().isEmpty()) {
//
//        }
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // add input validation
                String patientCodeIdentifier = generateUID();
                String firstName = firstNameText.getText();
                String lastName = lastNameText.getText();
                String middleName = middleNameText.getText();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String birthday = dateFormat.format(dateChooser.getDate());

                String gender = null;
                if (femaleButton.isSelected())
                    gender = "F";
                else if (maleButton.isSelected())
                    gender = "M";
                String address = addressText.getText();
                String phoneNo = phoneNoText.getText();
                long nationalIdNo = Long.parseLong(nationalIdText.getText());

                Patient patient = new Patient(patientCodeIdentifier, lastName, firstName, middleName, birthday, gender, address, phoneNo, nationalIdNo);
                patients.add(patient);

                String filename = "Patients.txt";
                int error = wtf.writeToPatients(filename, patient);
                if (error == 1) {
                    frame = new JFrame();
                    panel = new JPanel();

                    frame.setSize(960, 540);
                    frame.setTitle("Error Message");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    panel.setLayout(new BorderLayout());

                    JLabel errorLabel = new JLabel("Error occurred. Please try again.");
                    errorLabel.setBounds(10, 10, 250, 25);
                    errorLabel.setForeground(Color.RED);
                    panel.add(errorLabel);

                    Timer timer = new Timer(5000, null);
                    timer.setRepeats(false);
                    timer.start();

                    frame.dispose();
                    addNewPatient();
                }

                frame.dispose();

                frame = new JFrame();
                panel = new JPanel();

                frame.setSize(960, 540);
                frame.setTitle("Add New Patient");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                panel.setLayout(boxLayout);
                panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

                JLabel messageDialogue = new JLabel("Patient record successfully added.");
                messageDialogue.setBounds(10, 10, 250, 25);
                messageDialogue.setForeground(Color.BLUE);
                panel.add(messageDialogue);

                JLabel addOrReturnLabel = new JLabel("Would you like to add another patient or return to the main menu?");
                addOrReturnLabel.setBounds(10, 10, 250, 25);
                panel.add(addOrReturnLabel);

                JButton addNewButton = new JButton("Add New Patient");
                addNewButton.setBounds(10, 30, 80, 25);
                addNewButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        addNewPatient();
                    }
                });
                panel.add(addNewButton);

                JButton returnButton = new JButton("Return to the Main Menu");
                returnButton.setBounds(100, 30, 80, 25);
                returnButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        mm.mainMenu();
                    }
                });
                panel.add(returnButton);

                frame.add(panel);
                frame.setVisible(true);
            }
        });
        panel.add(yesButton);

        JButton noButton = new JButton("NO");
        noButton.setBounds(100, 300, 80, 25);
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

                frame = new JFrame();
                panel = new JPanel();

                frame.setSize(960, 540);
                frame.setTitle("Patient Records");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                panel.setLayout(boxLayout);
                panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

                JLabel messageDialogue = new JLabel("Patient record not added.");
                messageDialogue.setBounds(10, 10, 250, 25);
                messageDialogue.setForeground(Color.RED);
                panel.add(messageDialogue);

                JLabel addOrReturnLabel = new JLabel("Would you like to add another patient or return to the main menu?");
                addOrReturnLabel.setBounds(10, 50, 250, 25);
                panel.add(addOrReturnLabel);

                JButton addNewButton = new JButton("Add New Patient");
                addNewButton.setBounds(10, 80, 80, 25);
                addNewButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        addNewPatient();
                    }
                });
                panel.add(addNewButton);

                JButton returnButton = new JButton("Return to the Main Menu");
                returnButton.setBounds(100, 80, 80, 25);
                returnButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        mm.mainMenu();
                    }
                });
                panel.add(returnButton);

                frame.add(panel);
                frame.setVisible(true);
            }
        });
        panel.add(noButton);

        frame.add(panel);
        frame.setVisible(true);
    }

//    searches for a patient record
    public void searchPatientRecord() {
        mlr = new ManageLaboratoryRequest();
        rf = new ReadFile();
        mm = new MainMenu();

        int line=searchRecord();
        String input;

        // get all files in Patients.txt and save to String[][] patients
        String fileName = "Patients.txt";
        int error = rf.readPatients(fileName);
        if(error==1) {
            frame.dispose();

            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Search Patient Records");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel.setLayout(new BorderLayout());

            JLabel errorLabel = new JLabel("Patients.txt file not found. Please try again.");
            errorLabel.setBounds(10, 10, 250, 25);
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel);

            Timer timer = new Timer(5000, null);
            timer.setRepeats(false);
            timer.start();

            frame.dispose();
            searchPatientRecord();
        }
        String[][] patients = rf.getTempSearch();

        if(line==-1)
            searchPatientRecord();
        else if(line==-2) {
            frame.dispose();

            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Search Patient Records");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(boxLayout);
            panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

            JLabel dialogueMessage = new JLabel("No record found.");
            dialogueMessage.setBounds(10, 10, 100, 20);
            dialogueMessage.setForeground(Color.RED);
            panel.add(dialogueMessage);

            JLabel searchOrReturnLabel = new JLabel("Would you like to search for another patient record or return to the main menu?");
            searchOrReturnLabel.setBounds(10, 10, 300, 25);
            panel.add(searchOrReturnLabel);

            JButton searchButton = new JButton("Search for another patient record");
            searchButton.setBounds(10, 30, 80, 25);
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    searchPatientRecord();
                }
            });
            panel.add(searchButton);

            JButton returnButton = new JButton("Return to the Main Menu");
            returnButton.setBounds(100, 30, 80, 25);
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    mm.mainMenu();
                }
            });
            panel.add(returnButton);

            frame.add(panel);
            frame.setVisible(true);
        } else {
            frame.dispose();

            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Search Patient Records");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel.setLayout(new BorderLayout());

            String patientUID = patients[line][0];

            JLabel UIDLabel = new JLabel("Patient's UID: " + patients[line][0]);
            UIDLabel.setBounds(10, 10, 80, 20);
            panel.add(UIDLabel);

            JLabel nameLabel = new JLabel("Name: " + patients[line][1] + ", " + patients[line][2] + " " + patients[line][3]);
            nameLabel.setBounds(10, 40, 80, 20);
            panel.add(nameLabel);

            JLabel birthdayLabel = new JLabel("Birthday: " + patients[line][4]);
            birthdayLabel.setBounds(10, 70, 80, 20);
            panel.add(birthdayLabel);

            JLabel addressLabel = new JLabel("Address: " + patients[line][6]);
            addressLabel.setBounds(10, 100, 80, 20);
            panel.add(addressLabel);

            JLabel phoneNoLabel = new JLabel("Phone Number: " + patients[line][7]);
            phoneNoLabel.setBounds(10, 130, 80, 20);
            panel.add(phoneNoLabel);

            JLabel nationalIDNoLabel = new JLabel("National ID no.: " + patients[line][8]);
            nationalIDNoLabel.setBounds(10, 160, 80, 20);
            panel.add(nationalIDNoLabel);

            // get all lines in services.txt and save to String[][] services
            fileName = "services.txt";
            error = rf.readServices(fileName);
            if(error==1) {
                frame.dispose();

                frame = new JFrame();
                panel = new JPanel();

                frame.setSize(960, 540);
                frame.setTitle("Search Patient Records");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                panel.setLayout(new BorderLayout());

                JLabel errorMessage = new JLabel("services.txt file not found. Please try again.");
                errorMessage.setBounds(10, 10, 100, 20);
                errorMessage.setForeground(Color.RED);
                panel.add(errorMessage);

                frame.add(panel);
                frame.setVisible(true);

                searchPatientRecord();
                frame.dispose();
            }
            String[][] services = rf.getTempServ();
            services = sortArray(services);
            // count all services
            int count = 0;
            for (String[] service : services)
                for (int j = 0; j < services[0].length; j++)
                    if (service[0] != null && service[0].length() == 3) {
                        count++;
                        break;
                    }

            JLabel requestUIDLabel = new JLabel("Request's UID");
            requestUIDLabel.setBounds(10, 250, 100, 20);
            panel.add(requestUIDLabel);

            JLabel labTestTypeLabel = new JLabel("Lab Test Type");
            labTestTypeLabel.setBounds(100, 250, 100, 20);
            panel.add(labTestTypeLabel);

            JLabel requestDateLabel = new JLabel("Request Date");
            requestDateLabel.setBounds(190, 250, 100, 20);
            panel.add(requestDateLabel);

            JLabel requestLabel = new JLabel("Result");
            requestLabel.setBounds(280, 250, 100, 20);
            panel.add(requestLabel);

            String code;
            for(int i=0; i<count; i++) {
                code = services[i][0];
                fileName = code + "_Requests.txt";
                error = rf.readRequests(fileName);
                if (error == 1) {
                    frame.dispose();

                    frame = new JFrame();
                    panel = new JPanel();

                    frame.setSize(960, 540);
                    frame.setTitle("Search Patient Records");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    panel.setLayout(new BorderLayout());

                    JLabel errorMessage = new JLabel(code + "_Requests.txt file not found. Please try again.");
                    errorMessage.setBounds(10, 10, 100, 20);
                    errorMessage.setForeground(Color.RED);
                    panel.add(errorMessage);

                    frame.add(panel);
                    frame.setVisible(true);

                    frame.dispose();
                    searchPatientRecord();
                }
                String[][] requests = rf.getTempReq();
                requests = sortDate(requests);
                int length = 0;
                for (String[] request : requests)
                    for (int j = 0; j < services[0].length; j++)
                        if (request[0] != null && request[0].length() == 15) {
                            length++;
                            break;
                        }

                File file = new File(fileName);
                boolean exists = file.exists();

                int y1 = 250;
                for (int j = length - 1; j >= 0; j--) {
                    int x1 = 10;
                    y1+=30;
                    if (patientUID.equals(requests[j][1]) && exists) {
                        String tempRUID = requests[j][0];
                        String tempLab = requests[j][0].substring(0, 3);
                        String tempDate = requests[j][2];
                        String tempResult = requests[j][4];

                        JLabel requestUID = new JLabel(tempRUID);
                        requestUID.setBounds(x1, y1, 100, 20);
                        panel.add(requestUID);

                        x1+=90;

                        JLabel labTestType = new JLabel(tempLab);
                        labTestType.setBounds(x1, y1, 100, 20);
                        panel.add(labTestType);

                        x1+=90;

                        JLabel requestDate = new JLabel(tempDate);
                        requestDate.setBounds(x1, y1, 100, 20);
                        panel.add(requestDate);

                        x1+=90;

                        JLabel results = new JLabel(tempResult);
                        results.setBounds(x1, y1, 100, 20);
                        panel.add(results);
                    }
                }

                Arrays.stream(requests).forEach(x -> Arrays.fill(x, null));
            }
            frame.add(panel);
            frame.setVisible(true);

            JFrame popUpFrame = new JFrame();
            JPanel popUpPanel = new JPanel();

            popUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            popUpPanel.setLayout(new BorderLayout());

            JLabel printDialogue = new JLabel("Do you want to print a laboratory test result");
            printDialogue.setHorizontalAlignment(JLabel.CENTER);
            printDialogue.setVerticalAlignment(JLabel.NORTH);
            popUpPanel.add(printDialogue);

            JButton yesButton = new JButton("YES");
            yesButton.setVerticalAlignment(JLabel.SOUTH);
            yesButton.setHorizontalAlignment(JButton.EAST);
            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popUpFrame.dispose();

                    String[] ret = mlr.searchLaboratoryRequest(2);
                    String fileName = "Patients.txt";
                    rf.readPatients(fileName);
                    String[][] patients = rf.getTempSearch();

                    String name = patients[line][1] + ", " + patients[line][2] + " " + patients[line][3];
                    String sUID = ret[0];
                    String pUID = patients[line][0];
                    String birthday = patients[line][4];
                    String gender = patients[line][5];
                    String phoneNo = patients[line][7];
                    String test = ret[3];
                    String result = ret[2];
                    String rDate = ret[1];

                    // calculating age
                    int birthYear = Integer.parseInt(birthday.substring(0, 4));
                    int birthMonth = Integer.parseInt(birthday.substring(4, 6));
                    int birthDay = Integer.parseInt(birthday.substring(birthday.length()-2));
                    LocalDate start = LocalDate.of(birthYear, birthMonth, birthDay); // use for age-calculation: LocalDate.now()
                    LocalDate end = LocalDate.now(ZoneId.systemDefault());
                    int age = (int) ChronoUnit.YEARS.between(start, end);

                    //print pdf
                    String pdfName = patients[line][1] + "_" + sUID + "_" + rDate + ".pdf";
                    try {
                        PrintLabResults.printPdf(pdfName, name, sUID, pUID, rDate, birthday, gender, phoneNo, age, test, result);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            });
            popUpPanel.add(yesButton);

            JButton noButton = new JButton("NO");
            noButton.setVerticalAlignment(JLabel.SOUTH);
            noButton.setHorizontalAlignment(JButton.WEST);
            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popUpFrame.dispose();
                    mm.mainMenu();
                }
            });
            popUpPanel.add(noButton);

            popUpFrame.add(panel);
            popUpFrame.setVisible(true);
        }
    }

//    deletes a patient record
    public void deletePatientRecord() {
        mm = new MainMenu();

        int line=searchRecord();

        if(line==-1)
            deletePatientRecord();
        else if(line==-2) {
            frame.dispose();

            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Delete A Patient Record");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(boxLayout);
            panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

            JLabel dialogueMessage = new JLabel("No record found.");
            dialogueMessage.setBounds(10, 10, 100, 20);
            dialogueMessage.setForeground(Color.RED);
            panel.add(dialogueMessage);

            JLabel searchOrReturnLabel = new JLabel("Would you like to delete another patient record or return to the main menu?");
            searchOrReturnLabel.setBounds(10, 10, 300, 25);
            panel.add(searchOrReturnLabel);

            JButton searchButton = new JButton("Delete another patient record");
            searchButton.setBounds(10, 30, 80, 25);
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    deletePatientRecord();
                }
            });
            panel.add(searchButton);

            JButton returnButton = new JButton("Return to the Main Menu");
            returnButton.setBounds(100, 30, 80, 25);
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    mm.mainMenu();
                }
            });
            panel.add(returnButton);

            frame.add(panel);
            frame.setVisible(true);
        } else {
            frame.dispose();

            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Delete A Patient Record");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(boxLayout);
            panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

            JLabel reasonLabel = new JLabel("Please state reason for deletion: ");
            reasonLabel.setBounds(10, 10, 100, 20);
            panel.add(reasonLabel);

            final int[] success = {0};
            final String[] UID = new String[1];
            JTextField reasonText = new JTextField();
            reasonText.setBounds(10, 120, 100, 25);
            reasonText.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String reason = reasonText.getText();
                    String D = "D;";
                    String newLine = String.join("", D, reason);
                    String fileName = "Patients.txt";
                    try {
                        File file = new File(fileName);
                        Scanner scannerFile = new Scanner(file);

                        String tempLine = Files.readAllLines(Paths.get(fileName)).get(line);
                        StringBuilder buffer = new StringBuilder();
                        while(scannerFile.hasNextLine()) {
                            buffer.append(scannerFile.nextLine()).append(System.lineSeparator());
                        }
                        String fileContents = buffer.toString();
                        scannerFile.close();

                        String line1 = String.join("",tempLine,newLine,";");
                        fileContents = fileContents.replaceAll(tempLine,line1);
                        FileWriter fw = new FileWriter(fileName);
                        fw.append(fileContents);
                        fw.flush();

                        String[] splitLine = tempLine.split(";");
                        UID[0] = splitLine[0];

                        success[0] = 1;
                    } catch(IOException ioe) {
                        success[0] = -1;
                    }
                }
            });
            panel.add(reasonText);

            frame.add(panel);
            frame.setVisible(true);

            frame.dispose();

            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Delete A Patient Record");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BoxLayout newBoxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
            panel.setLayout(newBoxLayout);
            panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

            if (success[0] == 1) {
                String dialogue = "Data of patient " + UID[0] + " has been deleted.";
                JLabel successDialogue = new JLabel(dialogue);
                successDialogue.setBounds(10, 10, 200, 20);
                successDialogue.setForeground(Color.BLUE);
                panel.add(successDialogue);
            } else {
                JLabel errorLabel = new JLabel("Error occurred. Please try again.");
                errorLabel.setBounds(10, 10, 250, 25);
                errorLabel.setForeground(Color.RED);
                panel.add(errorLabel);

                Timer timer = new Timer(5000, null);
                timer.setRepeats(false);
                timer.start();

                frame.dispose();
                deletePatientRecord();
            }
            JLabel deleteOrReturnLabel = new JLabel("Would you like to delete another patient record or return to the main menu?");
            deleteOrReturnLabel.setBounds(10, 10, 250, 25);
            panel.add(deleteOrReturnLabel);

            JButton deleteButton = new JButton("Delete Patient Record");
            deleteButton.setBounds(10, 30, 80, 25);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    deletePatientRecord();
                }
            });
            panel.add(deleteButton);

            JButton returnButton = new JButton("Return to the Main Menu");
            returnButton.setBounds(100, 30, 80, 25);
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    mm.mainMenu();
                }
            });
            panel.add(returnButton);

            frame.add(panel);
            frame.setVisible(true);
        }
    }

//    edits a patients address or phone number
    public void editPatientRecord() {
        mm = new MainMenu();
        Scanner scanner = new Scanner(System.in);

        int line=searchRecord();
        String input;
        int update=0;

        if(line==-1)
            editPatientRecord();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to try again or return to the main menu?");
                System.out.println("[1] Edit another patient record");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    editPatientRecord();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                editPatientRecord();
            else
                mm.mainMenu();
        } else {
            do {
                System.out.println("Would you like to update the patient's Address or Phone Number?");
                System.out.println("[1] Address");
                System.out.println("[2] Phone Number");
                System.out.print("Select information to update: ");
                input = scanner.next();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    editPatientRecord();
                }
            } while(!input.equals("1") && !input.equals("2"));
            System.out.println();
            if(input.equals("1"))
                update = 1;
            else
                update = 2;
        }

        String newLine = "";
        scanner.nextLine();
        switch (update) {
            case 1 -> {
                System.out.print("Enter the patient's new Address: ");
                newLine = scanner.nextLine();
            }
            case 2 -> {
                System.out.print("Enter the patient's new Phone Number: ");
                newLine = scanner.next();
            }
        }
        String fileName = "Patients.txt";
        try {
            File file = new File(fileName);
            Scanner scannerFile = new Scanner(file);

            String tempLine = Files.readAllLines(Paths.get(fileName)).get(line);
            StringBuilder buffer = new StringBuilder();
            while(scannerFile.hasNextLine()) {
                buffer.append(scannerFile.nextLine()).append(System.lineSeparator());
            }
            String fileContents = buffer.toString();
            scannerFile.close();

            String[] splitLine = tempLine.split(";");
            if(update==1)
                splitLine[6] = newLine;
            else
                splitLine[7] = newLine;

            String line1 = String.join(";", splitLine);
            line1 = String.join("", line1, ";");

            fileContents = fileContents.replaceAll(tempLine,line1);
            FileWriter fw = new FileWriter(fileName);
            fw.append(fileContents);
            fw.flush();

            String UID = splitLine[0];
            System.out.println("The Address/Phone Number of patient " + UID + " has been updated.");
        } catch (IOException e) {
            System.out.println("Error occurred. Please try again.\n");
            editPatientRecord();
        }

        do {
            System.out.print("Do you want to edit another patient record? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y"))
                editPatientRecord();
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));
    }

/*
* searches Patients.txt for methods:
*   searchPatientRecord(), deletePatientRecord(), and editPatientRecord()
* returns the line number needed
* */
    public int searchRecord() {
        rf = new ReadFile();

        // get all lines in Patients.txt and save to String[][] patients
        String fileName = "Patients.txt";
        int error = rf.readPatients(fileName);
        if(error==1) {
            frame = new JFrame();
            panel = new JPanel();

            frame.setSize(960, 540);
            frame.setTitle("Searching Records");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel.setLayout(new BorderLayout());

            JLabel errorMessage = new JLabel("Patients.txt file not found. Please try again.");
            errorMessage.setBounds(10, 10, 100, 20);
            errorMessage.setForeground(Color.RED);
            panel.add(errorMessage);

            Timer timer = new Timer(5000, null);
            timer.setRepeats(false);
            timer.start();

            frame.add(panel);
            frame.setVisible(true);

            frame.dispose();
            return -1;
        }
        String[][] patients = rf.getTempSearch();

        // count total non-null entries in String[][] patients
        int count = 0;
        for (String[] patient : patients)
            if (patient[0] != null)
                count++;
        int finalCount = count;

        int line = 0;
        int[] lines = new int[256];

        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(960, 540);
        frame.setTitle("Searching Records");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(panel);
        frame.setVisible(true);

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

        JLabel patientUIDLabel = new JLabel("Do you know the patient's UID?");
        patientUIDLabel.setBounds(10, 10, 500, 20);
        panel.add(patientUIDLabel);

        JButton yesUIDButton = new JButton("YES");
        yesUIDButton.setBounds(10, 40, 80, 25);
        yesUIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

                frame = new JFrame();
                panel = new JPanel();

                frame.setSize(960, 540);
                frame.setTitle("Searching Records");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                panel.setLayout(boxLayout);
                panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

                JLabel patientUIDLabel = new JLabel("Enter patient's UID:");
                patientUIDLabel.setBounds(10, 10, 80, 20);
                panel.add(patientUIDLabel);

                JTextField patientUIDText = new JTextField();
                patientUIDText.setBounds(200, 10, 250, 25);
                panel.add(patientUIDText);

                JButton searchButton = new JButton("SEARCH");
                searchButton.setBounds(10, 40, 250, 25);
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String searchUID = patientUIDText.getText();

                        for (int i = 0; i < finalCount; i++)
                            if(Objects.equals(patients[i][0], searchUID) && !Objects.equals(patients[i][9], "D")) {
                                searched = 1;
                                lines[0] = i;
                                break;
                            } else
                                lines[0] = -2;
                    }
                });
                panel.add(searchButton);

                frame.add(panel);
                frame.setVisible(true);
            }
        });
        panel.add(yesUIDButton);

        frame.add(panel);
        frame.setVisible(true);

        JButton noUIDButton = new JButton("NO");
        noUIDButton.setBounds(100, 40, 80, 25);
        noUIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

                frame = new JFrame();
                panel = new JPanel();

                frame.setSize(960, 540);
                frame.setTitle("Searching Records");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                panel.setLayout(boxLayout);
                panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

                JLabel nationalIDLabel = new JLabel("Do you know the patient's National ID no.?");
                nationalIDLabel.setBounds(10, 10, 500, 20);
                panel.add(nationalIDLabel);

                JButton yesIDButton = new JButton("YES");
                yesIDButton.setBounds(10, 40, 80, 25);
                yesIDButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();

                        frame = new JFrame();
                        panel = new JPanel();

                        frame.setSize(960, 540);
                        frame.setTitle("Searching Records");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                        panel.setLayout(boxLayout);
                        panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

                        JLabel IDLabel = new JLabel("Enter patient's National ID no.:");
                        IDLabel.setBounds(10, 10, 80, 20);
                        panel.add(IDLabel);

                        JTextField IDText = new JTextField();
                        IDText.setBounds(200, 10, 250, 25);
                        panel.add(IDText);

                        JButton searchButton = new JButton("SEARCH");
                        searchButton.setBounds(10, 40, 250, 25);
                        searchButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String searchID = IDText.getText();

                                for (int i = 0; i < finalCount; i++)
                                    if (Objects.equals(patients[i][8], searchID) && !Objects.equals(patients[i][9], "D")) {
                                        searched = 1;
                                        lines[0] = i;
                                        break;
                                    } else
                                        lines[0] = -2;
                            }
                        });
                        panel.add(searchButton);

                        frame.add(panel);
                        frame.setVisible(true);
                    }
                });
                panel.add(yesIDButton);

                frame.add(panel);
                frame.setVisible(true);

                JButton noIDButton = new JButton("NO");
                noIDButton.setBounds(100, 40, 80, 25);
                noIDButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();

                        frame = new JFrame();
                        panel = new JPanel();

                        frame.setSize(960, 540);
                        frame.setTitle("Searching Records");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
                        panel.setLayout(boxLayout);
                        panel.setBorder(new EmptyBorder(new Insets(10, 10, 100, 10)));

                        JLabel lastNameLabel = new JLabel("Enter patient's last name:");
                        lastNameLabel.setBounds(10, 10, 80, 20);
                        panel.add(lastNameLabel);

                        JTextField lastNameText = new JTextField();
                        lastNameText.setBounds(200, 10, 250, 25);
                        panel.add(lastNameText);

                        JLabel firstNameLabel = new JLabel("Enter patient's first name:");
                        firstNameLabel.setBounds(10, 40, 80, 20);
                        panel.add(firstNameLabel);

                        JTextField firstNameText = new JTextField();
                        firstNameText.setBounds(200, 40, 80, 25);
                        panel.add(firstNameText);

                        JLabel birthdayLabel = new JLabel("Enter patient's birthday:");
                        birthdayLabel.setBounds(10, 70, 80, 20);
                        panel.add(birthdayLabel);

                        JTextField birthdayText = new JTextField();
                        birthdayText.setBounds(200, 70, 80, 25);
                        panel.add(birthdayText);

                        JButton searchButton = new JButton("SEARCH");
                        searchButton.setBounds(10, 100, 80, 25);
                        searchButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String searchLast = lastNameText.getText();
                                String searchFirst = firstNameText.getText();
                                String searchBirthday = birthdayText.getText();

                                for (int i = 0; i < finalCount; i++) {
                                    try {
                                        if (!Objects.equals(patients[i][9], "D") && patients[i][1].equalsIgnoreCase(searchLast) && patients[i][2].equalsIgnoreCase(searchFirst) && patients[i][4].equalsIgnoreCase(searchBirthday)) {
                                            lines[searched] = i;
                                            searched++;
                                        }
                                    } catch (NullPointerException ignored) {}
                                }
                                frame.dispose();

                                frame = new JFrame();
                                panel = new JPanel();

                                frame.setSize(960, 540);
                                frame.setTitle("Searching Records");
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                                panel.setLayout(new BorderLayout());

                                JLabel UID = new JLabel("Patient's UID");
                                UID.setBounds(10, 10, 100, 20);
                                panel.add(UID);

                                JLabel lastName = new JLabel("Last Name");
                                lastName.setBounds(100, 10, 100, 20);
                                panel.add(lastName);

                                JLabel firsName = new JLabel("First Name");
                                firsName.setBounds(190, 10, 100, 20);
                                panel.add(firsName);

                                JLabel middleName = new JLabel("Middle Name");
                                middleName.setBounds(280, 10, 100, 20);
                                panel.add(middleName);

                                JLabel birthday = new JLabel("Birthday");
                                birthday.setBounds(370, 10, 100, 20);
                                panel.add(birthday);

                                JLabel gender = new JLabel("Gender");
                                gender.setBounds(460, 10, 100, 20);
                                panel.add(gender);

                                JLabel address = new JLabel("Address");
                                address.setBounds(550, 10, 100, 20);
                                panel.add(address);

                                JLabel phoneNumber = new JLabel("Phone Number");
                                phoneNumber.setBounds(640, 10, 100, 20);
                                panel.add(phoneNumber);

                                JLabel nationalID = new JLabel("National ID. No");
                                nationalID.setBounds(730, 10, 100, 20);
                                panel.add(nationalID);

                                int x = 10;
                                int y = 10;
                                String temp;
                                for(int i = 0; i<lines.length; i++) {
                                    y+=30;
                                    for (int j = 0; j < 9; j++)
                                        if(patients[i][0] != null)
                                            if (patients[i][1].equalsIgnoreCase(searchLast) && patients[i][2].equalsIgnoreCase(searchFirst) && patients[i][4].equalsIgnoreCase(searchBirthday)) {
                                                temp = patients[i][j];

                                                JLabel print = new JLabel(temp);
                                                print.setBounds(x, y, 100, 20);
                                                panel.add(print);

                                                x+=90;
                                            }
                                }

                                JLabel enterUID = new JLabel("Enter the patient's UID: ");
                                enterUID.setBounds(x, y+30, 100, 20);
                                panel.add(enterUID);

                                JTextField UIDText = new JTextField();
                                UIDText.setBounds(x+100, y+30, 100, 25);
                                UIDText.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String input = UIDText.getText().toUpperCase();
                                        for(int i=0; i< patients.length; i++)
                                            if (Objects.equals(patients[i][0], input)) {
                                                lines[0] = i;
                                                searched = 1;
                                                break;
                                            }
                                        if(searched!=1)
                                            lines[0] = -2;
                                    }
                                });
                                panel.add(UIDText);

                                frame.add(panel);
                                frame.setVisible(true);
                            }
                        });
                        panel.add(searchButton);

                        frame.add(panel);
                        frame.setVisible(true);
                    }
                });
                panel.add(noIDButton);

                frame.add(panel);
                frame.setVisible(true);
            }
        });
        panel.add(noUIDButton);

        frame.add(panel);
        frame.setVisible(true);

        // if there is only 1 match search, return line number to line
        // else: ask user to input the patient's UID to display
        if(searched==0)
            return -2;
        else
            line = lines[0];

        return line;
    }

// sort array by UID
    public static String[][] sortArray(String[][] data) {
        int nonNull = 0;
        for(int i = 0; i < data[0].length; i++) {
            if(data[0][i] != null) {
                nonNull++;
            }
        }

        int counter = 0;
        String[][] newData = new String[nonNull][];
        for(int i = 0; i < data[0].length; i++) {
            if(data[0][i] != null) {
                newData[counter] = data[i];
                counter++;
            }
        }

        Arrays.sort(newData, (entry1, entry2) -> {
            final String time1 = entry1[0];
            final String time2 = entry2[0];


            return time1.compareTo(time2);

        });
        return newData;
    }

// sort array by date
    public static String[][] sortDate(String[][] data) {
        int nonNull = 0;
        for(int i = 0; i < data[0].length; i++) {
            if(data[i][2] != null) {
                nonNull++;
            }
        }

        int counter = 0;
        String[][] newData = new String[nonNull][];
        for(int i = 0; i < data[0].length; i++) {
            if(data[i][2] != null) {
                newData[counter] = data[i];
                counter++;
            }
        }

        Arrays.sort(newData, (entry1, entry2) -> {
            final String time1 = entry1[0];
            final String time2 = entry2[0];

            return time1.compareTo(time2);

        });
        return newData;
    }
}