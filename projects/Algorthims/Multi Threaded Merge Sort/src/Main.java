import java.util.Arrays;

/**
 * Created by aviam on 2/25/2017.
 */
public class Main {
    // Value of S starts at 0 and is changed to whatever the value of the of the array size that regular mergesort
    // is faster than naive. Since regular mergesort is always faster than naive, this loop will execute once.
    // Hardcoding in a value for S will make mergesort smart work, however.
    public static void main(String[] args) {
       double S = 0;
        for(int i = 6; i > 0; i--){
            Comparable[] list1 = new Comparable[(int) (Math.pow(10, i))];
            for(int j = 0; j < list1.length; j++){
                list1[j] = (int) (Math.random() * 1000);
            }
            Comparable[] list2 = Arrays.copyOf(list1, list1.length);

            long timeNaive = MergesortParallelNaive.sort(list1);
            long timeRegular = Mergesort.sort(list2);
            if(timeRegular < timeNaive){
                S = Math.pow(10, i);
                break;
            }
        }

        Comparable[] list = new Comparable[(int) (10 * S)];
        for(int j = 0; j < list.length; j++){
            list[j] = (int) (Math.random() * 1000);
        }
        MergesortParallelSmart.sort(list, S);
        System.out.println("");
    }

    /**
     * Testing for times on all versions of mergesort.
     */
    public static void testing(){
        Comparable[] l = new Comparable[(int) (Math.pow(10, 4))];

        for(int i = 0; i < l.length; i++){
            l[i] = (int) (Math.random() * 1000);
        }
        Comparable[] l2 = Arrays.copyOf(l, l.length);

        MergesortParallelSmart.sort(l, 80);
        MergesortParallelNaive.sort(l);
        Mergesort.sort(l2);
        System.out.println(arraysEqual(l, l2));
    }

    /**
     * Checks to make sure that the two provided arrays are equal in length and content.
     * Used for checking that threaded sort really did sort the data set.
     * @param a The first Array.
     * @param b The second Array.
     * @return True if the arrays contain exactly the same elements, false otherwise.
     */
    public static boolean arraysEqual(Comparable[] a, Comparable[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i< a.length; i++){
            if(a[i].compareTo(b[i]) != 0){
                return false;
            }
        }
        return true;
    }
}
