package show.result.report;

import util.DataStorage;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan.yuriev
 */
public class Main {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String... args) {
        try {
            File folder = new File(DataStorage.INIT.getValue());
            TimeMeasurement tm = new TimeMeasurement(folder);
            tm.runGetChildren(DataStorage.GET_CHILDREN.getValue());
            tm.runGetParents(DataStorage.GET_PARENTS.getValue());
            runMove(tm);
            runAdd(tm);
            tm.runDelete(DataStorage.DELETE.getValue());
            Report ct = new ConsoleReport(tm.getMeasurementResults());
            LOG.info(ct);
        } finally {
            close();
        }
    }

    private static void runMove(TimeMeasurement tm) {
        String moveProp = DataStorage.MOVE.getValue();
        String moveParentNodeName = moveProp.split(DataStorage.DELIMITER)[0];
        String moveSubNodeName = moveProp.split(DataStorage.DELIMITER)[1];
        tm.runMove(moveParentNodeName, moveSubNodeName);
    }

    private static void runAdd(TimeMeasurement tm) {
        String addProp = DataStorage.ADD.getValue();
        String addNodeName = addProp.split(DataStorage.DELIMITER)[0];
        File addFolder = new File(addProp.split(DataStorage.DELIMITER)[1]);
        tm.runAdd(addNodeName, addFolder);
    }

    private static void close() {
        adjacency.list.tree.HibernateUtil.close();
        closure.table.tree.HibernateUtil.close();
        nested.sets.tree.HibernateUtil.close();
        path.enumeration.tree.HibernateUtil.close();
    }
}
