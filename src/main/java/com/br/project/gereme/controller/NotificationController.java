package com.br.project.gereme.controller;

import com.br.project.gereme.controller.dto.ScheduleNotificationsDTO;
import com.br.project.gereme.entity.Notification;
import com.br.project.gereme.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification", description = "Envio de notificação utilizando Spring Schedule")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @Operation(summary = "Salva a notificação", description = "Salva a notificação a ser enviada no banco de dados com status pendente")
    @ApiResponse(responseCode = "201", description = "Mensagem gravada com sucesso no banco de dados")
    @ApiResponse(responseCode = "500", description = "Erro no servidor ao tentar salvar a mensagem")
    public ResponseEntity<Void> scheduleNotifications(@RequestBody ScheduleNotificationsDTO dto){
        log.info("scheduleNotifications started successfully");

        notificationService.scheduleNotification(dto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = "Consulta mensagem", description = "Realiza consulta da notificação no banco de dados a partir do id")
    @ApiResponse(responseCode = "200", description = "Mensagem gravada com sucesso no banco de dados")
    @ApiResponse(responseCode = "500", description = "Erro no servidor ao tentar consultar a notificação")
    public ResponseEntity<Notification> getNotification(@PathVariable("notificationId") Long notificationId) {
        log.info("getNotification started successfully");

        return notificationService.findById(notificationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Envia a mensagem", description = "Cancela uma notificação a partir do id")
    @ApiResponse(responseCode = "204", description = "Notificação cancelada com sucesso!")
    @ApiResponse(responseCode = "500", description = "Erro no servidor ao tentar cancelar a notificação")
    public ResponseEntity<Void>  cancelNotification(@PathVariable("notificationId") Long notificationId){
        log.info("cancelNotification started successfully");

        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
