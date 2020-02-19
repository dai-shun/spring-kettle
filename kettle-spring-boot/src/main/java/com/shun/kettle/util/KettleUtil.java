package com.shun.kettle.util;

import com.shun.kettle.core.JdbcConfig;
import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author daishun
 * @since 2020/1/18
 */
public class KettleUtil {

    private static File kettleDirectory = null;

    private static final String KETTLE_JNDI_ROOT = "KETTLE_JNDI_ROOT";

    public static void init() throws IOException {
        if (kettleDirectory == null) {
            synchronized (KettleUtil.class) {
                if (kettleDirectory == null) {
                    File userDirectory = FileUtils.getUserDirectory();
                    kettleDirectory = FileUtils.getFile(userDirectory, ".kettle");
                    kettleDirectory.deleteOnExit();
                    FileUtils.forceMkdir(kettleDirectory);
                }
            }
        }
    }

    public static File getJdbcFile() throws IOException {
        if (kettleDirectory == null) {
            init();
        }
        File jndiDirectory = getJndiDirectory();
        File jdbcFile = FileUtils.getFile(jndiDirectory, "jdbc.properties");
        if (!jdbcFile.exists()) {
            FileUtils.touch(jdbcFile);
        }
        return jdbcFile;
    }

    private static File getJndiDirectory() throws IOException {
        if (kettleDirectory == null) {
            init();
        }
        File jndiDir = FileUtils.getFile(kettleDirectory, "jndi");
        if (!jndiDir.exists()) {
            FileUtils.forceMkdir(jndiDir);
        }
        return jndiDir;
    }

    private static String buildContent(String dbName, JdbcConfig config) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s/type=%s\n", dbName, config.getType()));
        builder.append(String.format("%s/driver=%s\n", dbName, config.getDriver()));
        builder.append(String.format("%s/url=%s\n", dbName, config.getUrl()));
        builder.append(String.format("%s/user=%s\n", dbName, config.getUser()));
        builder.append(String.format("%s/password=%s\n", dbName, config.getPassword()));
        return builder.toString();
    }

    public static void writeJdbcFile(Map<String, JdbcConfig> dbMap) throws IOException, KettleException {
        File jdbcFile = getJdbcFile();
        if (!CollectionUtils.isEmpty(dbMap)) {

            for (String dbName : dbMap.keySet()) {
                JdbcConfig config = dbMap.get(dbName);
                String jdbcContent = buildContent(dbName, config);
                FileUtils.writeStringToFile(jdbcFile, jdbcContent);
            }
        }
        System.setProperty(KETTLE_JNDI_ROOT, jdbcFile.getParentFile().getAbsolutePath());
        KettleEnvironment.init();

    }

}
