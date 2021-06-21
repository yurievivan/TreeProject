package improved.closure.table.tree;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ivan.yuriev
 */
public class TreePathId implements Serializable {

    private long ancestor;
    private long descendant;

    public long getAncestor() {
        return ancestor;
    }

    public void setAncestor(long ancestorId) {
        this.ancestor = ancestorId;
    }

    public long getDescendant() {
        return descendant;
    }

    public void setDescendant(long descendantId) {
        this.descendant = descendantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreePathId that = (TreePathId) o;
        return ancestor == that.ancestor &&
                descendant == that.descendant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ancestor, descendant);
    }

    @Override
    public String toString() {
        return "TreePathId{" + "ancestor=" + ancestor + ", descendant=" + descendant + '}';
    }

}
