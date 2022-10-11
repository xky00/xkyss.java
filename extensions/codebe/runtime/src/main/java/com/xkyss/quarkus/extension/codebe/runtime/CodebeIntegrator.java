package com.xkyss.quarkus.extension.codebe.runtime;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.jboss.logging.Logger;

public class CodebeIntegrator implements Integrator {

    private static final Logger log = Logger.getLogger(CodebeIntegrator.class);

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        log.info("integrate");
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        log.info("disintegrate");
    }
}