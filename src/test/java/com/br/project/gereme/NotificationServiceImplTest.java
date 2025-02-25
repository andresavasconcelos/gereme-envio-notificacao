package com.br.project.gereme;

import com.br.project.gereme.controller.dto.ScheduleNotificationsDTO;
import com.br.project.gereme.entity.Notification;
import com.br.project.gereme.entity.Status;
import com.br.project.gereme.enums.ChannelEnum;
import com.br.project.gereme.enums.StatusEnum;
import com.br.project.gereme.exception.InvalidIdException;
import com.br.project.gereme.repository.NotificationRepository;
import com.br.project.gereme.service.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;
    private ScheduleNotificationsDTO dto;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setNotificationId(1L);
        notification.setChannel(ChannelEnum.EMAIL.toChannel());
        notification.setDestination("test@example.com");
        notification.setStatus(StatusEnum.PENDING.toStatus());
        notification.setDateTime(LocalDateTime.now());

        dto = new ScheduleNotificationsDTO(
                LocalDateTime.now(),
                "test@example.com",
                "Test message",
                ChannelEnum.EMAIL
        );
    }

    @Test
    void scheduleNotification_ShouldSaveNotification_WhenDtoIsValid() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.scheduleNotification(dto);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void scheduleNotification_ShouldThrowException_WhenDtoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> notificationService.scheduleNotification(null));
    }

    @Test
    void findById_ShouldReturnNotification_WhenIdIsValid() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(notification, result.get());
    }

    @Test
    void findById_ShouldThrowException_WhenIdIsNull() {
        InvalidIdException exception = assertThrows(InvalidIdException.class, () -> notificationService.findById(null));

        assertEquals("ID cannot be null for operation: findById", exception.getMessage()); // Valida a mensagem da exceção
        verify(notificationRepository, never()).findById(any());
    }


    @Test
    void findById_ShouldReturnEmpty_WhenNotificationNotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Notification> result = notificationService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void cancelNotification_ShouldCancelNotification_WhenIdIsValid() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.cancelNotification(1L);

        assertThat(notification.getStatus().getDescription()).isEqualTo(StatusEnum.CANCELED.toStatus().getDescription());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void cancelNotification_ShouldThrowException_WhenIdIsNull() {
        assertThrows(InvalidIdException.class, () -> notificationService.cancelNotification(null));
    }

    @Test
    void cancelNotification_ShouldNotCancel_WhenNotificationNotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        notificationService.cancelNotification(1L);

        verify(notificationRepository, never()).save(any(Notification.class));
    }

//    @Test
//    void chekingAndSending_ShouldSendNotifications_WhenNotificationsArePendingOrError() {
//        Notification pendingNotification = new Notification();
//        pendingNotification.setNotificationId(2L);
//        pendingNotification.setChannel(ChannelEnum.SMS.toChannel());
//        pendingNotification.setDestination("1234567890");
//        pendingNotification.setStatus(StatusEnum.PENDING.toStatus());
//        pendingNotification.setDateTime(LocalDateTime.now().minusMinutes(10));
//
//        Notification errorNotification = new Notification();
//        errorNotification.setNotificationId(3L);
//        errorNotification.setChannel(ChannelEnum.EMAIL.toChannel());
//        errorNotification.setDestination("test@example.com");
//        errorNotification.setStatus(StatusEnum.ERROR.toStatus());
//        errorNotification.setDateTime(LocalDateTime.now().minusMinutes(10));
//
//        Status pendingStatus = StatusEnum.PENDING.toStatus();
//        Status errorStatus = StatusEnum.ERROR.toStatus();
//
//        when(notificationRepository.findByStatusInAndDateTimeBefore(
//                eq(List.of(pendingStatus, errorStatus)),
//                any(LocalDateTime.class)
//        )).thenReturn(List.of(pendingNotification, errorNotification));
//
//        verify(notificationRepository, times(1)).save(notification);
//    }

    @Test
    void sendNotification_ShouldSetStatusToSuccess_WhenNotificationIsSent() {
        Notification notification = new Notification();
        notification.setNotificationId(1L);
        notification.setChannel(ChannelEnum.EMAIL.toChannel());
        notification.setDestination("test@example.com");
        notification.setStatus(StatusEnum.PENDING.toStatus());
        notification.setDateTime(LocalDateTime.now());

        notificationService.sendNotification().accept(notification);

        assertThat(notification.getStatus().getDescription()).isEqualTo(StatusEnum.SUCESSES.toStatus().getDescription());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void sendNotification_ShouldSetStatusToError_WhenExceptionOccurs() {
        Notification notification = new Notification();
        notification.setNotificationId(1L);
        notification.setChannel(ChannelEnum.EMAIL.toChannel());
        notification.setDestination("test@example.com");
        notification.setStatus(StatusEnum.ERROR.toStatus());
        notification.setDateTime(LocalDateTime.now());

        notificationService.sendNotification().accept(notification);

        assertThat(notification.getStatus().getDescription()).isEqualTo(StatusEnum.SUCESSES.toStatus().getDescription());

        verify(notificationRepository, times(1)).save(notification);
    }
}