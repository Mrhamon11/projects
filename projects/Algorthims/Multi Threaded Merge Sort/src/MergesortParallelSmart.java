import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by aviam on 2/26/2017.
 */
public class MergesortParallelSmart extends Sort {

    /**
     * Sorts the supplied array using threads only when the size of the array/sub-array is less than or equal to the
     * value of S. Returns how long the sort took in nanoseconds.
     * @param a The list to be sorted.
     * @param S The size for which mergesort should switch to not using threads.
     * @return How long the sorting took in nanoseconds.
     */
    public static long sort(Comparable[] a, double S) {
        Comparable[] aux = new Comparable[a.length];
        long start = System.nanoTime();
        ForkJoinPool fjp = new ForkJoinPool(2);
        sort(a, aux, 0, a.length - 1, fjp, S, false);
        long end = System.nanoTime();
        long totalTime = end - start;
        System.out.println("Sorted array of size " + a.length + " in " + totalTime + " nanoseconds using threads smartly.");
        return totalTime;
    }


    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, ForkJoinPool fjp, double S, boolean switched) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo) / 2;
        //recursively: reduce sub-arrays to length 1, merge up

        //First checks whether or not to thread. If yes, thread, otherwise, check if this is the first time we are
        //switching, if so print that we are switching, and the thread ID that the switch is occurring on.
        if((hi - lo + 1) > S){
            fjp.invoke(new WorkerThread(a, aux, lo, mid, fjp, S, switched));
        }
        else{
            if(!switched){
                System.out.println("Switching to unthreaded version at data size " + (hi - lo + 1) + ". Thread ID: "
                        + Thread.currentThread().getId());
                switched = true;
            }
            sort(a, aux, lo, mid, fjp, S, switched);
        }
        sort(a, aux, mid + 1, hi, fjp, S, switched);
        merge(a, aux, lo, mid, hi);
    }

    /**
     * Since the value of "switched" the boolean value in the private sort method wouldn't be able to be changed if it were
     * in an anonymous inner class like in MergesortParallelNaive, an innerclass is needed to pass to the ForkJoinPool instance
     * to make this work. Contains the implementation of the compute method overridden from RecursiveTask.
     */
    private static class WorkerThread extends RecursiveTask<Boolean>{
        private Comparable[] a;
        private Comparable[] aux;
        private int lo;
        private int mid;
        private ForkJoinPool fjp;
        private double S;
        private boolean switched;

        public WorkerThread(Comparable[] a, Comparable[] aux, int lo, int mid, ForkJoinPool fjp, double S, Boolean switched){
            this.a = a;
            this.aux = aux;
            this.lo = lo;
            this.mid = mid;
            this.fjp = fjp;
            this.S = S;
            this.switched = switched;
        }

        /**
         * Sorts the left side of the recursion tree in mergesort.
         * @return true.
         */
        @Override
        protected Boolean compute() {
            sort(a, aux, lo, mid, fjp, S, switched);
            return true;
        }
    }
}

