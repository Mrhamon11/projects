/**
 * A class that maintains information on a book.
 * This might form part of a larger application such
 * as a library system, for instance.
 *
 * @author (Insert your name here.)
 * @version (Insert today's date here.)
 */
class Book
{
    // The fields.
    private String author;
    private String title;
    private int pages;
    private String refNumber;
    private int borrowed;
    private boolean courseText;

    /**
     * Set the author and title fields when this object
     * is constructed.
     */
    public Book(String bookAuthor, String bookTitle, int bookPages, boolean courseBook)
    {
        author = bookAuthor;
        title = bookTitle;
        pages = bookPages;
        refNumber = "";
        borrowed = 0;
        courseText = courseBook; 
    }

    // Return the name of the author
    public String getAuthor() 
    {
        return author;
    }
    // Return the title of the book
    public String getTitle() 
    {
        return title;
    }
    // Return the number of pages in the book
    public int getPages()
    {
        return pages;
    }
    // Print name of author
    public void printAuthor()
    {
        System.out.println(author);
    }
    // Print title of book
    public void printTitle()
    {
        System.out.println(title);
    }
    // Print all details of the book
    public void printDetails()
    {
        if (refNumber == "") {
            System.out.print ("Title: " + title + ", " + "Author: " + author + ", " + "Pages: " + pages + ", " + "Reference Number: " + "ZZZ, " + "Borrowed " + borrowed + " times");
        }
        else {
            System.out.print ("Title: " + title + ", " + "Author: " + author + ", " + "Pages: " + pages + ", " + "Reference Number: " + refNumber + ", Borrowed " + borrowed + " times");
        }
    }
    // Reference number 
    public void setRefNumber(String ref)
    {
        if (ref.length() >= 3) {
            refNumber = ref;
        }
        else {
            System.out.println ("Error!");
        }
    }
    // Get Reference number
    public String getRefNumber()
    {
        return refNumber;
    }
    // Changes based on how many times the book was borrowed
    public void borrow()
    {
        borrowed = borrowed + 1;
    }
    // Return number of times borrowed
    public int getBorrowed()
    {
        return borrowed;
    }

    public boolean isCourseText()
    { if (courseText == true) {
            return true;
        }
        else {
            return false;
        }
    }
}
