import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    private JFrame frame;
    private JPanel panel;

    public void mainMenu() {
        frame = new JFrame();
        panel = new JPanel();

        frame.add(panel, BorderLayout.LINE_START);
        frame.setSize(960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Medical Laboratory System");
        frame.add(panel);

        panel.setLayout(null);

        JLabel patientsLabel = new JLabel("[1] Manage Patient Records");
        patientsLabel.setBounds(10, 10, 500, 25);
        panel.add(patientsLabel);

        JLabel servicesLabel = new JLabel("[2] Manage Services");
        servicesLabel.setBounds(10, 30, 500, 25);
        panel.add(servicesLabel);

        JLabel labLabel = new JLabel("[3] Manage Laboratory Results");
        labLabel.setBounds(10, 50, 500, 25);
        panel.add(labLabel);

        JLabel selectLabel = new JLabel("Select a transaction: ");
        selectLabel.setBounds(10, 80, 500, 25);
        panel.add(selectLabel);

        JTextField selectText = new JTextField(20);
        selectText.setBounds(140, 80, 160, 25);
        selectText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = selectText.getText();
                frame.dispose();
                switch (input) {
                    case "1" -> managePatientRecords();
                    case "2" -> manageServices();
                    case "3" -> manageLaboratoryRequest();
                    default -> mainMenu();
                }
            }
        });
        panel.add(selectText);
        frame.setVisible(true);
    }

    public void managePatientRecords() {
        ManagePatientRecords mpr = new ManagePatientRecords();
        mpr.managePatientRecords();
    }

    public void manageServices() {
        ManageServices ms = new ManageServices();
        ms.manageServices();
    }

    public void manageLaboratoryRequest() {
        ManageLaboratoryRequest mlr = new ManageLaboratoryRequest();
        mlr.manageLaboratoryRequest();
    }
}
