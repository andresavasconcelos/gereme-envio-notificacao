package com.br.project.gereme.scheluder;

import com.br.project.gereme.service.NotificationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class GeremeTaskScheduler {
    private static final Logger log = LoggerFactory.getLogger(GeremeTaskScheduler.class);

    private final NotificationServiceImpl notificationServiceImpl;

    public GeremeTaskScheduler(NotificationServiceImpl notificationServiceImpl) {
        this.notificationServiceImpl = notificationServiceImpl;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkingTasks(){
        LocalDateTime dateTime = LocalDateTime.now();
        log.info("Running at {}", dateTime);
        notificationServiceImpl.chekingAndSending(dateTime);
    }
}
