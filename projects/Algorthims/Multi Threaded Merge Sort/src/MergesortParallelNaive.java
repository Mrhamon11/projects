import java.util.concurrent.*;

/**
 * Created by aviam on 2/25/2017.
 */
public class MergesortParallelNaive extends Sort{
    public static void main(String[] args) {
        Comparable[] l = new Comparable[(int) (Math.pow(10, 5))];
        for(int i = 0; i < l.length; i++){
            l[i] = (int) (Math.random() * 1000);
        }
        MergesortParallelNaive naive = new MergesortParallelNaive();
        naive.sort(l);
    }

    /**
     * Sorts the supplied array using threads at every recursive call. Returns how long the sort took in nanoseconds.
     * @param a The list to be sorted.
     * @return How long the sorting took in nanoseconds.
     */
    public static long sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        long start = System.nanoTime();
        ForkJoinPool fjp = new ForkJoinPool(2);
        sort(a, aux, 0, a.length - 1, fjp);
        long end = System.nanoTime();
        long totalTime = end - start;
        System.out.println("Sorted array of size " + a.length + " in " + totalTime + " nanoseconds using threads.");
        return totalTime;
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, ForkJoinPool fjp) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo) / 2;
        //recursively: reduce sub-arrays to length 1, merge up
        //Use thread pool with anonymous inner class to thread left half of recursive tree.
        fjp.invoke(new RecursiveTask<Boolean>() {
            protected Boolean compute() {
                sort(a, aux, lo, mid, fjp);
                return true;
            }
        });
        sort(a, aux, mid + 1, hi, fjp);
        merge(a, aux, lo, mid, hi);
    }
}
