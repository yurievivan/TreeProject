package path.enumeration.tree;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import tree.TreeInitialization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan.yuriev
 */
public class PathTreeInitialization extends TreeInitialization<Files> {
    private static final Logger LOG = LogManager.getLogger(PathTreeInitialization.class);
    private File file;
    private List<Files> tree = new ArrayList<>();

    public PathTreeInitialization(File file) {
        super();
        setFile(file);
        setTree(tree);
        this.file = file;
    }

    @Override
    public void initTree() {
        if (file == null) return;
        
        try {
            java.nio.file.Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    tree.add(new Files(getPath(file.toFile()), file.getFileName().toString()));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException e) {
                    tree.add(new Files(getPath(file.toFile()), file.getFileName().toString()));
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    tree.add(new Files(getPath(dir.toFile()), dir.getFileName().toString()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }

    private String getPath(File someFile) {
        String parentPath = String.join(StringUtils.EMPTY, file.getParentFile().getPath(), File.separator);
        String filePath = someFile.getPath();
        return filePath.replaceFirst(Pattern.quote(parentPath), StringUtils.EMPTY);
    }

}
