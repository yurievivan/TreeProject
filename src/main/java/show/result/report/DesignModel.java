package show.result.report;

import tree.dao.TreeDao;
import tree.initialization.TreeInitialization;

/**
 *
 * @author ivan.yuriev
 */
public class DesignModel {

    private MeasureModel measureModel;
    private TreeDao<?> dao;
    private TreeInitialization<?> treeInit;

    public DesignModel() {

    }

    public DesignModel(MeasureModel measureModel, TreeDao<?> dao, TreeInitialization<?> treeInit) {
        this.measureModel = measureModel;
        this.dao = dao;
        this.treeInit = treeInit;
    }

    public MeasureModel getMeasureModel() {
        return measureModel;
    }

    public void setMeasureModel(MeasureModel measureModel) {
        this.measureModel = measureModel;
    }

    public TreeDao<?> getDao() {
        return dao;
    }

    public void setDao(TreeDao<?> dao) {
        this.dao = dao;
    }

    public TreeInitialization<?> getTreeInit() {
        return treeInit;
    }

    public void setTreeInit(TreeInitialization<?> treeInit) {
        this.treeInit = treeInit;
    }

    @Override
    public String toString() {
        return "DesignModel{" + "measureModel=" + measureModel + ", dao=" + dao + ", treeInit=" + treeInit + '}';
    }

    public static class Builder {

        private DesignModel designModel;

        public Builder() {
            designModel = new DesignModel();
        }

        public Builder measureModel(MeasureModel measureModel) {
            designModel.measureModel = measureModel;
            return this;
        }

        public Builder dao(TreeDao<?> dao) {
            designModel.dao = dao;
            return this;
        }

        public Builder treeInitialization(TreeInitialization<?> treeInit) {
            designModel.treeInit = treeInit;
            return this;
        }

        public DesignModel build() {
            return designModel;
        }
    }
}
