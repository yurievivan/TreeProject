package path.enumeration.tree;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author ivan.yuriev
 */
@Entity
@Table(name = "files", indexes = @Index(name = "IDX_PATH", columnList = "path"))
@DynamicUpdate
public class Files implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(length = 1000)
    private String path;
    @NotNull
    private String name;

    public Files() {

    }

    public Files(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Files{" + "id=" + id + ", path=" + path + ", name=" + name + '}';
    }

}
