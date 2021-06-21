package tree.initialization;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ivan.yuriev
 */
public class InitClosureModel<T> {

    private T fileName;
    private List<T> parents = new ArrayList<>();
    private File file;

    public InitClosureModel(T fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public T getFileName() {
        return fileName;
    }

    public void setFileName(T fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void addParent(T p) {
        parents.add(p);
    }

    public void addParents(List<T> p) {
        parents.addAll(p);
    }

    public List<T> getParents() {
        return parents;
    }

    public void setParents(List<T> parents) {
        this.parents = parents;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.fileName);
        hash = 59 * hash + Objects.hashCode(this.parents);
        hash = 59 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitClosureModel<?> that = (InitClosureModel<?>) o;
        return Objects.equals(getFileName(), that.getFileName()) && Objects.equals(getParents(), that.getParents()) && Objects.equals(getFile(), that.getFile());
    }

    @Override
    public String toString() {
        return "InitModel{" + "fileName=" + fileName + ", parents=" + parents + ", file=" + file + '}';
    }

}
