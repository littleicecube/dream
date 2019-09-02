package com.dream.web.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidConfig {
	    /**
	     * dao层的包路径
	     */
	    static final String PACKAGE = "com.bi.venus.reporter.bigdata.dao";

	    /**
	     * mapper文件的相对路径
	     */
	    private static final String MAPPER_LOCATION = "classpath:mapper/mysql/*.xml";

	    private String filters;
	    private String url;
	    private String username;
	    private String password;
	    private String driverClassName;
	    private int initialSize;
	    private int minIdle;
	    private int maxActive;
	    private long maxWait;
	    private long timeBetweenEvictionRunsMillis;
	    private long minEvictableIdleTimeMillis;
	    private String validationQuery;
	    private boolean testWhileIdle;
	    private boolean testOnBorrow;
	    private boolean testOnReturn;
	    private boolean poolPreparedStatements;
	    private int maxPoolPreparedStatementPerConnectionSize;
	    private int maxOpenPreparedStatements;

	    // 主数据源使用@Primary注解进行标识
	    @Bean(name = "bigDataDataSource")
	    public DataSource bigDataDataSource() throws SQLException {
	        DruidDataSource druid = new DruidDataSource();
	        // 监控统计拦截的filters
	        druid.setFilters(filters);

	        // 配置基本属性
	        druid.setDriverClassName(driverClassName);
	        druid.setUsername(username);
	        druid.setPassword(password);
	        druid.setUrl(url);

	        //初始化时建立物理连接的个数
	        druid.setInitialSize(initialSize);
	        //最大连接池数量
	        druid.setMaxActive(maxActive);
	        //最小连接池数量
	        druid.setMinIdle(minIdle);
	        //获取连接时最大等待时间，单位毫秒。
	        druid.setMaxWait(maxWait);
	        //间隔多久进行一次检测，检测需要关闭的空闲连接
	        druid.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	        //一个连接在池中最小生存的时间
	        druid.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	        //用来检测连接是否有效的sql
	        druid.setValidationQuery(validationQuery);
	        //建议配置为true，不影响性能，并且保证安全性。
	        druid.setTestWhileIdle(testWhileIdle);
	        //申请连接时执行validationQuery检测连接是否有效
	        druid.setTestOnBorrow(testOnBorrow);
	        druid.setTestOnReturn(testOnReturn);
	        //是否缓存preparedStatement，也就是PSCache，oracle设为true，mysql设为false。分库分表较多推荐设置为false
	        druid.setPoolPreparedStatements(poolPreparedStatements);
	        // 打开PSCache时，指定每个连接上PSCache的大小
	        druid.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
	        druid.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
	        return druid;
	    }

	    // 创建该数据源的事务管理
	    @Bean(name = "bigDataTransactionManager")
	    @Primary
	    public DataSourceTransactionManager primaryTransactionManager() throws SQLException {
	        return new DataSourceTransactionManager(bigDataDataSource());
	    }

	}

