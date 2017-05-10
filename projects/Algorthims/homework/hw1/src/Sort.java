public abstract class Sort
{
    /**
     * is v < w ?
     * @param v
     * @param w
     * @return
     */
    @SuppressWarnings("unchecked")
    protected static boolean less(Comparable v, Comparable w)
    {
	    return v.compareTo(w) < 0;
    }
    
    /**
     * exchange a[i] and a[j]
     * @param a
     * @param i
     * @param j
     */
    protected static void exch(Object[] a, int i, int j)
    {
	    Object swap = a[i];
	    a[i] = a[j];
	    a[j] = swap;
    }

    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
    protected static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi)
    {
        // copy to aux[]
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        // merge back to a[]
        int left = lo, right = mid + 1;
        for (int current = lo; current <= hi; current++)
        {
            if (left > mid) //left half exhausted
                a[current] = aux[right++]; //copy value from the right
            else if (right > hi) //right half exhausted
                a[current] = aux[left++]; //copy value from the left
                //neither exhausted - copy lower value
            else if (less(aux[right], aux[left]))
                a[current] = aux[right++];
            else
                a[current] = aux[left++];
        }
    }
}
