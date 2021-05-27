package show.result.report;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ivan.yuriev
 */
public class ConsoleReport extends Report {

    private static final String TABLE_DELIMITER = "+------------------+------------------+------------------+------------------+------------------+------------------+\n";
    private static final String TABLE_FORMAT = "| %-16s | %-16d | %-16d | %-16d | %-16d | %-16d |%n";
    private static final String HEADER_FORMAT = "| %-17s";

    public ConsoleReport(List<MeasureModel> data) {
        super(data);
    }

    public ConsoleReport(Report report) {
        super(report.getData());
    }

    @Override
    public String generateReport() {
        List<MeasureModel> data = getData();
        if (data == null) {
            return StringUtils.EMPTY;
        }
        StringJoiner table = new StringJoiner(StringUtils.EMPTY);
        StringJoiner header = new StringJoiner(StringUtils.EMPTY, StringUtils.EMPTY, "|\n");
        table.add(TABLE_DELIMITER);
        Arrays.asList(HEADER).forEach(name -> header.add(String.format(HEADER_FORMAT, name)));
        table.add(header.toString());
        table.add(TABLE_DELIMITER);

        for (MeasureModel d : data) {
            table.add(String.format(TABLE_FORMAT,
                    d.getDesign(),
                    d.getChildNodesTime(),
                    d.getParentNodesTime(),
                    d.getDeleteNodeTime(),
                    d.getAddNodeTime(),
                    d.getMoveNodeTime()));
            table.add(TABLE_DELIMITER);
        }
        return table.toString();
    }

    @Override
    public String toString() {
        return String.join(StringUtils.EMPTY, StringUtils.LF, generateReport());
    }

}
