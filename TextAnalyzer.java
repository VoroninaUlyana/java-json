package com.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextAnalyzer {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}

class MainWindow extends JFrame {
    private final JTextArea resultArea;
    private final JButton saveButton;
    private String currentReport = "";

    public MainWindow() {
        setTitle("Text Analyzer");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadButton = new JButton("📂 Загрузить файл");
        saveButton = new JButton("💾 Сохранить отчет");
        saveButton.setEnabled(false);

        topPanel.add(loadButton);
        topPanel.add(saveButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Моноширинный шрифт для ровных таблиц
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File("."));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                analyzeFile(fileChooser.getSelectedFile().toPath());
            }
        });

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File("report.txt"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    Files.writeString(fileChooser.getSelectedFile().toPath(), currentReport);
                    JOptionPane.showMessageDialog(this, "Отчет сохранен!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + ex.getMessage());
                }
            }
        });
    }

    private void analyzeFile(Path path) {
        try {
            AnalyzerLogic.AnalysisResult result = AnalyzerLogic.analyze(path);

            StringBuilder report = new StringBuilder();
            report.append("***** РЕЗУЛЬТАТ АНАЛИЗА *****\n");
            report.append(String.format("🔹 %-25s : %d\n", "Всего слов", result.totalWords));
            report.append(String.format("🔹 %-25s : %d\n", "Уникальных слов", result.uniqueWords));
            report.append(String.format("🔹 %-25s : %d\n", "Предложений", result.sentencesCount));
            report.append("\n");

            report.append(" Топ-10 частых слов:\n");
            result.topWords.forEach(s -> report.append("   • ").append(s).append("\n"));

            report.append("\n Найденные Email:\n");
            if (result.emails.isEmpty()) {
                report.append("   (Email не найдены)\n");
            } else {
                result.emails.forEach(e -> report.append("   • ").append(e).append("\n"));
            }

            currentReport = report.toString();
            resultArea.setText(currentReport);
            resultArea.setCaretPosition(0);
            saveButton.setEnabled(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка чтения файла: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class AnalyzerLogic {

    public static class AnalysisResult {
        int totalWords;
        int uniqueWords;
        long sentencesCount;
        List<String> topWords;
        List<String> emails;
    }

    public static AnalysisResult analyze(Path path) throws IOException {
        String content = Files.readString(path, StandardCharsets.UTF_8);
        AnalysisResult stats = new AnalysisResult();

        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
        Matcher emailMatcher = emailPattern.matcher(content);
        stats.emails = new ArrayList<>();

        while (emailMatcher.find()) {
            stats.emails.add(emailMatcher.group());
        }

        String contentWithoutEmails = content.replaceAll("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}", " ");

        Pattern wordPattern = Pattern.compile("[a-zA-Zа-яА-ЯёЁ]+(?:-[a-zA-Zа-яА-ЯёЁ]+)*");
        Matcher wordMatcher = wordPattern.matcher(contentWithoutEmails);
        Map<String, Integer> wordFrequency = new HashMap<>();

        while (wordMatcher.find()) {
            String word = wordMatcher.group().toLowerCase();
            stats.totalWords++;
            wordFrequency.merge(word, 1, Integer::sum);
        }
        stats.uniqueWords = wordFrequency.size();

        List<Map.Entry<String, Integer>> sortedEntries = wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        int maxWordLength = sortedEntries.stream()
                .mapToInt(e -> e.getKey().length())
                .max().orElse(10);

        stats.topWords = sortedEntries.stream()
                .map(entry -> {
                    String word = entry.getKey();
                    int count = entry.getValue();
                    String declension = getDeclension(count);
                    String format = "%-" + (maxWordLength + 4) + "s : %d %s";
                    return String.format(format, word, count, declension);
                })
                .collect(Collectors.toList());

        String[] sentences = content.split("[.!?]+(\\s+|$)");
        stats.sentencesCount = Arrays.stream(sentences).filter(s -> !s.isBlank()).count();

        return stats;
    }

    private static String getDeclension(int count) {
        if (count % 100 >= 11 && count % 100 <= 14) return "раз";
        int mod = count % 10;
        if (mod == 1) return "раз";
        if (mod >= 2 && mod <= 4) return "раза";
        return "раз";
    }
}