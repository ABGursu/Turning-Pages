package TurningPages;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSetMetaData;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.sql.PreparedStatement;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.MouseEvent;

public class BookStoreGUI {
    private JFrame frame;
    private String currentUser;
    private boolean isReadOnly;
    
    // Book components
    private JButton btnNewBook;
    private JButton btnUpdateBook;
    private JButton btnDeleteBook;
    
    // Supplier components
    private JButton btnAddSupplier;
    private JButton btnRemoveSupplier;
    
    // Stock components
    private JButton btnNewStock;
    private JButton btnDeleteStock;
    
    // Sales components
    private JButton btnNewSale;
    private JButton btnDeleteSale;
    
    // Data Management components
    private JButton btnExport;
    private JButton btnImportData;
    private JButton btnClearTables;

    // Add at the class level
    private static boolean globalDarkMode = false;  // Shared dark mode state
    private JButton darkModeButton;  // Make darkModeButton a class field

    // Add this constructor
    public BookStoreGUI(String username) {
        this.currentUser = username;
        this.isReadOnly = UserDatabase.isUserReadOnly(username);
        initialize();
        if (globalDarkMode) {
            applyDarkMode();
        }
    }

    public static void main(String[] args) {
        // Initialize the database first
        UserDatabase.initializeDatabase();
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    showLoginDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private static void showLoginDialog() {
        JFrame loginFrame = new JFrame("Turning Pages Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(420, 400);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.getContentPane().setLayout(null);
        loginFrame.getContentPane().setBackground(new Color(227, 208, 172));

        // Add dark mode button
        JButton loginDarkModeButton = new JButton("DARK MODE");
        loginDarkModeButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        loginDarkModeButton.setBounds(50, 291, 300, 30);
        loginFrame.getContentPane().add(loginDarkModeButton);

        // Initialize colors based on current global dark mode
        Color lightBackground = new Color(227, 208, 172);
        Color darkBackground = new Color(50, 50, 50);
        Color lightText = Color.BLACK;
        Color darkText = Color.WHITE;

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
        titleLabel.setBounds(150, 10, 340, 50);
        loginFrame.getContentPane().add(titleLabel);

        JLabel userLabel = new JLabel("USER:");
        userLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        userLabel.setBounds(50, 70, 100, 25);
        loginFrame.getContentPane().add(userLabel);

        JTextField userField = new JTextField();
        userField.setFont(new Font("Arial Black", Font.BOLD, 12));
        userField.setForeground(new Color(255, 0, 102));
        userField.setBounds(150, 70, 200, 25);
        loginFrame.getContentPane().add(userField);

        JLabel passLabel = new JLabel("PASSWORD:");
        passLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        passLabel.setBounds(50, 110, 100, 25);
        loginFrame.getContentPane().add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setForeground(new Color(255, 0, 102));
        passField.setFont(new Font("Arial Black", Font.BOLD, 12));
        passField.setBounds(150, 110, 200, 25);
        loginFrame.getContentPane().add(passField);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        loginButton.setBounds(50, 185, 300, 30);
        loginFrame.getContentPane().add(loginButton);

        JButton createUserButton = new JButton("CREATE NEW USER");
        createUserButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        createUserButton.setBounds(50, 237, 300, 30);
        loginFrame.getContentPane().add(createUserButton);

        JLabel messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBounds(50, 145, 300, 25);
        loginFrame.getContentPane().add(messageLabel);

        // Update dark mode button action listener
        loginDarkModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                globalDarkMode = !globalDarkMode;  // Toggle global state
                loginDarkModeButton.setText(globalDarkMode ? "LIGHT MODE" : "DARK MODE");
                
                // Update background colors
                loginFrame.getContentPane().setBackground(globalDarkMode ? darkBackground : lightBackground);
                
                // Update text colors
                titleLabel.setForeground(globalDarkMode ? darkText : lightText);
                userLabel.setForeground(globalDarkMode ? darkText : lightText);
                passLabel.setForeground(globalDarkMode ? darkText : lightText);
                
                // Update button colors
                Color buttonBackground = globalDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240);
                Color buttonText = globalDarkMode ? darkText : lightText;
                
                loginButton.setBackground(buttonBackground);
                loginButton.setForeground(buttonText);
                createUserButton.setBackground(buttonBackground);
                createUserButton.setForeground(buttonText);
                loginDarkModeButton.setBackground(buttonBackground);
                loginDarkModeButton.setForeground(buttonText);
                
                // Update input fields
                userField.setBackground(globalDarkMode ? new Color(60, 60, 60) : Color.WHITE);
                passField.setBackground(globalDarkMode ? new Color(60, 60, 60) : Color.WHITE);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();
                
                System.out.println("Login attempt with username: " + username); // Debug line
                
                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Please enter both username and password!");
                    return;
                }

                if (UserDatabase.validateUser(username, password)) {
                    System.out.println("Login successful"); // Debug line
                    if (username.equals("Admin")) {
                        showAdminMenu(loginFrame);
                    } else {
                        showUserMenu(loginFrame, username);
                    }
                } else {
                    System.out.println("Login failed"); // Debug line
                    messageLabel.setText("Invalid username or password!");
                    passField.setText("");
                }
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateUserDialog(loginFrame);
            }
        });

        // Add key listener for Enter key
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };
        userField.addKeyListener(enterKeyListener);
        passField.addKeyListener(enterKeyListener);

        loginFrame.setVisible(true);
    }

    private static void showCreateUserDialog(JFrame parentFrame) {
        JDialog createDialog = new JDialog(parentFrame, "Create New User", true);
        createDialog.setSize(350, 250);
        createDialog.setLocationRelativeTo(parentFrame);
        createDialog.getContentPane().setLayout(null);
        createDialog.getContentPane().setBackground(new Color(227, 208, 172));

        JLabel titleLabel = new JLabel("CREATE NEW USER");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 18));
        titleLabel.setBounds(70, 20, 200, 30);
        createDialog.getContentPane().add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        userLabel.setBounds(30, 70, 100, 25);
        createDialog.getContentPane().add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(130, 70, 180, 25);
        createDialog.getContentPane().add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        passLabel.setBounds(30, 110, 100, 25);
        createDialog.getContentPane().add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(130, 110, 180, 25);
        createDialog.getContentPane().add(passField);

        JButton createButton = new JButton("CREATE");
        createButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        createButton.setBounds(130, 150, 100, 30);
        createDialog.getContentPane().add(createButton);

        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBounds(30, 180, 280, 25);
        createDialog.getContentPane().add(messageLabel);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Username and password cannot be empty!");
                    return;
                }

                if (UserDatabase.createUser(username, password)) {
                    JOptionPane.showMessageDialog(createDialog, 
                        "User created successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    createDialog.dispose();
                } else {
                    messageLabel.setText("Username already exists or error occurred!");
                }
            }
        });

        createDialog.setVisible(true);
    }

    private static void showUserMenu(JFrame loginFrame, String username) {
        JDialog userMenu = new JDialog(loginFrame, "User Menu", true);
        userMenu.setSize(400, 200);
        userMenu.setLocationRelativeTo(loginFrame);
        userMenu.getContentPane().setLayout(null);
        userMenu.getContentPane().setBackground(globalDarkMode ? new Color(50, 50, 50) : new Color(227, 208, 172));

        JLabel titleLabel = new JLabel("USER MENU");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
        titleLabel.setBounds(120, 20, 200, 30);
        titleLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        userMenu.getContentPane().add(titleLabel);

        // Welcome label with username in red
        JLabel welcomeLabel = new JLabel("Welcome, ");
        welcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        welcomeLabel.setBounds(50, 50, 100, 20);
        welcomeLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        userMenu.getContentPane().add(welcomeLabel);

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        usernameLabel.setBounds(150, 50, 200, 20);
        usernameLabel.setForeground(Color.RED);
        userMenu.getContentPane().add(usernameLabel);

        // Update button colors and text colors based on dark mode
        Color buttonBackground = globalDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240);
        Color buttonText = globalDarkMode ? Color.WHITE : Color.BLACK;

        JButton updatePasswordButton = new JButton("Update Password");
        updatePasswordButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        updatePasswordButton.setBounds(50, 80, 300, 30);
        updatePasswordButton.setBackground(buttonBackground);
        updatePasswordButton.setForeground(buttonText);
        userMenu.getContentPane().add(updatePasswordButton);

        JButton enterGuiButton = new JButton("Enter GUI");
        enterGuiButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        enterGuiButton.setBounds(50, 120, 300, 30);
        enterGuiButton.setBackground(buttonBackground);
        enterGuiButton.setForeground(buttonText);
        userMenu.getContentPane().add(enterGuiButton);

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdatePasswordDialog(userMenu, username);
            }
        });

        enterGuiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userMenu.dispose();
                loginFrame.dispose();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            BookStoreGUI window = new BookStoreGUI(username);
                            window.frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        // Add read-only indicator if applicable
        if (UserDatabase.isUserReadOnly(username)) {
            JLabel readOnlyLabel = new JLabel("(Read-Only Account)");
            readOnlyLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
            readOnlyLabel.setForeground(Color.RED);
            readOnlyLabel.setBounds(50, 160, 300, 20);
            userMenu.getContentPane().add(readOnlyLabel);
        }

        userMenu.setVisible(true);
    }

    // Modified to pre-fill username and make it non-editable
    private static void showUpdatePasswordDialog(JDialog parentDialog, String username) {
        JDialog updateDialog = new JDialog(parentDialog, "Update Password", true);
        updateDialog.setSize(350, 400);  // Increased height from 250 to 400
        updateDialog.setLocationRelativeTo(parentDialog);
        updateDialog.getContentPane().setLayout(null);
        updateDialog.getContentPane().setBackground(globalDarkMode ? new Color(50, 50, 50) : new Color(227, 208, 172));

        JLabel titleLabel = new JLabel("UPDATE PASSWORD");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 18));
        titleLabel.setBounds(70, 20, 200, 30);
        titleLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        userLabel.setBounds(30, 70, 100, 25);
        userLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(userLabel);

        JTextField userField = new JTextField(username);
        userField.setEditable(false);
        userField.setBounds(130, 70, 180, 25);
        userField.setBackground(globalDarkMode ? new Color(60, 60, 60) : Color.WHITE);
        userField.setForeground(Color.RED);  // Username always in red
        userField.setCaretColor(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(userField);

        JLabel oldPassLabel = new JLabel("Old Password:");
        oldPassLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        oldPassLabel.setBounds(30, 110, 100, 25);
        oldPassLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(oldPassLabel);

        JPasswordField oldPassField = new JPasswordField();
        oldPassField.setBounds(130, 110, 180, 25);
        oldPassField.setBackground(globalDarkMode ? new Color(60, 60, 60) : Color.WHITE);
        oldPassField.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        oldPassField.setCaretColor(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(oldPassField);

        JLabel newPassLabel = new JLabel("New Password:");
        newPassLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        newPassLabel.setBounds(30, 150, 100, 25);
        newPassLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(newPassLabel);

        JPasswordField newPassField = new JPasswordField();
        newPassField.setBounds(130, 150, 180, 25);
        newPassField.setBackground(globalDarkMode ? new Color(60, 60, 60) : Color.WHITE);
        newPassField.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        newPassField.setCaretColor(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(newPassField);

        JButton updateButton = new JButton("UPDATE");
        updateButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        updateButton.setBounds(130, 190, 100, 30);
        updateButton.setBackground(globalDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240));
        updateButton.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        updateDialog.getContentPane().add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword = new String(oldPassField.getPassword()).trim();
                String newPassword = new String(newPassField.getPassword()).trim();

                if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(updateDialog,
                        "Please fill in all fields!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (oldPassword.equals(newPassword)) {
                    JOptionPane.showMessageDialog(updateDialog,
                        "New password must be different from old password!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (UserDatabase.updatePassword(username, oldPassword, newPassword)) {
                    JOptionPane.showMessageDialog(updateDialog,
                        "Password updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    updateDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(updateDialog,
                        "Invalid old password!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    oldPassField.setText("");
                    newPassField.setText("");
                }
            }
        });

        updateDialog.setVisible(true);
    }

    private static void showAdminMenu(JFrame loginFrame) {
        JDialog adminMenu = new JDialog(loginFrame, "Admin Menu", true);
        adminMenu.setSize(400, 300);
        adminMenu.setLocationRelativeTo(loginFrame);
        adminMenu.getContentPane().setLayout(null);
        adminMenu.getContentPane().setBackground(globalDarkMode ? new Color(50, 50, 50) : new Color(227, 208, 172));

        JLabel titleLabel = new JLabel("ADMIN MENU");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
        titleLabel.setBounds(120, 20, 200, 30);
        titleLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        adminMenu.getContentPane().add(titleLabel);

        // Welcome label with "Admin" in red
        JLabel welcomeLabel = new JLabel("Welcome, ");
        welcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        welcomeLabel.setBounds(50, 50, 100, 20);
        welcomeLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        adminMenu.getContentPane().add(welcomeLabel);

        JLabel adminLabel = new JLabel("Admin");
        adminLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
        adminLabel.setBounds(150, 50, 200, 20);
        adminLabel.setForeground(Color.RED);
        adminMenu.getContentPane().add(adminLabel);

        // Update button colors and text colors based on dark mode
        Color buttonBackground = globalDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240);
        Color buttonText = globalDarkMode ? Color.WHITE : Color.BLACK;

        // Adjust y-coordinates for all buttons
        JButton privilegesButton = new JButton("User Management");
        privilegesButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        privilegesButton.setBounds(50, 80, 300, 30);
        privilegesButton.setBackground(buttonBackground);
        privilegesButton.setForeground(buttonText);
        adminMenu.getContentPane().add(privilegesButton);

        JButton updatePasswordButton = new JButton("Update Password");
        updatePasswordButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        updatePasswordButton.setBounds(50, 120, 300, 30);
        updatePasswordButton.setBackground(buttonBackground);
        updatePasswordButton.setForeground(buttonText);
        adminMenu.getContentPane().add(updatePasswordButton);

        JButton deleteAllUsersButton = new JButton("Delete All Users");
        deleteAllUsersButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        deleteAllUsersButton.setBounds(50, 160, 300, 30);
        deleteAllUsersButton.setBackground(buttonBackground);
        deleteAllUsersButton.setForeground(Color.RED);
        adminMenu.getContentPane().add(deleteAllUsersButton);

        JButton enterGuiButton = new JButton("Enter GUI");
        enterGuiButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        enterGuiButton.setBounds(50, 200, 300, 30);
        enterGuiButton.setBackground(buttonBackground);
        enterGuiButton.setForeground(buttonText);
        adminMenu.getContentPane().add(enterGuiButton);

        // Add action listener for Delete All Users button
        deleteAllUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(adminMenu,
                    "Are you sure you want to delete ALL users?\nThis action cannot be undone!",
                    "Confirm Delete All Users",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (UserDatabase.deleteAllUsers()) {
                        JOptionPane.showMessageDialog(adminMenu,
                            "All users have been deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(adminMenu,
                            "Failed to delete users!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        privilegesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserPrivilegesDialog(adminMenu);
            }
        });

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdatePasswordDialog(adminMenu, "Admin");
            }
        });

        enterGuiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminMenu.dispose();
                loginFrame.dispose();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            BookStoreGUI window = new BookStoreGUI("Admin");
                            window.frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        adminMenu.setVisible(true);
    }

    private static void showUserPrivilegesDialog(JDialog parentDialog) {
        JDialog privilegesDialog = new JDialog(parentDialog, "User Privileges", true);
        privilegesDialog.setSize(400, 700);  // Increased height from 540 to 700
        privilegesDialog.setLocationRelativeTo(parentDialog);
        privilegesDialog.getContentPane().setLayout(null);
        privilegesDialog.getContentPane().setBackground(globalDarkMode ? new Color(50, 50, 50) : new Color(227, 208, 172));

        JLabel debugLabel = new JLabel("");
        debugLabel.setBounds(50, 5, 300, 20);
        debugLabel.setForeground(Color.BLUE);
        privilegesDialog.getContentPane().add(debugLabel);

        JLabel titleLabel = new JLabel("USER MANAGEMENT");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 16));
        titleLabel.setBounds(50, 20, 300, 30);
        titleLabel.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
        privilegesDialog.getContentPane().add(titleLabel);

        DefaultListModel<UserPrivilegeInfo> listModel = new DefaultListModel<>();
        JList<UserPrivilegeInfo> userList = new JList<>(listModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 14));
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBackground(globalDarkMode ? new Color(60, 60, 60) : new Color(255, 250, 240));
        userList.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);

        // Custom cell renderer with dark mode support
        userList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, 
                        index, isSelected, cellHasFocus);
                if (value instanceof UserPrivilegeInfo) {
                    UserPrivilegeInfo user = (UserPrivilegeInfo) value;
                    label.setText(user.toString());
                    if (user.isReadOnly()) {
                        label.setForeground(Color.RED);
                    } else {
                        label.setForeground(globalDarkMode ? Color.WHITE : Color.BLACK);
                    }
                    if (isSelected) {
                        label.setBackground(globalDarkMode ? new Color(100, 100, 100) : list.getSelectionBackground());
                    } else {
                        label.setBackground(globalDarkMode ? new Color(60, 60, 60) : list.getBackground());
                    }
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBounds(50, 60, 300, 300);
        scrollPane.getViewport().setBackground(globalDarkMode ? new Color(60, 60, 60) : new Color(255, 250, 240));
        privilegesDialog.getContentPane().add(scrollPane);

        // Update button styles with dark mode support
        Color buttonBackground = globalDarkMode ? new Color(70, 70, 70) : new Color(240, 240, 240);
        Color buttonText = globalDarkMode ? Color.WHITE : Color.BLACK;

        JButton showPasswordButton = new JButton("Show Selected User Password");
        showPasswordButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        showPasswordButton.setBounds(50, 330, 300, 30);
        showPasswordButton.setBackground(buttonBackground);
        showPasswordButton.setForeground(buttonText);
        privilegesDialog.getContentPane().add(showPasswordButton);

        JButton deleteUserButton = new JButton("Delete Selected User");
        deleteUserButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        deleteUserButton.setBounds(50, 370, 300, 30);
        deleteUserButton.setBackground(buttonBackground);
        deleteUserButton.setForeground(Color.RED);
        privilegesDialog.getContentPane().add(deleteUserButton);

        JButton toggleButton = new JButton("Toggle Read-Only Mode");
        toggleButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        toggleButton.setBounds(50, 410, 300, 30);
        toggleButton.setBackground(buttonBackground);
        toggleButton.setForeground(buttonText);
        privilegesDialog.getContentPane().add(toggleButton);

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Arial Black", Font.BOLD, 12));
        refreshButton.setBounds(50, 450, 300, 30);
        refreshButton.setBackground(buttonBackground);
        refreshButton.setForeground(buttonText);
        privilegesDialog.getContentPane().add(refreshButton);

        JLabel statusLabel = new JLabel("");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setBounds(50, 490, 300, 20);
        privilegesDialog.getContentPane().add(statusLabel);

        // Refresh list function
        Runnable refreshList = () -> {
            listModel.clear();
            List<UserPrivilegeInfo> users = UserDatabase.getAllUsersExceptAdmin();
            System.out.println("Refreshing list, found " + users.size() + " users");
            
            debugLabel.setText("Found " + users.size() + " users in database");
            
            for (UserPrivilegeInfo user : users) {
                System.out.println("Adding to list: " + user.toString());
                listModel.addElement(user);
            }
            
            if (listModel.isEmpty()) {
                statusLabel.setText("No users found in database");
            } else {
                statusLabel.setText(listModel.size() + " users loaded");
            }
        };

        // Add action listeners
        toggleButton.addActionListener(e -> {
            UserPrivilegeInfo selectedUser = userList.getSelectedValue();
            if (selectedUser != null) {
                boolean newState = !selectedUser.isReadOnly();
                if (UserDatabase.setUserReadOnly(selectedUser.getUsername(), newState)) {
                    refreshList.run();
                    statusLabel.setText("Privileges updated for " + selectedUser.getUsername());
                } else {
                    statusLabel.setText("Failed to update privileges!");
                }
            } else {
                statusLabel.setText("Please select a user!");
            }
        });

        refreshButton.addActionListener(e -> {
            refreshList.run();
            statusLabel.setText("List refreshed!");
        });

        // Add delete button action listener
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPrivilegeInfo selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    int confirm = JOptionPane.showConfirmDialog(privilegesDialog,
                        "Are you sure you want to delete user '" + selectedUser.getUsername() + "'?",
                        "Confirm Delete User",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (UserDatabase.deleteUser(selectedUser.getUsername())) {
                            refreshList.run();
                            statusLabel.setText("User '" + selectedUser.getUsername() + "' deleted successfully!");
                        } else {
                            statusLabel.setText("Failed to delete user!");
                        }
                    }
                } else {
                    statusLabel.setText("Please select a user to delete!");
                }
            }
        });

        // Initial list population
        refreshList.run();

        // Add action listener for Show Password button
        showPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPrivilegeInfo selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    String username = selectedUser.getUsername();
                    String password = UserDatabase.getUserPassword(username);
                    if (password != null) {
                        // Show confirmation dialog first
                        int confirm = JOptionPane.showConfirmDialog(privilegesDialog,
                            "Are you sure you want to view the password for user '" + username + "'?\n" +
                            "This action will be logged.",
                            "Confirm Password View",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(privilegesDialog,
                                "Password for user '" + username + "':\n" + password,
                                "User Password",
                                JOptionPane.INFORMATION_MESSAGE);
                            
                            // Log the password view action
                            try {
                                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(new Date());
                                FileWriter fw = new FileWriter("admin_actions.log", true);
                                fw.write(timestamp + " - Admin viewed password for user: " + username + "\n");
                                fw.close();
                            } catch (IOException ex) {
                                System.err.println("Error logging admin action: " + ex.getMessage());
                            }
                        }
                    } else {
                        statusLabel.setText("Could not retrieve password for user '" + username + "'");
                    }
                } else {
                    statusLabel.setText("Please select a user first!");
                }
            }
        });

        // Add mouse listener for right-click menu
        userList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {  // Right click
                    int index = userList.locationToIndex(e.getPoint());
                    userList.setSelectedIndex(index);
                    UserPrivilegeInfo selectedUser = userList.getSelectedValue();
                    
                    if (selectedUser != null) {
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem showPasswordItem = new JMenuItem("Show Password");
                        showPasswordItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                String username = selectedUser.getUsername();
                                String password = UserDatabase.getUserPassword(username);
                                if (password != null) {
                                    // Show confirmation dialog first
                                    int confirm = JOptionPane.showConfirmDialog(privilegesDialog,
                                        "Are you sure you want to view the password for user '" + username + "'?\n" +
                                        "This action will be logged.",
                                        "Confirm Password View",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.WARNING_MESSAGE);
                                    
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        JOptionPane.showMessageDialog(privilegesDialog,
                                            "Password for user '" + username + "':\n" + password,
                                            "User Password",
                                            JOptionPane.INFORMATION_MESSAGE);
                                        
                                        // Log the password view action
                                        try {
                                            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .format(new Date());
                                            FileWriter fw = new FileWriter("admin_actions.log", true);
                                            fw.write(timestamp + " - Admin viewed password for user: " + username + "\n");
                                            fw.close();
                                        } catch (IOException ex) {
                                            System.err.println("Error logging admin action: " + ex.getMessage());
                                        }
                                    }
                                } else {
                                    statusLabel.setText("Could not retrieve password for user '" + username + "'");
                                }
                            }
                        });
                        popup.add(showPasswordItem);
                        popup.show(userList, e.getX(), e.getY());
                    }
                }
            }
        });

        privilegesDialog.setVisible(true);
    }

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextArea txtrBooks;
	String url = "jdbc:sqlite:bookstore.db"; 
	String tableName = "BOOKS";
	Connection connection;
	Statement statement ;
    ResultSet rs ;
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextField textField_7;
    private JTextField textField_8;
    private JTextField textField_9;
    private JTextField textField_10;
    private JTextField textField_11;
    private JTextField textField_13;
    private JTextField textField_14;
    private JTextField textField_15;
    private JTextField textField_16;
    private JTextField textField_17;
    private JTextField textField_18;
    private JTextField textField_12;
    private JTextField searchField;
    private JButton searchButton;
    private boolean isDarkMode = false;
    private Color lightBackground = new Color(227, 208, 172);
    private Color darkBackground = new Color(50, 50, 50);
    private Color lightText = new Color(0, 0, 0);
    private Color darkText = new Color(255, 255, 255);
    
	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame("Turning Pages Bookstore GUI");
		frame.setBackground(new Color(153, 102, 0));
		frame.getContentPane().setBackground(lightBackground);
		frame.setForeground(lightText);
		frame.getContentPane().setForeground(lightText);
		frame.setBounds(100, 100, 1300, 660);
		frame.setLocationRelativeTo(null);  
		frame.setResizable(false);          
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		txtrBooks = new JTextArea();
		txtrBooks.setForeground(new Color(255, 0, 102));
		txtrBooks.setBackground(Color.WHITE);
		txtrBooks.setFont(new Font("Arial Rounded MT Bold", Font.ITALIC, 12));
		txtrBooks.setText("TURNING PAGES\r\nBOOK STORE GUI\r\n");
		txtrBooks.setColumns(40);
		txtrBooks.setRows(10);
		txtrBooks.setWrapStyleWord(true);
		txtrBooks.setLineWrap(true);
		txtrBooks.setBounds(10, 306, 500, 150);
		frame.getContentPane().add(txtrBooks);
		 try

	        {
			      connection = DriverManager.getConnection(url);
			      statement = connection.createStatement();
		          statement.setQueryTimeout(30); 
		          DatabaseMetaData meta = connection.getMetaData(); 
		          rs = meta.getTables(null, null, tableName, null); 
		    if (!rs.next()) { 
		        txtrBooks.setText(txtrBooks.getText()+"Creating tables...\r\n");
		    }

		    String[] tables = {"BOOKS", "SALES", "STOCK", "SUPPLIERS"};
		    for (String table : tables) {
		        rs = meta.getTables(null, null, table, null);
		        if (!rs.next()) {
		            txtrBooks.setText(txtrBooks.getText() + "Creating table " + table + "...\r\n");
		            switch(table) {
		                case "BOOKS":
		                    statement.executeUpdate("CREATE TABLE BOOKS (bookId integer PRIMARY KEY, title varchar(255), author varchar(255), price int, genre varchar(255))");
		                    break;
		                case "SALES":
		                    statement.executeUpdate("CREATE TABLE SALES (saleId integer PRIMARY KEY, bookId int, stockId int, quantitySold int, totalPrice int, SaleDate date, FOREIGN KEY(bookId) REFERENCES BOOKS(bookId), FOREIGN KEY(stockId) REFERENCES STOCK(stockId))");
		                    break;
		                case "STOCK":
		                    statement.executeUpdate("CREATE TABLE STOCK (stockId integer PRIMARY KEY, bookId int, supplierId varchar(40), stockQuantity int, FOREIGN KEY(bookId) REFERENCES BOOKS(bookId), FOREIGN KEY(supplierId) REFERENCES SUPPLIERS(supplierId))");
		                    break;
		                case "SUPPLIERS":
		                    statement.executeUpdate("CREATE TABLE SUPPLIERS (supplierId varchar(40) PRIMARY KEY, name varchar(40), contactInfo varchar(255), location varchar(255))");
		                    break;
		            }
		            txtrBooks.setText(txtrBooks.getText() + "Table " + table + " created successfully.\r\n");
		        } else {
		            txtrBooks.setText(txtrBooks.getText() + "Table " + table + " exists.\r\n");
		        }
		    }
	        }
		    catch(SQLException e)
	        {
	          e.printStackTrace(System.err);
	        }
		 
		JScrollPane scrollPane = new JScrollPane(txtrBooks);
		scrollPane.setBounds(400, 400, 500, 150);
		frame.getContentPane().add(scrollPane);
		 
		JLabel lblNewLabel = new JLabel("BOOKS");
		lblNewLabel.setFont(new Font("Arial Black", Font.BOLD, 13));
		lblNewLabel.setBounds(25, 47, 153, 37);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("BOOK ID:");
		lblNewLabel_1.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1.setBounds(25, 89, 106, 26);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("BOOK TITLE:");
		lblNewLabel_1_1.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_1.setBounds(25, 126, 106, 26);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("BOOK AUTHOR:");
		lblNewLabel_1_2.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_2.setBounds(25, 159, 106, 26);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("PRICE:");
		lblNewLabel_1_3.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_3.setBounds(25, 196, 106, 26);
		frame.getContentPane().add(lblNewLabel_1_3);
		
		textField = new JTextField();
		textField.setBounds(141, 95, 96, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(141, 129, 96, 19);
		frame.getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(141, 162, 96, 19);
		frame.getContentPane().add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(141, 199, 96, 19);
		frame.getContentPane().add(textField_3);
		
		btnNewBook = new JButton("NEW BOOK");
		btnNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String checkId = "SELECT bookId FROM BOOKS WHERE bookId = " + textField.getText();
					rs = statement.executeQuery(checkId);
					
					if (rs.next()) {
						txtrBooks.setText("Error: Book ID " + textField.getText() + " already exists!\n");
						return;
					}
					
					String query = "INSERT INTO BOOKS (bookId, title, author, price, genre) VALUES (" +
						textField.getText() + ", '" +  // Book ID
						textField_1.getText() + "', '" +  // Title
						textField_2.getText() + "', " +  // Author
						textField_3.getText() + ", '" +  // Price
						textField_11.getText() + "')";  // Genre
					statement.executeUpdate(query);
					txtrBooks.setText("Book added successfully!\n");
				} catch(SQLException er) {
					txtrBooks.setText("Error adding book: " + er.getMessage());
					er.printStackTrace(System.err);
				}
			}
		});
		
		btnNewBook.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnNewBook.setBounds(25, 259, 212, 26);
		frame.getContentPane().add(btnNewBook);
		
		JLabel lblNewLabel_1_4 = new JLabel("INPUT:");
		lblNewLabel_1_4.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_4.setBounds(400, 367, 41, 26);
		frame.getContentPane().add(lblNewLabel_1_4);
		
		textField_4 = new JTextField();
		textField_4.setForeground(lightText);
		textField_4.setFont(new Font("Arial Black", Font.BOLD | Font.ITALIC, 12));
		textField_4.setColumns(10);
		textField_4.setBounds(451, 370, 333, 19);
		frame.getContentPane().add(textField_4);
		
		JButton btnShowRecord = new JButton("PRINT");
		
		btnShowRecord.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnShowRecord.setBounds(794, 367, 106, 26);
		frame.getContentPane().add(btnShowRecord);
		
	
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtrBooks.setText("");
			}
		});
		btnClear.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnClear.setBounds(400, 561, 500, 26);
		frame.getContentPane().add(btnClear);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setMaximumRowCount(8);
		comboBox.setFont(new Font("Arial Black", Font.BOLD | Font.ITALIC, 10));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {
		    "Find Book by ID",
		    "List All Books",
		    "Check Stock by Book ID",
		    "View All Stock Levels",
		    "View Sales by Book ID",
		    "View All Sales",
		    "View Supplier Details",
		    "Low Stock Alert (< 5)"
		}));
		comboBox.setBounds(400, 335, 500, 21);
		frame.getContentPane().add(comboBox);

		btnShowRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					switch(comboBox.getSelectedIndex()) {
						case 0: // Find Book by ID
							if(!textField_4.getText().isEmpty()) {
								String query = "SELECT * FROM BOOKS WHERE bookId = " + textField_4.getText();
								rs = statement.executeQuery(query);
								while(rs.next()) {
									txtrBooks.setText("Book Details:\n" + 
										"Title: " + rs.getString("title") + "\n" +
										"Author: " + rs.getString("author") + "\n" +
										"Price: $" + rs.getInt("price") + "\n" +
										"Genre: " + rs.getString("genre") + "\n");
								}
							}
							break;

						case 1: // List All Books
							rs = statement.executeQuery("SELECT * FROM BOOKS");
							txtrBooks.setText("All Books:\n");
							while(rs.next()) {
								txtrBooks.append("ID: " + rs.getInt("bookId") + 
									" | Title: " + rs.getString("title") + 
									" | Author: " + rs.getString("author") + 
									" | Price: $" + rs.getInt("price") + 
									" | Genre: " + rs.getString("genre") + "\n");
							}
							break;

						case 2: // Check Stock by Book ID
							if(!textField_4.getText().isEmpty()) {
								String query = 
									"SELECT b.bookId, b.title, s.stockQuantity, sup.location, sup.name as supplier_name " +
									"FROM BOOKS b " +
									"LEFT JOIN STOCK s ON b.bookId = s.bookId " +
									"LEFT JOIN SUPPLIERS sup ON s.supplierId = sup.supplierId " +
									"WHERE b.bookId = " + textField_4.getText();
								
								rs = statement.executeQuery(query);
								boolean found = false;
								int totalStock = 0;
								
								while(rs.next()) {
									if (!found) {
										txtrBooks.setText("Stock Information for Book ID " + textField_4.getText() + ":\n");
										txtrBooks.append("Title: " + rs.getString("title") + "\n\n");
										txtrBooks.append("Stock Locations:\n");
										found = true;
									}
									String location = rs.getString("location");
									String supplierName = rs.getString("supplier_name");
									int stockQuantity = rs.getInt("stockQuantity");
									if (location != null) {
										txtrBooks.append("- Location: " + location + " | Supplier: " + supplierName + 
											" | Stock: " + stockQuantity + " units\n");
										totalStock += stockQuantity;
									}
								}
								if (found) {
									txtrBooks.append("\nTotal Stock: " + totalStock + " units\n");
								} else {
									txtrBooks.setText("No book found with ID " + textField_4.getText());
								}
							} else {
								txtrBooks.setText("Please enter a Book ID to check stock.");
							}
							break;

						case 3: // View All Stock Levels
							rs = statement.executeQuery(
								"SELECT b.bookId, b.title, b.price, s.stockId, s.stockQuantity, sup.location, sup.name as supplier_name " +
								"FROM BOOKS b " +
								"LEFT JOIN STOCK s ON b.bookId = s.bookId " +
								"LEFT JOIN SUPPLIERS sup ON s.supplierId = sup.supplierId " +
								"ORDER BY b.bookId");
							txtrBooks.setText("Current Stock Levels:\n");
							int currentBookId = -1;
							String currentTitle = "";
							int totalForBook = 0;
							
							while(rs.next()) {
								int bookId = rs.getInt("bookId");
								if (bookId != currentBookId) {
									if (currentBookId != -1) {
										txtrBooks.append("Total stock for " + currentTitle + ": " + totalForBook + "\n\n");
									}
									currentBookId = bookId;
									currentTitle = rs.getString("title");
									totalForBook = 0;
									txtrBooks.append("Book ID: " + bookId + 
										" | Title: " + currentTitle + 
										" | Price: $" + rs.getInt("price") + "\n");
								}
								String location = rs.getString("location");
								String supplierName = rs.getString("supplier_name");
								int stockQuantity = rs.getInt("stockQuantity");
								Integer stockId = rs.getObject("stockId") != null ? rs.getInt("stockId") : null;
								if (location != null) {
									txtrBooks.append("  Stock ID: " + stockId + 
										" | Location: " + location + 
										" | Supplier: " + supplierName + 
										" | Stock: " + stockQuantity + "\n");
									totalForBook += stockQuantity;
								}
							}
							if (currentBookId != -1) {
								txtrBooks.append("Total stock for " + currentTitle + ": " + totalForBook + "\n");
							}
							break;

						case 4: // View Sales by Book ID
							if(!textField_4.getText().isEmpty()) {
								rs = statement.executeQuery(
									"SELECT s.saleId, s.bookId, s.quantitySold, s.totalPrice, s.SaleDate, " +
									"b.title " +
									"FROM SALES s " +
									"JOIN BOOKS b ON s.bookId = b.bookId " +
									"WHERE b.bookId = " + textField_4.getText());
								txtrBooks.setText("Sales History:\n");
								while(rs.next()) {
									txtrBooks.append(
										"Sale ID: " + rs.getInt("saleId") + 
										" | Title: " + rs.getString("title") +
										" | Quantity: " + rs.getInt("quantitySold") +
										" | Total: $" + rs.getInt("totalPrice") +
										" | Date: " + rs.getString("SaleDate") + "\n");
								}
							}
							break;

						case 5: // View All Sales
							rs = statement.executeQuery(
								"SELECT s.saleId, s.bookId, s.quantitySold, s.totalPrice, s.SaleDate, " +
								"b.title " +
								"FROM SALES s " +
								"JOIN BOOKS b ON s.bookId = b.bookId");
							txtrBooks.setText("All Sales:\n");
							while(rs.next()) {
								txtrBooks.append(
									"Sale ID: " + rs.getInt("saleId") + 
									" | Book ID: " + rs.getInt("bookId") +
									" | Title: " + rs.getString("title") +
									" | Amount: $" + rs.getInt("totalPrice") +
									" | Date: " + rs.getString("SaleDate") +
									"\n");
							}
							break;

						case 6: // View Supplier Details
							rs = statement.executeQuery("SELECT * FROM SUPPLIERS");
							txtrBooks.setText("Supplier Information:\n");
							while(rs.next()) {
								txtrBooks.append("ID: " + rs.getString("supplierId") + 
									" | Name: " + rs.getString("name") +
									" | Contact: " + rs.getString("contactInfo") + "\n");
							}
							break;

						case 7: // Low Stock Alert
							rs = statement.executeQuery(
								"SELECT b.bookId, b.title, s.stockQuantity, sup.location, sup.name as supplier_name " +
								"FROM BOOKS b " +
								"LEFT JOIN STOCK s ON b.bookId = s.bookId " +
								"LEFT JOIN SUPPLIERS sup ON s.supplierId = sup.supplierId " +
								"GROUP BY b.bookId, b.title " +
								"HAVING SUM(s.stockQuantity) < 5 OR SUM(s.stockQuantity) IS NULL");
							txtrBooks.setText("Low Stock Alert:\n");
							while(rs.next()) {
								txtrBooks.append("Title: " + rs.getString("title") + 
									" | Location: " + rs.getString("location") +
									" | Supplier: " + rs.getString("supplier_name") +
									" | Current Stock: " + (rs.getObject("stockQuantity") == null ? 0 : rs.getInt("stockQuantity")) + "\n");
							}
							break;
					}
				} catch(SQLException ex) {
					txtrBooks.setText("Error: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});
		
		JLabel lblSTOCK = new JLabel("STOCK");
		lblSTOCK.setFont(new Font("Arial Black", Font.BOLD, 13));
		lblSTOCK.setBounds(303, 47, 153, 37);
		frame.getContentPane().add(lblSTOCK);
		
		JLabel lblNewLabel_1_5 = new JLabel("STOCK ID:");
		lblNewLabel_1_5.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_5.setBounds(303, 89, 116, 26);
		frame.getContentPane().add(lblNewLabel_1_5);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(439, 92, 96, 19);
		frame.getContentPane().add(textField_5);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("STOCK QUANTITY:");
		lblNewLabel_1_1_1.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_1_1.setBounds(303, 196, 112, 26);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(439, 199, 96, 19);
		frame.getContentPane().add(textField_6);
		
		btnNewStock = new JButton("NEW STOCK");
		btnNewStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String checkSupplier = "SELECT * FROM SUPPLIERS WHERE supplierId = " + textField_15.getText();
					ResultSet rsSupplier = statement.executeQuery(checkSupplier);
					
					if (rsSupplier.next()) {
						String query = "INSERT INTO STOCK (stockId, bookId, supplierId, stockQuantity) VALUES (" +
							textField_5.getText() + ", " +   // Stock ID
							textField_9.getText() + ", " +   // Book ID
							textField_15.getText() + ", " +  // Supplier ID
							textField_6.getText() + ")";     // Stock Quantity
						statement.executeUpdate(query);
						txtrBooks.setText("New stock added successfully!\n");
					} else {
						txtrBooks.setText("Error: Supplier ID " + textField_15.getText() + " does not exist!\n");
					}
					
				} catch(SQLException er) {
					txtrBooks.setText("Error managing stock: " + er.getMessage() + "\n");
					er.printStackTrace(System.err);
				}
			}
		});
		btnNewStock.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnNewStock.setBounds(303, 234, 232, 26);
		frame.getContentPane().add(btnNewStock);
		
		JLabel lblSales = new JLabel("SALES");
		lblSales.setFont(new Font("Arial Black", Font.BOLD, 13));
		lblSales.setBounds(603, 47, 153, 37);
		frame.getContentPane().add(lblSales);
		
		JLabel lblNewLabel_1_6 = new JLabel("BOOK ID:");
		lblNewLabel_1_6.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_6.setBounds(603, 126, 100, 26);
		frame.getContentPane().add(lblNewLabel_1_6);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(737, 129, 96, 19);
		frame.getContentPane().add(textField_7);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("SALE ID:");
		lblNewLabel_1_1_2.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_1_2.setBounds(603, 89, 126, 26);
		frame.getContentPane().add(lblNewLabel_1_1_2);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(737, 92, 96, 19);
		frame.getContentPane().add(textField_8);
		
		btnNewSale = new JButton("NEW SALE");
		btnNewSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String checkPrice = "SELECT price FROM BOOKS WHERE bookId = " + textField_7.getText();
					rs = statement.executeQuery(checkPrice);
					
					if (rs.next()) {
						int bookPrice = rs.getInt("price");
						int salePrice = Integer.parseInt(textField_14.getText());
						
						if (salePrice < bookPrice) {
							int response = JOptionPane.showConfirmDialog(frame,
								"Warning: Sale price ($" + salePrice + ") is less than the book's original price ($" + bookPrice + ").\n\n" +
								"Do you want to proceed with this sale?",
								"Price Warning",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
							
							if (response != JOptionPane.YES_OPTION) {
								txtrBooks.setText("Sale cancelled by user.\n");
								return;
							}
						}
						
						String query = "INSERT INTO SALES (saleId, bookId, stockId, quantitySold, totalPrice, SaleDate) VALUES (" +
							textField_8.getText() + ", " +  // Sale ID
							textField_7.getText() + ", " +  // Book ID
							textField_18.getText() + ", " +  // Stock ID
							textField_13.getText() + ", " +  // Quantity Sold
							textField_14.getText() + ", " +  // Total Price
							"date('now'))";  // Current date
						statement.executeUpdate(query);
						
						String updateStockLocation = "UPDATE STOCK SET stockQuantity = stockQuantity - " + 
							textField_13.getText() + " WHERE stockId = " + textField_18.getText();
						statement.executeUpdate(updateStockLocation);
						
						txtrBooks.setText("Sale recorded successfully!\n");
					} else {
						txtrBooks.setText("Error: Book ID not found!\n");
					}
				} catch(SQLException er) {
					txtrBooks.setText("Error recording sale: " + er.getMessage());
					er.printStackTrace(System.err);
				} catch(NumberFormatException er) {
					txtrBooks.setText("Error: Please ensure all numeric fields contain valid numbers.");
				}
			}
		});
		btnNewSale.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnNewSale.setBounds(603, 271, 95, 26);
		frame.getContentPane().add(btnNewSale);
			
		JLabel lblSuppliers = new JLabel("BOOK ID:");
		lblSuppliers.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblSuppliers.setBounds(303, 126, 116, 26);
		frame.getContentPane().add(lblSuppliers);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(439, 129, 96, 19);
		frame.getContentPane().add(textField_9);
		
		JLabel lblNewLabel_1_3_1 = new JLabel("SUPPLIER ID:");
		lblNewLabel_1_3_1.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_1_3_1.setBounds(954, 89, 100, 26);
		frame.getContentPane().add(lblNewLabel_1_3_1);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(1109, 92, 86, 19);
		frame.getContentPane().add(textField_10);
		
		JButton btnAddSupplier = new JButton("ADD SUPPLIER");
		btnAddSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String checkId = "SELECT supplierId FROM SUPPLIERS WHERE supplierId = '" + textField_10.getText() + "'";
					rs = statement.executeQuery(checkId);
					
					if (rs.next()) {
						txtrBooks.setText("Error: Supplier ID " + textField_10.getText() + " already exists!\n");
						return;
					}
					
					String query = "INSERT INTO SUPPLIERS (supplierId, name, contactInfo, location) VALUES ('" +
						textField_10.getText() + "', '" +  // Supplier ID
						textField_16.getText() + "', '" +  // Supplier Name
						textField_17.getText() + "', '" +  // Contact Info
					    textField_12.getText() + "')";     // Locatiion Info
					statement.executeUpdate(query);
					txtrBooks.setText("Supplier added successfully!\n");
				} catch(SQLException er) {
					txtrBooks.setText("Error adding supplier: " + er.getMessage());
					er.printStackTrace(System.err);
				}
			}
		});
		btnAddSupplier.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnAddSupplier.setBounds(954, 234, 241, 26);
		frame.getContentPane().add(btnAddSupplier);
		
		JLabel lblNewLabel_2 = new JLabel("BOOK GENRE:");
		lblNewLabel_2.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_2.setBounds(25, 228, 95, 26);
		frame.getContentPane().add(lblNewLabel_2);
		
		textField_11 = new JTextField();
		textField_11.setBounds(141, 229, 96, 20);
		frame.getContentPane().add(textField_11);
		textField_11.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("QUANTITY SOLD:");
		lblNewLabel_4.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_4.setBounds(603, 199, 100, 23);
		frame.getContentPane().add(lblNewLabel_4);
		
		textField_13 = new JTextField();
		textField_13.setBounds(737, 199, 96, 20);
		frame.getContentPane().add(textField_13);
		textField_13.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("PRICE:");
		lblNewLabel_5.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_5.setBounds(603, 234, 100, 20);
		frame.getContentPane().add(lblNewLabel_5);
		
		textField_14 = new JTextField();
		textField_14.setBounds(737, 237, 96, 20);
		frame.getContentPane().add(textField_14);
		textField_14.setColumns(10);
		
		btnDeleteSale = new JButton("DELETE SALE");
		btnDeleteSale.setForeground(new Color(204, 0, 0));
		btnDeleteSale.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnDeleteSale.setBounds(721, 272, 112, 24);
		frame.getContentPane().add(btnDeleteSale);
		
		btnRemoveSupplier = new JButton("REMOVE SUPPLIER");
		btnRemoveSupplier.setForeground(new Color(204, 0, 0));
		btnRemoveSupplier.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnRemoveSupplier.setBounds(954, 273, 241, 29);
		frame.getContentPane().add(btnRemoveSupplier);
		
		textField_15 = new JTextField();
		textField_15.setBounds(439, 159, 96, 20);
		frame.getContentPane().add(textField_15);
		textField_15.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("SUPPLIER ID:");
		lblNewLabel_6.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_6.setBounds(303, 162, 93, 20);
		frame.getContentPane().add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("SUPPLIER NAME:");
		lblNewLabel_7.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_7.setBounds(954, 132, 100, 14);
		frame.getContentPane().add(lblNewLabel_7);
		
		textField_16 = new JTextField();
		textField_16.setBounds(1109, 128, 86, 20);
		frame.getContentPane().add(textField_16);
		textField_16.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("SUPPLIER CONTACT:");
		lblNewLabel_8.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_8.setBounds(954, 163, 135, 19);
		frame.getContentPane().add(lblNewLabel_8);
		
		textField_17 = new JTextField();
		textField_17.setBounds(1109, 162, 86, 20);
		frame.getContentPane().add(textField_17);
		textField_17.setColumns(10);

		try {
			closeResources();
			
			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			DatabaseMetaData meta = connection.getMetaData(); 
			
			rs = meta.getTables(null, null, "BOOKS", null);
			if (!rs.next()) {
				txtrBooks.setText(txtrBooks.getText() + "Creating BOOKS table...\r\n");
				statement.executeUpdate("CREATE TABLE BOOKS (bookId integer PRIMARY KEY, title varchar(255), author varchar(255), price int, genre varchar(255))");
				txtrBooks.setText(txtrBooks.getText() + "BOOKS table created successfully.\r\n");
			}
			if (rs != null) rs.close();

			rs = meta.getTables(null, null, "SALES", null);
			if (!rs.next()) {
				txtrBooks.setText(txtrBooks.getText() + "Creating SALES table...\r\n");
				statement.executeUpdate("CREATE TABLE SALES (saleId integer PRIMARY KEY, bookId int, stockId int, quantitySold int, totalPrice int, SaleDate date)");
				txtrBooks.setText(txtrBooks.getText() + "SALES table created successfully.\r\n");
			}
			if (rs != null) rs.close();

			rs = meta.getTables(null, null, "STOCK", null);
			if (!rs.next()) {
				txtrBooks.setText(txtrBooks.getText() + "Creating STOCK table...\r\n");
				statement.executeUpdate("CREATE TABLE STOCK (stockId integer PRIMARY KEY, bookId int, supplierId varchar(40), stockQuantity int, FOREIGN KEY(bookId) REFERENCES BOOKS(bookId), FOREIGN KEY(supplierId) REFERENCES SUPPLIERS(supplierId))");
				txtrBooks.setText(txtrBooks.getText() + "STOCK table created successfully.\r\n");
			}
			if (rs != null) rs.close();

			rs = meta.getTables(null, null, "SUPPLIERS", null);
			if (!rs.next()) {
				txtrBooks.setText(txtrBooks.getText() + "Creating SUPPLIERS table...\r\n");
				statement.executeUpdate("CREATE TABLE SUPPLIERS (supplierId varchar(40) PRIMARY KEY, name varchar(40), contactInfo varchar(255), location varchar(255))");
				txtrBooks.setText(txtrBooks.getText() + "SUPPLIERS table created successfully.\r\n");
			}
			if (rs != null) rs.close();

		} catch(SQLException e) {
			txtrBooks.setText("Database Error: " + e.getMessage() + "\r\n");
			e.printStackTrace(System.err);
		} finally {
			closeResources();
			
			try {
				connection = DriverManager.getConnection(url);
				statement = connection.createStatement();
			} catch (SQLException e) {
				txtrBooks.setText(txtrBooks.getText() + "Error reconnecting to database: " + e.getMessage() + "\r\n");
				e.printStackTrace(System.err);
			}
		}

		btnDeleteSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!textField_8.getText().isEmpty()) {
						statement.executeUpdate("DELETE FROM SALES WHERE saleId = " + textField_8.getText());
						txtrBooks.setText("Sale deleted successfully!\n");
					} else {
						txtrBooks.setText("Please enter a Sale ID to delete\n");
					}
				} catch(SQLException ex) {
					txtrBooks.setText("Error deleting sale: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});

		btnRemoveSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(!textField_10.getText().isEmpty()) {
						statement.executeUpdate("DELETE FROM SUPPLIERS WHERE supplierId = '" + textField_10.getText() + "'");
						txtrBooks.setText("Supplier removed successfully!\n");
					} else {
						txtrBooks.setText("Please enter a Supplier ID to remove\n");
					}
				} catch(SQLException ex) {
					txtrBooks.setText("Error removing supplier: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeResources();
				frame.dispose();
				// Show login screen again
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							showLoginDialog();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}
		});

		btnExport = new JButton("EXPORT DATA");
		btnExport.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnExport.setBounds(928, 434, 126, 26);
		frame.getContentPane().add(btnExport);
		
		btnUpdateBook = new JButton("UPDATE BOOK");
		btnUpdateBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String checkId = "SELECT bookId FROM BOOKS WHERE bookId = " + textField.getText();
					rs = statement.executeQuery(checkId);
					
					if (!rs.next()) {
						txtrBooks.setText("Error: Book ID " + textField.getText() + " does not exist!\n");
						return;
					}
					
					String query = "UPDATE BOOKS SET " +
						"title = '" + textField_1.getText() + "', " +
						"author = '" + textField_2.getText() + "', " +
						"price = " + textField_3.getText() + ", " +
						"genre = '" + textField_11.getText() + "' " +
						"WHERE bookId = " + textField.getText();
						
					statement.executeUpdate(query);
					txtrBooks.setText("Book updated successfully!\n");
					
				} catch(SQLException er) {
					txtrBooks.setText("Error updating book: " + er.getMessage());
					er.printStackTrace(System.err);
				}
			}
		});
		btnUpdateBook.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnUpdateBook.setBounds(25, 296, 212, 26);
		frame.getContentPane().add(btnUpdateBook);
		
		JLabel lblNewLabel_9 = new JLabel("STOCK ID:");
		lblNewLabel_9.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_9.setBounds(603, 165, 100, 20);
		frame.getContentPane().add(lblNewLabel_9);
		
		textField_18 = new JTextField();
		textField_18.setBounds(737, 162, 96, 23);
		frame.getContentPane().add(textField_18);
		textField_18.setColumns(10);
		
		JButton btnClearTables = new JButton("CLEAR TABLES");
		btnClearTables.setForeground(new Color(204, 0, 0));
		btnClearTables.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnClearTables.setBounds(928, 471, 126, 26);
		frame.getContentPane().add(btnClearTables);
		
		JButton btnImportData = new JButton("IMPORT DATA");
		btnImportData.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnImportData.setBounds(928, 400, 126, 23);
		frame.getContentPane().add(btnImportData);
		
		JLabel lblNewLabel_3 = new JLabel("LOCATION:");
		lblNewLabel_3.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblNewLabel_3.setBounds(954, 200, 73, 19);
		frame.getContentPane().add(lblNewLabel_3);
		
		textField_12 = new JTextField();
		textField_12.setBounds(1109, 199, 86, 20);
		frame.getContentPane().add(textField_12);
		textField_12.setColumns(10);
		
		JLabel lblNewLabel_10 = new JLabel("SUPPLIERS");
		lblNewLabel_10.setFont(new Font("Arial Black", Font.BOLD, 13));
		lblNewLabel_10.setBounds(954, 47, 153, 25);
		frame.getContentPane().add(lblNewLabel_10);
		
		JButton btnDeleteBook = new JButton("DELETE BOOK");
		btnDeleteBook.setForeground(new Color(204, 0, 0));
		btnDeleteBook.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnDeleteBook.setBounds(25, 334, 212, 29);
		frame.getContentPane().add(btnDeleteBook);
		
		JButton btnDeleteStock = new JButton("DELETE STOCK");
		btnDeleteStock.setForeground(new Color(204, 0, 0));
		btnDeleteStock.setFont(new Font("Arial Black", Font.BOLD, 10));
		btnDeleteStock.setBounds(303, 271, 232, 31);
		frame.getContentPane().add(btnDeleteStock);
		
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] options = {"All Tables", "Books", "Sales", "Stock", "Suppliers"};
				String selected = (String)JOptionPane.showInputDialog(frame, 
					"Select table to export:", "Export Data",
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
				if (selected != null) {
					if (selected.equals("All Tables")) {
						exportAllTables();
					} else {
						exportToCSV(selected.toUpperCase());
					}
				}
			}
		});

		btnClearTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] options = {"All Tables", "Books", "Sales", "Stock", "Suppliers"};
				String selected = (String)JOptionPane.showInputDialog(frame, 
					"Select table to clear:", 
					"Clear Tables",
					JOptionPane.WARNING_MESSAGE, 
					null, 
					options, 
					options[0]);
				
				if (selected != null) {
					int response = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to clear " + selected + "?",
						"Confirm Clear Table",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
						
					if (response == JOptionPane.YES_OPTION) {
						try {
							if (selected.equals("All Tables")) {
								String[] tables = {"SALES", "STOCK", "BOOKS", "SUPPLIERS"};
								for (String table : tables) {
									statement.executeUpdate("DELETE FROM " + table);
								}
								txtrBooks.setText("All tables cleared successfully!\n");
							} else {
								String tableName = selected.toUpperCase();
								statement.executeUpdate("DELETE FROM " + tableName);
								txtrBooks.setText(tableName + " table cleared successfully!\n");
							}
						} catch (SQLException ex) {
							txtrBooks.setText("Error clearing table(s): " + ex.getMessage());
							ex.printStackTrace(System.err);
						}
					}
				}
			}
		});

		btnUpdateBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String checkId = "SELECT bookId FROM BOOKS WHERE bookId = " + textField.getText();
					rs = statement.executeQuery(checkId);
					
					if (!rs.next()) {
						txtrBooks.setText("Error: Book ID " + textField.getText() + " does not exist!\n");
						return;
					}
					
					String query = "UPDATE BOOKS SET " +
						"title = '" + textField_1.getText() + "', " +
						"author = '" + textField_2.getText() + "', " +
						"price = " + textField_3.getText() + ", " +
						"genre = '" + textField_11.getText() + "' " +
						"WHERE bookId = " + textField.getText();
						
					statement.executeUpdate(query);
					txtrBooks.setText("Book updated successfully!\n");
					
				} catch(SQLException er) {
					txtrBooks.setText("Error updating book: " + er.getMessage());
					er.printStackTrace(System.err);
				}
			}
		});

		btnImportData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] options = {"Books", "Sales", "Stock", "Suppliers"};
				String selected = (String)JOptionPane.showInputDialog(frame, 
					"Select table to import:", "Import Data",
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
				if (selected != null) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
						public boolean accept(File f) {
							return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
						}
						public String getDescription() {
							return "CSV Files (*.csv)";
						}
					});
					
					if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
						try {
							File file = fileChooser.getSelectedFile();
							BufferedReader reader = new BufferedReader(new FileReader(file));
							
							String headerLine = reader.readLine();
							if (headerLine == null) {
								throw new IOException("Empty file");
							}
							
							if (selected.contains("İ")) {
								selected = selected.replace("İ", "I");
							}
							
							String line;
							int successCount = 0;
							int errorCount = 0;
							StringBuilder errorMessages = new StringBuilder();
							
							while ((line = reader.readLine()) != null) {
								try {
									if (!line.trim().isEmpty()) {  
										String[] values = line.split(",");
										for (int i = 0; i < values.length; i++) {
											values[i] = values[i].trim();
											if (values[i].startsWith("\"") && values[i].endsWith("\"")) {
												values[i] = values[i].substring(1, values[i].length() - 1);
											}
										}
										
										StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
										queryBuilder.append(selected.toUpperCase()).append(" (");
										
										switch(selected.toUpperCase()) {
											case "BOOKS":
												queryBuilder.append("bookId, title, author, price, genre) VALUES (");
												break;
											case "STOCK":
												queryBuilder.append("stockId, bookId, supplierId, stockQuantity) VALUES (");
												break;
											case "SALES":
												queryBuilder.append("saleId, bookId, stockId, quantitySold, totalPrice, SaleDate) VALUES (");
												break;
											case "SUPPLIERS":
												queryBuilder.append("supplierId, name, contactInfo, location) VALUES (");
												break;
										}
										
										for (int i = 0; i < values.length; i++) {
											String value = values[i].trim();
											
											boolean isNumeric = 
												(selected.equalsIgnoreCase("BOOKS") && (i == 0 || i == 3)) || 
												(selected.equalsIgnoreCase("STOCK") && (i == 0 || i == 1 || i == 3)) || 
												(selected.equalsIgnoreCase("SALES") && (i == 0 || i == 1 || i == 2 || i == 3 || i == 4)); 
											
											if (isNumeric) {
												queryBuilder.append(value.isEmpty() ? "NULL" : value);
											} else {
												value = value.replace("'", "''");
												queryBuilder.append("'").append(value).append("'");
											}
											
											if (i < values.length - 1) {
												queryBuilder.append(", ");
											}
										}
										queryBuilder.append(")");
										
										statement.executeUpdate(queryBuilder.toString());
										successCount++;
									}
								} catch (SQLException ex) {
									errorCount++;
									errorMessages.append("Error on line: ").append(line).append("\n");
									errorMessages.append(ex.getMessage()).append("\n\n");
								}
							}
							
							reader.close();
							
							String resultMessage = String.format("Import completed:\n%d records imported successfully\n%d errors occurred", 
								successCount, errorCount);
							if (errorCount > 0) {
								resultMessage += "\n\nError details:\n" + errorMessages.toString();
							}
							txtrBooks.setText(resultMessage);
							
						} catch (Exception ex) {
							txtrBooks.setText("Error importing data: " + ex.getMessage() + "\n");
							ex.printStackTrace();
						}
					}
				}
			}
		});

		btnDeleteBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String bookId = textField.getText();
					
					if (bookId.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Please enter a Book ID to delete");
						return;
					}

					ResultSet checkBook = statement.executeQuery("SELECT * FROM BOOKS WHERE bookId = " + bookId);
					if (!checkBook.next()) {
						JOptionPane.showMessageDialog(frame, "Book ID " + bookId + " does not exist!");
						return;
					}

					int response = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to delete this book?\nThis will also delete related sales and stock records.",
						"Confirm Delete",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

					if (response == JOptionPane.YES_OPTION) {
						connection.setAutoCommit(false);
						try {
							statement.executeUpdate("DELETE FROM SALES WHERE bookId = " + bookId);
							statement.executeUpdate("DELETE FROM STOCK WHERE bookId = " + bookId);
							statement.executeUpdate("DELETE FROM BOOKS WHERE bookId = " + bookId);
							
							connection.commit();
							txtrBooks.setText("Book and related records deleted successfully!\n");
							
							textField.setText("");
							textField_1.setText("");
							textField_2.setText("");
							textField_3.setText("");
							textField_11.setText("");
							
						} catch (SQLException ex) {
							connection.rollback();
							throw ex;
						} finally {
							connection.setAutoCommit(true);
						}
					}
				} catch (SQLException ex) {
					txtrBooks.setText("Error deleting book: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});

		btnDeleteStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String stockId = textField_5.getText();
					
					if (stockId.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Please enter a Stock ID to delete");
						return;
					}

					ResultSet checkStock = statement.executeQuery("SELECT s.stockId, b.title, s.stockQuantity " +
						"FROM STOCK s JOIN BOOKS b ON s.bookId = b.bookId " +
						"WHERE s.stockId = " + stockId);
					
					if (!checkStock.next()) {
						JOptionPane.showMessageDialog(frame, "Stock ID " + stockId + " does not exist!");
						return;
					}

					String bookTitle = checkStock.getString("title");
					int quantity = checkStock.getInt("stockQuantity");
					int response = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to delete this stock record?\n\n" +
						"Stock ID: " + stockId + "\n" +
						"Book: " + bookTitle + "\n" +
						"Quantity: " + quantity + "\n\n" +
						"This will also delete related sales records.",
						"Confirm Delete",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

					if (response == JOptionPane.YES_OPTION) {
						connection.setAutoCommit(false);
						try {
							statement.executeUpdate("DELETE FROM SALES WHERE stockId = " + stockId);
							statement.executeUpdate("DELETE FROM STOCK WHERE stockId = " + stockId);

							connection.commit();
							txtrBooks.setText("Stock and related records deleted successfully!\n");

							textField_5.setText("");  // Stock ID
							textField_9.setText("");  // Book ID
							textField_15.setText(""); // Supplier ID
							textField_6.setText("");  // Stock Quantity
							
						} catch (SQLException ex) {
							connection.rollback();
							throw ex;
						} finally {
							connection.setAutoCommit(true);
						}
					}
				} catch (SQLException ex) {
					txtrBooks.setText("Error deleting stock: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});

		// Move search components higher
		JLabel lblSearch = new JLabel("SEARCH:");
		lblSearch.setFont(new Font("Arial Black", Font.BOLD, 10));
		lblSearch.setBounds(400, 11, 60, 26);  // Changed Y from 47 to 11
		frame.getContentPane().add(lblSearch);

		searchField = new JTextField();
		searchField.setBounds(470, 14, 300, 23);  // Changed Y from 50 to 14
		frame.getContentPane().add(searchField);
		searchField.setColumns(10);

		searchButton = new JButton("SEARCH ALL");
		searchButton.setFont(new Font("Arial Black", Font.BOLD, 10));
		searchButton.setBounds(780, 11, 120, 26);  // Changed Y from 47 to 11
		frame.getContentPane().add(searchButton);

		// Add search button action listener
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String searchTerm = searchField.getText().trim();
				if (searchTerm.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Please enter a search term");
					return;
				}

				try {
					StringBuilder results = new StringBuilder("Search Results:\n\n");
					
					// Search in BOOKS table
					String booksQuery = "SELECT * FROM BOOKS WHERE " +
						"LOWER(title) LIKE LOWER(?) OR " +
						"LOWER(author) LIKE LOWER(?) OR " +
						"LOWER(genre) LIKE LOWER(?) OR " +
						"CAST(bookId AS TEXT) LIKE ?";
					
					PreparedStatement booksStmt = connection.prepareStatement(booksQuery);
					String searchPattern = "%" + searchTerm + "%";
					booksStmt.setString(1, searchPattern);
					booksStmt.setString(2, searchPattern);
					booksStmt.setString(3, searchPattern);
					booksStmt.setString(4, searchPattern);
					
					ResultSet booksRs = booksStmt.executeQuery();
					boolean foundBooks = false;
					while (booksRs.next()) {
						if (!foundBooks) {
							results.append("BOOKS:\n");
							foundBooks = true;
						}
						results.append("ID: ").append(booksRs.getInt("bookId"))
							   .append(" | Title: ").append(booksRs.getString("title"))
							   .append(" | Author: ").append(booksRs.getString("author"))
							   .append(" | Genre: ").append(booksRs.getString("genre"))
							   .append(" | Price: $").append(booksRs.getInt("price"))
							   .append("\n");
					}
					if (foundBooks) results.append("\n");

					// Search in SUPPLIERS table
					String suppliersQuery = "SELECT * FROM SUPPLIERS WHERE " +
						"LOWER(supplierId) LIKE LOWER(?) OR " +
						"LOWER(name) LIKE LOWER(?) OR " +
						"LOWER(contactInfo) LIKE LOWER(?) OR " +
						"LOWER(location) LIKE LOWER(?)";
					
					PreparedStatement suppliersStmt = connection.prepareStatement(suppliersQuery);
					for (int i = 1; i <= 4; i++) {
						suppliersStmt.setString(i, searchPattern);
					}
					
					ResultSet suppliersRs = suppliersStmt.executeQuery();
					boolean foundSuppliers = false;
					while (suppliersRs.next()) {
						if (!foundSuppliers) {
							results.append("SUPPLIERS:\n");
							foundSuppliers = true;
						}
						results.append("ID: ").append(suppliersRs.getString("supplierId"))
							   .append(" | Name: ").append(suppliersRs.getString("name"))
							   .append(" | Contact: ").append(suppliersRs.getString("contactInfo"))
							   .append(" | Location: ").append(suppliersRs.getString("location"))
							   .append("\n");
					}
					if (foundSuppliers) results.append("\n");

					// Search in STOCK table
					String stockQuery = "SELECT s.*, b.title FROM STOCK s " +
						"JOIN BOOKS b ON s.bookId = b.bookId " +
						"WHERE CAST(s.stockId AS TEXT) LIKE ? OR " +
						"CAST(s.bookId AS TEXT) LIKE ? OR " +
						"LOWER(b.title) LIKE LOWER(?)";
					
					PreparedStatement stockStmt = connection.prepareStatement(stockQuery);
					for (int i = 1; i <= 3; i++) {
						stockStmt.setString(i, searchPattern);
					}
					
					ResultSet stockRs = stockStmt.executeQuery();
					boolean foundStock = false;
					while (stockRs.next()) {
						if (!foundStock) {
							results.append("STOCK:\n");
							foundStock = true;
						}
						results.append("Stock ID: ").append(stockRs.getInt("stockId"))
							   .append(" | Book: ").append(stockRs.getString("title"))
							   .append(" | Quantity: ").append(stockRs.getInt("stockQuantity"))
							   .append("\n");
					}
					if (foundStock) results.append("\n");

					// Search in SALES table
					String salesQuery = "SELECT s.*, b.title FROM SALES s " +
						"JOIN BOOKS b ON s.bookId = b.bookId " +
						"WHERE CAST(s.saleId AS TEXT) LIKE ? OR " +
						"CAST(s.bookId AS TEXT) LIKE ? OR " +
						"LOWER(b.title) LIKE LOWER(?)";
					
					PreparedStatement salesStmt = connection.prepareStatement(salesQuery);
					for (int i = 1; i <= 3; i++) {
						salesStmt.setString(i, searchPattern);
					}
					
					ResultSet salesRs = salesStmt.executeQuery();
					boolean foundSales = false;
					while (salesRs.next()) {
						if (!foundSales) {
							results.append("SALES:\n");
							foundSales = true;
						}
						results.append("Sale ID: ").append(salesRs.getInt("saleId"))
							   .append(" | Book: ").append(salesRs.getString("title"))
							   .append(" | Quantity: ").append(salesRs.getInt("quantitySold"))
							   .append(" | Total: $").append(salesRs.getInt("totalPrice"))
							   .append(" | Date: ").append(salesRs.getString("SaleDate"))
							   .append("\n");
					}

					if (!foundBooks && !foundSuppliers && !foundStock && !foundSales) {
						results.append("No results found for '").append(searchTerm).append("'");
					}

					txtrBooks.setText(results.toString());

				} catch (SQLException ex) {
					txtrBooks.setText("Error performing search: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});

		addDarkModeButton();

        // Move read-only setup to the end of initialize()
        if (isReadOnly) {
            // Add error message for read-only users
            ActionListener readOnlyError = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(frame,
                        "You don't have permission to perform this action.\nThis is a read-only account.",
                        "Access Denied",
                        JOptionPane.WARNING_MESSAGE);
                }
            };

            // Books
            btnNewBook.setEnabled(false);
            btnNewBook.addActionListener(readOnlyError);
            btnUpdateBook.setEnabled(false);
            btnUpdateBook.addActionListener(readOnlyError);
            btnDeleteBook.setEnabled(false);
            btnDeleteBook.addActionListener(readOnlyError);
            
            // Suppliers
            btnAddSupplier.setEnabled(false);
            btnAddSupplier.addActionListener(readOnlyError);
            btnRemoveSupplier.setEnabled(false);
            btnRemoveSupplier.addActionListener(readOnlyError);
            
            // Stock
            btnNewStock.setEnabled(false);
            btnNewStock.addActionListener(readOnlyError);
            btnDeleteStock.setEnabled(false);
            btnDeleteStock.addActionListener(readOnlyError);
            
            // Sales
            btnNewSale.setEnabled(false);
            btnNewSale.addActionListener(readOnlyError);
            btnDeleteSale.setEnabled(false);
            btnDeleteSale.addActionListener(readOnlyError);
            
            // Data Management
            btnExport.setEnabled(false);
            btnExport.addActionListener(readOnlyError);
            btnImportData.setEnabled(false);
            btnImportData.addActionListener(readOnlyError);
            btnClearTables.setEnabled(false);
            btnClearTables.addActionListener(readOnlyError);
            
            // Add a visual indicator of read-only mode
            JLabel readOnlyLabel = new JLabel("READ ONLY MODE");
            readOnlyLabel.setForeground(Color.RED);
            readOnlyLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
            readOnlyLabel.setBounds(928, 11, 150, 26);
            frame.getContentPane().add(readOnlyLabel);
        }

        // Update main GUI dark mode button initialization
        darkModeButton = new JButton(globalDarkMode ? "LIGHT MODE" : "DARK MODE");
		darkModeButton.setFont(new Font("Arial Black", Font.BOLD, 12));
		darkModeButton.setBounds(928, 561, 120, 26);  // Positioned next to search button
		frame.getContentPane().add(darkModeButton);

        // Apply initial dark mode state if needed
        if (globalDarkMode) {
            applyDarkMode();
        }

        // Update dark mode button action listener
        darkModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                globalDarkMode = !globalDarkMode;  // Toggle global state
                darkModeButton.setText(globalDarkMode ? "LIGHT MODE" : "DARK MODE");
                if (globalDarkMode) {
                    applyDarkMode();
                } else {
                    applyLightMode();
                }
            }
        });
	}

	private boolean isValidNumber(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private void refreshDisplay() {
		txtrBooks.setText("");
		textField.setText("");
		textField_1.setText("");
		textField_2.setText("");
		textField_3.setText("");
	}

	private void closeResources() {
		try {
			if (rs != null && !rs.isClosed()) rs.close();
			if (statement != null && !statement.isClosed()) statement.close();
			if (connection != null && !connection.isClosed()) connection.close();
		} catch (SQLException e) {
			System.err.println("Error closing resources: " + e.getMessage());
		}
	}

	private void initializeButtons() {
		btnNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "INSERT INTO BOOKS (bookId, title, author, price, genre) VALUES (" +
						textField.getText() + ", '" +  // Book ID
						textField_1.getText() + "', '" +  // Title
						textField_2.getText() + "', " +  // Author
						textField_3.getText() + ", '" +  // Price
						textField_11.getText() + "')";  // Genre
					statement.executeUpdate(query);
					txtrBooks.setText("Book added successfully!\n");
				} catch(SQLException er) {
					txtrBooks.setText("Error adding book: " + er.getMessage());
					er.printStackTrace(System.err);
				}
			}
		});

		btnNewSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = "INSERT INTO SALES (saleId, bookId, stockId, quantitySold, totalPrice, SaleDate) VALUES (" +
						textField_8.getText() + ", " +  // Sale ID
						textField_7.getText() + ", " +  // Book ID
						textField_18.getText() + ", " +  // Stock ID
						textField_13.getText() + ", " +  // Quantity Sold
						textField_14.getText() + ", " +  // Total Price
						"date('now'))";  // Current date
					statement.executeUpdate(query);
					
					String updateBooksStock = "UPDATE BOOKS SET stockQuantity = stockQuantity - " + 
						textField_13.getText() + " WHERE bookId = " + textField_7.getText();
					statement.executeUpdate(updateBooksStock);
					
					String updateStockLocation = "UPDATE STOCK SET stockQuantity = stockQuantity - " + 
						textField_13.getText() + " WHERE stockId = " + textField_18.getText();
					statement.executeUpdate(updateStockLocation);
					
					txtrBooks.setText("Sale recorded successfully!\n");
				} catch(SQLException er) {
					txtrBooks.setText("Error recording sale: " + er.getMessage());
					er.printStackTrace(System.err);
				}
			}
		});

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!isValidNumber(textField_8.getText())) {
						JOptionPane.showMessageDialog(frame, "Please enter a valid Sale ID");
						return;
					}

					int response = JOptionPane.showConfirmDialog(frame, 
						"Are you sure you want to delete this sale?", 
						"Confirm Delete", 
						JOptionPane.YES_NO_OPTION);
					
					if (response == JOptionPane.YES_OPTION) {
						statement.executeUpdate("DELETE FROM SALES WHERE saleId = " + textField_8.getText());
						txtrBooks.setText("Sale deleted successfully!\n");
						refreshDisplay();
					}
				} catch (SQLException ex) {
					txtrBooks.setText("Error deleting sale: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		});
	}

	private boolean validateBookData() {
		if (textField.getText().isEmpty() || textField_1.getText().isEmpty() || 
			textField_2.getText().isEmpty() || textField_3.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "All fields must be filled out");
			return false;
		}
		
		if (!isValidNumber(textField.getText())) {
			JOptionPane.showMessageDialog(frame, "Book ID must be a number");
			return false;
		}
		
		if (!isValidNumber(textField_3.getText())) {
			JOptionPane.showMessageDialog(frame, "Price must be a number");
			return false;
		}
		
		return true;
	}

	private void exportToCSV(String tableName) {
		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = tableName.toLowerCase() + "_" + timestamp + ".csv";
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File(fileName));
			if (fileChooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			File file = fileChooser.getSelectedFile();
			FileWriter writer = new FileWriter(file);
			
			if (tableName.contains("İ")) {
				tableName = tableName.replace("İ", "I");
			}
			
			String query = "";
			switch(tableName.toUpperCase()) {
				case "BOOKS":
					query = "SELECT bookId, title, author, price, genre FROM BOOKS";
					break;
				case "STOCK":
					query = "SELECT stockId, bookId, supplierId, stockQuantity FROM STOCK";
					break;
				case "SALES":
					query = "SELECT saleId, bookId, stockId, quantitySold, totalPrice, SaleDate FROM SALES";
					break;
				case "SUPPLIERS":
					query = "SELECT * FROM SUPPLIERS";
					break;
				default:
					throw new IllegalArgumentException("Unknown table: " + tableName);
			}
			
			txtrBooks.setText("Executing query: " + query + "\n");
			
			ResultSet rs = statement.executeQuery(query);
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++) {
				writer.append(metadata.getColumnName(i));
				if (i < columnCount) writer.append(',');
			}
			writer.append('\n');
			
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String value = rs.getString(i);
					if (value != null) {
						value = value.replace(",", ";");
						if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
							value = "\"" + value.replace("\"", "\"\"") + "\"";
						}
					}
					writer.append(value != null ? value : "");
					if (i < columnCount) writer.append(',');
				}
				writer.append('\n');
			}
			
			writer.flush();
			writer.close();
			
			txtrBooks.append("Export completed. File saved to: " + file.getAbsolutePath() + "\n");
			JOptionPane.showMessageDialog(frame, "Data exported successfully to " + file.getAbsolutePath());
			
		} catch (Exception e) {
			e.printStackTrace();
			txtrBooks.setText("Error exporting data: " + e.getMessage() + "\n");
			JOptionPane.showMessageDialog(frame, "Error exporting data: " + e.getMessage());
		}
	}

	private void exportAllTables() {
		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogTitle("Select Directory to Save All Tables");
			
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File directory = fileChooser.getSelectedFile();
				String[] tables = {"BOOKS", "SALES", "STOCK", "SUPPLIERS"};
				StringBuilder exportReport = new StringBuilder("Export Results:\n\n");
				
				for (String table : tables) {
					try {
						String fileName = table.toLowerCase() + "_" + timestamp + ".csv";
						File file = new File(directory, fileName);
						FileWriter writer = new FileWriter(file);
						
						String query = "";
						switch(table) {
							case "BOOKS":
								query = "SELECT bookId, title, author, price, genre FROM BOOKS";
								break;
							case "STOCK":
								query = "SELECT stockId, bookId, supplierId, stockQuantity FROM STOCK";
								break;
							case "SALES":
								query = "SELECT saleId, bookId, stockId, quantitySold, totalPrice, SaleDate FROM SALES";
								break;
							case "SUPPLIERS":
								query = "SELECT * FROM SUPPLIERS";
								break;
						}
						
						ResultSet rs = statement.executeQuery(query);
						ResultSetMetaData metadata = rs.getMetaData();
						int columnCount = metadata.getColumnCount();
						int rowCount = 0;
						
						for (int i = 1; i <= columnCount; i++) {
							writer.append(metadata.getColumnName(i));
							if (i < columnCount) writer.append(',');
						}
						writer.append('\n');
						
						while (rs.next()) {
							rowCount++;
							for (int i = 1; i <= columnCount; i++) {
								String value = rs.getString(i);
								if (value != null) {
									value = value.replace(",", ";");
									if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
										value = "\"" + value.replace("\"", "\"\"") + "\"";
									}
								}
								writer.append(value != null ? value : "");
								if (i < columnCount) writer.append(',');
							}
							writer.append('\n');
						}
						
						writer.flush();
						writer.close();
						
						exportReport.append(table + ": Successfully exported " + rowCount + " records to " + fileName + "\n");
						
					} catch (Exception ex) {
						exportReport.append(table + ": Error during export - " + ex.getMessage() + "\n");
						ex.printStackTrace();
					}
				}
				
				txtrBooks.setText(exportReport.toString());
				JOptionPane.showMessageDialog(frame, "Export completed. Check the text area for details.");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			txtrBooks.setText("Error during export: " + e.getMessage() + "\n");
			JOptionPane.showMessageDialog(frame, "Error during export: " + e.getMessage());
		}
	}

	private void addDarkModeButton() {
		darkModeButton = new JButton("DARK MODE");
		darkModeButton.setFont(new Font("Arial Black", Font.BOLD, 12));
		darkModeButton.setBounds(928, 561, 120, 26);  // Positioned next to search button
		frame.getContentPane().add(darkModeButton);
		
		darkModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				toggleDarkMode();
			}
		});
	}

	private void toggleDarkMode() {
		isDarkMode = !isDarkMode;
		Color backgroundColor = isDarkMode ? darkBackground : lightBackground;
		Color textColor = isDarkMode ? darkText : lightText;
		
		// Update frame colors
		frame.getContentPane().setBackground(backgroundColor);
		
		// Update text colors for all labels
		for (java.awt.Component comp : frame.getContentPane().getComponents()) {
			if (comp instanceof JLabel) {
				comp.setForeground(textColor);
			}
		}
		
		// Update text area colors - keep background white and text pink
		txtrBooks.setBackground(Color.WHITE);
		txtrBooks.setForeground(new Color(255, 0, 102));  // Always keep pink text
		
		// Update text field colors
		for (java.awt.Component comp : frame.getContentPane().getComponents()) {
			if (comp instanceof JTextField) {
				comp.setBackground(isDarkMode ? new Color(70, 70, 70) : Color.WHITE);
				comp.setForeground(textColor);
			}
		}
		
		// Update button text
		darkModeButton.setText(isDarkMode ? "LIGHT MODE" : "DARK MODE");
		
		// Update scroll pane
		for (java.awt.Component comp : frame.getContentPane().getComponents()) {
			if (comp instanceof JScrollPane) {
				comp.setBackground(backgroundColor);
				JScrollPane scrollPane = (JScrollPane) comp;
				scrollPane.getViewport().setBackground(isDarkMode ? new Color(70, 70, 70) : Color.WHITE);
			}
		}
		
		// Update combo box
		for (java.awt.Component comp : frame.getContentPane().getComponents()) {
			if (comp instanceof JComboBox) {
				comp.setBackground(isDarkMode ? new Color(70, 70, 70) : Color.WHITE);
				comp.setForeground(textColor);
			}
		}
	}

    // Keep only the getter
    public JFrame getFrame() {
        return frame;
    }

    // Add new methods for dark mode
    private void applyDarkMode() {
        frame.getContentPane().setBackground(darkBackground);
        txtrBooks.setBackground(darkBackground);
        txtrBooks.setForeground(darkText);
        for (java.awt.Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(darkText);
            }
        }
        for (java.awt.Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JTextField) {
                comp.setBackground(darkBackground);
                comp.setForeground(darkText);
            }
        }
        darkModeButton.setText("LIGHT MODE");
        darkModeButton.setBackground(darkBackground);
        darkModeButton.setForeground(darkText);
    }

    private void applyLightMode() {
        frame.getContentPane().setBackground(lightBackground);
        txtrBooks.setBackground(lightBackground);
        txtrBooks.setForeground(lightText);
        for (java.awt.Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(lightText);
            }
        }
        for (java.awt.Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JTextField) {
                comp.setBackground(lightBackground);
                comp.setForeground(lightText);
            }
        }
        darkModeButton.setText("DARK MODE");
        darkModeButton.setBackground(lightBackground);
        darkModeButton.setForeground(lightText);
    }
}
