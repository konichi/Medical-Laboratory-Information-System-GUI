import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JLabel selectLabel;
    private JButton patientsButton;
    private JButton servicesButton;
    private JButton labButton;

    public void mainMenu() {
        frame = new JFrame();
        panel = new JPanel();

        frame.setSize(960, 540);
        frame.setTitle("Medical Laboratory System");
        frame.add(panel, BorderLayout.LINE_START);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectLabel = new JLabel("Select a transaction: ");
        selectLabel.setBounds(10, 10, 250, 25);
        panel.add(selectLabel);

        patientsButton = new JButton("Manage Patient Records");
        patientsButton.setBounds(10, 30, 250, 25);
        patientsButton.addActionListener(this);
        panel.add(patientsButton);

        servicesButton = new JButton("Manage Services");
        servicesButton.setBounds(10, 50, 250, 25);
        servicesButton.addActionListener(this);
        panel.add(servicesButton);

        labButton = new JButton("Manage Laboratory Results");
        labButton.setBounds(10, 70, 250, 25);
        labButton.addActionListener(this);
        panel.add(labButton);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        if (e.getSource() == patientsButton)
            managePatientRecords();
        else if (e.getSource() == servicesButton)
            manageServices();
        else if (e.getSource() == labButton)
            manageLaboratoryRequest();
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
