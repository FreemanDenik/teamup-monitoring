package ru.team.up.sup.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сущность параметр
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "parameters")
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String parameterName;

    @Enumerated(EnumType.STRING)
    @Column
    private SupParameterType parameterType;

    @Column(nullable = false)
    private AppModuleNameDto systemName;

    @Column(nullable = false)
    private String parameterValue;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private Boolean inUse;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private LocalDate lastUsedDate;

    @Column
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "user_who_last_change_parameters")
    private User userWhoLastChangeParameters;
}
