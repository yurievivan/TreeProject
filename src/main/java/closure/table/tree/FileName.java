package closure.table.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author ivan.yuriev
 */
@Entity
@Table(name = "file_name")
@DynamicUpdate
public class FileName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "descendant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TreePath> parents = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ancestor")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TreePath> children = new ArrayList<>();
    
    @Transient
    private String delimiter = "\\";
    
    public FileName(){
        
    }

    public FileName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreePath> getParents() {
        return parents;
    }

    public void setParents(List<TreePath> parents) {
        this.parents = parents;
    }

    public List<TreePath> getChildren() {
        return children;
    }

    public void setChildren(List<TreePath> children) {
        this.children = children;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void addParent(TreePath p) {
        parents.add(p);
    }

    public void addChild(TreePath ch) {
        children.add(ch);
    }

    public void addParents(List<TreePath> p) {
        parents.addAll(p);
    }

    public void addChildren(List<TreePath> ch) {
        children.addAll(ch);
    }
    
    @Override
    public String toString() {
        return "FileName{" + "id=" + id + ", name=" + name + '}';
    }
}
