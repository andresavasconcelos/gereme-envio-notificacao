package com.br.project.gereme.enums;

import com.br.project.gereme.entity.Channel;

import java.util.Arrays;

public enum ChannelEnum {
    EMAIL(1L, "email"),
    SMS(2L, "sms"),
    PUSH(3L, "push"),
    WHATSAPP(4L, "whatsapp");

    private Long id;
    private String description;


    ChannelEnum(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Channel toChannel() {
        return new Channel(id, description);
    }

    public static ChannelEnum fromDescription(Channel channel) {
        if(channel != null){
            for (ChannelEnum channelEnum : ChannelEnum.values()) {
                if (channel.getDescription().equalsIgnoreCase(channel.getDescription())) {
                    return channelEnum;
                }
            }
            throw new IllegalArgumentException("Invalid channel description: " + channel.getDescription());
        }
        return null;
    }
}
