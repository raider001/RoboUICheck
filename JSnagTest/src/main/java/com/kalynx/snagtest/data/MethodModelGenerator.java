package com.kalynx.snagtest.data;

import com.kalynx.snagtest.template.ArgModel;
import com.kalynx.snagtest.template.MethodModel;
import org.robotframework.javalib.library.AnnotationLibrary;

import java.util.ArrayList;
import java.util.List;

public class MethodModelGenerator {

    private final List<MethodModel> methods = new ArrayList<>();

    public void addMethods(AnnotationLibrary annotationLibrary) {
        for (String keywordName : annotationLibrary.getKeywordNames()) {
            List<ArgModel> args = annotationLibrary.getKeywordArguments(keywordName).stream().map(ArgModel::new).toList();
            String doc = annotationLibrary.getKeywordDocumentation(keywordName);

            MethodModel mm = new MethodModel(keywordName, doc, args);
            methods.add(mm);
        }
    }

    public List<MethodModel> getMethods() {
        return methods;
    }

}
