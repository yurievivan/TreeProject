package improved.closure.table.tree;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
