package edu.wol;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"edu.wol"})
@EnableTransactionManagement
public class JpaTestConfig {
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){

    LocalContainerEntityManagerFactoryBean lcemfb= new LocalContainerEntityManagerFactoryBean();

    lcemfb.setDataSource(this.dataSource());
    lcemfb.setPackagesToScan(new String[] {"edu.wol"});
    lcemfb.setPersistenceUnitName("MyTestPU");

    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    lcemfb.setJpaVendorAdapter(vendorAdapter);

    Properties ps = new Properties();
    ps.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    ps.put("hibernate.hbm2ddl.auto", "create");
    lcemfb.setJpaProperties(ps);

    //lcemfb.afterPropertiesSet();
    return lcemfb;

    }

    @Bean
    public DataSource dataSource(){

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");//jdbc:h2:mem:datajpa;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
        ds.setUsername("sa");
        ds.setPassword("");

        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
       JpaTransactionManager transactionManager = new JpaTransactionManager();
       transactionManager.setEntityManagerFactory(emf);
  
       return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
