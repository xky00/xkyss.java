//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.quarkus.platform:quarkus-bom:3.3.3@pom
//DEPS io.quarkus:quarkus-picocli
//Q:CONFIG quarkus.banner.enabled=false


package com.xkyss.exp;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import picocli.CommandLine;


@TopCommand
@CommandLine.Command(mixinStandardHelpOptions = true, subcommands = {HelloCommand.class, GoodByeCommand.class})
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