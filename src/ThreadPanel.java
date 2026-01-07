import javax.swing.*;
import java.awt.*;
public class ThreadPanel extends JPanel
{
    private final SortPanel sortPanel;
    private final JLabel startLabel;
    private final JLabel endLabel;
    private volatile boolean finished = true;
    private final int[] baseArray;
    public ThreadPanel(String title, int[] baseArray)
    {
        this.baseArray = baseArray.clone();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(title));
        JPanel status = new JPanel(new GridLayout(2,1));
        startLabel = new JLabel("Start: ---");
        endLabel = new JLabel("End: ---");
        status.add(startLabel);
        status.add(endLabel);
        sortPanel = new SortPanel(baseArray.clone());
        add(status, BorderLayout.NORTH);
        add(sortPanel, BorderLayout.CENTER);
    }
    public void setStartTime(String text)
    {
        SwingUtilities.invokeLater(() -> startLabel.setText("Start: " + text));
    }
    public void setEndTime(String text)
    {
        SwingUtilities.invokeLater(() -> endLabel.setText("End: " + text));
    }
    public void setArrayForDisplay(int[] arr)
    {
        sortPanel.setArray(arr);
    }
    public int[] getArrayCopy()
    {
        return baseArray.clone();
    }
    public void resetToBase()
    {
        finished = true;
        setStartTime("---");
        setEndTime("---");
        setArrayForDisplay(baseArray.clone());
    }
    public void markStarted() { finished = false; }
    public void markFinished() { finished = true; }
    public boolean isFinished() { return finished; }
}
