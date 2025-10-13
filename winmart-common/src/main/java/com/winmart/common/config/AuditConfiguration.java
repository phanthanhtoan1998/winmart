package com.winmart.common.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình để tạo bean AuditReader (Hibernate Envers)
 * cho Spring Boot 3 / Hibernate 6 (Jakarta).
 */
@Configuration
public class AuditConfiguration {

    private final EntityManagerFactory entityManagerFactory;

    public AuditConfiguration(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public AuditReader auditReader() {
        // Tạo AuditReader từ EntityManager (Jakarta)
        return AuditReaderFactory.get(
                entityManagerFactory.createEntityManager()
        );
    }
}
