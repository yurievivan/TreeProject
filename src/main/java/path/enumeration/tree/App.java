package path.enumeration.tree;

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
            FilesDao dao = new FilesDao();
            File file = new File(DataStorage.INIT.getValue());
            TreeInitialization<Files> treeInit = new PathTreeInitialization(file);
            treeInit.initTree();
            dao.save(treeInit.getTree());
            List<Files> nodes = dao.getAll();
            nodes.forEach(node -> LOG.info(node.getPath()));
        } finally {
            HibernateUtil.close();
        }
    }
}
