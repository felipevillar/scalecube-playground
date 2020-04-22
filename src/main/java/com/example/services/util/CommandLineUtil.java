package com.example.services.util;

import io.scalecube.net.Address;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommandLineUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommandLineUtil.class);

    public static Address getDiscoveryAddress(String[] args) {
        Options options = new Options();

        String discoveryAddressOpt = "discoveryAddress";
        Option input = new Option("a", discoveryAddressOpt, true, "discovery node address");
        input.setRequired(false);
        options.addOption(input);

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            String address = cmd.getOptionValue(discoveryAddressOpt);
            return address != null ? Address.from(address) : null;
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("valid options", options);
            System.exit(1);
        }

        return null;
    }
}
