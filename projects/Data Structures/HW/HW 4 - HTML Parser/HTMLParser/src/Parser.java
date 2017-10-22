import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class will convert a balanced HTML document that contain specific tags, into a DOM tree, keeping the parent
 * child relationship between nested tags. Only the following tags can be parsed into a tree, and any other tag used
 * will be rejected: html, head, title, body, p, b, u, i, ol, ul, li, h1, h2, blockquote, div, code, pre, span, iframe.
 * Created by Avi on 11/11/2016.
 */
public class Parser {
    private String htmlDoc;
    private HTMLTreeNode<String> DOMTree;
    private Set<String> tagSet;
    private Set<String> preExistingIDs;
    private int id;

    /**
     * Constructor for a Parser object. Takes in a directory for where the HTML document is located, and converts it
     * into a string which will then be converted into a tree model.
     * @param dir The directory where the HTML file resides.
     */
    public Parser(String dir){
        readHTMLFile(dir);
        makeTagSet();
        this.preExistingIDs = new HashSet<String>();
        this.id = 0;
    }

    //Tests the parse method, and then prints out the node information in depth first search order, followed by
    //breath first search order.
    public static void main(String[] args) {
        Parser parser = new Parser("C:\\Users\\Avi\\OneDrive\\School\\YU\\Classes\\2016 Fall\\Data Structures\\HW\\HW 5\\html-test-file.html"); //Directory of html doc.
        parser.parse(parser.getHtmlDoc());
        parser.depthFirst();
        System.out.println("");
        parser.breathFirst();
    }

