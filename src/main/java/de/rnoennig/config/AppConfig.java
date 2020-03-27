package de.rnoennig.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = "de.rnoennig")
public class AppConfig {

    @Autowired
    private Environment env;

    public String getMapboxAccessToken() {
        return env.getProperty("mapbox_token");
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("de.rnoennig");
        sessionFactory.setHibernateProperties(hibernateProperties());
        System.out.println("------------------------------------------------------sessionFactory()----------------------------------------------------------------------");
        return sessionFactory;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "create-only");

        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.DerbyTenSevenDialect");

        hibernateProperties.setProperty(
                "hibernate.show_sql", "true");

        return hibernateProperties;
    }

    @Bean(name = "mySqlDataSource")
    @Primary
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //MySQL database we are using
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:memory:mydb;create=true");
        //dataSource.setUsername("userid");// raises Schema 'userid' does not exist
        //dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}