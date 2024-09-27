package mate.academy.taskmanagementsystem.service;

import static mate.academy.taskmanagementsystem.util.AttachmentTestUtil.createTestAttachment;
import static mate.academy.taskmanagementsystem.util.AttachmentTestUtil.createTestAttachmentDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import mate.academy.taskmanagementsystem.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementsystem.mapper.AttachmentMapper;
import mate.academy.taskmanagementsystem.model.Attachment;
import mate.academy.taskmanagementsystem.repository.attachment.AttachmentRepository;
import mate.academy.taskmanagementsystem.service.internal.impl.AttachmentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceTest {
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AttachmentMapper attachmentMapper;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Test
    @DisplayName("""
            Verify that correct Attachment list was returned when calling
            getAttachments() method""")
    public void getAttachments_WithValidTaskId_ShouldReturnCorrectCommentList() {
        //Given
        Attachment attachment = createTestAttachment();
        List<Attachment> attachmentList = List.of(attachment);
        AttachmentDto attachmentDto = createTestAttachmentDto(attachment);
        List<AttachmentDto> expected = List.of(attachmentDto);
        Long taskId = attachmentDto.getTaskId();

        when(attachmentRepository.findAllByTaskId(taskId)).thenReturn(attachmentList);
        when(attachmentMapper.toDtoList(attachmentList)).thenReturn(expected);
        //When
        List<AttachmentDto> actual = attachmentService.getAttachments(taskId);
        //Then
        assertEquals(expected, actual);

        verify(attachmentRepository, times(1)).findAllByTaskId(taskId);
        verify(attachmentMapper, times(1)).toDtoList(attachmentList);
        verifyNoMoreInteractions(attachmentRepository, attachmentMapper);
    }
}
