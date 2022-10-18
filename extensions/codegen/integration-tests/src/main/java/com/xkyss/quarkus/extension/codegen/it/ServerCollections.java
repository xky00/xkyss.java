package com.xkyss.quarkus.extension.codegen.it;

import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Optional;
import java.util.Set;


//@ConfigMapping(prefix = "serverx")
public interface ServerCollections {
    Set<Environment> environments();

    interface Environment {
        String name();

        List<App> apps();

        interface App {
            String name();

            List<String> services();

            Optional<List<String>> databases();
        }
    }
}
