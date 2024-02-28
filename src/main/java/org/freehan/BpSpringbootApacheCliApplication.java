package org.freehan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.freehan.conf.Configure;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class BpSpringbootApacheCliApplication implements CommandLineRunner {

    private final Configure configure;

    public static void main(String[] args) {
        SpringApplication.run(BpSpringbootApacheCliApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                printHelpAndExit(0, options);
            }
            if (cmd.hasOption("m")) {
                configure.setRemoteHost(cmd.getOptionValue("m", "127.0.0.1"));
            }
            if (cmd.hasOption("port")) {
                configure.setRemotePort(Integer.parseInt(cmd.getOptionValue("port")));
            }
            if (cmd.hasOption("command")) {
                String command = cmd.getOptionValue("command").toLowerCase(Locale.ROOT);
                if (Arrays.asList(configure.getCommandList()).contains(command)) {
                    configure.setCommand(command);
                } else {
                    throw new IllegalArgumentException(String.format("%s is not defined.", command));
                }
            } else {
                printHelpAndExit(1, options);
            }

            log.info("remote {}:{} execute command {}", configure.getRemoteHost(), configure.getRemotePort(), configure.getCommand());
        } catch (ParseException | IllegalArgumentException e) {
            log.error(e.getMessage());
            printHelpAndExit(1, options);
        }
    }

    private void printHelpAndExit(int exitCode, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("application.jar -m <host> -p <port> -c <command> ", options);
        System.exit(exitCode);
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption(new Option("h", "help", false, "print this message"));
        options.addOption(new Option("m", true, "remote host"));
        options.addOption(new Option("p", "port", true, "remote port"));

        String commandDesc = String.join("|", configure.getCommandList());
        options.addOption(new Option("c", "command", true, commandDesc));
        return options;
    }
}