    /**
     * Takes in a directory where the HTML file is located, and reads each line to convert it into a string.
     * Newly created string is assigned to a field for an object of this class. The <!DOCTYPE html> tag, if included
     * in the HTML document, will not be considered in the creation of the tree, and will be omitted when creating the
     * string.
     * @param dir The directory where the HTML file resides.
     */
    public void readHTMLFile(String dir){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(dir)));
            String line;
            while ((line = reader.readLine()) != null){
                if(!line.equals("<!DOCTYPE html>")) {
                    sb.append(line);
                }
            }
            this.htmlDoc = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Initializes a set to contain all of the tags that can be parsed. Used to check while parse that the current tag
     * being read is among the permitted tags to parse.
     */
    private void makeTagSet(){
        tagSet = new HashSet<>();
        tagSet.add("html");
        tagSet.add("head");
        tagSet.add("title");
        tagSet.add("body");
        tagSet.add("p");
        tagSet.add("b");
        tagSet.add("u");
        tagSet.add("i");
        tagSet.add("u");
        tagSet.add("i");
        tagSet.add("ol");
        tagSet.add("ul");
        tagSet.add("li");
        tagSet.add("h1");
        tagSet.add("h2");
        tagSet.add("blockquote");
        tagSet.add("div");
        tagSet.add("code");
        tagSet.add("pre");
        tagSet.add("span");
        tagSet.add("iframe");
    }


    /**
     * Parses the html document that has been converted into a string and creates a tree representation of the tags and
     * content. Tree is then assigned to this object's DOMTree field for future access and printing.
     * @param htmlDoc A string of the HTML file that was created by the readHTMLFile method.
     */
    public void parse(String htmlDoc){
        this.htmlDoc = htmlDoc.replaceAll("\\t", "");
        this.DOMTree = parseTree(null); //null to make parent of the <html> tag null.
        preOrderGetIDs(this.DOMTree);
        setNodeIDs(this.DOMTree);
    }

    /**
     * Method to parse an HTML string into a tree. Recursively runs through the htmlDoc field and creates tree nodes
     * depending on which section it reads.
     * @param DOMParent The current node's parent, but null should be supplied when calling this function in a different
     *                  method.
     * @return The tree representation of the HTML document.
     */
    private HTMLTreeNode<String> parseTree(HTMLTreeNode<String> DOMParent){
        if(this.htmlDoc == null || this.htmlDoc.isEmpty()){
            return null;
        }
        if(this.htmlDoc.charAt(0) != '<' || this.htmlDoc.startsWith("</")){
            throw new IllegalArgumentException("Tags aren't balanced");
        }

        //We have a valid tag, so create a new node for this tag.
        HTMLTreeNode<String> node = new HTMLTreeNode<>(DOMParent, null);
        this.htmlDoc = this.htmlDoc.substring(1); //Everything after the first '<' read.
        String tagName = getTagName(this.htmlDoc);
        node.setTagName(tagName);
        int tagNameLength = tagName.length(); //Used for trimming.
        String checkNoAttributes = this.htmlDoc.substring(tagNameLength);
        //If the tag has no attributes (meaning the next character after the tag name is a "<"
        if(checkNoAttributes.trim().charAt(0) == '>'){
            Map<String, String> onlyID = new HashMap<String, String>();
            onlyID.put("id", null);
            node.setAttributes(onlyID);
        }
        //Otherwise, it has attributes and they need to be parsed. pass string to attributeMap method.
        else{
            node.setAttributes(attributeMap(this.htmlDoc, tagName));
        }

        int index = this.htmlDoc.indexOf(">"); //Find the index of the next "<" symbol (will be the one after the tags
                                               //and attributes.
        if(index >= 0){
            this.htmlDoc = this.htmlDoc.substring(index + 1);
        }
        //Tags aren't balanced, or something else went wrong.
        else{
            throw new IllegalArgumentException("Tags aren't balanced");
        }

        //Parse for parts of string after the tag attributes.
        while(!this.htmlDoc.isEmpty()){

            //If we are at a closing tag, remove the closing tag, and then return the node, going back to previous stack frame.
            if(this.htmlDoc.startsWith("</")){
                Pattern p = Pattern.compile("</(.*?)>"); //Regex: </ Sigma* >
                Matcher m = p.matcher(this.htmlDoc);
                if(m.find()){
                    String removeEndTags = "</" + m.group(1) + ">";
                    int strLength = removeEndTags.length();
                    this.htmlDoc = this.htmlDoc.substring(strLength);
                    this.htmlDoc = this.htmlDoc.trim();
                    return node;
                }
            }

            //If we get here, the tag is an opening one, and program will create new node for it recursively.
            if(this.htmlDoc.charAt(0) == '<' && !this.htmlDoc.startsWith("</")){
                node.addChild(parseTree(node));
            }
            //If it's a closing tag, return node.
            else if(this.htmlDoc.startsWith("</" + tagName + ">")){
                String removeEndTags = "</" + tagName + ">";
                int strLength = removeEndTags.length();
                this.htmlDoc = this.htmlDoc.substring(strLength);
                return node;
            }
            //Otherwise, we have text to parse. Find all text leading up to next opening tag and create text node for it,
            //making it's parent "node" which is the node with the tag that is being referred to in this stack frame.
            else{
                Pattern text = Pattern.compile("(.*?)<"); //Regex: Sigma* <
                Matcher matcher = text.matcher(this.htmlDoc);
                String textAttribute;
                if(matcher.find()){
                    textAttribute = matcher.group(1);
                }

                else{
                    textAttribute = ""; //Something went wrong, make a text node from an empty string.
                }
                int textLength = textAttribute.length();
                this.htmlDoc = this.htmlDoc.substring(textLength);

                //Create text node.
                HTMLTreeNode<String> textNode = new HTMLTreeNode<String>(node, "text");
                node.addChild(textNode);
                Map<String, String> textNodeAttributes = new HashMap<String, String>();
                textNodeAttributes.put("id", null);
                textNodeAttributes.put("content", textAttribute);
                textNode.setAttributes(textNodeAttributes);
            }
        }
        throw new IllegalArgumentException("Tags weren't balanced");
    }

    /**
     * Gets the tag name of the current tag being read.
     * @param input The string of the HTML file that is being parsed.
     * @return The name of the tag.
     * @throws IllegalArgumentException
     */
    private String getTagName(String input) throws IllegalArgumentException{
        String onlyTag = input.replaceAll("\\s", "");
        onlyTag = onlyTag.substring(0, onlyTag.indexOf(">")); //Removes the > at the end and checks if only the tag is left,
                                                              //i.e., it has no attributes.
        if(this.tagSet.contains(onlyTag)){
            return onlyTag;
        }
        String[] tagName = input.split(" ", 2); //Extracts first word to find tag name;
        if(this.tagSet.contains(tagName[0])){
            return tagName[0];
        }
        throw new IllegalArgumentException("Can't parse this tag");
    }

    /**
     * Parses the given input to find all the attributes of the supplied tag name. A map is then created and returned
     * so that the node representing that tag can assign this returned value to its attributes map field. If tag has
     * no id attribute, one will be created for it with a null value, and will be changed later on.
     * @param input The html file being parsed starting at the current tag attributes and on.
     * @param tagName The name of the tag whose attributes we want to find.
     * @return A map of all of the attributes, including id if it didn't have one, for the tag.
     */
    private Map<String, String> attributeMap(String input, String tagName){
        Map<String, String> tagAttributes = new HashMap<>();
        Pattern attributePattern = Pattern.compile(tagName + " (.*?)>"); //Regex: tagName " " Sigma* >
        Matcher matcher = attributePattern.matcher(input);
        if(matcher.find()){
            String attributeString = matcher.group(1);
            attributeString = attributeString.replaceAll("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", ""); //Removes white space if not in quotes.

            Pattern p = Pattern.compile("(.*?)=[\"\'](.*?)[\"\']"); //Regex: Sigma* =" Sigma* "
            Matcher m = p.matcher(attributeString);
            int counter = 0; //To ensure that m.find only works
            while(m.find()){
                String attributeName = m.group(1); //group(1) is the first part of the regex, i.e. the attribute name.
                String attributeValues = m.group(2); //group(2) is the second part of the regex, i.e. the attribute value.
                if(attributeValues.contains(";")){
                    attributeValues = attributeValues.replaceAll(";", "; ");
                }

                //This if statement ensures that if a user typed any of the following as their id attribute, it gets
                //converted into a lowercase version as to ensure later on that each id is only used once.
                if(attributeName.contains("ID")){
                    attributeName = attributeName.replaceAll("ID", "id");
                }
                else if(attributeName.contains("iD")){
                    attributeName = attributeName.replaceAll("iD", "id");
                }
                else if(attributeName.contains("Id")){
                    attributeName = attributeName.replaceAll("Id", "id");
                }
                tagAttributes.put(attributeName, attributeValues);
            }
            Set<String> attributeKeys = tagAttributes.keySet();
            if(!attributeKeys.contains("id")){
                tagAttributes.put("id", null);
            }
        }
        return tagAttributes;
    }

    /**
     * Prints out the contents of the DOMTree in breath first order. Tags for each
     * node will be printed first, followed by a -> symbol to indicate the start of the
     * attribute list. Attributes are first printed, followed by their values, and then a
     * semicolon. This repeats for all attributes.
     */
    public void breathFirst(){
        breathFirstPrint(this.DOMTree);
    }

    /**
     * Prints out the contents of the DOMTree in depth first order (pre-order). Tags for each
     * node will be printed first, followed by a -> symbol to indicate the start of the
     * attribute list. Attributes are first printed, followed by their values, and then a
     * semicolon. This repeats for all attributes.
     */
    public void depthFirst(){
        depthFirstPrint(this.DOMTree);
    }

    /**
     * Pre-order traversal of fully parsed tree to to add id values for all tags that didn't supply an id value in the
     * HTML document. This method assures no duplicate id values can be assigned between any two nodes.
     * @param tree The tree whose nodes contain an id attribute in the attributes map field that have been given a value.
     */
    private void setNodeIDs(HTMLTreeNode<String> tree){
        if(tree.getAttributes().get("id") == null){
            boolean exists = true;
            while(exists) {
                if (this.preExistingIDs.contains("" + this.id)) {
                    this.id++;
                }
                else{
                    tree.getAttributes().put("id", "" + this.id);
                    this.id++;
                    exists = false;
                }
            }
        }
        for(HTMLTreeNode<String> node : tree.getChildren()){
            setNodeIDs(node);
        }
    }

    /**
     * Pre-order traversal of fully parsed tree to to check which id values were supplied in the HTML document. Values will
     * be stored in a set in order to compare against when adding id values for those nodes that don't have one.
     * @param tree The tree whose nodes contain an id attribute in the attributes map field.
     */
    private void preOrderGetIDs(HTMLTreeNode<String> tree){
        Set<String> nodeAttributes = tree.getAttributes().keySet();
        if(nodeAttributes.contains("id")){
            this.preExistingIDs.add(tree.getAttributes().get("id"));
        }
        for(HTMLTreeNode<String> node: tree.getChildren()){
            preOrderGetIDs(node);
        }
    }

    /**
     * Breath first traversal of all of the nodes in the tree. For every node, all tag names, attributes, and attribute
     * values will be printed with the help of the printData method. This method is called by the breathFirst method and
     * does all of the work.
     * @param tree The tree we are traversing.
     */
    private void breathFirstPrint(HTMLTreeNode<String> tree){
        LinkedBlockingQueue<HTMLTreeNode<String>> queue = new LinkedBlockingQueue<>();
        queue.add(tree);
        while(!queue.isEmpty()){
            HTMLTreeNode<String> current = queue.remove();
            String attributeList = "";

            printData(current);

            for(HTMLTreeNode<String> child : current.getChildren()){
                queue.add(child);
            }
        }
    }

    /**
     * Depth first traversal of all of the nodes in the tree. For every node, all tag names, attributes, and attribute
     * values will be printed with the help of the printData method. This method is called by the depthFirst method and
     * does all of the work.
     * @param tree The tree we are traversing.
     */
    private void depthFirstPrint(HTMLTreeNode<String> tree){
        printData(tree);

        for(HTMLTreeNode<String> node : tree.getChildren()){
            depthFirstPrint(node);
        }
    }

    /**
     * Helper method that actually prints the tag names and attributes, as well as attribute values of the node supplied.
     * @param node The node whose tag name and attribute name and values we want printed.
     */
    private void printData(HTMLTreeNode<String> node){
        String attributeList = "";
        for(String attribute : node.getAttributes().keySet()){
            attributeList += attribute + " = " + node.getAttributes().get(attribute) + "; ";
        }
        System.out.println("Tag: " + node.getTagName() + " -> Attributes: " + attributeList);
    }

    /**
     * Returns the htmlDoc field of a Parser object as a String.
     * @return The htmlDoc field of a Parser object as a String, or null if document has not yet been supplied.
     */
    public String getHtmlDoc() {
        return this.htmlDoc;
    }

    /**
     * Returns the DOMTree representation of the HTML document.
     * @return The DOMTree representation of the HTML document, or null if HTML document has not yet been parsed.
     */
    public HTMLTreeNode<String> getDOMTree() {
        return this.DOMTree;
    }
}