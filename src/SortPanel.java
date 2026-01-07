import javax.swing.*;
import java.awt.*;
public class SortPanel extends JPanel
{
    private int[] array;
    public SortPanel(int[] initial) {
        this.array = initial.clone();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        int minHeight = 300;
        int maxHeight = 600;
        int elementHeight = 10;

        int calculatedHeight = array.length * elementHeight + 20; // +20 для отступов
        if (calculatedHeight < minHeight) calculatedHeight = minHeight;
        if (calculatedHeight > maxHeight) calculatedHeight = maxHeight;

        setPreferredSize(new Dimension(0, calculatedHeight));
    }

    public synchronized void setArray(int[] arr)
    {
        this.array = arr.clone();
        repaint();
    }
    public synchronized int[] getArrayCopy()
    {
        return array == null ? new int[0] : array.clone();
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int[] arr = getArrayCopy();
        if (arr == null || arr.length == 0) return;
        int W = getWidth() - 19;
        int H = getHeight() - 10;
        int barH = Math.max(4, H / arr.length);
        int max = Integer.MIN_VALUE;
        for (int v : arr) if (v > max) max = v;
        if (max == 0) max = 1;
        for (int i = 0; i < arr.length; i++)
        {
            double ratio = arr[i] / (double) max;
            int barW = (int) Math.round(W * ratio);
            int green = Math.max(30, 200 - i * 3);
            g.setColor(new Color(50, Math.min(220, green), 170));
            int y = 5 + i * barH;
            g.fillRect(5, y, Math.max(1, barW), barH - 2);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(5, y, Math.max(1, barW), barH - 2);
            g.setColor(Color.BLACK);
            g.setFont(g.getFont().deriveFont(14f));
            g.drawString(String.valueOf(arr[i]), -2 + barW, y + barH - 4);

        }
    }
}
