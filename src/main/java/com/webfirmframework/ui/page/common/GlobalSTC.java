package com.webfirmframework.ui.page.common;

import com.webfirmframework.wffweb.tag.html.SharedTagContent;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GlobalSTC {

    public static final SharedTagContent<String> LOGGER_STC = new SharedTagContent<>("");

    public static final SharedTagContent<ZonedDateTime> CLOCK = new SharedTagContent<>(ZonedDateTime.now(Clock.systemUTC()));

    static {
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(
                        () -> CLOCK.setContent(ZonedDateTime.now(Clock.systemUTC())),
                        1,
                        1,
                        TimeUnit.SECONDS);
    }
}
