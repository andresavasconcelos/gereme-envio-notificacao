package com.br.project.gereme.service;

import com.br.project.gereme.controller.dto.ScheduleNotificationsDTO;
import com.br.project.gereme.entity.Notification;

import java.time.LocalDateTime;
import java.util.Optional;

public interface INotificationService {

    void scheduleNotification(ScheduleNotificationsDTO dto);

    Optional<Notification> findById(Long id);

    void cancelNotification(Long id);

    void chekingAndSending(LocalDateTime localDateTime);
}
