package Panels;

import Models.Teacher;
import Provider.InstanceProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controller.TeacherController;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class TeacherPanel extends JPanel {

    private ArrayList<Teacher> teacherList;
    private JTable teacherTable;
    private DefaultTableModel teacherTableModel;
    private TeacherController teacherController;

    // Color scheme (same as StudentPanel)
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color foregroundColor = new Color(229, 229, 229);
    private final Color accentColor = new Color(0, 150, 136);
    private final Color secondaryBackgroundColor = new Color(30, 30, 30);
    private final Color tableAlternateRowColor = new Color(24, 24, 24);

    public TeacherPanel(InstanceProvider provider) {

        this.teacherController = (TeacherController)provider.getInstance(TeacherController.class);

        // Initialize teacher list
        teacherList = new ArrayList<>();
        LoadData();  // Load 5 dummy teachers

        // Set layout and panel appearance
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Table model for teachers
        teacherTableModel = new DefaultTableModel(new Object[]{"Teacher ID", "Name", "University", "Number of Borrow", "Total Spent"}, 0);
        teacherTable = new JTable(teacherTableModel);
        styleTable(teacherTable);

        // Add teachers to the table
        loadTeachersToTable();

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(backgroundColor);

        // Add teacher button
        JButton addTeacherButton = createStyledButton("Add Teacher");
        buttonPanel.add(addTeacherButton);

        // Remove teacher button
        JButton removeTeacherButton = createStyledButton("Remove Selected");
        removeTeacherButton.setBackground(new Color(220, 53, 69)); // Red color for remove button
        buttonPanel.add(removeTeacherButton);

        // Style the button panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Add teacher button functionality
        addTeacherButton.addActionListener(e -> showAddTeacherDialog());

        // Remove teacher button functionality
        removeTeacherButton.addActionListener(e -> showDeleteTeacherDialog());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(accentColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 30));
        return button;
    }

    private void styleTable(JTable table) {
        table.setBackground(backgroundColor);
        table.setForeground(foregroundColor);
        table.setGridColor(secondaryBackgroundColor);
        table.setSelectionBackground(accentColor);
        table.setSelectionForeground(foregroundColor);

        table.getTableHeader().setBackground(accentColor);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));

        table.setRowHeight(40);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final int LEFT_PADDING = 10;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(new EmptyBorder(0, LEFT_PADDING, 0, 0));
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? backgroundColor : tableAlternateRowColor);
                    }
                }
                return c;
            }
        });
    }

    // Load dummy data for 5 teachers
    private void LoadData() {
        teacherList = teacherController.getTeachers();
    }

    // Add the teacher list to the table
    private void loadTeachersToTable() {
        for (Teacher teacher : teacherList) {
            teacherTableModel.addRow(new Object[]{teacher.teacherId, teacher.name, teacher.universityName, teacher.numberOfBorrow, teacher.totalSpent});
        }
    }

    private void showAddTeacherDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField(20);
        JTextField universityField = new JTextField(20);
        JTextField teacherIdField = new JTextField(20);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("University:"));
        panel.add(universityField);
        panel.add(new JLabel("Teacher ID:"));
        panel.add(teacherIdField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Teacher",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String universityName = universityField.getText().trim();
            String teacherIdStr = teacherIdField.getText().trim();

            if (name.isEmpty() || universityName.isEmpty() || teacherIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int teacherId = Integer.parseInt(teacherIdStr);
                int newId = teacherList.size() + 1; // Auto-generate a new ID

                // Create new teacher and add to list
                Teacher newTeacher = new Teacher(newId, teacherId, name, universityName);
                teacherList.add(newTeacher);

                // Add new teacher to the table
                teacherTableModel.addRow(new Object[]{newTeacher.teacherId, newTeacher.name, newTeacher.universityName, newTeacher.numberOfBorrow, newTeacher.totalSpent});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid teacher ID. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteTeacherDialog() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow != -1) {
            // Ask for confirmation before removing the teacher
            int confirmation = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to remove the selected teacher?", 
                    "Confirm Removal", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirmation == JOptionPane.YES_OPTION) {
                // Remove teacher from the list and table
                teacherList.remove(selectedRow);
                teacherTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a teacher to remove.");
        }
    }
}
