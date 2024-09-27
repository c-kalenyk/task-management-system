package mate.academy.taskmanagementsystem.util;

import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTask;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.taskmanagementsystem.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementsystem.model.Attachment;

public class AttachmentTestUtil {
    public static Attachment createTestAttachment() {
        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setTask(createTestTask());
        attachment.setDropboxFileId("file id");
        attachment.setFilename("filename");
        attachment.setUploadDate(LocalDateTime.of(2024, 9, 27, 13, 46, 34));
        return attachment;
    }

    public static AttachmentDto createTestAttachmentDto(Attachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(attachment.getId());
        attachmentDto.setTaskId(attachment.getTask().getId());
        attachmentDto.setDropboxFileId(attachment.getDropboxFileId());
        attachmentDto.setFilename(attachment.getFilename());
        attachmentDto.setUploadDate(attachment.getUploadDate());
        return attachmentDto;
    }

    public static List<AttachmentDto> fillExpectedAttachmentDtoList() {
        List<AttachmentDto> expected = new ArrayList<>();
        expected.add(new AttachmentDto().setId(1L).setTaskId(1L).setDropboxFileId("1")
                .setFilename("file 1")
                .setUploadDate(LocalDateTime.of(2024, 9, 27, 13, 46, 34)));
        expected.add(new AttachmentDto().setId(2L).setTaskId(1L).setDropboxFileId("2")
                .setFilename("file 2")
                .setUploadDate(LocalDateTime.of(2024, 9, 27, 13, 46, 34)));
        expected.add(new AttachmentDto().setId(3L).setTaskId(1L).setDropboxFileId("3")
                .setFilename("file 3")
                .setUploadDate(LocalDateTime.of(2024, 9, 27, 13, 46, 34)));
        return expected;
    }
}
