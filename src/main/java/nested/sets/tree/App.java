package nested.sets.tree;

import tree.initialization.NestedSetsInitialization;
import data.storage.DataStorage;
import java.io.File;
import tree.initialization.TreeInitialization;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan.yuriev
 */
public class App {

    private static final Logger LOG = LogManager.getLogger(App.class);

    public static void main(String... args) {
        try {
            NestedSetsDao dao = new NestedSetsDao();
            File file = new File(DataStorage.INIT.getValue());
            TreeInitialization<NestedSetsTree> treeInit = new NestedSetsInitialization(file);
            treeInit.initTree();
            dao.save(treeInit.getTree());
            List<NestedSetsTree> nodes = dao.getAll();
            nodes.forEach(node -> LOG.info(dao.getPath(node)));
        } finally {
            HibernateUtil.close();
        }
    }
}
