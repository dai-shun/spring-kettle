package com.shun.kettle.autoconfigure;

import com.shun.kettle.core.KettleRepository;
import com.shun.kettle.util.KettleUtil;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * @author daishun
 * @since 2020/1/17
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KettleProperties.class)
@ConditionalOnProperty(name = "spring.kettle.enable", havingValue = "true", matchIfMissing = true)
public class KettleAutoConfiguration {

    private KettleProperties kettleProperties;

    public KettleAutoConfiguration(KettleProperties kettleProperties) {
        this.kettleProperties = kettleProperties;
    }

    @ConditionalOnProperty(name = "spring.kettle.repository-type", havingValue = "file", matchIfMissing = true)
    @Bean
    public KettleFileRepositoryMeta kettleFileRepositoryMeta() {
        KettleProperties.FileRepositoryConfig config = this.kettleProperties.getFileRepository();
        KettleFileRepositoryMeta meta = new KettleFileRepositoryMeta();
        meta.setName(config.getName());
        meta.setDefault(true);
        meta.setBaseDirectory(config.getBaseDirectory());
        meta.setHidingHiddenFiles(config.isHidingHiddenFiles());
        meta.setReadOnly(config.isReadOnly());
        meta.setDescription(config.getDescription());
        return meta;
    }

    @ConditionalOnProperty(name = "spring.kettle.repository-type", havingValue = "file", matchIfMissing = true)
    @Bean
    public Repository kettleFileRepository(@Autowired KettleFileRepositoryMeta kettleFileRepositoryMeta) {
        Repository repository = new KettleFileRepository();
        repository.init(kettleFileRepositoryMeta);
        return repository;
    }

    @Bean
    @ConditionalOnMissingBean(KettleRepository.class)
    public KettleRepository repository(@Autowired Repository repository) throws IOException, KettleException {
        KettleUtil.writeJdbcFile(this.kettleProperties.getDatasource());
        KettleRepository kettleRepository = new KettleRepository();
        kettleRepository.setLogLevel(this.kettleProperties.getLogLevel());
        kettleRepository.setRepository(repository);
        return kettleRepository;
    }

}
