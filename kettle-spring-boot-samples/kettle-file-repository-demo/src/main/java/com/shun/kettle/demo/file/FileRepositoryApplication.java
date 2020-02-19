package com.shun.kettle.demo.file;

import com.shun.kettle.core.KettleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author daishun
 * @since 2020/1/18
 */
@SpringBootApplication
@Slf4j
public class FileRepositoryApplication implements CommandLineRunner {
    @Resource
    private KettleRepository kettleRepository;
    @Value("${spring.kettle.file-repository.base-directory}")
    private String repositoryDir;
    @Value("${card.file-path}")
    private String cardFilePath;

    public static void main(String[] args) {
        SpringApplication.run(FileRepositoryApplication.class, args);
    }

    public void run(String... args) throws Exception {
        log.info(repositoryDir);
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardFilePath", cardFilePath);
        log.info(cardFilePath);
        kettleRepository.runTransformation("random_credit_card_no", params);
    }
}
