package com.xkyss.quarkus.extension.codebe.deployment.devconsole;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.jboss.logging.Logger;

public class CodebeDevConsoleIntegrator implements Integrator {

    private static final Logger log = Logger.getLogger(CodebeDevConsoleIntegrator.class);

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        log.info("integrate");
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        log.info("disintegrate");
    }
}
