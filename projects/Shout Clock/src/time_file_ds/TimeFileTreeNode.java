package time_file_ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by aviam on 8/2/2017.
 */
public class TimeFileTreeNode implements Comparable<TimeFileTreeNode>{
    private TimeFile data;
    private TimeFileTreeNode parent;
    private List<TimeFileTreeNode> children;

    public TimeFileTreeNode(TimeFileTreeNode parent, TimeFile data){
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
        addChild(this.parent, this);
    }

    public TimeFile getData() {
        return this.data;
    }

    public List<TimeFileTreeNode> getChildren() {
        return this.children;
    }

    public TimeFileTreeNode getParent() {
        return this.parent;
    }

    private void addChild(TimeFileTreeNode parent,TimeFileTreeNode child){
        if(parent == null || child == null)
            return;
        parent.getChildren().add(child);
        Collections.sort(parent.getChildren());
    }

    @Override
    public int compareTo(TimeFileTreeNode o) {
        return this.getData().toString().compareTo(o.getData().toString());
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    public static void main(String[] args) {
        TimeFile t1 = new TimeFile("test/12/01.file"), t2 = new TimeFile("test/12/02.file"), t3 = new TimeFile("test/11/58.file"),
                t4 = new TimeFile("test/custom/12 Vaders.file"), t5 = new TimeFile("test/custom/myfile.file"), t6 = new TimeFile("test/01/23.file");

        TimeFileTreeNode root = new TimeFileTreeNode(null, new TimeFile("test/"));
        TimeFileTreeNode n1 = new TimeFileTreeNode(root, t1);
        TimeFileTreeNode n2 = new TimeFileTreeNode(root, t2);
        TimeFileTreeNode n3 = new TimeFileTreeNode(root, t3);
        TimeFileTreeNode n4 = new TimeFileTreeNode(root, t4);
        TimeFileTreeNode n5 = new TimeFileTreeNode(root, t5);
        TimeFileTreeNode n6 = new TimeFileTreeNode(root, t6);

        TimeFileTree.breadthFirst(root);
    }
}
