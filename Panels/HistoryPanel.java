package Panels;

import Models.History;
import Provider.InstanceProvider;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Ab_interface.Observer;
import Controller.HistoryController;

import java.awt.*;
import java.util.ArrayList;

public class HistoryPanel extends JPanel implements Observer {

    private ArrayList<History> historyList;
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private HistoryController historyController;
    private InstanceProvider provider;

    // Color scheme
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color foregroundColor = new Color(229, 229, 229);
    private final Color accentColor = new Color(0, 150, 136);
    private final Color secondaryBackgroundColor = new Color(30, 30, 30);
    private final Color tableAlternateRowColor = new Color(24, 24, 24);

    public HistoryPanel(InstanceProvider provider) {

        this.provider = provider;
        this.historyController = (HistoryController)provider.getInstance(HistoryController.class);
        this.historyController.addObserver(this);

        // Initialize history list
        historyList = historyController.getHistory(provider.getCurrentUser());

        // Set layout and panel appearance
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Table model for history
        historyTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Borrowed On", "Return On", "Total Cost(฿)"}, 0);
        historyTable = new JTable(historyTableModel);
        styleTable(historyTable);

        // Add history records to the table
        loadHistoryToTable();

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons (if needed for future extensions)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(backgroundColor);

        // Example button (could be used for additional functionality in the future)
        JButton clearHistoryButton = createStyledButton("Clear History");
        clearHistoryButton.setBackground(new Color(220, 53, 69)); // Red color for clear history button
        buttonPanel.add(clearHistoryButton);

        // Style the button panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Button functionality to clear history
        clearHistoryButton.addActionListener(e -> clearHistory());
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
                    label.setBorder(BorderFactory.createEmptyBorder(0, LEFT_PADDING, 0, 0));
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? backgroundColor : tableAlternateRowColor);
                    }
                }
                return c;
            }
        });
    }

    // Add the history list to the table
    private void loadHistoryToTable() {
        for (History history : historyList) {
            historyTableModel.addRow(new Object[]{
                    history.getId(),
                    history.getName(),
                    history.getBorrowedOn(),
                    history.getReturnOn(),
                    String.format("%.2f", history.getCost())  // Format the cost to two decimal places
            });
        }
    }

     // Call this method whenever you update history data
     public void refreshHistory() {
        historyList = historyController.getHistory(provider.getCurrentUser()); // Update the history list from the controller
        loadHistoryToTable(); // Reload the table with the new data
    }

    // Method to clear all history records (if needed)
    private void clearHistory() {
        int confirmation = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to clear all history?", 
                "Confirm Clear", 
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            historyList.clear();
            historyTableModel.setRowCount(0); // Remove all rows from the table
        }
    }

    @Override
    public void onChanged() {
        refreshHistory();
    }
}
