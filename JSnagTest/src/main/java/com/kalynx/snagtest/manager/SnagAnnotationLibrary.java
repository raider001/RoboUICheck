package com.kalynx.snagtest.manager;

import org.robotframework.javalib.beans.annotation.IBeanLoader;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.DocumentedKeyword;
import org.robotframework.javalib.library.AnnotationLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SnagAnnotationLibrary extends AnnotationLibrary {

    private SnagKeywordFactory keywordFactory;

    public SnagAnnotationLibrary() {
        super();
    }

    public SnagAnnotationLibrary(String keywordPattern) {
        super(keywordPattern);
    }

    public SnagAnnotationLibrary(List<String> keywordPatterns) {
        super(keywordPatterns);
    }

    public String getKeywordCustomName(String keyword) {
        return keywordFactory.getCustomKeywordName(keyword);
    }

    protected KeywordFactory<DocumentedKeyword> createKeywordFactory() {
        assumeKeywordPatternIsSet();
        if (keywordFactory == null) {
            List<Map> keywordBeansMaps = new ArrayList<>();
            for (IBeanLoader beanLoader : beanLoaders) {
                keywordBeansMaps.add(beanLoader.loadBeanDefinitions(classFilter));
            }
            keywordFactory = new SnagKeywordFactory(keywordBeansMaps);

            List<Object> injectionValues = new ArrayList<>();
            injectionValues.add(this);
            for (Map keywordBeansMap : keywordBeansMaps) {
                injectionValues.addAll(keywordBeansMap.values());
            }
            for (Object injectionTarget : injectionValues) {
                autowireFields(injectionTarget, injectionValues);
            }
        }
        return keywordFactory;
    }

    private void assumeKeywordPatternIsSet() {
        if (beanLoaders.isEmpty()) {
            throw new IllegalStateException("Keyword pattern must be set before calling getKeywordNames.");
        }
    }
}
