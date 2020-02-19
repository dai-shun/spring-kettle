package com.shun.kettle.core;

import lombok.Data;

/**
 * @author daishun
 * @since 2020/1/18
 */
@Data
public class JdbcConfig {

    private String type;

    private String driver;

    private String url;

    private String user;

    private String password;
}
