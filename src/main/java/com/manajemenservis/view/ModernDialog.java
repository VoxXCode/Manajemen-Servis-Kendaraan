package com.manajemenservis.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernDialog extends JDialog {

    // --- WARNA TEMA ---
    private final Color COLOR_BG        = new Color(30, 30, 30);
    private final Color COLOR_TEXT      = new Color(255, 255, 255);
    private final Color COLOR_BORDER    = new Color(60, 60, 60);

    // Warna Tombol OK
    private final Color COLOR_BTN_BG    = new Color(65, 105, 225); // Biru Royal
    private final Color COLOR_BTN_HOVER = new Color(50, 90, 200);

    // Warna Indikator Tipe Pesan
    public static final int SUCCESS = 1;
    public static final int WARNING = 2;
    public static final int ERROR   = 3;

    public ModernDialog(Window parent, String title, String message, int type) {
        super(parent, ModalityType.APPLICATION_MODAL); // Modal = blokir window belakang
        setUndecorated(true); // Hilangkan border bawaan OS
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- PANEL UTAMA ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new LineBorder(COLOR_BORDER, 1)); // Border tipis sekeliling
        add(mainPanel);

        // --- 1. HEADER (Title) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setBackground(COLOR_BG);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Warna Judul Berdasarkan Tipe
        if (type == SUCCESS) lblTitle.setForeground(new Color(46, 204, 113)); // Hijau
        else if (type == WARNING) lblTitle.setForeground(new Color(241, 196, 15)); // Kuning
        else if (type == ERROR) lblTitle.setForeground(new Color(231, 76, 60)); // Merah
        else lblTitle.setForeground(COLOR_TEXT);

        headerPanel.add(lblTitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. CONTENT (Message) ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(5, 25, 10, 25));

        JTextArea txtMessage = new JTextArea(message);
        txtMessage.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtMessage.setForeground(new Color(200, 200, 200));
        txtMessage.setBackground(COLOR_BG);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setEditable(false);
        txtMessage.setHighlighter(null); // Hilangkan seleksi teks

        contentPanel.add(txtMessage, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // --- 3. FOOTER (Button) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(COLOR_BG);

        JButton btnOk = new JButton("OK");
        styleButton(btnOk);
        btnOk.addActionListener(e -> dispose()); // Tutup dialog saat diklik

        footerPanel.add(btnOk);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_BTN_BG);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 25, 8, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efek Hover Sederhana
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_BTN_HOVER); }
            public void mouseExited(MouseEvent e) { btn.setBackground(COLOR_BTN_BG); }
        });
    }

    // --- STATIC HELPER METHODS (Agar mudah dipanggil) ---
    public static void showSuccess(Window parent, String message) {
        new ModernDialog(parent, "Berhasil", message, SUCCESS).setVisible(true);
    }

    public static void showWarning(Window parent, String message) {
        new ModernDialog(parent, "Peringatan", message, WARNING).setVisible(true);
    }

    public static void showError(Window parent, String message) {
        new ModernDialog(parent, "Error", message, ERROR).setVisible(true);
    }
}