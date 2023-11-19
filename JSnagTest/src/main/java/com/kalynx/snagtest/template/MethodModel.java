package com.kalynx.snagtest.template;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class MethodModel {
    private final String pyName;
    private final String robotName;
    private String methodName;
    private List<ArgModel> arguments;
    private String document;

    public MethodModel(String methodName, String document, List<ArgModel> arguments) {
        this.methodName = methodName;
        this.pyName = camelToSnake(methodName);
        this.robotName = sentenceCase(methodName);
        this.arguments = arguments;
        this.document = document.replace("\n", "\n        ");
    }

    public MethodModel(String methodName, String document, List<ArgModel> arguments, String pyNameOverride) {
        this.methodName = methodName;
        this.pyName = camelToSnake(methodName);
        this.robotName = pyNameOverride;
        this.arguments = arguments;
        this.document = document.replace("\n", "\n        ");
    }

    public String getRobotName() {
        return robotName;
    }

    public String getPyName() {
        return pyName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ArgModel> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgModel> arguments) {
        this.arguments = arguments;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    private String camelToSnake(String camel) {
        return camel.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private String sentenceCase(String methodName) {
        Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");
        String[] words = WORD_FINDER.matcher(methodName).results().map(MatchResult::group).toArray(String[]::new);
        String sentence = "";
        for (String word : words) {
            sentence = sentence + word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
        }
        return sentence.trim();

    }
}
