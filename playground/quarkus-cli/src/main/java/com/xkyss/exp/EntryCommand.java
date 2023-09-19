//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.quarkus.platform:quarkus-bom:3.3.3@pom
//DEPS io.quarkus:quarkus-picocli
//Q:CONFIG quarkus.banner.enabled=false


package com.xkyss.exp;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import picocli.CommandLine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {HelloCommand.class, GoodByeCommand.class, UniCommand.class})
public class EntryCommand {
}

@CommandLine.Command(name = "hello", description = "Greet World!")
class HelloCommand implements Runnable {

    @Inject
    Logger logger;

    @Override
    public void run() {
        logger.info("Hello World!! ");
    }
}

@CommandLine.Command(name = "goodbye", description = "Say goodbye to World!")
class GoodByeCommand implements Runnable {
    @Inject
    Logger logger;

    @Override
    public void run() {
        logger.info("Goodbye World!!");
    }
}

@CommandLine.Command(name = "uni", description = "Something about Mitiny")
class UniCommand implements Runnable {
    @Inject
    Logger logger;

    @Override
    public void run() {
        logger.info("Hello Uni");
        List<Long> roles = List.of(1L, 2L);

        Set<String> perms = new HashSet<>();
        Uni.combine().all()
            .unis(roles.stream()
                .map(roleId -> selectMenuPermsByRoleId(roleId)
                    .onItem().invoke(pers -> {
                        logger.info(String.join(",", pers));
                    }))
                .collect(Collectors.toList()))
            .combinedWith(permsLists -> {
                @SuppressWarnings("unchecked")
                List<Set<String>> lists = (List<Set<String>>) permsLists;
                lists.forEach(perms::addAll);
                return perms;
            })
            .subscribe().with(ps -> {
                logger.info("All perms: ");
                logger.info(String.join(",", ps));
            });
    }

    private Uni<Set<String>> selectMenuPermsByRoleId(Long id) {
        return Uni.createFrom().item(Set.of(
            "perm-0",
            String.format("perm-%d", id),
            String.format("perm-%d%d", id, id)
        ));
    }
}