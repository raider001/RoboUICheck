package com.kalynx.uitestframework.arg;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ArgParser {

    private final Logger LOGGER = LoggerFactory.getLogger(ArgParser.class);
    private static final int DESCRIPTION_LENGTH = 60;
    private static final Pattern mainKey = Pattern.compile("^(--[A-Za-z0-9]+)");
    private static final Pattern shortKey = Pattern.compile("^(-[A-Za-z0-9]+)");
    private final List<Arg<?>> argList = new ArrayList<>();
    public ArgParser() {
        addArg("help", String[].class)
            .setShortKey('h')
            .setDefault(new String[0])
            .setHelp("Displays the help for arguments")
            .setCommand(params -> {
                if(params.length == 0) {
                    LOGGER.info("|%s|".formatted("-".repeat(102)));
                    LOGGER.info(String.format("| %-24s | %-9s | %-60s |", "Argument", "Short Hand", "Description"));
                    LOGGER.info("|%s|".formatted("-".repeat(102)));
                    argList.forEach(arg -> {
                        List<String> lines = getStrings(arg);
                        LOGGER.info(String.format("| %-24s | %-9s | %-60s |%n","--"+arg.key, StringUtils.center("-"+arg.shortKey, 10), lines.get(0)));
                        lines.remove(0);
                        for(String line: lines) {
                            LOGGER.info(String.format("| %-37s | %-60s |%n", "", line));
                        }
                        if(arg.defaultVal != null) {
                            LOGGER.info(String.format("| %-37s | %-60s |%n", "", "Default: " + (arg.defaultVal.getClass().isArray() ? Arrays.toString((Object[]) arg.defaultVal) : arg.defaultVal)));
                        }
                    });
                    LOGGER.info("|%s|".formatted("-".repeat(102)));
                }
                System.exit(0);
            }).ignoreWhenNotProvided();
    }

    private static List<String> getStrings(Arg<?> arg) {
        List<String> lines = new ArrayList<>();
        int pos = 0;
        do {
            int newPos = pos + DESCRIPTION_LENGTH;
            String newSubString = arg.help.substring(pos, Math.min(newPos, arg.help.length()));
            newSubString = newSubString.stripLeading();
            lines.add(newSubString);
            pos = newPos;
        } while(pos < arg.help.length());
        return lines;
    }

    public void parse(String... args) {
        List<String> appliedArgs = new ArrayList<>();
        for(int i = 0; i < args.length; i++) {
            if (mainKey.matcher(args[i]).find()) {
                String foundKey = args[i].substring(2);
                Optional<Arg<?>> arg = argList.stream().filter(item -> item.key.equals(foundKey)).findFirst();
                if (arg.isEmpty()) {
                    throw new IllegalArgumentException("No parameter named " + foundKey + " found");
                }
                handle(arg.get(), collectVal(i + 1, args));
                appliedArgs.add(foundKey);
            } else if (shortKey.matcher(args[i]).find()) {
                if (args[i].length() > 2) {
                    throw new IllegalArgumentException("Invalid short key given. Only one character expected. Was given " + args[i].charAt(1));
                }
                char k = args[i].charAt(1);
                Optional<Arg<?>> arg = argList.stream().filter(item -> item.shortKey == k).findFirst();
                if(arg.isPresent()) {
                    handle(arg.get(), collectVal(i + 1, args));
                    appliedArgs.add(arg.get().key);
                }
            }
        }
        argList.stream().filter(arg -> !appliedArgs.contains(arg.key) && !arg.ignoreÍfNotGiven).forEach(arg ->
            ((Consumer<Object>) arg.cmd).accept(arg.defaultVal)
        );
    }

    private <T> void handle(Arg<T> arg, String... vals) {
        if(arg.argType.equals(Integer[].class)) {
            List<Integer> res = Arrays.stream(vals).map(Integer::valueOf).toList();
            arg.cmd.accept((T) res.toArray(Integer[]::new));
        } else if(arg.argType.equals(String[].class)) {
            if(arg.argType.componentType().equals(String.class)) {
                arg.cmd.accept((T) vals);
            }
            if(arg.argType.componentType().equals(Boolean.class)) {
                List<Boolean> res = Arrays.stream(vals).map(Boolean::valueOf).toList();
                arg.cmd.accept((T) res);
            }
            if(arg.argType.componentType().equals(Integer.class)) {
                List<Integer> res = Arrays.stream(vals).map(Integer::valueOf).toList();
                arg.cmd.accept((T) res);
            }
            if(arg.argType.componentType().equals(Double.class)) {
                List<Double> res = Arrays.stream(vals).map(Double::valueOf).toList();
                arg.cmd.accept((T) res);
            }
            if(arg.argType.componentType().equals(Float.class)) {
                List<Float> res = Arrays.stream(vals).map(Float::valueOf).toList();
                arg.cmd.accept((T) res);
            }
        } else {
            if(vals.length != 1) {
                throw new IllegalArgumentException("Incorrect number of values given for argument. Given " + Arrays.toString(vals));
            }
            if(arg.argType.equals(String.class)){
                arg.cmd.accept((T) vals[0]);
            } else if(arg.argType.equals(Boolean.class)) {
                arg.cmd.accept((T) Boolean.valueOf(vals[0]));
            } else if (arg.argType.equals(Integer.class)) {
                arg.cmd.accept((T) Integer.valueOf(vals[0]));
            } else if (arg.argType.equals(Double.class)) {
                arg.cmd.accept((T) Double.valueOf(vals[0]));
            } else if (arg.argType.equals(Float.class)) {
                arg.cmd.accept((T) Float.valueOf(vals[0]));
            }
        }
    }

    private String[] collectVal(int startIndex, String... args) {
        List<String> vals = new ArrayList<>();
        for(int i = startIndex; i < args.length; i++) {
            final int iterator = i;
            if(args[i].startsWith("-")) {
                Optional<?> o = argList.stream().filter(arg -> args[iterator].substring(1).equals(arg.key) || args[iterator].substring(1).equals(String.valueOf(arg.shortKey))).findFirst();
                if(o.isPresent()) {
                    return vals.toArray(new String[0]);
                }
            }
            vals.add(args[i]);
        }
        return vals.toArray(new String[0]);
    }

    public <T> Arg<T> addArg(String key, Class<T> argType) {
        Arg<T> arg = new Arg<>(key, argType);
        argList.add(arg);
        return arg;
    }

    private void throwIllegalArgumentIfKeyUsed(String key) {
        if(argList.stream().anyMatch(item -> item.key.equals(key))) {
            throw new IllegalArgumentException("Key " + key + " already exists as an argument.");
        }
    }



    public class Arg<T> {
        private final Class<T> argType;
        private final String key;
        private char shortKey;
        private T defaultVal = null;
        private String help = "";

        private boolean ignoreÍfNotGiven = false;
        private Consumer<T> cmd = val -> { throw new IllegalArgumentException("Command not defined"); };
        public Arg(String key, Class<T> argType) {
            throwIllegalArgumentIfKeyUsed(key);
            throwUnsupportedExceptionIfUnsupportedType(argType);
            this.key = key;
            this.argType = argType;
        }

        public Arg<T> setHelp(String help) {
            this.help = help;
            return this;
        }

        public Arg<T> ignoreWhenNotProvided() {
            ignoreÍfNotGiven = true;
            return this;
        }

        public Arg<T> setShortKey(char shortKey) {
            throwIllegalArgumentIfShortKeyUsed(shortKey);
            this.shortKey = shortKey;
            return this;
        }

        public char getShortKey() {
            return shortKey;
        }

        public Arg<T> setDefault(T defaultVal) {
            this.defaultVal = defaultVal;
            return this;
        }

        public Arg<T> setCommand(Consumer<T> cmd) {
            if(cmd == null) {
                throw new IllegalArgumentException("cmd cannot be null");
            }

            this.cmd = cmd;
            return this;
        }

        private void throwIllegalArgumentIfShortKeyUsed(char shortKey) {
            if(argList.stream().anyMatch(item -> item.getShortKey() == shortKey)) {
                throw new IllegalArgumentException("Short Key " + shortKey + " already exists as an argument.");
            }
        }

        private void throwUnsupportedExceptionIfUnsupportedType(Class<?> type) {
            if(!(type.equals(Integer.class)
                    || type.equals(Boolean.class)
                    || type.equals(String.class)
                    || type.equals(Double.class)
                    || type.equals(Float.class)
                    || type.equals(Integer[].class)
                    || type.equals(Boolean[].class)
                    || type.equals(String[].class)
                    || type.equals(Double[].class)
                    || type.equals(Float[].class))) {
                throw new UnsupportedOperationException("Type " + type.getName() + " not supported.");
            }
        }
    }
}
