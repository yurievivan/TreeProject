package util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import show.result.report.Main;

/**
 *
 * @author ivan.yuriev
 */
public enum DataStorage {
    GET_CHILDREN("getAllChildren"),
    GET_PARENTS("getAllParents"),
    INIT("init"),
    ADD("add"),
    MOVE("move"),
    DELETE("delete");
    private static final String DATA_PROPERTY_FILE = "data.properties";
    private static final Logger LOG = LogManager.getLogger(DataStorage.class);
    public static final String DELIMITER = ";";
    private String name;

    DataStorage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        Properties prop = new Properties();
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(DATA_PROPERTY_FILE)) {
            if (inputStream == null) {
                return StringUtils.EMPTY;
            }
            prop.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8.name()));
        } catch (Exception ex) {
            LOG.error(ex);
        }
        Object result = prop.getProperty(name);
        return result == null ? StringUtils.EMPTY : result.toString();
    }

}
