package show.result.report;

/**
 *
 * @author ivan.yuriev
 */
public class MeasureModel {

    private DesignType designType;
    private long childNodesTime;
    private long parentNodesTime;
    private long deleteNodeTime;
    private long addNodeTime;
    private long moveNodeTime;

    public MeasureModel(DesignType designType) {
        this.designType = designType;
    }

    public long getChildNodesTime() {
        return childNodesTime;
    }

    public void setChildNodesTime(long childNodesTime) {
        this.childNodesTime = childNodesTime;
    }

    public long getParentNodesTime() {
        return parentNodesTime;
    }

    public void setParentNodesTime(long parentNodesTime) {
        this.parentNodesTime = parentNodesTime;
    }

    public long getDeleteNodeTime() {
        return deleteNodeTime;
    }

    public void setDeleteNodeTime(long deleteNodeTime) {
        this.deleteNodeTime = deleteNodeTime;
    }

    public long getAddNodeTime() {
        return addNodeTime;
    }

    public void setAddNodeTime(long addNodeTime) {
        this.addNodeTime = addNodeTime;
    }

    public long getMoveNodeTime() {
        return moveNodeTime;
    }

    public void setMoveNodeTime(long moveNodeTime) {
        this.moveNodeTime = moveNodeTime;
    }

    public DesignType getDesignType() {
        return designType;
    }

    public void setDesignType(DesignType designType) {
        this.designType = designType;
    }

    @Override
    public String toString() {
        return "MeasureModel{" + "designType=" + designType + ", childNodesTime=" + childNodesTime + ", parentNodesTime=" + parentNodesTime + ", deleteNodeTime=" + deleteNodeTime + ", addNodeTime=" + addNodeTime + ", moveNodeTime=" + moveNodeTime + '}';
    }
}
