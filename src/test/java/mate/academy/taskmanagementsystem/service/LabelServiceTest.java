package mate.academy.taskmanagementsystem.service;

import static mate.academy.taskmanagementsystem.util.LabelTestUtil.createTestLabel;
import static mate.academy.taskmanagementsystem.util.LabelTestUtil.createTestLabelRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.LabelMapper;
import mate.academy.taskmanagementsystem.model.Label;
import mate.academy.taskmanagementsystem.repository.label.LabelRepository;
import mate.academy.taskmanagementsystem.service.internal.impl.LabelServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private LabelMapper labelMapper;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    @DisplayName("Verify that correct Label was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnCorrectLabel() {
        //Given
        Label expected = createTestLabel();
        LabelRequestDto requestDto = createTestLabelRequestDto(expected);

        when(labelMapper.toModel(requestDto)).thenReturn(expected);
        when(labelRepository.save(expected)).thenReturn(expected);
        //When
        Label actual = labelService.create(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(labelMapper, times(1)).toModel(requestDto);
        verify(labelRepository, times(1)).save(expected);
        verifyNoMoreInteractions(labelMapper, labelRepository);
    }

    @Test
    @DisplayName("Verify that correct Label list was returned when calling getAll() method")
    public void getAll_WithValidRequest_ShouldReturnCorrectLabelList() {
        //Given
        List<Label> expected = List.of(createTestLabel());

        when(labelRepository.findAll()).thenReturn(expected);
        //When
        List<Label> actual = labelService.getAll();
        //Then
        assertEquals(expected, actual);

        verify(labelRepository, times(1)).findAll();
        verifyNoMoreInteractions(labelRepository);
    }

    @Test
    @DisplayName("Verify that correct Label was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidLabel() {
        //Given
        Label expected = createTestLabel();
        expected.setName("updated name");
        expected.setColor("updated color");
        LabelRequestDto requestDto = createTestLabelRequestDto(expected);
        Long id = expected.getId();

        when(labelRepository.existsById(id)).thenReturn(true);
        when(labelMapper.toModel(requestDto)).thenReturn(expected);
        when(labelRepository.save(expected)).thenReturn(expected);
        //When
        Label actual = labelService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(labelRepository, times(1)).existsById(id);
        verify(labelMapper, times(1)).toModel(requestDto);
        verify(labelRepository, times(1)).save(expected);
        verifyNoMoreInteractions(labelRepository, labelMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling update() method with invalid id")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        Label label = createTestLabel();
        LabelRequestDto requestDto = createTestLabelRequestDto(label);
        requestDto.setName("updated name");
        Long id = 2L;

        when(labelRepository.existsById(id)).thenReturn(false);
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> labelService.update(id, requestDto)
        );
        //Then
        String expected = "Can't find label by id " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(labelRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(labelRepository);
    }
}
