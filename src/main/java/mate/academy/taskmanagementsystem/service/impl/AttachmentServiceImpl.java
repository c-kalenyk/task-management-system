package mate.academy.taskmanagementsystem.service.impl;

import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.exception.FileUploadException;
import mate.academy.taskmanagementsystem.mapper.AttachmentMapper;
import mate.academy.taskmanagementsystem.model.Attachment;
import mate.academy.taskmanagementsystem.model.Task;
import mate.academy.taskmanagementsystem.repository.attachment.AttachmentRepository;
import mate.academy.taskmanagementsystem.repository.task.TaskRepository;
import mate.academy.taskmanagementsystem.service.AttachmentService;
import mate.academy.taskmanagementsystem.service.DropboxClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final TaskRepository taskRepository;
    private final DropboxClient dropboxClient;

    @Override
    public AttachmentDto upload(Long taskId, String filename, InputStream inputStream) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new EntityNotFoundException("Can't find task by id: " + taskId));
        String dropboxFileId;
        try {
            dropboxFileId = dropboxClient.uploadFile(filename, inputStream);
            Attachment attachment = new Attachment();
            attachment.setTask(task);
            attachment.setDropboxFileId(dropboxClient.getFileLink(dropboxFileId));
            attachment.setFilename(filename);
            return attachmentMapper.toDto(attachmentRepository.save(attachment));
        } catch (Exception e) {
            throw new FileUploadException("Can't upload file: " + filename);
        }
    }

    @Override
    public List<AttachmentDto> getAttachments(Long taskId) {
        return attachmentMapper.toDtoList(attachmentRepository.findAllByTaskId(taskId));
    }
}
