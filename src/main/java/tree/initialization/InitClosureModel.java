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
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InitClosureModel<?> other = (InitClosureModel<?>) obj;
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(this.parents, other.parents)) {
            return false;
        }
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InitModel{" + "fileName=" + fileName + ", parents=" + parents + ", file=" + file + '}';
    }

}
