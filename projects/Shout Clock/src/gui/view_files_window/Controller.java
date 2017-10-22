package gui.view_files_window;

import gui.util.WindowControlls;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import time_file_ds.TimeFileTree;
import time_file_ds.TimeFileTreeNode;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TreeView<String> tree;


    public void back(){
        WindowControlls.returnToMainWindow();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tree.setShowRoot(false);
        TreeItem<String> root = createTree();
        this.tree.setRoot(root);

        tree = new TreeView<>(root);
        tree.setShowRoot(false);
    }

    private TreeItem<String> createTree(){
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(false);
        TimeFileTreeNode rootNode = ViewFilesWindow.getDb().getFileTree();

        depthFirst(rootNode, root);

//        bucky = makeBranch("Bucky", root);
//        makeBranch("thenewboston", bucky);
//        makeBranch("YouTube", bucky);
//        makeBranch("Chicken", bucky);
//
//        //Megan
//        megan = makeBranch("Megan", root);
//        makeBranch("Glitter", megan);
//        makeBranch("Makeup", megan);

        return root;
    }

    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
        return item;
    }

    public void depthFirst(TimeFileTreeNode node, TreeItem<String> treeItem){
        for(TimeFileTreeNode child : node.getChildren()){
            TreeItem<String> newItem = makeBranch(child.toString(), treeItem);
            depthFirst(child, newItem);
        }
    }

    public void playFile(){

    }

    public void setFileToTime(){
        System.out.println(this.tree.getSelectionModel().getSelectedItem());
    }
}
