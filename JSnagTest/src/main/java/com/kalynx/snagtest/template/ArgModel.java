package com.kalynx.snagtest.template;

public class ArgModel {

    private String defaultVal = "";
    private String argName;

    public ArgModel(String argName) {
        String[] split = argName.split("=");
        this.argName = camelToSnake(split[0]);
        if (split.length > 1) {
            this.defaultVal = split[1];
        }
    }

    public String getArgName() {
        return argName;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    private String camelToSnake(String camel) {
        return camel.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
