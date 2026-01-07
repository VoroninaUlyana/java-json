package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Main {
    private static String selectedCongratulator = null;
    private static boolean concertSelected = false;
    private static final Map<String, Map<String, Integer>> giftData = new LinkedHashMap<>();
    private static JLabel totalLabel;
    private static JTextArea orderArea;
    private static JList<String> giftList;
    private static JCheckBox loyalBox;
    private static java.util.List<String> orderedGifts = new ArrayList<>();

    public static void main(String[] args) {
        loadGiftData("gifts.txt");
        SwingUtilities.invokeLater(Main::createMainWindow);
    }

    private static void loadGiftData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(":");
                if (parts.length != 2) continue;
                String congratulator = parts[0].trim();
                Map<String, Integer> gifts = new LinkedHashMap<>();
                String[] items = parts[1].split(",");
                for (String item : items) {
                    String[] kv = item.trim().split("=");
                    if (kv.length != 2) continue;
                    gifts.put(kv[0].trim(), Integer.parseInt(kv[1].trim()));
                }
                giftData.put(congratulator, gifts);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ошибка чтения файла gifts.txt");
            System.exit(1);
        }
    }

    private static void createMainWindow() {
        JFrame frame = new JFrame("Вычисление затрат на награждение победителей олимпиады");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        JPanel congrPanel = new JPanel(new GridLayout(0, 1));
        congrPanel.setBorder(BorderFactory.createTitledBorder("Выберите поздравителя"));
        congrPanel.setBackground(new Color(230, 240, 255));
        ButtonGroup congrGroup = new ButtonGroup();
        java.util.List<JRadioButton> radioButtons = new ArrayList<>();
        for (String congratulator : giftData.keySet()) {
            JRadioButton rb = new JRadioButton(congratulator);
            rb.setBackground(new Color(230, 240, 255));
            congrGroup.add(rb);
            congrPanel.add(rb);
            radioButtons.add(rb);
        }

        JPanel concertPanel = new JPanel(new GridLayout(1, 2));
        concertPanel.setBorder(BorderFactory.createTitledBorder("Концерт"));
        concertPanel.setBackground(new Color(255, 245, 230));
        JRadioButton concertYes = new JRadioButton("Да");
        JRadioButton concertNo = new JRadioButton("Нет", true);
        concertYes.setBackground(new Color(255, 245, 230));
        concertNo.setBackground(new Color(255, 245, 230));
        ButtonGroup concertGroup = new ButtonGroup();
        concertGroup.add(concertYes);
        concertGroup.add(concertNo);
        concertPanel.add(concertYes);
        concertPanel.add(concertNo);

        JPanel giftPanel = new JPanel(new BorderLayout());
        giftPanel.setBorder(BorderFactory.createTitledBorder("Выберите подарок"));
        giftPanel.setBackground(new Color(245, 255, 245));
        giftList = new JList<>();
        giftList.setVisibleRowCount(6);
        giftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane giftScroll = new JScrollPane(giftList);
        giftPanel.add(giftScroll, BorderLayout.CENTER);

        JButton addGiftButton = new JButton("Добавить подарок");

        loyalBox = new JCheckBox("Постоянный клиент (скидка 10%)");
        loyalBox.setBackground(new Color(245, 255, 245));

        JButton calcButton = new JButton("Рассчитать");

        totalLabel = new JLabel("Итог: 0 руб.");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.BLUE);

        orderArea = new JTextArea(8, 40);
        orderArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(orderArea);

        for (JRadioButton rb : radioButtons) {
            rb.addActionListener(e -> {
                selectedCongratulator = rb.getText();
                orderedGifts.clear();
                updateGiftList(selectedCongratulator);
                updateOrderArea();
            });
        }

        concertYes.addActionListener(e -> concertSelected = true);
        concertNo.addActionListener(e -> concertSelected = false);

        addGiftButton.addActionListener(e -> {
            String selectedGift = giftList.getSelectedValue();
            if (selectedGift == null) {
                JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите подарок!");
                return;
            }
            orderedGifts.add(selectedGift);
            updateOrderArea();
        });

        calcButton.addActionListener(e -> calculateTotal());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(congrPanel);
        topPanel.add(concertPanel);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1));
        centerPanel.add(giftPanel);
        centerPanel.add(addGiftButton);
        centerPanel.add(loyalBox);
        centerPanel.add(calcButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalLabel, BorderLayout.NORTH);
        bottomPanel.add(scroll, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void updateGiftList(String congratulator) {
        Map<String, Integer> gifts = giftData.get(congratulator);
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Map.Entry<String, Integer> entry : gifts.entrySet()) {
            model.addElement(entry.getKey() + " (" + entry.getValue() + " руб.)");
        }
        giftList.setModel(model);
    }

    private static void updateOrderArea() {
        StringBuilder order = new StringBuilder();
        order.append("Поздравитель: ").append(selectedCongratulator == null ? "" : selectedCongratulator).append("\n");
        order.append("Подарки:\n");
        for (String giftStr : orderedGifts) {
            order.append("- ").append(giftStr).append("\n");
        }
        orderArea.setText(order.toString());
    }

    private static void calculateTotal() {
        if (selectedCongratulator == null) {
            JOptionPane.showMessageDialog(null, "Пожалуйста, выберите поздравителя!");
            return;
        }
        if (orderedGifts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Пожалуйста, добавьте хотя бы один подарок!");
            return;
        }

        double total = 0;
        for (String giftStr : orderedGifts) {
            String giftName = giftStr.substring(0, giftStr.indexOf(" (")).trim();
            int price = giftData.get(selectedCongratulator).get(giftName);
            total += price;
        }

        StringBuilder order = new StringBuilder();
        order.append("Поздравитель: ").append(selectedCongratulator).append("\n");
        order.append("Подарки:\n");
        for (String giftStr : orderedGifts) {
            order.append("- ").append(giftStr).append("\n");
        }

        if (concertSelected) {
            total += 50;
            order.append("Концерт: Да (+50 руб.)\n");
        } else {
            order.append("Концерт: Нет\n");
        }

        if (loyalBox.isSelected()) {
            order.append("Скидка 10%\n");
            total *= 0.9;
        }

        order.append("------------------------\n");
        order.append(String.format("Итоговая сумма: %.2f руб.\n", total));
        totalLabel.setText(String.format("Итог: %.2f руб.", total));
        orderArea.setText(order.toString());
    }
}
