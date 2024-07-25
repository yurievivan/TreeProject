package show.result.report;

import data.storage.DataStorage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

            File dir = new File(DataStorage.INIT.getValue());
            File dirAdd = new File(DataStorage.ADD.getValue());
            String root = dir.getName();
            List<Path> paths = new ArrayList<>();
            goRecursive(dir, paths);
            Optional<Path> deepestFile = paths.stream().filter(Files::isRegularFile).max(Comparator.comparing(Path::getNameCount));
            Optional<Path> dirFirstLevel = Arrays.stream(dir.listFiles()).map(f -> f.toPath()).filter(Files::isDirectory).findFirst();
            Optional<Path> dirForMove = paths.stream().filter(Files::isDirectory).filter(p -> !p.getFileName().equals(dirFirstLevel.get().getFileName())).findFirst();

            String deepestFileName = deepestFile.get().getFileName().toString();
            String dirFirstLevelName = dirFirstLevel.get().getFileName().toString();
            String dirForMoveName = dirForMove.get().getFileName().toString();

            TimeMeasurement tm = new TimeMeasurement(dir);
            tm.initTree();
            tm.runGetChildren(root);
            tm.runGetParents(deepestFileName);
            tm.runMove(dirFirstLevelName, dirForMoveName);
            tm.runAdd(dirFirstLevelName, dirAdd);
            tm.runDelete(root);
            Report ct = new ConsoleReport(tm.getMeasurementResults());
            LOG.info(ct);
        } finally {
            close();
        }
    }

    private static void goRecursive(File dir, List<Path> result) {
        File[] files = dir.listFiles();
        if (dir.isDirectory() && files != null && files.length != 0) {
            result.addAll(Arrays.stream(files).map(f -> f.toPath()).collect(Collectors.toList()));
            for (File subdir : dir.listFiles()) {
                goRecursive(subdir, result);
            }
        }
    }

    private static void close() {
        adjacency.list.tree.HibernateUtil.close();
        closure.table.tree.HibernateUtil.close();
        improved.closure.table.tree.HibernateUtil.close();
        nested.sets.tree.HibernateUtil.close();
        improved.nested.sets.tree.HibernateUtil.close();
        path.enumeration.tree.HibernateUtil.close();
    }
}