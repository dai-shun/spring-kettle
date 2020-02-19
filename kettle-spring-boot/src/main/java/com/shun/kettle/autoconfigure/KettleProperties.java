package com.shun.kettle.autoconfigure;

import com.shun.kettle.core.JdbcConfig;
import lombok.Data;
import org.pentaho.di.core.logging.LogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author daishun
 * @since 2020/1/17
 */
@ConfigurationProperties(prefix = "spring.kettle")
@Data
public class KettleProperties {

    private boolean enable = true;

    private LogLevel logLevel = LogLevel.BASIC;

    private RepositoryType repositoryType;

    private FileRepositoryConfig fileRepository;

    private DatabaseRepositoryConfig databaseRepository;

    private Map<String, JdbcConfig> datasource;


    @Data
    public static class FileRepositoryConfig{
        private String baseDirectory;

        private String name;

        private String description;

        private boolean readOnly = true;

        private boolean hidingHiddenFiles = true;
    }

    @Data
    public static class DatabaseRepositoryConfig{

    }

    public enum RepositoryType {
        FILE,
        DATABASE
    }

}
