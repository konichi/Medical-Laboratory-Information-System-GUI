/*
 * Class: ManageServices
 * Manages Services offered such as
 *   adding a new service,
 *   searching for a service,
 *   deleting a service,
 *   and editing a service.
 * */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ManageServices {
    private ArrayList<Service> services;

    private MainMenu mm;
    private ReadFile rf;
    private WriteToFile wtf;

    private JFrame frame;
    private JPanel panel;

    public void manageServices() {
        mm = new MainMenu();
        frame = new JFrame();
        panel = new JPanel();

        frame.add(panel, BorderLayout.LINE_START);
        frame.setSize(960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Services");
        frame.add(panel);

        panel.setLayout(null);

        JLabel addLabel = new JLabel("[1] Add New Service");
        addLabel.setBounds(10, 10, 500, 25);
        panel.add(addLabel);

        JLabel editLabel = new JLabel("[2] Search Service");
        editLabel.setBounds(10, 30, 500, 25);
        panel.add(editLabel);

        JLabel deleteLabel = new JLabel("[3] Delete Service");
        deleteLabel.setBounds(10, 50, 500, 25);
        panel.add(deleteLabel);

        JLabel searchLabel = new JLabel("[4] Edit Service");
        searchLabel.setBounds(10, 70, 500, 25);
        panel.add(searchLabel);

        JLabel returnLabel = new JLabel("[X] Return to Main Menu");
        returnLabel.setBounds(10, 90, 500, 25);
        panel.add(returnLabel);

        JLabel selectLabel = new JLabel("Select a transaction: ");
        selectLabel.setBounds(10, 110, 500, 25);
        panel.add(selectLabel);

        JTextField selectText = new JTextField(20);
        selectText.setBounds(140, 110, 160, 25);
        selectText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = selectText.getText().toUpperCase();
                frame.dispose();
                switch (input) {
                    case "1" -> addService(0);
                    case "2" -> searchService();
                    case "3" -> deleteService();
                    case "4" -> editService();
                    case "X" -> mm.mainMenu();
                    default -> manageServices();
                }
            }
        });
        panel.add(selectText);
        frame.setVisible(true);
    }

/*
* adds a new service
* accepts parameter int type for editService
* */
    public void addService(int type) {
        mm = new MainMenu();
        rf = new ReadFile();
        wtf = new WriteToFile();
        services = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        String fileName = "services.txt";
        System.out.print("Enter unique 3-code Service Code: ");
        String code = scanner.next().toUpperCase();
        scanner.nextLine();
        System.out.print("Enter laboratory service Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter laboratory service Price: ");
        int price = 0;
        try {
            price = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid format! Please try again.");
            addService(0);
        }

        //check if service code already exists
        int exists;
        exists = rf.checkCode(fileName, code);
        do {
            if (exists == 1) {
                System.out.print("This service code already exists. Please enter a new code: ");
                code = scanner.next().toUpperCase();
            }
            exists = rf.checkCode(fileName, code);
        } while(exists==1);

        Service service = new Service(code, description, price);
        services.add(service);

        int error = wtf.writeToServices(fileName, service);
        if(error==1)
            addService(0);
        else
            System.out.println(code + " " + description + " has been added.");
        System.out.println();

        if(type == 1)
            return;

        String input;
        do {
            System.out.print("Do you want to add another service? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y"))
                addService(0);
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));
    }

