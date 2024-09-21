package mate.academy.taskmanagementsystem.service.internal;

import java.io.InputStream;
import java.util.List;
import mate.academy.taskmanagementsystem.dto.attachment.AttachmentDto;

public interface AttachmentService {
    AttachmentDto upload(Long taskId, String filename, InputStream inputStream);

    List<AttachmentDto> getAttachments(Long taskId);
}
