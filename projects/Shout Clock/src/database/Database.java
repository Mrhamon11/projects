package database;

import file_viewing_management.FileStack;
import time_file_ds.TimeFile;
import time_file_ds.TimeFileTreeNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by aviam on 7/30/2017.
 */
public class Database {
    private TimeFile[][] db;
    private TimeFileTreeNode fileTree;
    private FileStack viewableDir;

    public Database(){
        Retriever retriever = new Retriever(this.db);
        this.db = retriever.populateDB();
        this.fileTree = initFileTree();
        this.viewableDir = new FileStack(this.fileTree);
    }

    private TimeFileTreeNode initFileTree(){
        TimeFile defaultParentPath = new TimeFile(DatabaseProperties.getDefaultParentPath());
        TimeFileTreeNode root = new TimeFileTreeNode(null, defaultParentPath);
        createTree(root);
        return root;
    }

    private void createTree(TimeFileTreeNode parent){
        File[] children = parent.getData().getFile().listFiles();
        if(children != null){
            for(File c : children){
                TimeFile timeFileChild = new TimeFile(c);
                TimeFileTreeNode childNode = new TimeFileTreeNode(parent, timeFileChild);
                createTree(childNode);
            }
        }
    }

    public void changeTimeFile(TimeFile newTimeFile, int hour, int minute){
        this.db[hour - 1][minute] = newTimeFile;
        new Logger(this.db).save();
    }

    public TimeFile getFileAtIndex(int hour, int minute){
        return this.db[hour - 1][minute]; //hour - 1 because hours start at 1 not 0
    }

    public List<TimeFileTreeNode> viewCurrent(){
        return viewableDir.viewCurrent();
    }

    public void listChildren(TimeFileTreeNode dir){
        this.viewableDir.listChildren(dir);
    }

    public boolean backDir(){
        return this.viewableDir.backDir();
    }

    public TimeFileTreeNode containsNameInCurrentView(String name){
        List<TimeFileTreeNode> list = viewCurrent();
        for(TimeFileTreeNode node : list){
            if(node.getData().getName().equals(name)){
                return node;
            }
        }
        return null;
    }

    public TimeFileTreeNode getFileTree() {
        return this.fileTree;
    }
}
