package file_viewing_management;

import time_file_ds.TimeFileTreeNode;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Created by Avi on 8/4/2017.
 */
public class FileStack {
    private Stack<List<TimeFileTreeNode>> viewableDir;

    public FileStack(TimeFileTreeNode root){
        this.viewableDir = new Stack<>();
        List<TimeFileTreeNode> stackList = new ArrayList<>();
        stackList.add(root);
        this.viewableDir.push(stackList);
    }

    public List<TimeFileTreeNode> viewCurrent(){
        return this.viewableDir.peek();
    }

    public void listChildren(TimeFileTreeNode dir){
        if(dir.getData().getFile().isDirectory()) {
            this.viewableDir.push(dir.getChildren());
        }
    }

    public boolean backDir(){
        List<TimeFileTreeNode> current = this.viewableDir.pop();
        //If we were already at the root directory
        if(peek() == null){
            this.viewableDir.push(current);
            return false;
        }
        return true;
    }

    private List<TimeFileTreeNode> peek(){
        try{
            return this.viewableDir.peek();
        } catch (EmptyStackException ese){
            return null;
        }
    }
}
