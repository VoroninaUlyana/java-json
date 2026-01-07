public class Line {
    private String indent;  // Отступ (пробелы/табуляция в начале строки)
    private String content; // Содержимое без отступа

    public Line(String indent, String content) {
        this.indent = indent;
        this.content = content;
    }

    public String getIndent() {
        return indent;
    }

    public String getContent() {
        return content;
    }

    public boolean isEmpty() {
        return content.trim().isEmpty();
    }
}

