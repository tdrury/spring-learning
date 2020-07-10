package com.tdrury.springlearning.properties;

import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@ToString
@Component
public class PropsBean {

    @Value("${tdrury.source:undefined}")
    private String source;

    @Value("${tdrury.string1:undefined}")
    private String string1;

    @Value("${tdrury.int1:-1}")
    private int int1;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent ev) {
        log.info("onApplicationEvent: event={}", ev.toString());
        log.info("onApplicationEvent: props=[{}]", this);

        File dir = new File(".");
        log.info("onApplicationEvent: running in dir [{}] with perms [{}]", dir.getAbsoluteFile(), getPermissions(dir));
        listFiles(dir);
        File configDir = new File(dir, "config");
        log.info("onApplicationEvent: config dir exists? [{}]", configDir.exists());
        if (configDir.exists()) {
            log.info("onApplicationEvent: config dir perms [{}]", getPermissions(configDir));
            listFiles(configDir);
        }
    }

    @SneakyThrows
    protected void listFiles(File dir) {
        String[] files = dir.list();
        log.info("found {} files in {}", files.length, dir.getCanonicalPath());
        for (String f : files) {
            log.info("   {}", f);
        }
    }

    protected String getPermissions(File f) {
        String read = f.canRead() ? "r" : "-";
        String write = f.canWrite() ? "w" : "-";
        String execute = f.canExecute() ? "x" : "-";
        return read+write+execute;
    }
}
