public class Mergesort extends Sort
{

    // This class should not be instantiated.
    private Mergesort()
    {
    }
    
    public static long sort(Comparable[] a)
    {
		Comparable[] aux = new Comparable[a.length];
		long start = System.nanoTime();
		sort(a, aux, 0, a.length - 1);
		long end = System.nanoTime();
		long totalTime = end - start;
		System.out.println("Sorted array of size " + a.length + " in " + totalTime + " nanoseconds without threads.");
		return totalTime;
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
    {
	if (hi <= lo)
	    return;
	int mid = lo + (hi - lo) / 2;
	//recursively: reduce sub-arrays to length 1, merge up 
	sort(a, aux, lo, mid);
	sort(a, aux, mid + 1, hi); 
	merge(a, aux, lo, mid, hi);
    }
    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays

    /*
    public static void linearithmic(int N)
    {
	if (N == 0) return;
	//solve 2 problems of half the size
	linearithmic(N/2);
	linearithmic(N/2);
	//do a linear amount of work
	linear(N);
    }
    */
}