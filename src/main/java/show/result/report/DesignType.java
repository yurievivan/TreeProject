package show.result.report;

/**
 *
 * @author ivan.yuriev
 */
public enum DesignType {
    ADJACENCY_LIST("Adjacency List"),
    CLOSURE_TABLE("Closure Table"),
    IMPROVED_CLOSURE_TABLE("Closure Table*"),
    NESTED_SETS("Nested Sets"),
    IMPROVED_NESTED_SETS("Nested Sets*"),
    PATH_ENUMERATION("Path Enumeration");

    private String title;

    private DesignType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

}
