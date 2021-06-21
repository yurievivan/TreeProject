package tree.initialization;

import java.io.File;
import java.util.Objects;

/**
 *
 * @author ivan.yuriev
 */
public class InitNestedSetsModel<T> {

    private T fileName;
    private File file;
    private int level;

    public InitNestedSetsModel(T fileName, File file, int level) {
        this.fileName = fileName;
        this.file = file;
        this.level = level;
    }

    public T getNestedSetsTree() {
        return fileName;
    }

    public void setNestedSetsTree(T fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InitNestedSetsModel<?> that = (InitNestedSetsModel<?>) o;
        return getLevel() == that.getLevel() && Objects.equals(fileName, that.fileName) && Objects.equals(getFile(), that.getFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, file, level);
    }

    @Override
    public String toString() {
        return "InitModel{" + "fileName=" + fileName + ", file=" + file + ", level=" + level + '}';
    }
}
