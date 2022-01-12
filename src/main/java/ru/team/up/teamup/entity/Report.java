package ru.team.up.teamup.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;


@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    String id;
    Control control;
    InitiatorType initiatorType;
    String initiatorName;
    Long initiatorId;
    Date time;
    Status status;
    Map<String, Object> parameters;
}
