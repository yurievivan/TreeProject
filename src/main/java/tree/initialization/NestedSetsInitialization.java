package tree.initialization;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import nested.sets.tree.NestedSetsTree;

/**
 *
 * @author ivan.yuriev
 */
public class NestedSetsInitialization extends TreeInitialization<NestedSetsTree> {

    private List<NestedSetsTree> tree = new ArrayList<>();
    private ArrayDeque<InitNestedSetsModel> stack = new ArrayDeque<>();
    private LinkedList<NestedSetsTree> nodeLevel = new LinkedList<>();
    private File file;
    private long level = 1L;
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
        InitNestedSetsModel parentNode = new InitNestedSetsModel(parent, file, level);
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

    private void firstVisit(InitNestedSetsModel parentNode) {
        File parentFile = parentNode.getFile();
        File[] files = parentFile.listFiles();
        if (files == null) return;
        for (File f : files) {
            NestedSetsTree child = new NestedSetsTree(f.getName(), 0, 0);
            tree.add(child);
            if (isContainChildren(f)) {
                InitNestedSetsModel childNode = new InitNestedSetsModel(child, f, level);
                nodeLevel.addFirst(child);
                stack.addFirst(childNode);
            } else {
                child.setLeft(++counter);
                child.setRight(++counter);
            }
        }
    }

    private void secondVisit(InitNestedSetsModel parentNode) {
        NestedSetsTree parent = parentNode.getNestedSetsTree();
        long parentLevel = parentNode.getLevel();
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
}
