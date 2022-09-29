package com.liverussia.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.liverussia.dao.repository.roulette",
        entityManagerFactoryRef = "rouletteEntityManagerFactory",
        transactionManagerRef= "rouletteTransactionManager")
public class RouletteDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.roulette")
    public DataSourceProperties rouletteDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource rouletteDataSource() {
        return rouletteDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "rouletteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rouletteEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(rouletteDataSource())
                .packages("com.liverussia.dao.entity.roulette")
                .build();
    }

    @Bean
    public PlatformTransactionManager rouletteTransactionManager(
            final @Qualifier("rouletteEntityManagerFactory") LocalContainerEntityManagerFactoryBean bankEntityManagerFactory) {
        return new JpaTransactionManager(bankEntityManagerFactory.getObject());
    }

}
