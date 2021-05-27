package closure.table.tree;

import java.io.File;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tree.TreeInitialization;
import util.DataStorage;

/**
 *
 * @author ivan.yuriev
 */
public class App {

    private static final Logger LOG = LogManager.getLogger(App.class);

    public static void main(String... args) {
        File file = new File(DataStorage.INIT.getValue());
        FileNameDao dao = new FileNameDao();
        TreeInitialization<FileName> treeInit = new ClosureTreeInitialization(file);
        treeInit.initTree();
        dao.save(treeInit.getTree());
        List<FileName> nodes = dao.getAll();
        nodes.forEach(node -> LOG.info(dao.getPath(node)));
    }
}
