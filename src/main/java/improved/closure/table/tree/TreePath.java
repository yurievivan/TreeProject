package improved.closure.table.tree;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author ivan.yuriev
 */
@Entity
@Table(name = "tree_path2", indexes = {
    @Index(name = "IDX_ANCESTOR2", columnList = "ancestor"),
    @Index(name = "IDX_DESCENDANT2", columnList = "descendant")})
@DynamicUpdate
@IdClass(TreePathId.class)
public class TreePath implements Serializable {

    @Id
    @ManyToOne(targetEntity = FileName.class)
    @JoinColumn(name = "ancestor", nullable = false, foreignKey = @ForeignKey(name = "FK_ANCESTOR"))
    private FileName ancestor;

    @Id
    @ManyToOne(targetEntity = FileName.class)
    @JoinColumn(name = "descendant", nullable = false, foreignKey = @ForeignKey(name = "FK_DESCENDANT"))
    private FileName descendant;

    public TreePath() {

    }

    public TreePath(FileName ancestor, FileName descendant) {
        this.ancestor = ancestor;
        this.descendant = descendant;
    }

    public FileName getAncestor() {
        return ancestor;
    }

    public void setAncestor(FileName ancestor) {
        this.ancestor = ancestor;
    }

    public FileName getDescendant() {
        return descendant;
    }

    public void setDescendant(FileName descendant) {
        this.descendant = descendant;
    }

    @Override
    public String toString() {
        return "TreePath{" + "ancestor=" + ancestor + ", descendant=" + descendant + '}';
    }
}
