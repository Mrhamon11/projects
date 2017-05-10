/**
 * This class will allow a user to reverse a linked list by every n elements. For example, if a user wanted to reverse
 * the list 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 by every 5 elements, the resulting list will be:
 * 5 -> 4 -> 3 -> 2 -> 1 -> 8 -> 7 -> 6.
 * Created by Avi on 11/28/2016.
 */
public class ReverseByN {
    private SinglyLinkedList<Integer> list;
    private SinglyLinkedList<Integer> reversed;
    private SinglyLinkedList<Integer> listCopy;
    private int n;

    /**
     * Constructs a ReverseByN object that will allow a user to reverse every n elements in a list.
     * @param list The linked list that one wants to reverse.
     * @param n Every n elements in the list will be reversed.
     */
    public ReverseByN(SinglyLinkedList<Integer> list, int n){
        this.list = list;
        this.reversed = new SinglyLinkedList<>();
        this.listCopy = list;
        this.n = n;
    }

    //Input must be entered on the command line in the following format: first number is the number n for which the
    //list will be reversed by, and the following numbers are the the list. All numbers are to be separated by spaces.
    //Example: Reverse by 3 on the list 1 2 3 4 would be entered as follows: 3 1 2 3 4
    public static void main(String[] args) {
        SinglyLinkedList<Integer> newList = new SinglyLinkedList<>();
        int n = Integer.parseInt(args[0]);
        for(int i = 1; i < args.length; i++){
            newList.addLast(Integer.parseInt(args[i]));
        }
        ReverseByN rbn = new ReverseByN(newList, n);
        rbn.reverseBy();
        System.out.println(rbn.toString());
    }

    /**
     * Reverses every n elements in the linked list supplied by the user. n must be supplied by the user. If the length
     * of the list is less than the supplied n, then the whole list will be reversed. If the length of the list is not
     * divisible by n, then it will reverse every n elements until the number of elements remaining is less than n, in
     * which case, those elements will be reversed.
     */
    public void reverseBy(){
        int listSize = this.listCopy.size();

        //The size of the list is less than n, so reverse full list.
        if(listSize < this.n){
            SinglyLinkedList<Integer> temp = reverseFirstNElements(listSize, this.listCopy);
            SinglyLinkedList.Node<Integer> head = temp.headNode();
            SinglyLinkedList.Node<Integer> tail = temp.lastNode();
            this.reversed.setHeadNode(head);
            this.reversed.setTailNode(tail);
            this.reversed.setSize(this.reversed.size() + listSize);
        }
        //We need to split up the list into n parts, and then reverse the rest of the list.
        else {
            int remainder = listSize % n; //We subtract this from the listSize value so the loop knows when to stop.
            for (int i = 0; i < listSize - remainder; i = i + this.n) {
                SinglyLinkedList<Integer> temp = reverseFirstNElements(this.n, this.listCopy);
                SinglyLinkedList.Node<Integer> head = temp.headNode();
                SinglyLinkedList.Node<Integer> tail = temp.lastNode();
                if(this.reversed.headNode() == null) {
                    this.reversed.setHeadNode(head);
                    this.reversed.setTailNode(tail);
                }
                else{
                    this.reversed.lastNode().setNext(head);
                    this.reversed.setTailNode(tail);
                }
                this.reversed.setSize(this.reversed.size() + temp.size());
            }
            if(!this.listCopy.isEmpty()) {
                int listCopySize = this.listCopy.size();
                SinglyLinkedList<Integer> temp = reverseFirstNElements(listCopySize, this.listCopy);
                SinglyLinkedList.Node<Integer> head = temp.headNode();
                SinglyLinkedList.Node<Integer> tail = temp.lastNode();
                this.reversed.lastNode().setNext(head);
                this.reversed.setTailNode(tail);
                this.reversed.setSize(this.reversed.size() + listCopySize);
            }
        }
    }

    /**
     * Takes a sublist of the whole linked list and reverses it. This sublist is returned to be appended onto the
     * end of tail of the main list.
     * @param n The number by which we want to reverse.
     * @param listCopy The full list, of which only n elements will be used.
     * @return A new linked list of the first n elements in the listCopy list.
     */
    private SinglyLinkedList<Integer> reverseFirstNElements(int n, SinglyLinkedList<Integer> listCopy){
        SinglyLinkedList<Integer> temp = new SinglyLinkedList<>();
        for(int i = 0; i < n; i++){
            temp.addFirst(listCopy.removeFirst());
        }
        return temp;
    }

    /**
     * Returns a string representation of the reversed list where each element is separated by spaces.
     * @return A string representation fo the reversed list.
     */
    @Override
    public String toString() {
        SinglyLinkedList.Node<Integer> node = this.reversed.headNode();
        StringBuilder sb = new StringBuilder();
        sb.append("Reversed List: ");
        while (node != null) {
            sb.append(node.getElement().toString() + " ");
            node = node.getNext();
        }
        return sb.toString();
    }

    public SinglyLinkedList<Integer> getReversed() {
        return this.reversed;
    }
}