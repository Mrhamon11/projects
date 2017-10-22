package time_file_ds;

import javafx.scene.control.TreeItem;

public interface TimeFileToTreeItem {
    void linkNodes(TimeFileTreeNode child, TimeFileTreeNode parent, TreeItem<String> treeItem);
}
