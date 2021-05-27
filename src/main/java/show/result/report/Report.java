package show.result.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan.yuriev
 */
public abstract class Report {

    private static final Logger LOG = LogManager.getLogger(Report.class);

    protected static final String[] HEADER = {"Design", "Get Children", "Get Parents", "Delete Node", "Add Node", "Move Node"};
    private List<MeasureModel> data;

    protected Report(List<MeasureModel> data) {
        this.data = data;
    }

    protected List<MeasureModel> getData() {
        return data;
    }

    protected void setData(List<MeasureModel> data) {
        this.data = data;
    }

    protected abstract String generateReport();

    public void toFile(String path) {
        try {
            String report = generateReport();
            Files.write(Paths.get(path), report.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }

}
