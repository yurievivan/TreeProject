package show.result.report;

/**
 *
 * @author ivan.yuriev
 */
public class MeasureModel {

    private String design;
    private long childNodesTime;
    private long parentNodesTime;
    private long deleteNodeTime;
    private long addNodeTime;
    private long moveNodeTime;

    public MeasureModel(String design) {
        this.design = design;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
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

    @Override
    public String toString() {
        return "MeasureModel{" + "design=" + design + ", childNodesTime=" + childNodesTime + ", parentNodesTime=" + parentNodesTime + ", deleteNodeTime=" + deleteNodeTime + ", addNodeTime=" + addNodeTime + ", moveNodeTime=" + moveNodeTime + '}';
    }

}
