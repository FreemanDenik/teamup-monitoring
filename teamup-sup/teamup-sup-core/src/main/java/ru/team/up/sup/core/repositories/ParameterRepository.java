package ru.team.up.sup.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.entity.Parameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> getParametersBySystemName(AppModuleNameDto systemName);

    Parameter getParameterByParameterName(String parameterName);

    Parameter getParameterById(Long id);

    List<Parameter> findByParameterNameContains(String parameterName);

    List<Parameter> findByCreationDateBetween(LocalDate date1, LocalDate date2);

    List<Parameter> findByUpdateDateBetween(LocalDateTime date1, LocalDateTime date2);

}
