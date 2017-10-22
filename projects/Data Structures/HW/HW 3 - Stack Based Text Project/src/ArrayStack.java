/**
 * Array implementation of a stack, with unlimited size. Objects of this class will automatically increase stack
 * capacity to accommodate an unlimited amount of data, and will automatically decrease size to save on memory when
 * number of elements reaches half of the max capacity.
 *
 * Parts of this implementation are taken from Goodrich book, while the expandArray and shrinkArray methods were
 * taken from top answer from the following site:
 * https://stackoverflow.com/questions/22415650/how-can-i-implement-a-dynamic-array-stack-in-java
 *
 * Created by Avi on 10/1/2016.
 */
public class ArrayStack<E> {
    private static final int INITIAL_CAPACITY = 10;
    private int maxSize;
    private E[] arrayStack;
    private int top;

    /**
     * Constructor for ArrayStack class, that creates an array implementation of a stack.
     */
    public ArrayStack() {
        this.maxSize = INITIAL_CAPACITY;
        this.arrayStack = (E[]) new Object[this.maxSize];
        this.top = -1;
    }

//    public static void main(String[] args) {
//        ArrayStack<Integer> a = new ArrayStack<>();
//        System.out.println(a.pop());
//        System.out.println(a.isEmpty());
//        a.push(5);
//        a.push(4);
//        System.out.println(a.peek());
//        System.out.println(a.isFull());
//        System.out.println(a.isEmpty());
//        a.push(9);
//        System.out.println(a.peek());
//        System.out.println(a.isFull());
//        System.out.println(a.size());
//        System.out.println(a.pop());
//        System.out.println(a.size());
//        a.push(1);
//        System.out.println(a.isFull());
//        a.push(2);
//        System.out.println(a.peek());
//        System.out.println(a.isFull());
//        a.push(3);
//        System.out.println(a.size());
//    }

    /**
     * Adds an element of generic type E to the top of the stack.
     * @param e the element to be added.
     */
    public void push(E e) {
        if(isFull()) {
            expandArray();
        }
        this.arrayStack[++this.top] = e;
    }

    /**
     * Removes and returns the top element on the stack.
     * @return the top element on the stack.
     */
    public E pop(){
        if(isEmpty()){
            return null;
        }
        else{
            shrinkArray();
            E stackTop = this.arrayStack[this.top];
            this.arrayStack[this.top] = null;
            this.top--;
            return stackTop;
        }
    }

    /**
     * Returns, but does not remove, the top element on the stack.
     * @return the top element on the stack.
     */
    public E peek(){
        if(isEmpty()){
            return null;
        }
        else{
           return this.arrayStack[this.top];
        }
    }

    /**
     * Returns the number of elements in the stack.
     * @return the number of elements in the stack
     */
    public int size(){
        return this.top + 1;
    }

    private boolean isEmpty(){
        return this.top == -1;
    }

    private boolean isFull(){
        return this.maxSize == size();
    }

    private void expandArray(){
        int arraySize = size();
        E[] biggerArray = (E[]) new Object[arraySize * 2];
        for(int i = 0; i < arraySize; i++){
            biggerArray[i] = this.arrayStack[i];
        }
        this.arrayStack = biggerArray;
        this.maxSize = biggerArray.length;
    }

    private void shrinkArray(){
        int arraySize = size();
        if(arraySize < this.maxSize / 4) {
            E[] smallerArray = (E[]) new Object[this.arrayStack.length / 2];
            for(int i = 0; i < arraySize; i++){
                smallerArray[i] = this.arrayStack[i];
            }
            this.arrayStack = smallerArray;
            this.maxSize = smallerArray.length;
        }
    }
}
