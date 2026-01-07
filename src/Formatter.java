import java.util.*;

public class Formatter {
    private int width;
    private static final int TAB_WIDTH = 8; //количество символов, которые занимает табуляция

    public Formatter(int width) {
        this.width = width;
    }

    public List<String> formatText(List<Line> inputLines) {
        List<String> result = new ArrayList<>();

        // Деление на абзацы с сохранением пустых строк
        List<Paragraph> paragraphs = splitIntoParagraphs(inputLines);

        for (Paragraph paragraph : paragraphs) {
            List<String> formattedParagraph = formatParagraph(paragraph);
            result.addAll(formattedParagraph);
        }

        return result;
    }

    private List<Paragraph> splitIntoParagraphs(List<Line> inputLines) {
        List<Paragraph> paragraphs = new ArrayList<>();
        List<Line> current = new ArrayList<>();

        for (Line line : inputLines) {
            if (line.isEmpty()) {
                if (!current.isEmpty()) {
                    paragraphs.add(new Paragraph(current));
                    current = new ArrayList<>();
                }
                paragraphs.add(new Paragraph(Collections.singletonList(line)));
            } else if (!line.getIndent().isEmpty()) {
                if (!current.isEmpty()) {
                    paragraphs.add(new Paragraph(current));
                    current = new ArrayList<>();
                }
                // строка с отступом — новый абзац
                paragraphs.add(new Paragraph(Collections.singletonList(line)));
            } else {
                current.add(line);
            }
        }

        if (!current.isEmpty()) {
            paragraphs.add(new Paragraph(current));
        }

        return paragraphs;
    }

    private List<String> formatParagraph(Paragraph paragraph) {
        List<String> result = new ArrayList<>();

        if (paragraph.lines.size() == 1 && paragraph.lines.get(0).isEmpty()) {
            result.add("");
            return result;
        }

        String paragraphIndent = "\t";

        List<String> words = new ArrayList<>();
        for (Line line : paragraph.lines) {
            String content = line.getContent().trim();
            if (!content.isEmpty()) {
                String[] parts = content.split("\\s+");
                Collections.addAll(words, parts);
            }
        }

        // Форматируем слова в строки
        List<String> currentLineWords = new ArrayList<>();
        int currentLineLength = 0; // сумма длин слов без пробелов
        boolean isFirstLine = true;

        for (String word : words) {
            int indentVisible = isFirstLine ? visibleWidth(paragraphIndent) : 0;
            int availableWidth = width - indentVisible;
            int spacesNeeded = currentLineWords.isEmpty() ? 0 : currentLineWords.size();

            if (currentLineLength + word.length() + spacesNeeded <= availableWidth) {
                currentLineWords.add(word);
                currentLineLength += word.length();
            } else {
                String lineText = (isFirstLine ? paragraphIndent : "")
                        + justifyLine(currentLineWords, currentLineLength, isFirstLine ? availableWidth : width);
                result.add(lineText);
                currentLineWords.clear();
                currentLineWords.add(word);
                currentLineLength = word.length();
                isFirstLine = false;
            }
        }

        if (!currentLineWords.isEmpty()) {
            String lastLine = (isFirstLine ? paragraphIndent : "") + String.join(" ", currentLineWords);
            result.add(lastLine);
        }

        return result;
    }

    // Выравнивание строки по ширине
    private String justifyLine(List<String> words, int wordsLength, int lineWidth) {
        if (words.size() == 0) return "";
        if (words.size() == 1) return words.get(0);

        int totalSpaces = lineWidth - wordsLength;
        int gaps = words.size() - 1;
        int baseSpaces = totalSpaces / gaps;
        int extraSpaces = totalSpaces % gaps;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            sb.append(words.get(i));
            if (i < gaps) {
                int spacesToAdd = baseSpaces + (i < extraSpaces ? 1 : 0);
                sb.append(" ".repeat(Math.max(0, spacesToAdd)));
            }
        }
        return sb.toString();
    }

    private int visibleWidth(String s) {
        int w = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\t') w += TAB_WIDTH;
            else w += 1;
        }
        return w;
    }

    private static class Paragraph {
        List<Line> lines;

        Paragraph(List<Line> lines) {
            this.lines = new ArrayList<>(lines);
        }
    }
}
