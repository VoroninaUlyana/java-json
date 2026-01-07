package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

class ParseError {
    int position;
    String message;

    public ParseError(int position, String message) {
        this.position = position;
        this.message = message;
    }
}

class Node<T> {
    T data;
    Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    public T getData() { return data; }

    public Node<T> getNext() { return next; }
    public void setNext(Node<T> next) { this.next = next; }
}

class MyList<T> {
    protected Node<T> head = null;
    protected Node<T> tail = null;
    protected int size = 0;

    public void pushFront(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.setNext(head);
        head = newNode;
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    public void popFront() {
        if (head == null) {
            throw new RuntimeException("Список пуст");
        }
        head = head.getNext();
        if (head == null) {
            tail = null;
        }
        size--;
    }

    public T front() {
        if (head == null) {
            throw new RuntimeException("Список пуст");
        }
        return head.getData();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }
}

class MyStack<T> extends MyList<T> {
    public void push(T value) {
        pushFront(value);
    }

    public void pop() {
        popFront();
    }

    public T top() {
        return front();
    }
}

class PolishNotationCalculator extends JFrame {
    private JTextField inputField;
    private JTextField rpnField;
    private JTextField resultField;
    private JTextArea variablesArea;
    private Map<String, Double> variables;

    public PolishNotationCalculator() {
        variables = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Калькулятор с обратной польской записью");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font bigFont = new Font("Arial", Font.PLAIN, 16);
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font textAreaFont = new Font("Arial", Font.PLAIN, 14);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 15));

        JLabel exprLabel = new JLabel("Выражение:");
        exprLabel.setFont(labelFont);
        inputPanel.add(exprLabel);

        inputField = new JTextField();
        inputField.setFont(bigFont);
        inputField.setPreferredSize(new Dimension(400, 40));
        inputPanel.add(inputField);

        JLabel rpnLabel = new JLabel("Обратная польская запись:");
        rpnLabel.setFont(labelFont);
        inputPanel.add(rpnLabel);

        rpnField = new JTextField();
        rpnField.setFont(bigFont);
        rpnField.setEditable(false);
        rpnField.setPreferredSize(new Dimension(400, 40));
        inputPanel.add(rpnField);

        JLabel resultLabel = new JLabel("Результат:");
        resultLabel.setFont(labelFont);
        inputPanel.add(resultLabel);

        resultField = new JTextField();
        resultField.setFont(bigFont);
        resultField.setEditable(false);
        resultField.setPreferredSize(new Dimension(400, 40));
        inputPanel.add(resultField);

        JButton calculateButton = new JButton("ВЫЧИСЛИТЬ (=)");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.setPreferredSize(new Dimension(200, 50));
        calculateButton.setBackground(new Color(30, 144, 255)); // Яркий синий
        calculateButton.setForeground(Color.BLACK); // Черный текст
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        calculateButton.setOpaque(true);
        calculateButton.addActionListener(new CalculateButtonListener());
        inputPanel.add(new JLabel()); // пустая ячейка для выравнивания
        inputPanel.add(calculateButton);

        JPanel variablesPanel = new JPanel(new BorderLayout(10, 10));
        JLabel varsLabel = new JLabel("Переменные (формат: имя=значение):");
        varsLabel.setFont(labelFont);
        variablesPanel.add(varsLabel, BorderLayout.NORTH);

        variablesArea = new JTextArea(8, 30); // больше строк и столбцов
        variablesArea.setFont(textAreaFont);
        variablesArea.setText("a=5\nb=3\nc=10\nd=2");
        JScrollPane scrollPane = new JScrollPane(variablesArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        variablesPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton clearButton = new JButton("ОЧИСТИТЬ ВСЕ");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setPreferredSize(new Dimension(150, 35));
        clearButton.setBackground(new Color(220, 20, 60));
        calculateButton.setForeground(Color.BLACK);
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        clearButton.setContentAreaFilled(false); // Отключаем стандартное заполнение
        clearButton.setOpaque(true); // Делаем непрозрачной
        clearButton.addActionListener(e -> {
            inputField.setText("");
            rpnField.setText("");
            resultField.setText("");
        });
        buttonPanel.add(clearButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(variablesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(800, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 600));
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            calculate();
        }
    }

