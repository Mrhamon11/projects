package time_file_ds;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by aviam on 8/3/2017.
 */
public class TimeFileTree {
    private TimeFileTreeNode root;

//    public TimeFileTree(TimeFileTreeNode root){
//        this.root = root;
//    }

//    public TimeFileTreeNode getRoot() {
//        return this.root;
//    }

    public static int getDepth(TimeFileTreeNode node){
        if(node.getParent() == null){
            return 0;
        }
        return 1 + getDepth(node.getParent());
    }

    public static int getHeight(TimeFileTreeNode node){
        int height = 0;
        for(TimeFileTreeNode child : node.getChildren()){
            height = Math.max(height, 1 + getHeight(child));
        }
        return height;
    }

    public static void breadthFirst(TimeFileTreeNode node){
        LinkedBlockingQueue<TimeFileTreeNode> queue = new LinkedBlockingQueue<>();
        queue.add(node);
        while(!queue.isEmpty()){
            TimeFileTreeNode current = queue.remove();
            //TODO Do Action Here
            System.out.println(current.getData());
            for(TimeFileTreeNode child : current.getChildren()){
                queue.add(child);
            }
        }
    }


}
