package com.xkyss.quarkus.demo.command;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(
    mixinStandardHelpOptions = true,
    subcommands = {RunSqlCommand.class})
public class EntryCommand {

}
