package adjacency.list.tree;

import util.DataStorage;
import java.io.File;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tree.TreeInitialization;

/**
 *
 * @author ivan.yuriev
 */
public class App {

    private static final Logger LOG = LogManager.getLogger(App.class);

    public static void main(String... args) {
        try {
            NodeDao dao = new NodeDao();
            File file = new File(DataStorage.INIT.getValue());
            TreeInitialization<Node> treeInit = new AdjacencyTreeInitialization(file);
            treeInit.initTree();
            dao.save(treeInit.getTree());
            List<Node> nodes = dao.getAll();
            nodes.forEach(node -> LOG.info(dao.getPath(node)));
        } finally {
            HibernateUtil.close();
        }
    }
}
