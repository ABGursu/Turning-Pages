package TurningPages;

import javax.swing.JOptionPane;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

public class UserMenu {
    // Frame components
    private JFrame userMenu;
    private JFrame loginFrame;
    
    // User interface components
    private JButton enterGuiButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    private JLabel usernameLabel;
    private String username;
    
    // Constructor
    public UserMenu(JFrame loginFrame, String username) {
        this.loginFrame = loginFrame;
        this.username = username;
        initialize();
    }
    
    private void initialize() {
        userMenu = new JFrame();
        userMenu.setTitle("User Menu");
        userMenu.setBounds(100, 100, 450, 300);
        userMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userMenu.getContentPane().setLayout(null);
        
        // Welcome Label
        welcomeLabel = new JLabel("Welcome to Turning Pages!");
        welcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 16));
        welcomeLabel.setBounds(115, 50, 250, 30);
        userMenu.getContentPane().add(welcomeLabel);
        
        // Username Label
        usernameLabel = new JLabel("User: " + username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(175, 90, 200, 20);
        userMenu.getContentPane().add(usernameLabel);
        
        // Enter GUI Button
        enterGuiButton = new JButton("Enter GUI");
        enterGuiButton.setBounds(150, 130, 150, 30);
        enterGuiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userMenu.dispose();
                loginFrame.dispose();
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            BookStoreGUI window = new BookStoreGUI(username);
                            window.getFrame().setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        userMenu.getContentPane().add(enterGuiButton);
        
        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 170, 150, 30);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userMenu.dispose();
                loginFrame.setVisible(true);
            }
        });
        userMenu.getContentPane().add(logoutButton);
        
        userMenu.setLocationRelativeTo(null);
        userMenu.setVisible(true);
    }
    
    // Getter for the frame
    public JFrame getFrame() {
        return userMenu;
    }
} 