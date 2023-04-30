package improved.nested.sets.tree;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author ivan.yuriev
 */
@Entity
@Table(name = "nested_sets2", indexes = {
    @Index(name = "IDX_LFT2", columnList = "lft"),
    @Index(name = "IDX_RGT2", columnList = "rgt")})
@DynamicUpdate
public class NestedSetsTree implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    @NotNull
    @Column(name = "lft")
    private long left;
    @NotNull
    @Column(name = "rgt")
    private long right;

    private int level;
    @Transient
    private String delimiter = "\\";

    public NestedSetsTree() {

    }

    public NestedSetsTree(String name, long left, long right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }

    public long getAllChildrenSize() {
        return (right - left - 1) / 2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + (int) (this.left ^ (this.left >>> 32));
        hash = 89 * hash + (int) (this.right ^ (this.right >>> 32));
        hash = 89 * hash + this.level;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NestedSetsTree that = (NestedSetsTree) o;
        return getId() == that.getId() && getLeft() == that.getLeft() && getRight() == that.getRight() && getLevel() == that.getLevel() && Objects.equals(getName(), that.getName());
    }

    @Override
    public String toString() {
        return "NestedSetsTree{" + "id=" + id + ", name=" + name + ", left=" + left + ", right=" + right + ", level=" + level + ", delimiter=" + delimiter + '}';
    }

}
