package GUI;

import javax.swing.*;
import java.awt.*;

public class DashBoard extends JFrame{
    private JPanel dbP;
    private JPanel mainP;
    private JButton manageDoctorsButton1;
    private JButton manageMedicalServicesButton;
    private JButton generateReportsButton;
    private JButton userManagementButton;
    private JTable table1;

    public DashBoard() {
        super();
        this.setContentPane(dbP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
}
