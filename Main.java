import javax.swing.*;

import Controller.BookController;
import Controller.HistoryController;
import Controller.StudentController;
import Controller.TeacherController;
import Controller.UserController;
import Models.Admin;
import Models.Teacher;
import Models.User;
import Panels.HistoryPanel;
import Panels.MainPanel;
import Panels.StudentPanel;
import Panels.TeacherPanel;
import Provider.InstanceProvider;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

public class Main {
    static InstanceProvider provider;
    public static void main(String[] args) {

        provider = StartInstances();

        Login(provider).thenAccept(user -> {
            createMainApplication(provider, user);
        }).exceptionally(ex -> {
            return null;
        });
    }

    private static CompletableFuture<User> Login(InstanceProvider provider) {
        CompletableFuture<User> future = new CompletableFuture<>();
    
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(provider);
            loginFrame.setVisible(true);
    
            // Use a timer to periodically check if a user has logged in
            Timer timer = new Timer(100, e -> {
                User user = provider.getCurrentUser();
                if (user != null) {
                    ((Timer) e.getSource()).stop();
                    loginFrame.dispose();
                    future.complete(user); // Complete the future with the logged-in user
                }
            });
            timer.start();
        });
    
        return future; // Return the CompletableFuture
    }
    

    private static InstanceProvider StartInstances(){
        InstanceProvider provider = new InstanceProvider();

        UserController userController = new UserController();
        provider.addNewObject(userController);

        BookController bookController = new BookController();
        provider.addNewObject(bookController);

        HistoryController historyController = new HistoryController(bookController.getBooks());
        provider.addNewObject(historyController);

        return provider;
    }

    private static void createMainApplication(InstanceProvider provider, User user) {
        
        // Create the JFrame
        JFrame frame = new JFrame("PSU Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setBackground(Color.black);
        frame.setForeground(Color.white);

        // Set the frame size
        frame.setSize(1000, 800);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create a JTabbedPane and set its background and foreground color
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // tabbedPane.setBackground(new Color(50, 50, 50));  // Tab pane background
        // tabbedPane.setForeground(new Color(230, 230, 230));  // Tab text color

        tabbedPane.addTab("Books", new MainPanel(frame, provider));

        UserController controller = (UserController)provider.getInstance(UserController.class);

        //Admin mode
        if (user instanceof Admin) {

            TeacherController teacherController = new TeacherController(controller.getTeachers());
            provider.addNewObject(teacherController);

            StudentController studentController = new StudentController(controller.getStudents());
            provider.addNewObject(studentController);

            tabbedPane.addTab("Students", new StudentPanel(provider));
            tabbedPane.addTab("Teacher", new TeacherPanel(provider));
        }

        //Teacher mode
        if (user instanceof Teacher) {
            TeacherController teacherController = new TeacherController(controller.getTeachers());
            provider.addNewObject(teacherController);

            tabbedPane.addTab("Students", new StudentPanel(provider));
        }

        tabbedPane.addTab("Activities", new HistoryPanel(provider));

        // Add "Logout" tab
    tabbedPane.addTab("Logout", null); // Add the tab without a panel

    // Add a listener to handle logout when the "Logout" tab is selected
    tabbedPane.addChangeListener(e -> {
        // Check if the "Logout" tab is selected
        if (tabbedPane.getSelectedIndex() == tabbedPane.getTabCount() - 1) {
            // Show confirmation dialog
            int confirmed = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to log out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                frame.dispose(); // Close the main application window
                SwingUtilities.invokeLater(() -> {
                    InstanceProvider newProvider = StartInstances();
                    Login(newProvider).thenAccept(newUser -> {
                        createMainApplication(newProvider, newUser);
                    }).exceptionally(ex -> {
                        return null;
                    });
                });
            } else {
                // Go back to the previous selected tab if "No" is clicked
                tabbedPane.setSelectedIndex(0); // Adjust this to set it back to a meaningful default tab
            }
        }
    });

        frame.add(tabbedPane);

        frame.setVisible(true);
    }

}