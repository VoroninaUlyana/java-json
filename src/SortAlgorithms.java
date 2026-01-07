public class SortAlgorithms
{
    // Пузырёк
    public static void bubbleSort(int[] arr, Runnable onStep)
    {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++)
        {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++)
            {
                if (arr[j] > arr[j+1])
                {
                    int t = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = t;
                    swapped = true;
                    onStep.run();
                }
            }
            if (!swapped) break;
        }
    }
    // Вставками
    public static void insertionSort(int[] arr, Runnable onStep)
    {
        for (int i = 1; i < arr.length; i++)
        {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key)
            {
                arr[j + 1] = arr[j];
                j--;
                onStep.run();
            }
            arr[j + 1] = key;
            onStep.run();
        }
    }
    // Быстрая сортировка
    public static void quickSort(int[] arr, int low, int high, Runnable onStep)
    {
        if (low < high)
        {
            int p = partition(arr, low, high, onStep);
            quickSort(arr, low, p - 1, onStep);
            quickSort(arr, p + 1, high, onStep);
        }
    }
    private static int partition(int[] arr, int low, int high, Runnable onStep)
    {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++)
        {
            if (arr[j] < pivot)
            {
                i++;
                swap(arr, i, j, onStep);
            }
        }
        swap(arr, i + 1, high, onStep);
        return i + 1;
    }
    private static void swap(int[] arr, int a, int b, Runnable onStep)
    {
        if (a == b) return;
        int t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
        onStep.run();
    }
}
