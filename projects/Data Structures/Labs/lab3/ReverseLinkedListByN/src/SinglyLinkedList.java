/**
 * Created by Avi on 11/28/2016.
 */
public class SinglyLinkedList<E> {
    //----------Nested Node class-----------
    public static class Node<E>{
        private E element;
        private Node<E> next;
        public Node(E e, Node<E> n) {
            this.element = e;
            this.next = n;
        }
        public E getElement() {
            return this.element;
        }
        public Node<E> getNext() {
            return this.next;
        }
        public void setNext(Node<E> next) {
            this.next = next;
        }
    }
    //----------End of nested Node class--------

    //LinkedListNode fields
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size = 0;
    public SinglyLinkedList(){ } //initially constructs an empty list

    //access methods:
    public int size(){
        return this.size;
    }
    public boolean isEmpty(){
        return this.size == 0;
    }
    public E first(){
        if(isEmpty())
            return null;
        return this.head.getElement();
    }

    public E headElement(){
        if(isEmpty())
            return null;
        return this.head.getElement();
    }
    public E lastElement(){
        if(isEmpty())
            return null;
        return this.tail.getElement();
    }

    public Node<E> headNode(){
        if(isEmpty())
            return null;
        return this.head;
    }

    public void setHeadNode(Node<E> node){
        this.head = node;
    }

    public void setTailNode(Node<E> node){
        this.tail = node;
    }

    public Node<E> lastNode(){
        if(isEmpty())
            return null;
        return this.tail;
    }

    //update methods:
    public void addFirst(E e){
        this.head = new Node<>(e, this.head);
        if(isEmpty())
            this.tail = this.head;
        this.size++;
    }
    public void addLast(E e){
        Node<E> newest = new Node<>(e, null);
        if(isEmpty())
            this.head = newest;
        else
            this.tail.setNext(newest);
        this.tail = newest;
        this.size++;
    }
    public E removeFirst(){
        if(isEmpty())
            return null;
        E answer = this.head.getElement();
        this.head = this.head.getNext();
        this.size--;
        if(isEmpty())
            this.tail = null;
        return answer;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
