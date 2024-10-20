package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

import Controller.StudentController;
import Models.Student;
import Models.Teacher;
import Models.User;
import Provider.InstanceProvider;

public class StudentPanel extends JPanel {

    private ArrayList<Student> studentList;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private StudentController studentController;
    private InstanceProvider provider;
    private Teacher loggedInTeacher;

    // Color scheme (matching MainFrame)
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color foregroundColor = new Color(229, 229, 229);
    private final Color accentColor = new Color(0, 150, 136);
    private final Color secondaryBackgroundColor = new Color(30, 30, 30);
    private final Color tableAlternateRowColor = new Color(24, 24, 24);

    public StudentPanel(InstanceProvider provider) {

        this.provider = provider;
        this.studentController = (StudentController)provider.getInstance(StudentController.class);

        User user = provider.getCurrentUser();
        if(user instanceof Teacher){
            loggedInTeacher = (Teacher)user;
        }

        // Initialize student list
        studentList = new ArrayList<>();
        LoadData();  // Load 5 dummy students

        // Set layout and panel appearance
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Update table model to reflect the new columns
        studentTableModel = new DefaultTableModel(new Object[]{"Student ID", "University", "Name", "Number of Borrow", "Total Spent"}, 0);
        studentTable = new JTable(studentTableModel);
        styleTable(studentTable);

        // Add students to the table
        loadStudentsToTable();

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(backgroundColor);

        // Add student button
        JButton addStudentButton = createStyledButton("Add Student");
        buttonPanel.add(addStudentButton);

        // Remove student button
        JButton removeStudentButton = createStyledButton("Remove Selected");
        removeStudentButton.setBackground(new Color(220, 53, 69));
        buttonPanel.add(removeStudentButton);

        // Style the button panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Add student button functionality
        addStudentButton.addActionListener(e -> showAddStudentDialog());

        // Remove student button functionality
        removeStudentButton.addActionListener(e -> showDeleteStudentDialog());
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

    private void LoadData() {
        User user = provider.getCurrentUser();
        if(user instanceof Teacher){
            Teacher teacher = (Teacher) user;
            studentList = studentController.getStudents(teacher.teacherId);
        }
        else{
            studentList = studentController.getStudents();
        }
    }

    // Add the student list to the table
    private void loadStudentsToTable() {
        for (Student student : studentList) {
            studentTableModel.addRow(new Object[]{
                    student.studentId,
                    student.universityName,
                    student.name,
                    student.numberOfBorrow,
                    student.totalSpent
            });
        }
    }
    
    private void showAddStudentDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nameField = new JTextField(20);
        JTextField universityField = new JTextField(20);
        JTextField studentIdField = new JTextField(20);
        JTextField teacherIdField = new JTextField(20);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("University:"));
        panel.add(universityField);
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        if(loggedInTeacher == null){
            panel.add(new JLabel("Teacher ID:"));
            panel.add(teacherIdField);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String universityName = universityField.getText().trim();
            String studentIdStr = studentIdField.getText().trim();
            String teacherIdStr = teacherIdField.getText().trim();

            if (name.isEmpty() || universityName.isEmpty() || studentIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int studentId = Integer.parseInt(studentIdStr);
                int teacherId = loggedInTeacher == null ? Integer.parseInt(teacherIdStr) : loggedInTeacher.teacherId;
                int newId = studentList.size() + 1; // Auto-generate a new ID

                // Create new student and add to list
                Student newStudent = new Student(newId, studentId, teacherId, name, universityName);
                newStudent.numberOfBorrow = 0; // Default value for numberOfBorrow
                newStudent.totalSpent = 0.0; // Default value for totalSpent
                studentList.add(newStudent);

                // Add new student to the table
                studentTableModel.addRow(new Object[]{newStudent.studentId, newStudent.universityName, newStudent.name, newStudent.numberOfBorrow, newStudent.totalSpent});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid student ID. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Ask for confirmation before removing the student
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove the selected student?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                // Remove student from the list and table
                studentList.remove(selectedRow);
                studentTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to remove.");
        }
    }
}
