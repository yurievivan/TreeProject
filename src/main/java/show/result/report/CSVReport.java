package show.result.report;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan.yuriev
 */
public class CSVReport extends Report {

    private static final Logger LOG = LogManager.getLogger(CSVReport.class);
    private static final char DELIMITER = ';';

    public CSVReport(List<MeasureModel> data) {
        super(data);

    }

    public CSVReport(Report report) {
        super(report.getData());
    }

    @Override
    public String generateReport() {
        List<MeasureModel> data = getData();
        if (data == null) {
            return StringUtils.EMPTY;
        }
        StringWriter sw = new StringWriter();
        try (CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.EXCEL.withDelimiter(DELIMITER).withHeader(HEADER))) {
            for (MeasureModel m : data) {
                csvPrinter.printRecord(
                        m.getDesign(),
                        m.getChildNodesTime(),
                        m.getParentNodesTime(),
                        m.getDeleteNodeTime(),
                        m.getAddNodeTime(),
                        m.getMoveNodeTime());
            }
            csvPrinter.flush();
        } catch (IOException ex) {
            LOG.error(ex);
        }
        return sw.toString();
    }

    @Override
    public String toString() {
        return String.join(StringUtils.EMPTY, StringUtils.LF, generateReport());
    }

}