//    searches for a service
    public void searchService() {
        mm = new MainMenu();
        Scanner scanner = new Scanner(System.in);

        int line=search();
        String input;

        if(line==-1)
            searchService();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to try again or return to the main menu?");
                System.out.println("[1] Search for another service");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    searchService();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                searchService();
            else
                mm.mainMenu();
        } else {
            do {
                System.out.println();
                System.out.print("Do you want to search again? [Y/N]: ");
                input = scanner.next().toUpperCase();
                System.out.println();
                if(input.equals("Y"))
                    searchService();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

//    deletes a service
    public void deleteService() {
        mm = new MainMenu();
        Scanner scanner = new Scanner(System.in);

        int line = search();
        String input;

        if(line==-1)
            deleteService();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to search again or return to the main menu?");
                System.out.println("[1] Delete a service");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    deleteService();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                deleteService();
            else
                mm.mainMenu();
        } else {
            delete(1, line);

            System.out.println();
            do {
                System.out.print("Do you want to delete another patient record? [Y/N]: ");
                input = scanner.next().toUpperCase();
                if(input.equals("Y"))
                    deleteService();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

//    edit a service
    public void editService() {
        mm = new MainMenu();
        Scanner scanner = new Scanner(System.in);

        String input;
        System.out.print("""
                The services cannot be edited.
                If you would like to edit an existing service, the service will be first deleted from the file, and a new service will be created.\s
                Would you like to proceed? [Y/N]:\s""");
        input = scanner.next().toUpperCase();
        if(input.equals("N"))
            mm.mainMenu();

        int line = search();
        delete(2, line);
        addService(1);

        System.out.println();
        do {
            System.out.print("Do you want to edit another patient record? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y"))
                editService();
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));
    }

/*
* searches services.txt for methods:
*   searchService(), deleteService(), and editService()
* returns the line number needed
* */
    public int search() {
        rf = new ReadFile();
        Scanner scanner = new Scanner(System.in);

        int scan;
        int line = 0;
        String input;

        System.out.print("Do you know the service code?[Y/N]: ");
        input = scanner.next().toUpperCase();
        if(input.equals("Y"))
            scan = 1;
        else {
            System.out.print("Input a keyword of the service's description: ");
            input = scanner.next();
            scan = 2;
        }

        // get all lines in services.txt and save to String[][] services
        String fileName = "services.txt";
        int error = rf.readServices(fileName);
        if(error==1)
            return -1;
        String[][] services = rf.getTempServ();

        // count total non-null entries in String[][] services
        int count = 0;
        for (String[] service : services)
            for (int j = 0; j < services[0].length; j++)
                if (service[0] != null && service[0].length() == 3) {
                    count++;
                    break;
                }

        // switch case
        // get input from user to search match/es in String services[][]
        int searched = 0;
        int[] lines = new int[256];
        String searchCode;
        switch (scan) {
            case 1 -> {
                System.out.print("Enter service code: ");
                searchCode = scanner.next().toUpperCase();
                for (int i = 0; i < count; i++) {
                    if (Objects.equals(services[i][0], searchCode) && !Objects.equals(services[i][3], "D")) {
                        searched = 1;
                        lines[0] = i;
                        break;
                    } else
                        lines[0] = -2;
                }
            }
            case 2 -> {
                for (int i = 0; i < count; i++) {                       // for every service
                    String[] temp = services[i][1].split(" ");          // get words in description
                    // for every word in description
                    for (String s : temp)
                        try {
                            if (!Objects.equals(services[i][3], "D"))
                                if (s.equalsIgnoreCase(input)) {
                                    lines[searched] = i;
                                    searched++;
                                }
                        } catch (NullPointerException ignored) { }
                }
            }
            default -> {
                System.out.println("An error occurred. Please try again.");
                System.out.println();
                return -1;
            }
        }

        // if there is only 1 match search, return line number to line
        // else: ask user to input the patient's UID to display
        String[] temp;
        if(searched==0)
            return -2;
        else if(searched>1) {
            System.out.printf("%-15s", "Service Code");
            System.out.printf("%-25s", "Description");
            System.out.printf("%-20s", "Price");
            System.out.println();
            services = sortArray(services);
            try {
                for(int i=0; i<lines.length; i++) {
                    temp = services[i][1].split(" ");
                    for (String s : temp) {
                        if (!Objects.equals(services[i][3], "D"))
                            if (s.equalsIgnoreCase(input)) {
                                System.out.printf("%-15s", services[i][0]);
                                System.out.printf("%-25s", services[i][1]);
                                System.out.printf("%-20s", services[i][2]);
                                System.out.println();
                            }
                    }
                }
            } catch (NullPointerException ignored) {}
            System.out.println();
            System.out.print("Enter the service code: ");
            input = scanner.next().toUpperCase();
            System.out.println();
            for(int i=0; i< services.length; i++)
                if(Objects.equals(services[i][0], input)) {
                    line = i;
                    searched = 1;
                    break;
                }
            if(searched!=1)
                return -2;
        } else
            line = lines[0];

        System.out.printf("%-15s", "Service Code");
        System.out.printf("%-25s", "Description");
        System.out.printf("%-20s", "Price");
        System.out.println();
        System.out.printf("%-15s", services[line][0]);
        System.out.printf("%-25s", services[line][1]);
        System.out.printf("%-20s", services[line][2]);
        System.out.println();

        return line;
    }

/*
* deletes a service for methods:
*   deleteService() and editService()
* accepts parameters:
*   int type for deleteService() and editService()
*   int line for which line will be deleted
* */
    public void delete(int type, int line) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please state reason for deletion: ");
        String reason = scanner.nextLine();
        String D = "D;";
        String newLine = String.join("", D, reason);

        String fileName = "services.txt";
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
            String code = splitLine[0];
            String description = splitLine[1];

            System.out.println(code + " " +  description + " has been deleted.");
            System.out.println();
        } catch(IOException e) {
            System.out.println("Error occurred. Please try again");
            if (type==1)
                deleteService();
            else
                editService();
        }
    }

// sort array by code
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
}
