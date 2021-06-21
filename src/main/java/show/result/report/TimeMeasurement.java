package show.result.report;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import tree.dao.TreeDao;
import tree.initialization.TreeInitialization;

/**
 *
 * @author ivan.yuriev
 */
@SuppressWarnings("rawtypes")
public class TimeMeasurement {

    private List<DesignModel> allDesignModels;
    private Map<DesignType, MeasureModel> mapMeasureModel;

    public TimeMeasurement(File folder) {
        DesignFactory factory = new DesignFactory(folder);
        allDesignModels = factory.getAllDesignModels();
    }

    public void initTree() {
        allDesignModels.forEach(dm -> {
            TreeDao dao = dm.getDao();
            TreeInitialization<?> ti = dm.getTreeInit();
            ti.initTree();
            dao.save((List) ti.getTree());
        });
    }

    public void runAdd(String parentNodeName, File folder) {
        DesignFactory factory = new DesignFactory(folder);
        List<DesignModel> designModels = factory.getAllDesignModels();
        designModels.forEach(dm -> {
            TreeDao dao = dm.getDao();
            TreeInitialization<?> ti = dm.getTreeInit();
            DesignType designType = dm.getMeasureModel().getDesignType();
            MeasureModel measureModel = getMeasureModelByDesignType(designType);
            Object parentNode = dao.getByName(parentNodeName).get(0);
            ti.initTree();
            long startTime = System.nanoTime();
            dao.add(parentNode, ti.getTree());
            long spentTime = System.nanoTime() - startTime;
            measureModel.setAddNodeTime(spentTime);
        });
    }

    public void runMove(String parentNodeName, String subNodeName) {
        allDesignModels.forEach(dm -> {
            TreeDao dao = dm.getDao();
            MeasureModel measureModel = dm.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            Object subNode = dao.getByName(subNodeName).get(0);
            long startTime = System.nanoTime();
            dao.move(parentNode, subNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setMoveNodeTime(spentTime);
        });
    }

    public void runGetChildren(String parentNodeName) {
        allDesignModels.forEach(dm -> {
            TreeDao dao = dm.getDao();
            MeasureModel measureModel = dm.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            long startTime = System.nanoTime();
            dao.getAllChildren(parentNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setChildNodesTime(spentTime);
        });
    }

    public void runGetParents(String nodeName) {
        allDesignModels.forEach(dm -> {
            TreeDao dao = dm.getDao();
            MeasureModel measureModel = dm.getMeasureModel();
            Object parentNode = dao.getByName(nodeName).get(0);
            long startTime = System.nanoTime();
            dao.getAllParents(parentNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setParentNodesTime(spentTime);
        });
    }

    public void runDelete(String parentNodeName) {
        allDesignModels.forEach(dm -> {
            TreeDao dao = dm.getDao();
            MeasureModel measureModel = dm.getMeasureModel();
            Object parentNode = dao.getByName(parentNodeName).get(0);
            long startTime = System.nanoTime();
            dao.delete(parentNode);
            long spentTime = System.nanoTime() - startTime;
            measureModel.setDeleteNodeTime(spentTime);
        });
    }

    public List<MeasureModel> getMeasurementResults() {
        return allDesignModels.stream().map(DesignModel::getMeasureModel).collect(Collectors.toList());
    }

    private MeasureModel getMeasureModelByDesignType(DesignType type) {
        if (MapUtils.isEmpty(mapMeasureModel)) {
            mapMeasureModel = allDesignModels
                    .stream()
                    .map(DesignModel::getMeasureModel)
                    .collect(Collectors.toMap(MeasureModel::getDesignType, Function.identity()));
        }
        return mapMeasureModel.get(type);
    }
}
