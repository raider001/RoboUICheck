package com.kalynx.snagtest.manager;

import com.kalynx.snagtest.annotations.RobotKeywordName;
import org.robotframework.javalib.factory.AnnotationKeywordFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnagKeywordFactory extends AnnotationKeywordFactory {

    private Map<String, String> customKeywords;

    public SnagKeywordFactory(Map<String, Object> keywordBeansMap) {
        super(keywordBeansMap);
    }

    public SnagKeywordFactory(List<Map> keywordBeansMaps) {
        super(keywordBeansMaps);
    }

    public String getCustomKeywordName(String keywordName) {
        return customKeywords.getOrDefault(keywordName, null);
    }

    @Override
    protected void extractKeywordsFromKeywordBeans(Map<String, Object> keywordBeansMap) {
        if (customKeywords == null) {
            customKeywords = new HashMap<>();
        }
        for (Map.Entry<String, Object> entry : keywordBeansMap.entrySet()) {
            String keywordName = entry.getKey();
            Object keywordBean = entry.getValue();
            Method[] methods = keywordBean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RobotKeywordName.class)) {
                    String customKeywordName = method.getAnnotation(RobotKeywordName.class).value();
                    if (customKeywordName != null) {
                        customKeywords.put(method.getName(), customKeywordName);
                    }
                }
            }
        }
        super.extractKeywordsFromKeywordBeans(keywordBeansMap);
    }
}
