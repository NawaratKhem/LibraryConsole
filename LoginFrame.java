import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import Controller.UserController;
import Models.User;
import Provider.InstanceProvider;

public class LoginFrame extends JFrame {

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 500;
    private static final int IMAGE_PANEL_WIDTH = 400;
    private static final String BACKGROUND_IMAGE_PATH = "Images/books.jpg";

    // Dark theme colors
    private final Color backgroundColor = new Color(18, 18, 18);
    private final Color foregroundColor = new Color(64, 64, 64);
    private final Color accentColor = new Color(0, 150, 136);
    private final Color secondaryBackgroundColor = new Color(30, 30, 30);
    //private final Color tableAlternateRowColor = new Color(24, 24, 24);

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final UserController userController;
    private final InstanceProvider provider;
    private BufferedImage backgroundImage;

    public LoginFrame(InstanceProvider provider) {
        this.provider = provider;
        this.userController = (UserController) provider.getInstance(UserController.class);

        loadBackgroundImage();
        initializeFrame();

        JPanel mainPanel = createMainPanel();
        add(mainPanel);

        usernameField = createTextField();
        passwordField = createPasswordField();

        JPanel formPanel = createFormPanel();
        mainPanel.add(createImagePanel(), BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);
    }

    private void loadBackgroundImage() {
        try {
            Path imagePath = Paths.get(BACKGROUND_IMAGE_PATH);
            backgroundImage = ImageIO.read(imagePath.toFile());
        } catch (IOException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
    }

    private void initializeFrame() {
        setTitle("Login");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, FRAME_WIDTH, FRAME_HEIGHT, 15, 15));
        setBackground(backgroundColor);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        panel.setBackground(backgroundColor);
        return panel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(IMAGE_PANEL_WIDTH, FRAME_HEIGHT));
        return imagePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setColor(backgroundColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);

        formPanel.add(createLoginTitle(), gbc);

        gbc.insets = new Insets(20, 40, 5, 40);
        formPanel.add(createInputPanel("Username", usernameField), gbc);

        gbc.insets = new Insets(15, 40, 5, 40);
        formPanel.add(createInputPanel("Password", passwordField), gbc);

        gbc.insets = new Insets(30, 40, 5, 40);
        formPanel.add(createLoginButton(), gbc);

        gbc.insets = new Insets(10, 40, 20, 40);
        formPanel.add(createForgetPasswordButton(), gbc);

        return formPanel;
    }

    private JLabel createLoginTitle() {
        JLabel loginTitle = new JLabel("Welcome Back", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        loginTitle.setForeground(foregroundColor);
        return loginTitle;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(secondaryBackgroundColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        configureField(textField);
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(secondaryBackgroundColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        configureField(passwordField);
        return passwordField;
    }

    private void configureField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
        field.setForeground(foregroundColor);
        field.setCaretColor(foregroundColor);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setOpaque(true);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        JLabel label = new JLabel(labelText);
        label.setForeground(foregroundColor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(textField, gbc);

        return panel;
    }

    private JButton createLoginButton() {
        JButton loginButton = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? accentColor.darker() : accentColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setForeground(backgroundColor);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(false);
        loginButton.setPreferredSize(new Dimension(250, 40));
        loginButton.addActionListener(this::authenticate);
        return loginButton;
    }

    private JButton createForgetPasswordButton() {
        JButton forgetPasswordButton = new JButton("Forgot Password?");
        forgetPasswordButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgetPasswordButton.setForeground(accentColor);
        forgetPasswordButton.setContentAreaFilled(false);
        forgetPasswordButton.setBorderPainted(false);
        forgetPasswordButton.setFocusPainted(false);
        forgetPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return forgetPasswordButton;
    }

    private void authenticate(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User loginResult = userController.Authenticate(username, password);
        if (loginResult == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        provider.setLoggedinUser(loginResult);
        dispose();
    }
}