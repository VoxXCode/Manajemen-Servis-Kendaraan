package com.manajemenservis.view;

import com.manajemenservis.controller.AuthController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JFrame {

    // --- WARNA TEMA (Dark Mode) ---
    private final Color COLOR_BG_MAIN     = new Color(18, 18, 18);
    private final Color COLOR_BG_SIDEBAR  = new Color(10, 10, 10);
    private final Color COLOR_SEARCH_BAR  = new Color(30, 30, 30);
    private final Color COLOR_TEXT_WHITE  = new Color(255, 255, 255);
    private final Color COLOR_TEXT_GRAY   = new Color(150, 150, 150);

    private final Font FONT_ICON_MENU = new Font("SansSerif", Font.PLAIN, 14);
    private final Font FONT_BOLD_TITLE = new Font("SansSerif", Font.BOLD, 22);

    public DashboardView() {
        setTitle("BENGKEL KU - Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(COLOR_BG_MAIN);

        JLabel placeholder = new JLabel("Overview Content Area", SwingConstants.CENTER);
        placeholder.setForeground(COLOR_TEXT_GRAY);
        placeholder.setFont(new Font("SansSerif", Font.PLAIN, 24));
        mainContentPanel.add(placeholder, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG_SIDEBAR);
        panel.setPreferredSize(new Dimension(240, 700));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // A. LOGO
        JLabel brandLabel = new JLabel("<html><b>BENGKEL</b><br><b>KU</b></html>");
        brandLabel.setFont(FONT_BOLD_TITLE);
        brandLabel.setForeground(COLOR_TEXT_WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0, 5, 25, 0);
        panel.add(brandLabel, gbc);

        // B. SEARCH
        JPanel searchPanel = createSearchBar();
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 25, 0);
        panel.add(searchPanel, gbc);

        // C. MENU ITEMS
        gbc.insets = new Insets(0, 0, 8, 0);

        // 1. OVERVIEW
        gbc.gridy = 2;
        panel.add(createMenuButton("Overview", "📊"), gbc);

        // (Payments dihapus)

        // 2. CUSTOMER
        gbc.gridy = 3;
        JButton btnCustomer = createMenuButton("Customer", "👥");
        btnCustomer.addActionListener(e -> {
            new CustomerTable().setVisible(true);
            dispose();
        });
        panel.add(btnCustomer, gbc);

        // 3. PRODUCT
        gbc.gridy = 4;
        JButton btnProduct = createMenuButton("Product", "🛍️");
        btnProduct.addActionListener(e -> {
            new InventoryTable().setVisible(true);
            dispose();
        });
        panel.add(btnProduct, gbc);

        // 4. HISTORY
        gbc.gridy = 5;
        JButton btnHistory = createMenuButton("History", "📜");
        btnHistory.addActionListener(e -> {
            new ServiceHistoryTable().setVisible(true);
            dispose();
        });
        panel.add(btnHistory, gbc);

        // D. SPACER
        gbc.gridy = 6; gbc.weighty = 1.0;
        panel.add(Box.createGlue(), gbc);

        // E. MENU BAWAH
        gbc.weighty = 0;
        gbc.gridy = 7; panel.add(createMenuButton("Settings", "🛠️"), gbc);

        gbc.gridy = 8;
        JButton btnLogout = createMenuButton("Support (Logout)", "❓");
        btnLogout.addActionListener(e -> new AuthController().logout(this));
        panel.add(btnLogout, gbc);

        return panel;
    }

    private JPanel createSearchBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_SEARCH_BAR);
        panel.setBorder(new EmptyBorder(8, 10, 8, 10));
        JLabel icon = new JLabel("🔍");
        icon.setFont(FONT_ICON_MENU);
        icon.setForeground(COLOR_TEXT_GRAY);
        icon.setBorder(new EmptyBorder(0, 0, 0, 10));
        JTextField field = new JTextField("Search");
        field.setBackground(COLOR_SEARCH_BAR);
        field.setForeground(COLOR_TEXT_GRAY);
        field.setBorder(null);
        field.setCaretColor(Color.WHITE);
        field.setFont(FONT_ICON_MENU);
        panel.add(icon, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createMenuButton(String text, String icon) {
        JButton btn = new JButton("  " + icon + "    " + text);
        styleButton(btn);
        return btn;
    }

    private void styleButton(JButton btn) {
        btn.setFont(FONT_ICON_MENU);
        btn.setForeground(COLOR_TEXT_GRAY);
        btn.setBackground(COLOR_BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 5, 8, 0));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(COLOR_TEXT_WHITE); }
            public void mouseExited(MouseEvent e) { btn.setForeground(COLOR_TEXT_GRAY); }
        });
    }
}