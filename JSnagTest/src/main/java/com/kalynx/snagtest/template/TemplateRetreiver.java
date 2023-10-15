package com.kalynx.snagtest.template;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class TemplateRetreiver {

    private final Template temp;

    public TemplateRetreiver() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
        cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));
        temp = cfg.getTemplate("keywords.ftl");
    }

    public void generateKeywords(List<MethodModel> methods) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("PySnagTest/SnagTest.py"))) {
            Map<String, List<MethodModel>> map = Map.of("methods", methods);
            temp.process(map, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

    }
}
