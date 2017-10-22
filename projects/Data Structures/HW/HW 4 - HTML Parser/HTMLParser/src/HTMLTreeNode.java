import java.util.*;

/**
 * This class is a linked implementation of a tree that represents a parsed HTML document with a parent child relationship
 * between HTML tags. Each node has a reference to the tag name, its parent tag, all of the attributes, including id, even
 * if one was not supplied in the document, and a list of all of the children tags it has. All text will have a special
 * "text" node created for it as well, with its parent being the tag the text resided in.
 * Created by Avi on 11/11/2016.
 */
public class HTMLTreeNode<E> {
    private E tagName;
    private HTMLTreeNode<E> parent;
    private Map<String, String> attributes;
    private List<HTMLTreeNode<E>> children;

    /**
     * Constructor for HTMLTreeNode.
     * @param parent The parent of the tag.
     * @param tagName The name of the tag.
     */
    public HTMLTreeNode(HTMLTreeNode<E> parent, E tagName){
        this.parent = parent;
        this.tagName = tagName;
        this.attributes = new HashMap<>();
        this.children = new ArrayList<>();
    }

    /**
     * Returns the name of the tag in this node.
     * @return The name of the tag in this node.
     */
    public E getTagName() {
        return this.tagName;
    }

    /**
     * Sets the name of the tag to the value supplied.
     * @param tagName The new tag name.
     */
    public void setTagName(E tagName) {
        this.tagName = tagName;
    }

    /**
     * Returns a list of all of the children tags that this node has.
     * @return A list of all of the children tags that this node has.
     */
    public List<HTMLTreeNode<E>> getChildren() {
        return this.children;
    }

    /**
     * Adds a child the list of children.
     * @param child The child to be added.
     * @return True if added.
     */
    public boolean addChild(HTMLTreeNode<E> child){
        return this.children.add(child);
    }

    /**
     * Returns a map with all of the attributes names and values that this node has.
     * @return A map with all of the attributes names and values that this node has.
     */
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    /**
     * Sets the attributes field to a new map supplied.
     * @param attributes The new map.
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}