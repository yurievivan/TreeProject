package show.result.report;

import adjacency.list.tree.NodeDao;
import closure.table.tree.FileNameDao;
import closure.table.tree.TreePathDao;
import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nested.sets.tree.NestedSetsDao;
import path.enumeration.tree.FilesDao;
import tree.dao.Dao;
import tree.dao.TreeDao;
import tree.initialization.AdjacencyTreeInitialization;
import tree.initialization.ClosureTreeInitialization;
import tree.initialization.NestedSetsInitialization;
import tree.initialization.PathTreeInitialization;
import tree.initialization.TreeInitialization;

/**
 *
 * @author ivan.yuriev
 */
@SuppressWarnings("rawtypes")
public class TimeMeasurement {

    private enum TreeMeasure {
        ADJACENCY_LIST(new MeasureModel("Adjacency List"), new NodeDao()),
        CLOSURE_TABLE(new MeasureModel("Closure Table"), new FileNameDao()),
        NESTED_SETS(new MeasureModel("Nested Sets"), new NestedSetsDao()),
        PATH_ENUMERATION(new MeasureModel("Path Enumeration"), new FilesDao());
        private MeasureModel measureModel;
        private TreeDao dao;

        TreeMeasure(MeasureModel measureModel, TreeDao<?> dao) {
            this.measureModel = measureModel;
            this.dao = dao;
        }

        public MeasureModel getMeasureModel() {
            return measureModel;
        }

        public TreeDao getDao() {
            return dao;
        }

    }

    public TimeMeasurement(File folder) {
        initTree(folder);
    }

    private void initTree(File folder) {
        Map<Dao<?>, TreeInitialization<?>> treeInitMap = getTreeInitMap(folder);
        treeInitMap.forEach((k, v) -> {
            v.initTree();
            k.save((List) v.getTree());
        });
    }

    public void runAdd(String parentNodeName, File folder) {
        Map<TreeMeasure, TreeInitialization<?>> treeInitMap = getForAddInitMap(folder);
        treeInitMap.forEach((k, v) -> {
            TreeDao dao = k.getDao();
            MeasureModel measureModel = k.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            v.initTree();
            long startTime = System.nanoTime();
            dao.add(parentNode, v.getTree());
            long spentTime = System.nanoTime() - startTime;
            measureModel.setAddNodeTime(spentTime);
        });
    }

    public void runMove(String parentNodeName, String subNodeName) {
        for (TreeMeasure treeMeasure : TreeMeasure.values()) {
            TreeDao dao = treeMeasure.getDao();
            MeasureModel measureModel = treeMeasure.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            Object subNode = dao.getByName(subNodeName).get(0);
            long startTime = System.nanoTime();
            dao.move(parentNode, subNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setMoveNodeTime(spentTime);
        }
    }

    public void runGetChildren(String parentNodeName) {
        for (TreeMeasure treeMeasure : TreeMeasure.values()) {
            TreeDao dao = treeMeasure.getDao();
            MeasureModel measureModel = treeMeasure.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            long startTime = System.nanoTime();
            dao.getAllChildren(parentNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setChildNodesTime(spentTime);
        }
    }

    public void runGetParents(String nodeName) {
        for (TreeMeasure treeMeasure : TreeMeasure.values()) {
            TreeDao dao = treeMeasure.getDao();
            MeasureModel measureModel = treeMeasure.getMeasureModel();
            Object parentNode = dao.getByName(nodeName).get(0);
            long startTime = System.nanoTime();
            dao.getAllParents(parentNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setParentNodesTime(spentTime);
        }
    }

    public void runDelete(String parentNodeName) {
        for (TreeMeasure treeMeasure : TreeMeasure.values()) {
            TreeDao dao = treeMeasure.getDao();
            MeasureModel measureModel = treeMeasure.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            long startTime = System.nanoTime();
            dao.delete(parentNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setDeleteNodeTime(spentTime);
        }
    }

    public List<MeasureModel> getMeasurementResults() {
        return Stream.of(TreeMeasure.values()).map(TreeMeasure::getMeasureModel).collect(Collectors.toList());
    }

    private Map<Dao<?>, TreeInitialization<?>> getTreeInitMap(File folder) {
        Map<Dao<?>, TreeInitialization<?>> treeInitMap = new HashMap<>();
        treeInitMap.put(new NodeDao(), new AdjacencyTreeInitialization(folder));
        treeInitMap.put(new TreePathDao(), new ClosureTreeInitialization(folder));
        treeInitMap.put(new NestedSetsDao(), new NestedSetsInitialization(folder));
        treeInitMap.put(new FilesDao(), new PathTreeInitialization(folder));
        return treeInitMap;
    }

    private Map<TreeMeasure, TreeInitialization<?>> getForAddInitMap(File folder) {
        Map<TreeMeasure, TreeInitialization<?>> treeInitMap = new EnumMap<>(TreeMeasure.class);
        treeInitMap.put(TreeMeasure.ADJACENCY_LIST, new AdjacencyTreeInitialization(folder));
        treeInitMap.put(TreeMeasure.CLOSURE_TABLE, new ClosureTreeInitialization(folder));
        treeInitMap.put(TreeMeasure.NESTED_SETS, new NestedSetsInitialization(folder));
        treeInitMap.put(TreeMeasure.PATH_ENUMERATION, new PathTreeInitialization(folder));
        return treeInitMap;
    }
}
