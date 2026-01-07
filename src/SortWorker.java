import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class SortWorker implements Runnable
{
    private final int[] array;
    private final ThreadPanel panel;
    private final SortAlgorithm algorithm;
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
    public SortWorker(int[] array, ThreadPanel panel, SortAlgorithm algorithm)
    {
        this.array = array;
        this.panel = panel;
        this.algorithm = algorithm;
    }
    @Override
    public void run()
    {
        panel.markStarted();
        panel.setStartTime(df.format(new Date()));
        panel.setArrayForDisplay(array.clone());
        long tStart = System.currentTimeMillis();
        switch (algorithm)
        {
            case BUBBLE:
                SortAlgorithms.bubbleSort(array, this::onStep);
                break;
            case INSERTION:
                SortAlgorithms.insertionSort(array, this::onStep);
                break;
            case QUICK:
                SortAlgorithms.quickSort(array, 0, array.length - 1, this::onStep);
                break;
        }
        long tEnd = System.currentTimeMillis();
        panel.setEndTime(df.format(new Date()) + " (" + String.format("%.3f s", (tEnd - tStart)/1000.0) + ")");
        panel.markFinished();
    }
    private void onStep()
    {
        SwingUtilities.invokeLater(() -> panel.setArrayForDisplay(array.clone()));
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }
}
