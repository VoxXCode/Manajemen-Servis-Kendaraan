package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JFrame {

    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    private final Color COLOR_BG_SIDEBAR  = new Color(10, 10, 10);
    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(150, 150, 150);
    private final Color COLOR_ACCENT      = new Color(65, 105, 225);

    public DashboardView() {
        setTitle("BENGKEL KU - Dashboard");
        setSize(1350, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Sidebar Navigasi
        add(createSidebar(), BorderLayout.WEST);

        // 2. Konten Utama
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_BG_MAIN);

        JLabel lblWelcome = new JLabel("Selamat Datang, Admin!");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblWelcome.setForeground(COLOR_TEXT_WHITE);

        JLabel lblSub = new JLabel("Silakan pilih menu di samping untuk memulai.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblSub.setForeground(COLOR_TEXT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0; gbc.gridy=0; mainPanel.add(lblWelcome, gbc);
        gbc.gridy=1; gbc.insets = new Insets(10,0,0,0); mainPanel.add(lblSub, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    // --- SIDEBAR DENGAN LINK KE SERVICE ENTRY ---
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(240, 800));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.gridx = 0;

        // Brand
        JLabel brandLabel = new JLabel("<html><b>BENGKEL</b><br><b>KU</b></html>");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        brandLabel.setForeground(COLOR_TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 5, 25, 0); panel.add(brandLabel, gbc);

        // Search Dummy
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(30, 30, 30));
        searchPanel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel icon = new JLabel("🔍"); icon.setForeground(COLOR_TEXT_GRAY);
        searchPanel.add(icon, BorderLayout.WEST);
        gbc.gridy = 1; panel.add(searchPanel, gbc);
        gbc.insets = new Insets(0, 0, 8, 0);

        // MENU UTAMA (Perhatikan "Input Servis")
        gbc.gridy = 2; panel.add(createMenuButton("Overview", "📊", true), gbc);
        gbc.gridy = 3; panel.add(createMenuButton("Customer", "👥", false), gbc);
        gbc.gridy = 4; panel.add(createMenuButton("Product", "🛍️", false), gbc);

        // [BARU] Tombol Input Servis
        gbc.gridy = 5; panel.add(createMenuButton("Input Servis", "📄", false), gbc);

        gbc.gridy = 6; panel.add(createMenuButton("History", "📜", false), gbc);

        gbc.gridy = 7; gbc.weighty = 1.0; panel.add(Box.createGlue(), gbc);
        gbc.weighty = 0; gbc.gridy = 8; panel.add(createMenuButton("Settings", "🛠️", false), gbc);

        gbc.gridy = 9;
        JButton btnLogout = createMenuButton("Logout", "❓", false);
        btnLogout.addActionListener(e -> new AuthController().logout(this));
        panel.add(btnLogout, gbc);

        return panel;
    }

    private JButton createMenuButton(String text, String icon, boolean isActive) {
        JButton btn = new JButton("  " + icon + "    " + text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setForeground(isActive ? COLOR_TEXT_WHITE : COLOR_TEXT_GRAY);
        btn.setBackground(COLOR_BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 5, 8, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(COLOR_TEXT_WHITE); }
            public void mouseExited(MouseEvent e) { if(!isActive) btn.setForeground(COLOR_TEXT_GRAY); }
        });

        // --- NAVIGASI ---
        if(text.equals("Customer")) btn.addActionListener(e -> { new CustomerTable().setVisible(true); dispose(); });
        if(text.equals("Product")) btn.addActionListener(e -> { new InventoryTable().setVisible(true); dispose(); });
        if(text.equals("History")) btn.addActionListener(e -> { new ServiceHistoryTable().setVisible(true); dispose(); });

        // [LINK KE FORM INPUT SERVIS]
        if(text.equals("Input Servis")) btn.addActionListener(e -> { new ServiceEntryForm().setVisible(true); dispose(); });

        return btn;
    }
}