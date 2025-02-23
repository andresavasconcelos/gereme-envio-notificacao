package com.br.project.gereme.controller.dto;

import com.br.project.gereme.entity.Notification;
import com.br.project.gereme.enums.ChannelEnum;
import com.br.project.gereme.enums.ChannelEnumDeserializer;
import com.br.project.gereme.enums.StatusEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

public record ScheduleNotificationsDTO(LocalDateTime dateTime,
                                       String destination,
                                       String message,
                                       @JsonDeserialize(using = ChannelEnumDeserializer.class) ChannelEnum channel) {

    public Notification toNotification(){
        return new Notification(
                dateTime,
                destination,
                message,
                channel.toChannel(),
                StatusEnum.PENDING.toStatus()
        );
    }
}
