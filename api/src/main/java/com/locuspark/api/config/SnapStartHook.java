package com.locuspark.api.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnapStartHook implements Resource {

    @Autowired
    private HikariDataSource dataSource;

    @PostConstruct
    public void register() {
        Core.getGlobalContext().register(this);
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        // Evicta conexões antigas sem fechar o pool
        dataSource.getHikariPoolMXBean().softEvictConnections();
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        // Abre e fecha uma conexão só pra validar
        try (var conn = dataSource.getConnection()) { }
    }
}