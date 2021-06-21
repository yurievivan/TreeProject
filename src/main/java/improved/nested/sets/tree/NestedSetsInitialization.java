package improved.nested.sets.tree;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import tree.initialization.TreeInitialization;
import tree.initialization.InitNestedSetsModel;

/**
 *
 * @author ivan.yuriev
 */
public class NestedSetsInitialization extends TreeInitialization<NestedSetsTree> {

    private List<NestedSetsTree> tree = new ArrayList<>();
    private ArrayDeque<InitNestedSetsModel<NestedSetsTree>> stack = new ArrayDeque<>();
    private LinkedList<NestedSetsTree> nodeLevel = new LinkedList<>();
    private File file;
    private int level = 1;
    private long counter = 0L;

    public NestedSetsInitialization(File file) {
        super();
        setFile(file);
        setTree(tree);
        this.file = file;
    }

    @Override
    public void initTree() {
        if (file == null) return;
        NestedSetsTree parent = new NestedSetsTree(file.getName(), counter, 0);
        parent.setLevel(level);
        InitNestedSetsModel<NestedSetsTree> parentNode = new InitNestedSetsModel<>(parent, file, level);
        stack.add(parentNode);
        tree.add(parent);
        nodeLevel.add(parent);
        while (!stack.isEmpty()) {
            parentNode = stack.poll();
            secondVisit(parentNode);
            ++level;
            firstVisit(parentNode);
        }
        nodeLevel.forEach(node -> node.setRight(++counter));
    }

    private void firstVisit(InitNestedSetsModel<NestedSetsTree> parentNode) {
        File parentFile = parentNode.getFile();
        File[] files = parentFile.listFiles();
        if (files == null) return;
        for (File f : files) {
            NestedSetsTree child = new NestedSetsTree(f.getName(), 0, 0);
            child.setLevel(level);
            tree.add(child);
            if (isContainChildren(f)) {
                InitNestedSetsModel<NestedSetsTree> childNode = new InitNestedSetsModel<>(child, f, level);
                nodeLevel.addFirst(child);
                stack.addFirst(childNode);
            } else {
                child.setLeft(++counter);
                child.setRight(++counter);
            }
        }
    }

    private void secondVisit(InitNestedSetsModel<NestedSetsTree> parentNode) {
        NestedSetsTree parent = parentNode.getNestedSetsTree();
        int parentLevel = parentNode.getLevel();
        if (parentLevel < level && !nodeLevel.isEmpty()) {
            NestedSetsTree subNode = nodeLevel.getFirst();
            while (!parent.equals(subNode)) {
                subNode.setRight(++counter);
                nodeLevel.removeFirst();
                level = parentLevel;
                subNode = nodeLevel.getFirst();
            }
        }
        parent.setLeft(++counter);
    }

    private boolean isContainChildren(File f) {
        File[] files = f != null ? f.listFiles() : null;
        return files != null && files.length > 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
