package ru.team.up.sup.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.repositories.ParameterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ParameterServiceImplTest {

    private ParameterService underTest;
    @Mock
    private ParameterRepository parameterRepository;
    @Mock
    private KafkaSupService kafkaSupService;


    private final User testUser = User.builder()
            .id(1L)
            .name("TestName")
            .lastName("TestLastName")
            .email("test@mail.com")
            .password("testPass")
            .lastAccountActivity(LocalDateTime.now())
            .build();

    @BeforeEach
    void setUp() {
        underTest = new ParameterServiceImpl(parameterRepository, kafkaSupService);
    }

    //Тесты метода getAllParameters
    @Test
    void canGetAllParameters() {
        // Given
        Parameter bdParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        Parameter bdParam2 = Parameter.builder()
                .id(2L)
                .parameterName("TestParam2")
                .parameterType(SupParameterType.BOOLEAN)
                .systemName(AppModuleNameDto.TEAMUP_KAFKA)
                .parameterValue("true")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        List<Parameter> list = List.of(bdParam1, bdParam2);

        doReturn(list).when(parameterRepository).findAll();

        // When Then
        assertThat(underTest.getAllParameters()).isEqualTo(list);
    }

    @Test
    void willThrownWhenGetAllParametersListIsEmpty() {
        doReturn(List.of()).when(parameterRepository).findAll();
        assertThatThrownBy(() -> underTest.getAllParameters())
                .isInstanceOf(NoContentException.class);
    }

    //Тесты метода getParametersBySystemName
    @Test
    void canGetParametersBySystemName() {
        // Given
        Parameter bdParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        List<Parameter> list = List.of(bdParam1);

        doReturn(list).when(parameterRepository)
                .getParametersBySystemName(bdParam1.getSystemName());

        // When Then
        assertThat(underTest.getParametersBySystemName(bdParam1.getSystemName())).isEqualTo(list);
    }

    @Test
    void willThrownWhenGetParametersBySystemNameListIsEmpty() {
        AppModuleNameDto testModule = AppModuleNameDto.TEAMUP_CORE;
        doReturn(List.of()).when(parameterRepository).getParametersBySystemName(testModule);
        assertThatThrownBy(() -> underTest.getParametersBySystemName(testModule))
                .isInstanceOf(NoContentException.class);
    }

    //Тесты метода getParameterById
    @Test
    void canGetParameterById() {
        // Given
        Parameter bdParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        doReturn(Optional.ofNullable(bdParam)).when(parameterRepository).findById(bdParam.getId());

        // When Then
        assertThat(underTest.getParameterById(bdParam.getId())).isEqualTo(bdParam);
    }

    @Test
    void willThrownWhenGetParameterByIdIsNotInDb() {
        Long testId = 1L;
        doReturn(Optional.ofNullable(null)).when(parameterRepository).findById(testId);
        assertThatThrownBy(() -> underTest.getParameterById(testId))
                .isInstanceOf(NoContentException.class);
    }


    //Тесты метода getParameterByParameterName
    @Test
    void canGetParameterByParameterName() {
        // Given
        Parameter bdParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();

        doReturn(bdParam1).when(parameterRepository)
                .getParameterByParameterName(bdParam1.getParameterName());

        // When Then
        assertThat(underTest.getParameterByParameterName(bdParam1.getParameterName())).isEqualTo(bdParam1);
    }

    @Test
    void willThrownWhenGetParameterByParameterNameIsNotInDb() {
        String testName = "Test";
        doReturn(null).when(parameterRepository).getParameterByParameterName(testName);
        assertThatThrownBy(() -> underTest.getParameterByParameterName(testName))
                .isInstanceOf(NoContentException.class);
    }

    //Тесты метода saveParameter
    @Test
    void canSaveParameter() {
        // Given
        LocalDate creationDate = LocalDate.now().minusDays(5L);
        LocalDateTime updateDate = LocalDateTime.now().minusMinutes(5L);
        Parameter inputParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(creationDate)
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(updateDate)
                .userWhoLastChangeParameters(testUser)
                .build();

        // When
        underTest.saveParameter(inputParam);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        verify(kafkaSupService).send(parameterArgumentCaptor.capture());
        List<Parameter> captorList = parameterArgumentCaptor.getAllValues();
        assertThat(captorList.get(0)).isEqualTo(captorList.get(1));
        assertThat(captorList.get(0).getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(captorList.get(0).getUpdateDate()).isAfter(updateDate);
    }

    //Тесты метода deleteParameter
    @Test
    void canDeleteParameter() {
        // Given
        Parameter bdParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        doReturn(Optional.of(bdParam1)).when(parameterRepository).findById(bdParam1.getId());

        // When
        underTest.deleteParameter(bdParam1.getId());

        // Then
        ArgumentCaptor<Long> longArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);
        verify(parameterRepository).deleteById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(bdParam1.getId());
    }

    @Test
    void willThrownWhenParameterToDeleteIsNotInDb() {
        Long testId = 1L;
        doReturn(Optional.ofNullable(null)).when(parameterRepository).findById(testId);
        assertThatThrownBy(() -> underTest.deleteParameter(testId))
                .isInstanceOf(NoContentException.class);
        verify(parameterRepository).findById(testId);
    }

    //Тесты метода editParameter
    @Test
    void canEditParameter() {
        // Given
        LocalDateTime updateDate = LocalDateTime.now().minusMinutes(5L);
        Parameter inputParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(null)
                .inUse(false)
                .lastUsedDate(null)
                .userWhoLastChangeParameters(testUser)
                .build();

        Parameter bdParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello")
                .creationDate(LocalDate.now().minusDays(5L))
                .inUse(false)
                .lastUsedDate(null)
                .userWhoLastChangeParameters(testUser)
                .build();

        doReturn(Optional.ofNullable(bdParam)).when(parameterRepository).findById(inputParam.getId());

        // When
        underTest.editParameter(inputParam);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        verify(kafkaSupService).send(parameterArgumentCaptor.capture());
        List<Parameter> captorList = parameterArgumentCaptor.getAllValues();
        System.out.println(captorList);
        assertThat(captorList.get(0)).isEqualTo(captorList.get(1));
        assertThat(captorList.get(0).getCreationDate()).isEqualTo(bdParam.getCreationDate());
        assertThat(captorList.get(0).getUpdateDate()).isAfter(updateDate);
    }

    @Test
    void willThrownWhenParameterToEditIsNotInDb() {
        Parameter inputParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(null)
                .inUse(false)
                .lastUsedDate(null)
                .userWhoLastChangeParameters(testUser)
                .build();
        doReturn(Optional.ofNullable(null)).when(parameterRepository).findById(inputParam.getId());
        assertThatThrownBy(() -> underTest.editParameter(inputParam))
                .isInstanceOf(NoContentException.class);
    }

    //Тесты метода compareWithDefaultAndUpdate
    @Test
    void testCompareParameterInBdInUse() {
        // Given
        Parameter bdParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam3")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        Parameter bdParamResult = Parameter.builder()
                .id(1L)
                .parameterName("TestParam3")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam3")
                .parameterValue("Hello world!")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();
        ListSupParameterDto listSupParameterDto = new ListSupParameterDto();
        listSupParameterDto.addParameter(defaultParam);
        doReturn(List.of(bdParam)).when(parameterRepository)
                .getParametersBySystemName(listSupParameterDto.getModuleName());

        // When
        underTest.compareWithDefaultAndUpdate(listSupParameterDto);


        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(bdParamResult);
    }

    @Test
    void testCompareParameterInBdNotInUse() {
        // Given
        Parameter bdParam = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .userWhoLastChangeParameters(testUser)
                .build();
        Parameter bdParamResult = Parameter.builder()
                .id(1L)
                .parameterName("TestParam1")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(bdParam.getUpdateDate())
                .userWhoLastChangeParameters(testUser)
                .build();
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam3")
                .parameterValue("Hello world!")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();
        Parameter newParam = Parameter.builder()
                .parameterName(defaultParam.getParameterName())
                .parameterType(defaultParam.getParameterType())
                .systemName(defaultParam.getSystemName())
                .parameterValue(defaultParam.getParameterValue().toString())
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .build();

        ListSupParameterDto listSupParameterDto = new ListSupParameterDto();
        listSupParameterDto.addParameter(defaultParam);
        doReturn(List.of(bdParam)).when(parameterRepository)
                .getParametersBySystemName(listSupParameterDto.getModuleName());

        // When
        underTest.compareWithDefaultAndUpdate(listSupParameterDto);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor = ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository, times(2)).save(parameterArgumentCaptor.capture());
        List<Parameter> results = parameterArgumentCaptor.getAllValues();
        assertThat(results.get(0)).isEqualTo(bdParamResult);
        assertThat(results.get(1)).isEqualTo(newParam);
    }

    @Test
    void testCompareParameterNotInBd() {
        // Given
        AppModuleNameDto testedSystem = AppModuleNameDto.TEAMUP_CORE;
        SupParameterDto defaultParam = SupParameterDto.builder()
                .parameterName("TestParam25")
                .parameterValue(123)
                .parameterType(SupParameterType.INTEGER)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .updateTime(LocalDateTime.now())
                .build();
        Parameter result = Parameter.builder()
                .parameterName(defaultParam.getParameterName())
                .parameterType(defaultParam.getParameterType())
                .systemName(defaultParam.getSystemName())
                .parameterValue(defaultParam.getParameterValue().toString())
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now())
                .build();

        ListSupParameterDto listSupParameterDto = new ListSupParameterDto();
        listSupParameterDto.addParameter(defaultParam);
        doReturn(List.of()).when(parameterRepository)
                .getParametersBySystemName(testedSystem);

        // When
        underTest.compareWithDefaultAndUpdate(listSupParameterDto);

        // Then
        ArgumentCaptor<Parameter> parameterArgumentCaptor =
                ArgumentCaptor.forClass(Parameter.class);
        verify(parameterRepository).save(parameterArgumentCaptor.capture());
        assertThat(parameterArgumentCaptor.getValue()).isEqualTo(result);
    }

    @Test
    void compareWillThrowIfDtoIsNull() {
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void compareWillThrowIfModuleIsNull() {
        // Given
        final ListSupParameterDto list = new ListSupParameterDto();
        list.setModuleName(null);
        list.setList(List.of(SupParameterDto.builder().build()));

        // When Then
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(list))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void compareWillThrowIfListIsNull() {
        // Given
        final ListSupParameterDto list = new ListSupParameterDto();
        list.setModuleName(AppModuleNameDto.TEAMUP_CORE);
        list.setList(null);

        // When Then
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(list))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    @Test
    void compareWillThrowIfListIsEmpty() {
        // Given
        final ListSupParameterDto list = new ListSupParameterDto();
        list.setModuleName(AppModuleNameDto.TEAMUP_CORE);
        list.setList(List.of());

        // When Then
        assertThatThrownBy(() -> underTest.compareWithDefaultAndUpdate(list))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Получен пустой лист параметров по умолчанию");

    }

    //Тесты метода purge
    @Test
    void canPurgeIfUsedDateIsNull() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(null)
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        ArgumentCaptor<Long> idArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);
        verify(parameterRepository).deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(testParam1.getId());
    }

    @Test
    void canPurgeIfUsedDateIsGreaterThanThreshold() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(LocalDate.now().minusDays(8L))
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        ArgumentCaptor<Long> idArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);
        verify(parameterRepository).deleteById(idArgumentCaptor.capture());
        assertThat(idArgumentCaptor.getValue()).isEqualTo(testParam1.getId());
    }

    @Test
    void dontPurgeIfUsedDateIsLowerThanThreshold() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(false)
                .lastUsedDate(LocalDate.now().minusDays(1L))
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        verify(parameterRepository, never()).deleteById(testParam1.getId());
    }


    @Test
    void dontPurgeIfInUse() {
        // Given
        Parameter testParam1 = Parameter.builder()
                .id(1L)
                .parameterName("TestParam2")
                .parameterType(SupParameterType.STRING)
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .parameterValue("Hello world!")
                .creationDate(LocalDate.now())
                .inUse(true)
                .lastUsedDate(LocalDate.now().minusDays(100L))
                .updateDate(LocalDateTime.now())
                .build();
        doReturn(List.of(testParam1)).when(parameterRepository).findAll();
        // When
        underTest.purge();

        // Then
        verify(parameterRepository, never()).deleteById(testParam1.getId());
    }


}