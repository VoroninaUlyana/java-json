import javax.swing.*;
import java.awt.*;
public class MainWindow extends JFrame
{
    public MainWindow()
    {
        setTitle("Three Threads Sorting Visualizer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        int[] base = ArrayLoader.load("input.txt");
        if (base == null || base.length == 0)
        {
            JOptionPane.showMessageDialog(this,
                    "Не получилось загрузить массив из input.txt.\n" +
                            "Помести целые числа через пробел или перевод строки в файл input.txt\n" +
                            "и перезапусти программу.",
                    "Ошибка загрузки", JOptionPane.ERROR_MESSAGE);
            base = new int[] {50, 10, 90, 30, 70, 5, 85, 40, 29, 49, 35, 15};
        }
        JButton startBtn = new JButton("Start");
        JButton resetBtn = new JButton("Reset");
        JPanel control = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        control.add(resetBtn);
        control.add(startBtn);
        JPanel threadsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        threadsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        ThreadPanel p1 = new ThreadPanel("Thread 1 — Bubble", base.clone());
        ThreadPanel p2 = new ThreadPanel("Thread 2 — Insertion", base.clone());
        ThreadPanel p3 = new ThreadPanel("Thread 3 — Quick", base.clone());
        threadsPanel.add(p1);
        threadsPanel.add(p2);
        threadsPanel.add(p3);
        add(control, BorderLayout.NORTH);
        add(new JScrollPane(threadsPanel), BorderLayout.CENTER);
        startBtn.addActionListener(e -> {
            startBtn.setEnabled(false);
            resetBtn.setEnabled(false);
            new Thread(new SortWorker(p1.getArrayCopy(), p1, SortAlgorithm.BUBBLE)).start();
            new Thread(new SortWorker(p2.getArrayCopy(), p2, SortAlgorithm.INSERTION)).start();
            new Thread(new SortWorker(p3.getArrayCopy(), p3, SortAlgorithm.QUICK)).start();
            new Thread(() -> {
                try
                {
                    while (!p1.isFinished() || !p2.isFinished() || !p3.isFinished())
                    {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ignored) {}
                SwingUtilities.invokeLater(() -> {
                    startBtn.setEnabled(true);
                    resetBtn.setEnabled(true);
                });
            }).start();
        });
        resetBtn.addActionListener(e -> {
            p1.resetToBase();
            p2.resetToBase();
            p3.resetToBase();
        });

        resetBtn.setEnabled(true);

        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
