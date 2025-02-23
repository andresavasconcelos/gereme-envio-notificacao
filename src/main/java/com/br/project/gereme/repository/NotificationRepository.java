package com.br.project.gereme.repository;

import com.br.project.gereme.entity.Notification;
import com.br.project.gereme.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public List<Notification> findByStatusInAndDateTimeBefore(List<Status> status, LocalDateTime localDateTime);
}
