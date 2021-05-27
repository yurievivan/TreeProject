package closure.table.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ivan.yuriev
 */
public class InitModel {
    private FileName fileName;
    private List<FileName> parents = new ArrayList<>();
    private File file;

    public InitModel(FileName fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public FileName getFileName() {
        return fileName;
    }

    public void setFileName(FileName fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
    public void addParent(FileName p) {
        parents.add(p);
    }
        
    public void addParents(List<FileName> p) {
        parents.addAll(p);
    }
    
    
    public List<FileName> getParents() {
        return parents;
    }

    public void setParents(List<FileName> parents) {
        this.parents = parents;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.fileName);
        hash = 67 * hash + Objects.hashCode(this.parents);
        hash = 67 * hash + Objects.hashCode(this.file);
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
        final InitModel other = (InitModel) obj;
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
