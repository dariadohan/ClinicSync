package GUI;

import Data.Role;
import Data.User;
import controller.UserController;
import dao.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DashBoardAD extends JFrame {
    private JPanel mainP;
    private JTable table1;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private JPanel dbPAD;

    public DashBoardAD() {
        super();
        this.setTitle("Admin Dashboard");

        mainP = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Back");
        backButton.setMargin(new Insets(5, 15, 5, 15));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(backButton, BorderLayout.EAST);
        mainP.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Specialty", "Email", "Phone Number"};
        Object[][] data = fetchAllUsers();  // Populate data from the database

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Make all columns editable except the ID column
            }
        };

        table1 = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table1);
        mainP.add(scrollPane, BorderLayout.CENTER);

        // Add TableModelListener to update the database when the table is modified
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    // Retrieve updated values from the table
                    int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                    String name = model.getValueAt(row, 1).toString();
                    String specialty = model.getValueAt(row, 2).toString();
                    String email = model.getValueAt(row, 3).toString();
                    String phone = model.getValueAt(row, 4).toString();

                    updateUserInDatabase(id, name, specialty, email, phone);
                }
            }
        });
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();

                    if (row == -1 || col == -1) {
                        return; // Prevent processing when the indices are invalid
                    }


                    int id = (int) model.getValueAt(row, 0); // ID column
                    String name = model.getValueAt(row, 1).toString();
                    String specialty = model.getValueAt(row, 2).toString();
                    String email = model.getValueAt(row, 3).toString();
                    String phone = model.getValueAt(row, 4).toString();


                    updateUserInDatabase(id, name, specialty, email, phone);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        addButton.setMargin(new Insets(8, 20, 8, 20));
        deleteButton.setMargin(new Insets(8, 20, 8, 20));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        mainP.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(mainP);
        this.setSize(new Dimension(650, 530));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainFrame(false);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new DashBoard();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow(); // Get the selected row
                if (selectedRow != -1) {
                    // Get the user ID from the first column of the selected row
                    int userId = (int) table1.getValueAt(selectedRow, 0);

                    // Confirm deletion
                    int confirm = JOptionPane.showConfirmDialog(DashBoardAD.this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Delete user from the database
                        deleteUserFromDatabase(userId);
                        // Remove the user from the table
                        DefaultTableModel model = (DefaultTableModel) table1.getModel();
                        model.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(DashBoardAD.this, "Please select a user to delete.", "No User Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        this.setVisible(true);
    }

    private void updateUserInDatabase(int id, String name, String specialty, String email, String phone) {
        String sql = "UPDATE user SET name = ?, specialty = ?, email = ?, phoneNo = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, specialty);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setInt(5, id);

            stmt.executeUpdate();

            // After update in the database, you might want to refresh the table data
            refreshTableData();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUserFromDatabase(int userId) {
        String sql = "DELETE FROM user WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete user from the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTableData() {
        Object[][] newData = fetchAllUsers();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        String[] columnNames = {"ID", "Name", "Specialty", "Email", "Phone Number"};
        model = new DefaultTableModel(newData, columnNames);
    }

    public Object[][] fetchAllUsers() {
        List<User> users = UserController.fetchUsersByRole(Role.DOCTOR);
        if (users == null || users.isEmpty()) {
            return new Object[0][0]; // Prevent null or empty list from being passed to the table
        }

        Object[][] data = new Object[users.size()][];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i] = new Object[]{
                    user.getId(),
                    user.getName(),
                    user.getSpecialty() != null ? user.getSpecialty().name() : "",
                    user.getEmail(),
                    user.getPhoneNo()
            };
        }

        return data;
    }
}
