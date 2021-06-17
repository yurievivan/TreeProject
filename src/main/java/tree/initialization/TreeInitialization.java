package tree.initialization;

import java.io.File;
import java.util.List;

/**
 *
 * @author ivan.yuriev
 */
public abstract class TreeInitialization<T> {

    private List<T> tree;
    private File file;

    protected TreeInitialization() {

    }

    protected TreeInitialization(List<T> tree, File file) {
        this.tree = tree;
        this.file = file;
    }

    public abstract void initTree();

    public List<T> getTree() {
        return tree;
    }

    protected void setTree(List<T> tree) {
        this.tree = tree;
    }

    public File getFile() {
        return file;
    }

    protected void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "TreeInitialization{" + "tree=" + tree + ", file=" + file + '}';
    }
}
