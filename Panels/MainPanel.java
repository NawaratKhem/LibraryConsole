package Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import Ab_interface.Observer;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.util.Arrays;

//Custom classes
import Models.Book;
import Models.User;
import Controller.BookController;
import Controller.HistoryController;
import Provider.InstanceProvider;

public class MainPanel extends JPanel implements Observer {

    private JTextField searchField;
    private JButton searchButton;
    private JButton filterButton;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JFrame parentFrame;

    private BookController _bookController;
    private HistoryController _historyController;

    // Dark theme colors
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color foregroundColor = new Color(229, 229, 229);
    private final Color accentColor = new Color(0, 150, 136);
    private final Color secondaryBackgroundColor = new Color(30, 30, 30);
    private final Color tableAlternateRowColor = new Color(24, 24, 24);

    private boolean isAdmin = false;
    private User currentUser;

    public MainPanel(JFrame frame, InstanceProvider provider) {

        this.parentFrame = frame;
        this.currentUser = provider.getCurrentUser();
        
        this._bookController = (BookController) provider.getInstance(BookController.class);
        this._bookController.addObserver(this);

        this._historyController = (HistoryController) provider.getInstance(HistoryController.class);
        isAdmin = provider.IsAdmin();

        setLayout(new BorderLayout());

        // Set dark theme
        setBackground(backgroundColor);

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBackground(backgroundColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search field
        searchField = new JTextField(20);
        styleTextField(searchField);
        topPanel.add(searchField);

        // Search button
        searchButton = createStyledButton("Search");
        topPanel.add(searchButton);

        // Filter button
        filterButton = createStyledButton("Filter");
        topPanel.add(filterButton);

        add(topPanel, BorderLayout.NORTH);

        ArrayList<String> columnNames = new ArrayList<>(
                Arrays.asList("ID", "Title", "Author", "Publish Date", "Status"));
        if (isAdmin) {
            columnNames.add("Edit");
            columnNames.add("Delete");
        }else{
            columnNames.add("Action");
        }
        tableModel = new DefaultTableModel(columnNames.toArray(), 0);
        bookTable = new JTable(tableModel);

        // Style the table
        styleTable(bookTable);

        // Set up sorter
        sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Add dummy data
        populateTable();

        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        filterButton.addActionListener(e -> showFilterDialog());
    }

    // Styling component
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(accentColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    private void styleTextField(JTextField textField) {
        textField.setBackground(secondaryBackgroundColor);
        textField.setForeground(foregroundColor);
        textField.setCaretColor(foregroundColor);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(secondaryBackgroundColor);
        comboBox.setForeground(foregroundColor);
        comboBox.setBorder(BorderFactory.createLineBorder(accentColor));
    }

    private void styleTable(JTable table) {

        final int LEFT_PADDING = 10;

        table.setBackground(backgroundColor);
        table.setForeground(foregroundColor);
        table.setGridColor(secondaryBackgroundColor);
        table.setSelectionBackground(accentColor);
        table.setSelectionForeground(foregroundColor);

        table.getTableHeader().setBackground(accentColor);
        table.getTableHeader().setForeground(Color.black);

        table.setRowHeight(40);

        // Custom cell renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof JButton) {
                    return (JButton) value; // Return the button as is
                }

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, LEFT_PADDING, 0, 0));
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? backgroundColor : tableAlternateRowColor);
                    }
                }
                return c;
            }
        });

        // Make all cells non-editable
        table.setDefaultEditor(Object.class, null);

        // Add a mouse listener to handle button clicks
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col >= 0) {
                    Object value = table.getValueAt(row, col);
                    if (value instanceof JButton) {
                        ((JButton) value).doClick();
                    }
                }
            }
        });

        // Set custom column widths
        TableColumnModel columnModel = table.getColumnModel();
        int[] columnWidths = { 80, 200, 150, 100, 100, 50, 50 }; // Adjust these values as needed

        for (int i = 0; i < columnModel.getColumnCount() && i < columnWidths.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
            column.setMinWidth(columnWidths[i]);
        }

    }

    private JButton createEditButton(int bookId) {
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int row = getRowByBookId(bookId);
            if (row != -1) {
                openEditDialog(row);
            }
        });
        editButton.setBackground(accentColor);
        editButton.setForeground(Color.white);
        return editButton;
    }
    
    private JButton createBorrowButton(int bookId) {
        JButton editButton = new JButton("Borrow");
        editButton.addActionListener(e -> {
            int row = getRowByBookId(bookId);
            if (row != -1) {
                openBorrowDialog(row);
            }
        });
        Book book = _bookController.getBookById(bookId);
        if(book.getBookStatus()){
            editButton.setBackground(Color.pink);
        }else{
            editButton.setBackground(accentColor);
        }
        editButton.setForeground(Color.white);
        return editButton;
    }

    private JButton createDeleteButton(int bookId) {
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int row = getRowByBookId(bookId);
            if (row != -1) {
                confirmDeleteRow(row);
            }
        });
        deleteButton.setBackground(Color.red);
        deleteButton.setForeground(Color.white);
        return deleteButton;
    }

    // Table manupulation
    private void populateTable() {
        List<Book> books = _bookController.getBooks();
        for (Book book : books) {
            addBookToTable(book);
        }
    }

    private void addBookToTable(Book book) {
        JButton editButton = createEditButton(book.getId());
        JButton deleteButton = createDeleteButton(book.getId());
        JButton borrowButton = createBorrowButton(book.getId());

        Object[] rowData = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublishDate(),
                book.IsAvailable(),
                isAdmin ? editButton : borrowButton,
                isAdmin ? deleteButton : null
        };
        tableModel.addRow(rowData);
    }

    private int getRowByBookId(int bookId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(bookId)) {
                return i;
            }
        }
        return -1; // Not found
    }

    // Helper functions
    private void performSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm));
        }
    }

    private void showFilterDialog() {

        JDialog filterDialog = new JDialog(parentFrame, "Filter Books", true);
        filterDialog.setLayout(new BorderLayout());
        filterDialog.setSize(300, 200);
        filterDialog.setLocationRelativeTo(this);

        JPanel filterPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        filterPanel.setBackground(backgroundColor);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(foregroundColor);
        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "All", "Borrowed", "Available" });
        styleComboBox(statusCombo);

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setForeground(foregroundColor);
        JTextField yearField = new JTextField();
        styleTextField(yearField);

        filterPanel.add(statusLabel);
        filterPanel.add(statusCombo);
        filterPanel.add(yearLabel);
        filterPanel.add(yearField);

        JButton applyFilterButton = createStyledButton("Apply Filter");
        applyFilterButton.addActionListener(e -> {
            applyFilter(statusCombo.getSelectedItem().toString(), yearField.getText());
            filterDialog.dispose();
        });

        filterPanel.add(new JLabel()); // Empty label for spacing
        filterPanel.add(applyFilterButton);

        filterDialog.add(filterPanel, BorderLayout.CENTER);
        filterDialog.setVisible(true);
    }

    private void applyFilter(String status, String year) {
        RowFilter<DefaultTableModel, Object> statusFilter = null;
        RowFilter<DefaultTableModel, Object> yearFilter = null;

        if (!status.equals("All")) {
            statusFilter = RowFilter.regexFilter(status, 4); // Status column index
        }

        if (!year.isEmpty()) {
            yearFilter = RowFilter.regexFilter(year, 3); // Publish Date column index
        }

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        if (statusFilter != null)
            filters.add(statusFilter);
        if (yearFilter != null)
            filters.add(yearFilter);

        if (!filters.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else {
            sorter.setRowFilter(null);
        }
    }

    public void confirmDeleteRow(int row) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this book?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int selectedBook_id = (int) tableModel.getValueAt(row, 0);
            tableModel.removeRow(row);
            _bookController.RemoveBook(selectedBook_id);
        }
    }

    public void openEditDialog(int row) {
        // Extract current row data
        int id = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        String author = (String) tableModel.getValueAt(row, 2);
        String publishDate = (String) tableModel.getValueAt(row, 3);
        String status = (String) tableModel.getValueAt(row, 4); // Change this line

        // Create a popup dialog with input fields for each column
        JTextField titleField = new JTextField(title);
        JTextField authorField = new JTextField(author);
        JTextField publishDateField = new JTextField(publishDate);
        JCheckBox statusCheckBox = new JCheckBox("Available", status.equals("Available")); // Change this line

        JPanel editPanel = new JPanel(new GridLayout(0, 1));
        editPanel.add(new JLabel("Title:"));
        editPanel.add(titleField);
        editPanel.add(new JLabel("Author:"));
        editPanel.add(authorField);
        editPanel.add(new JLabel("Publish Date:"));
        editPanel.add(publishDateField);
        editPanel.add(new JLabel("Status:"));
        editPanel.add(statusCheckBox);

        int result = JOptionPane.showConfirmDialog(
                this,
                editPanel,
                "Edit Book Details",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            Book updatedBook = new Book(id, title, author, publishDate, statusCheckBox.isSelected());

            _bookController.UpdateBook(updatedBook);

            // Update table model with new values
            tableModel.setValueAt(titleField.getText(), row, 1);
            tableModel.setValueAt(authorField.getText(), row, 2);
            tableModel.setValueAt(publishDateField.getText(), row, 3);
            tableModel.setValueAt(statusCheckBox.isSelected() ? "Available" : "Borrowed", row, 4); // Change this line
        }
    }

    public void openBorrowDialog(int row) {
        // Extract current row data
        int id = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        String author = (String) tableModel.getValueAt(row, 2);
        String publishDate = (String) tableModel.getValueAt(row, 3);
        String status = (String) tableModel.getValueAt(row, 4);

        // Get the book object from the controller
        Book book = _bookController.getBookById(id);

        // Create a panel with book details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        detailsPanel.add(new JLabel("Title:"));
        detailsPanel.add(new JLabel(title));
        detailsPanel.add(new JLabel("Author:"));
        detailsPanel.add(new JLabel(author));
        detailsPanel.add(new JLabel("Publish Date:"));
        detailsPanel.add(new JLabel(publishDate));
        detailsPanel.add(new JLabel("Status:"));
        detailsPanel.add(new JLabel(status));
        detailsPanel.add(new JLabel("Upfront Cost:"));
        detailsPanel.add(new JLabel("฿" + book.upfrontCost));
        detailsPanel.add(new JLabel("Daily Cost:"));
        detailsPanel.add(new JLabel("฿" + book.dailyCost));

        // Add a spinner for number of days
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        detailsPanel.add(new JLabel("Number of Days:"));
        detailsPanel.add(daysSpinner);

        // Add a label for total cost
        JLabel totalCostLabel = new JLabel("฿" + book.TotalCost(1));
        detailsPanel.add(new JLabel("Total Cost:"));
        detailsPanel.add(totalCostLabel);

        // Update total cost when days change
        daysSpinner.addChangeListener(e -> {
            int days = (int) daysSpinner.getValue();
            totalCostLabel.setText("฿" + book.TotalCost(days));
        });

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(
                this,
                detailsPanel,
                "Borrow Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Handle the borrowing process
            int days = (int) daysSpinner.getValue();
            boolean success = _bookController.BorrowBook(id, days);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
                _historyController.AddHistory(book, days, currentUser.name);

                // Update the table
                tableModel.setValueAt("Borrowed", row, 4);
            } else {
                JOptionPane.showMessageDialog(this, "This book is not available", "Opps!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    //Observer
    @Override
    public void onChanged() {
        populateTable();
    }
}