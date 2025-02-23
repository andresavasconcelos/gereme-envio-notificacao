package com.br.project.gereme.service;

import com.br.project.gereme.controller.dto.ScheduleNotificationsDTO;
import com.br.project.gereme.entity.Notification;
import com.br.project.gereme.enums.ChannelEnum;
import com.br.project.gereme.enums.StatusEnum;
import com.br.project.gereme.exception.InvalidIdException;
import com.br.project.gereme.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class NotificationServiceImpl implements INotificationService {

    private static final Integer ONESECOND = 1000;

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void scheduleNotification(ScheduleNotificationsDTO dto){
        if(dto == null){
            log.warn("Attempted to schesule notification with null DTO");
            throw new IllegalArgumentException("ScheduleNotificationsDTO cannot be null");
        }

        try{
            Notification notification = dto.toNotification();
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Notification scheduled successfully with ID: {}", savedNotification.getNotificationId());
        } catch (Exception e){
            log.error("Error scheduling notification", e);
            throw new RuntimeException("Faled to schedule notification", e);
        }
    }

    @Override
    public Optional<Notification> findById(Long id) {
        validateId(id, "findById");

        if (id == null) {
            log.warn("Attempted to find notification with null ID");
            throw new IllegalArgumentException("Notification ID cannot be null");
        }

        try {
            Optional<Notification> notificationOpt = notificationRepository.findById(id);
            if (notificationOpt.isEmpty()) {
                log.warn("Notification with ID {} not found", id);
            } else {
                log.info("Notification with ID {} found", id);
            }
            return notificationOpt;
        } catch (Exception e) {
            log.error("Error finding notification with ID {}", id, e);
            throw new RuntimeException("Failed to find notification", e);
        }
    }

    @Override
    public void cancelNotification(Long id){
        validateId(id, "cancelNotification");

        try {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);

            if(notificationOptional.isPresent()){
                Notification notification = notificationOptional.get();
                notification.setStatus(StatusEnum.CANCELED.toStatus());
                notificationRepository.save(notification);

                log.info("Notification with ID {} has been canceled", id);
            } else {
                log.warn("Notification with ID {} not found", id);
            }

        } catch (Exception e){
            log.error("Error canceling notification with ID {}", id, e);
            throw new RuntimeException("Faled to cancel notification", e);
        }
    }

    @Override
    public void chekingAndSending(LocalDateTime localDateTime){
       List<Notification> notificationList =  notificationRepository.findByStatusInAndDateTimeBefore(
                List.of(
                        StatusEnum.PENDING.toStatus(),
                        StatusEnum.ERROR.toStatus()
                ),
                localDateTime
        );
       
       notificationList.forEach(sendNotification());

    }

    private Consumer<Notification> sendNotification() {
        return notification -> {
            try{

                ChannelEnum channel = ChannelEnum.fromDescription(notification.getChannel());

                log.info("notification: ", notification.getChannel());

                if(channel.getDescription() != null){
                    switch (channel){
                        case EMAIL:
                            simulateNotification(notification);
                            break;
                        case SMS:
                            simulateNotification(notification);
                            break;
                        case PUSH:
                            simulateNotification(notification);
                            break;
                        case WHATSAPP:
                            simulateNotification(notification);
                            break;
                        default:
                            log.warn("Unsupported notification channel: {}", channel);
                            throw new UnsupportedOperationException("Unsupported notification channel: " + channel);
                    }
                } else {
                    log.warn("Notification channel not specified for notification ID: {}", notification.getNotificationId());
                    throw new IllegalArgumentException("Notification channel not specified");
                }

                notification.setStatus(StatusEnum.SUCESSES.toStatus());
                notificationRepository.save(notification);
                log.info("Notification with ID {} sent successfully via {}", notification.getNotificationId(), channel);
            }catch (Exception e){
                notification.setStatus(StatusEnum.ERROR.toStatus());
                notificationRepository.save(notification);
                log.error("Failed to send notification with ID {}: {}", notification.getNotificationId(), e.getMessage(), e);
            }
        };
    }

    private void simulateNotification(Notification notification) {
        log.info("Simulating {} sending to: {}", notification.getChannel(), notification.getDestination());
        try {
            Thread.sleep(ONESECOND);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void validateId(Long id, String operation) {
        if (id == null) {
            String errorMessage = String.format("ID cannot be null for operation: %s", operation);
            log.error(errorMessage);
            throw new InvalidIdException(errorMessage);
        }
    }
}
