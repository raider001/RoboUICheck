package com.kalynx.snagtest.data;

import com.kalynx.snagtest.manager.SnagAnnotationLibrary;
import com.kalynx.snagtest.template.ArgModel;
import com.kalynx.snagtest.template.MethodModel;

import java.util.ArrayList;
import java.util.List;

public class MethodModelGenerator {

    private final List<MethodModel> methods = new ArrayList<>();

    public void addMethods(SnagAnnotationLibrary annotationLibrary) {
        for (String keywordName : annotationLibrary.getKeywordNames()) {
            List<ArgModel> args = annotationLibrary.getKeywordArguments(keywordName).stream().map(ArgModel::new).toList();
            String doc = annotationLibrary.getKeywordDocumentation(keywordName);
            String customKeywordName = annotationLibrary.getKeywordCustomName(keywordName);
            MethodModel mm = customKeywordName == null ? new MethodModel(keywordName, doc, args) : new MethodModel(keywordName, doc, args, customKeywordName);
            methods.add(mm);
        }
    }

    public List<MethodModel> getMethods() {
        return methods;
    }

}
