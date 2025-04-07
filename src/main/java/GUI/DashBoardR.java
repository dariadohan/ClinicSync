package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DashBoardR extends JFrame{
    private JPanel dbrP;
    private JPanel dbPA;
    private JPanel mainP;
    private JButton managePatientsButton;
    private JButton manageAppointmentsButton;

    public DashBoardR() {
        super();
        this.setContentPane(mainP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        managePatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainFrame(true);
            }
        });


        manageAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    DashBoardRA dashBoardRA=new DashBoardRA();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
