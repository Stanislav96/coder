package com.company;

import org.apache.commons.cli.*;

import java.io.File;

public class Main {

  public static void main(String[] args) {
    final Options options = new Options();
    Option option = new Option("i", true, "Input File");
    option.setArgs(1);
    option.setArgName("input file");
    options.addOption(option);
    option = new Option("o", true, "Output File");
    option.setArgs(1);
    option.setArgName("output file");
    options.addOption(option);
    option = new Option("encode", false, "Input file should be encoded");
    option.setArgs(0);
    option.setArgName("encode");
    options.addOption(option);
    option = new Option("decode", false, "Input file should be decoded");
    option.setArgs(0);
    option.setArgName("decode");
    options.addOption(option);
    final CommandLineParser parser = new DefaultParser();
    try {
      final CommandLine line = parser.parse(options, args);
      if (!line.hasOption("i") || !line.hasOption("o") || line.hasOption("encode") == line.hasOption("decode")) {
        new HelpFormatter().printHelp("java coder.jar", options, true);
      }
      else if (line.hasOption("encode")) {
        Encoder.encode(new File(line.getOptionValue("i")), new File(line.getOptionValue("o")));
      }
      else {
        Decoder.decode(new File(line.getOptionValue("i")), new File(line.getOptionValue("o")));
      }
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

}