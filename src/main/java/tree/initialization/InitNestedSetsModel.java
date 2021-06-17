package tree.initialization;

import java.io.File;
import java.util.Objects;
import nested.sets.tree.NestedSetsTree;

/**
 *
 * @author ivan.yuriev
 */
public class InitNestedSetsModel {

    private NestedSetsTree fileName;
    private File file;
    private long level;

    public InitNestedSetsModel(NestedSetsTree fileName, File file, long level) {
        this.fileName = fileName;
        this.file = file;
        this.level = level;
    }

    public NestedSetsTree getNestedSetsTree() {
        return fileName;
    }

    public void setNestedSetsTree(NestedSetsTree fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "InitModel{" + "fileName=" + fileName + ", file=" + file + ", level=" + level + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitNestedSetsModel initModel = (InitNestedSetsModel) o;
        return level == initModel.level &&
                fileName.equals(initModel.fileName) &&
                file.equals(initModel.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, file, level);
    }
}