    private void calculate() {
        String expression = inputField.getText().trim();

        if (expression.isEmpty()) {
            JOptionPane.showMessageDialog(PolishNotationCalculator.this, "Введите выражение", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        parseVariables();

        List<ParseError> errors = new ArrayList<>();
        if (!checkExpression(expression, errors)) {
            StringBuilder errorMsg = new StringBuilder("Некорректное выражение:\n");
            for (ParseError err : errors) {
                errorMsg.append("Позиция ").append(err.position).append(": ").append(err.message).append("\n");
            }
            JOptionPane.showMessageDialog(PolishNotationCalculator.this, errorMsg.toString(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String rpn = toRPN(expression);
            rpnField.setText(rpn);

            double result = evaluateRPN(rpn);
            resultField.setText(String.valueOf(result));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PolishNotationCalculator.this, "Ошибка вычисления: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parseVariables() {
        variables.clear();
        String[] lines = variablesArea.getText().split("\n");

        for (String line : lines) {
            line = line.replaceAll("\\s", "");
            int eqPos = line.indexOf('=');
            if (eqPos != -1 && eqPos != 0) {
                String var = line.substring(0, eqPos).toLowerCase();
                try {
                    double value = Double.parseDouble(line.substring(eqPos + 1));
                    variables.put(var, value);
                } catch (NumberFormatException e) {
                    // Игнорируем некорректные строки
                }
            }
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean checkExpression(String expr, List<ParseError> errors) {
        MyStack<Character> brackets = new MyStack<>();
        Map<Character, Character> bracketPairs = new HashMap<>();
        bracketPairs.put('(', ')');
        bracketPairs.put('[', ']');
        bracketPairs.put('{', '}');

        boolean expectOperand = true;

        for (int i = 0; i < expr.length(); ++i) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (bracketPairs.containsKey(c)) {
                if (!expectOperand) {
                    errors.add(new ParseError(i, "Пропущен оператор перед скобкой"));
                    return false;
                }
                brackets.push(c);
                expectOperand = true;
            } else if (c == ')' || c == ']' || c == '}') {
                if (brackets.isEmpty()) {
                    errors.add(new ParseError(i, "Несоответствующая закрывающая скобка"));
                    return false;
                }
                char expected = bracketPairs.get(brackets.top());
                brackets.pop();
                if (c != expected) {
                    errors.add(new ParseError(i, "Ожидалось '" + expected + "'"));
                    return false;
                }
                expectOperand = false;
            } else if (isOperator(c)) {
                if (expectOperand && c != '-') {
                    errors.add(new ParseError(i, "Неожиданный оператор"));
                    return false;
                }
                if (c == '-' && !expectOperand) {
                    expectOperand = true;
                } else {
                    expectOperand = true;
                }
            } else if (Character.isLetterOrDigit(c) || c == '.') {
                if (!expectOperand) {
                    errors.add(new ParseError(i, "Пропущен оператор между операндами"));
                    return false;
                }
                while (i + 1 < expr.length() &&
                        (Character.isLetterOrDigit(expr.charAt(i + 1)) || expr.charAt(i + 1) == '.')) {
                    i++;
                }
                expectOperand = false;
            } else {
                errors.add(new ParseError(i, "Недопустимый символ: " + c));
                return false;
            }
        }

        if (!brackets.isEmpty()) {
            errors.add(new ParseError(expr.length(), "Незакрытая скобка: '" + brackets.top() + "'"));
            return false;
        }

        return true;
    }

    private String toRPN(String expr) {
        MyStack<Character> ops = new MyStack<>();
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 2);

        StringBuilder output = new StringBuilder();
        StringBuilder token = new StringBuilder();
        boolean expectUnary = true;

        for (int i = 0; i < expr.length(); ++i) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (Character.isLetterOrDigit(c) || c == '.') {
                token.append(c);
                continue;
            }

            if (token.length() > 0) {
                output.append(token).append(" ");
                token.setLength(0);
                expectUnary = false;
            }

            if (c == '(' || c == '[' || c == '{') {
                ops.push('(');
                expectUnary = true;
            } else if (c == ')' || c == ']' || c == '}') {
                while (!ops.isEmpty() && ops.top() != '(') {
                    output.append(ops.top()).append(" ");
                    ops.pop();
                }
                ops.pop();
                expectUnary = false;
            } else if (isOperator(c)) {
                if (c == '-' && expectUnary) {
                    output.append("0 ");
                }
                while (!ops.isEmpty() && ops.top() != '(' &&
                        precedence.getOrDefault(ops.top(), 0) >= precedence.getOrDefault(c, 0)) {
                    output.append(ops.top()).append(" ");
                    ops.pop();
                }
                ops.push(c);
                expectUnary = true;
            }
        }

        if (token.length() > 0) {
            output.append(token).append(" ");
        }

        while (!ops.isEmpty()) {
            output.append(ops.top()).append(" ");
            ops.pop();
        }

        if (output.length() > 0 && output.charAt(output.length() - 1) == ' ') {
            output.setLength(output.length() - 1);
        }

        return output.toString();
    }

    private double evaluateRPN(String rpn) {
        MyStack<Double> stack = new MyStack<>();
        String[] tokens = rpn.split("\\s+");

        for (String token : tokens) {
            if (Character.isLetter(token.charAt(0))) {
                String varLower = token.toLowerCase();
                if (!variables.containsKey(varLower)) {
                    throw new RuntimeException("Неопределённая переменная: " + token);
                }
                stack.push(variables.get(varLower));
            } else if (Character.isDigit(token.charAt(0))) {
                stack.push(Double.parseDouble(token));
            } else {
                if (stack.getSize() < 2) {
                    throw new RuntimeException("Недостаточно операндов для оператора: " + token);
                }
                double b = stack.top();
                stack.pop();
                double a = stack.top();
                stack.pop();

                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            throw new RuntimeException("Деление на ноль");
                        }
                        stack.push(a / b);
                        break;
                    default:
                        throw new RuntimeException("Неизвестный оператор: " + token);
                }
            }
        }

        if (stack.isEmpty()) {
            throw new RuntimeException("Пустое выражение");
        }

        return stack.top();
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            PolishNotationCalculator calculator = new PolishNotationCalculator();
            calculator.setVisible(true);
        });
    }
}