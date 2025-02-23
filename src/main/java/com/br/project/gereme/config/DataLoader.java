package com.br.project.gereme.config;

import com.br.project.gereme.enums.ChannelEnum;
import com.br.project.gereme.enums.StatusEnum;
import com.br.project.gereme.repository.ChannelRepository;
import com.br.project.gereme.repository.StatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final ChannelRepository channelRepository;
    private final StatusRepository statusRepository;

    public DataLoader(ChannelRepository channelRepository, StatusRepository statusRepository) {
        this.channelRepository = channelRepository;
        this.statusRepository = statusRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(ChannelEnum.values())
                .map(ChannelEnum::toChannel)
                .forEach(channelRepository::save);

        Arrays.stream(StatusEnum.values())
                .map(StatusEnum::toStatus)
                .forEach(statusRepository::save);
    }
}
