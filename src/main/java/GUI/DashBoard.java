package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DashBoard extends JFrame{
    private JPanel dbPA;
    private JPanel mainP;
    private JButton manageDoctorsButton1;
    private JButton manageMedicalServicesButton;
    private JButton generateReportsButton;
    private JButton userManagementButton;

    public DashBoard() {
        super();
        this.setContentPane(dbPA);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        manageDoctorsButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DashBoardAD dashboardad= new DashBoardAD();
            }
        });
        manageMedicalServicesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DashBoardAMS dashboardams= new DashBoardAMS();
            }
        });
        generateReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    DashBoardAR dashboardar= new DashBoardAR();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
