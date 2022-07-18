package ru.team.up.sup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.team.up.dto.ListSupParameterDto;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class ParameterSenderImpl implements ParameterSender {

    private final RestTemplate restTemplate;

    @Autowired
    public ParameterSenderImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendDefaultsToSup() {
        ListSupParameterDto listToSend = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            listToSend = mapper.readValue(new File("./Parameters.json"), ListSupParameterDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listToSend == null) {
            throw new RuntimeException("Не удалось прочитать лист с параметрами по умолчанию");
        }
        HttpEntity<ListSupParameterDto> request = new HttpEntity<>(listToSend);
        try {
            restTemplate.postForObject(ParameterService.getSupDefaultParamURL.getValue(), request, ListSupParameterDto.class);
        } catch (ResourceAccessException e) {
            log.debug("Не удалось отправить параметры по умолчанию");
        }
    }

}
