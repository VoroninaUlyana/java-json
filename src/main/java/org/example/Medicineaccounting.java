package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

class Medicine {
    String name;
    String country;
    int quantity;
    LocalDate manufactureDate;
    LocalDate expiryDate;

    public Medicine(String name, String country, int quantity, LocalDate manufactureDate, LocalDate expiryDate) {
        this.name = name;
        this.country = country;
        this.quantity = quantity;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
    }

    public Object[] toObjectArray() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return new Object[]{
                name,
                country,
                quantity,
                manufactureDate.format(formatter),
                expiryDate.format(formatter)
        };
    }
}

public class Medicineaccounting extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Medicine> medicines;
    private JComboBox<String> sortComboBox;
    private JTextField searchField;
    private JButton searchButton, sortAscButton, sortDescButton, expiredButton;

    public Medicineaccounting() {
        super("Учет лекарств");

        medicines = readFromFile("medicines.txt");

        String[] columns = {"Название", "Страна", "Количество", "Дата изготовления", "Дата просроченности"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(225, 245, 255)); // светло-голубой
                    } else {
                        c.setBackground(Color.white);
                    }
                }
                return c;
            }
        });

        refreshTable(medicines);

        JPanel controlPanel = new JPanel();
        sortComboBox = new JComboBox<>(columns);
        sortAscButton = new JButton("Сортировка ↑");
        sortDescButton = new JButton("Сортировка ↓");
        searchField = new JTextField(10);
        searchButton = new JButton("Поиск");
        expiredButton = new JButton("Показать просроченные");

        controlPanel.add(new JLabel("Сортировать по:"));
        controlPanel.add(sortComboBox);
        controlPanel.add(sortAscButton);
        controlPanel.add(sortDescButton);
        controlPanel.add(new JLabel("Поиск по названию:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(expiredButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);

        sortAscButton.addActionListener(e -> sortTable(true));
        sortDescButton.addActionListener(e -> sortTable(false));
        searchButton.addActionListener(e -> searchMedicine());
        expiredButton.addActionListener(e -> showExpired());

        setSize(1200, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private List<Medicine> readFromFile(String fileName) {
        List<Medicine> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    list.add(new Medicine(
                            parts[0].trim(),
                            parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            LocalDate.parse(parts[3].trim(), formatter),
                            LocalDate.parse(parts[4].trim(), formatter)
                    ));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка при чтении файла: " + e.getMessage());
        }

        return list;
    }

    private void refreshTable(List<Medicine> list) {
        tableModel.setRowCount(0);
        for (Medicine m : list) {
            tableModel.addRow(m.toObjectArray());
        }
    }

    private void sortTable(boolean ascending) {
        String criteria = (String) sortComboBox.getSelectedItem();
        Comparator<Medicine> comparator;

        switch (criteria) {
            case "Название" ->
                    comparator = Comparator.comparing((Medicine m) -> m.name);
            case "Страна" ->
                    comparator = Comparator.comparing((Medicine m) -> m.country)
                            .thenComparing(m -> m.name);
            case "Количество" ->
                    comparator = Comparator.comparingInt((Medicine m) -> m.quantity)
                            .thenComparing(m -> m.name);
            case "Дата изготовления" ->
                    comparator = Comparator.comparing((Medicine m) -> m.manufactureDate)
                            .thenComparing(m -> m.name);
            case "Дата просроченности" ->
                    comparator = Comparator.comparing((Medicine m) -> m.expiryDate)
                            .thenComparing(m -> m.name);
            default ->
                    comparator = Comparator.comparing((Medicine m) -> m.name);
        }

        if (!ascending) comparator = comparator.reversed();
        medicines.sort(comparator);
        refreshTable(medicines);
    }

    private void searchMedicine() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            refreshTable(medicines);
            return;
        }

        List<Medicine> result = new ArrayList<>();
        for (Medicine m : medicines) {
            if (m.name.toLowerCase().contains(searchText)) {
                result.add(m);
            }
        }

        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Лекарство не найдено.");
        }

        refreshTable(result);
    }

    private void showExpired() {
        LocalDate today = LocalDate.now();
        List<Medicine> expired = new ArrayList<>();

        for (Medicine m : medicines) {
            if (m.expiryDate.isBefore(today)) {
                expired.add(m);
            }
        }

        if (expired.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Просроченных лекарств нет ✅");
        }

        refreshTable(expired);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Medicineaccounting::new);
    }
}


