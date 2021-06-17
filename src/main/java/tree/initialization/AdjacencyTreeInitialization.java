package tree.initialization;

import adjacency.list.tree.Node;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author ivan.yuriev
 */
public class AdjacencyTreeInitialization extends TreeInitialization<Node> {

    private Deque<TreeInitialization<Node>> stack = new ArrayDeque<>();

    public AdjacencyTreeInitialization(File file) {
        super();
        if (file == null) {
            return;
        }
        setTree(Collections.singletonList(new Node(null, file.getName())));
        setFile(file);
    }

    private AdjacencyTreeInitialization(Node node, File file) {
        super(Collections.singletonList(node), file);
    }

    @Override
    public void initTree() {
        if (getFile() == null) return;        
        stack.push(this);
        while (!stack.isEmpty()) {
            TreeInitialization<Node> parentTree = stack.pop();
            Node parentNode = parentTree.getTree().get(0);
            File parentFile = parentTree.getFile();
            visit(parentNode, parentFile);
        }
    }

    private void visit(Node parentNode, File parentFile) {
        File[] files = parentFile.listFiles();
        if (files == null) return;        
        List<Node> children = parentNode.getChildren();
        for (File f : files) {
            Node child = new Node(parentNode, f.getName());
            children.add(child);
            if (f.isDirectory()) {
                TreeInitialization<Node> subTree = new AdjacencyTreeInitialization(child, f);
                stack.push(subTree);
            }
        }
    }

}
