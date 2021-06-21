package show.result.report;

import adjacency.list.tree.NodeDao;
import closure.table.tree.FileNameDao;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nested.sets.tree.NestedSetsDao;
import path.enumeration.tree.FilesDao;
import tree.initialization.AdjacencyTreeInitialization;
import tree.initialization.ClosureTreeInitialization;
import tree.initialization.NestedSetsInitialization;
import tree.initialization.PathTreeInitialization;

/**
 *
 * @author ivan.yuriev
 */
public class DesignFactory {

    private File folder;

    public DesignFactory(File folder) {
        this.folder = folder;
    }

    public DesignModel getDesignModel(DesignType type) {
        DesignModel toReturn = null;
        MeasureModel measureModel = new MeasureModel(type);
        switch (type) {
            case ADJACENCY_LIST:
                toReturn = new DesignModel.Builder()
                        .measureModel(measureModel)
                        .dao(new NodeDao())
                        .treeInitialization(new AdjacencyTreeInitialization(folder))
                        .build();
                break;
            case CLOSURE_TABLE:
                toReturn = new DesignModel.Builder()
                        .measureModel(measureModel)
                        .dao(new FileNameDao())
                        .treeInitialization(new ClosureTreeInitialization(folder))
                        .build();
                break;
            case IMPROVED_CLOSURE_TABLE:
                toReturn = new DesignModel.Builder()
                        .measureModel(measureModel)
                        .dao(new improved.closure.table.tree.FileNameDao())
                        .treeInitialization(new improved.closure.table.tree.ClosureTreeInitialization(folder))
                        .build();
                break;
            case NESTED_SETS:
                toReturn = new DesignModel.Builder()
                        .measureModel(measureModel)
                        .dao(new NestedSetsDao())
                        .treeInitialization(new NestedSetsInitialization(folder))
                        .build();
                break;
            case IMPROVED_NESTED_SETS:
                toReturn = new DesignModel.Builder()
                        .measureModel(measureModel)
                        .dao(new improved.nested.sets.tree.NestedSetsDao())
                        .treeInitialization(new improved.nested.sets.tree.NestedSetsInitialization(folder))
                        .build();
                break;
            case PATH_ENUMERATION:
                toReturn = new DesignModel.Builder()
                        .measureModel(measureModel)
                        .dao(new FilesDao())
                        .treeInitialization(new PathTreeInitialization(folder))
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Wrong design type:" + type);
        }
        return toReturn;
    }

    public List<DesignModel> getAllDesignModels() {
        return Stream.of(DesignType.values()).map(this::getDesignModel).collect(Collectors.toList());
    }
}
