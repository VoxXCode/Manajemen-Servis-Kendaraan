package com.manajemenservis.view;
import com.manajemenservis.controller.AuthController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginView extends JFrame {

    private final Color CLR_BG_DARK    = new Color(18, 18, 18);   // Background utama
    private final Color CLR_BG_FIELD   = new Color(40, 40, 40);   // Background input
    private final Color CLR_TEXT_WHITE = new Color(240, 240, 240);
    private final Color CLR_TEXT_GRAY  = new Color(160, 160, 160);
    private final Color CLR_BTN_BLUE   = new Color(59, 130, 246); // Biru tombol
    private final Color CLR_LINE       = new Color(60, 60, 60);

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private AuthController authController;
    private boolean isProcessing = false;

    public LoginView() {
        authController = new AuthController();

        setTitle("BENGKEL KU - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel dengan BoxLayout Y_AXIS (Vertikal)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CLR_BG_DARK);
        mainPanel.setBorder(new EmptyBorder(50, 45, 50, 45));

        // --- Logo / Header (Tetap Center) ---
        JLabel lblLogo = new JLabel("BENGKEL-KU");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 35));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblLogo);

        JLabel lblSub = new JLabel("Login to your account");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(CLR_TEXT_GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setBorder(new EmptyBorder(5, 0, 40, 0));
        mainPanel.add(lblSub);

        // --- Container untuk Form agar Rata Kiri ---
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(CLR_BG_DARK);
        formContainer.setAlignmentX(Component.CENTER_ALIGNMENT); // Wadahnya di tengah, isinya di kiri

        // Username
        formContainer.add(createFieldLabel("Username"));
        txtUsername = createStyledTextField();
        formContainer.add(txtUsername);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password
        formContainer.add(createFieldLabel("Password"));
        txtPassword = createStyledPasswordField();
        formContainer.add(txtPassword);
        formContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        mainPanel.add(formContainer);

        // --- Login Button ---
        JButton btnLogin = new JButton("Login");
        styleButton(btnLogin);
        mainPanel.add(btnLogin);

        add(mainPanel);

        // --- Listeners ---
        btnLogin.addActionListener(e -> performLogin());

        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocus();
                    e.consume();
                }
            }
        });

        this.getRootPane().setDefaultButton(btnLogin);
    }

    private void performLogin() {
        if (isProcessing) return;

        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        isProcessing = true;
        authController.login(user, pass, this);

        Timer timer = new Timer(1500, e -> isProcessing = false);
        timer.setRepeats(false);
        timer.start();
    }

    // Helper agar label otomatis rata kiri
    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(CLR_TEXT_GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT); // Rata kiri
        lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT); // Field rata kiri
        field.setBackground(CLR_BG_FIELD); // Warna input gelap
        field.setForeground(CLR_TEXT_WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CLR_LINE, 1),
                new EmptyBorder(0, 10, 0, 10)
        ));
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(CLR_BTN_BLUE); // Warna biru
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}