package improved.closure.table.tree;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import tree.initialization.InitClosureModel;
import tree.initialization.TreeInitialization;

/**
 *
 * @author ivan.yuriev
 */
public class ClosureTreeInitialization extends TreeInitialization<FileName> {

    private Deque<InitClosureModel<FileName>> stack = new ArrayDeque<>();
    private InitClosureModel<FileName> parentModel;
    private int initialLevel = 1;

    public ClosureTreeInitialization(File file) {
        super();
        if (file == null) {
            return;
        }
        setFile(file);
        FileName parent = new FileName(file.getName(), initialLevel);
        parent.addParent(new TreePath(parent, parent));
        setTree(Collections.singletonList(parent));
        parentModel = new InitClosureModel<>(parent, file);
    }

    @Override
    public void initTree() {
        if (parentModel == null) {
            return;
        }
        stack.push(parentModel);
        while (!stack.isEmpty()) {
            parentModel = stack.pop();
            FileName parentNode = parentModel.getFileName();
            File parentFile = parentModel.getFile();
            visit(parentNode, parentFile);
        }
    }

    private void visit(FileName parentNode, File parentFile) {
        File[] files = parentFile.listFiles();
        if (files == null) {
            return;
        }
        
        int level = parentNode.getLevel() + 1;
        
        for (File f : files) {
            FileName child = new FileName(f.getName(), level);
            addParents(parentNode, child);
            addChildren(parentNode, child);
            if (f.isDirectory()) {
                InitClosureModel<FileName> childNode = new InitClosureModel<>(child, f);
                addParents(parentNode, childNode);
                stack.push(childNode);
            }
        }
    }

    private void addChildren(FileName parent, FileName child) {
        parent.addChild(new TreePath(parent, child));
        parentModel.getParents().forEach(f -> f.addChild(new TreePath(f, child)));
    }

    private void addParents(FileName parent, FileName child) {
        child.addParent(new TreePath(child, child));
        child.addParents(parent.getParents());
    }

    private void addParents(FileName parent, InitClosureModel<FileName> child) {
        child.addParent(parent);
        child.addParents(parentModel.getParents());
    }

    public int getLevel() {
        return initialLevel;
    }

    public void setLevel(int initialLevel) {
        this.initialLevel = initialLevel;
    }

}
