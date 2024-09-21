package mate.academy.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementsystem.service.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Attachment managing", description = "Endpoints for managing attachments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Operation(summary = "Upload attachment", description = "Upload an attachment to a task")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AttachmentDto upload(
            @RequestParam("taskId") Long taskId,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return attachmentService.upload(taskId, file.getOriginalFilename(), file.getInputStream());
    }

    @Operation(summary = "Retrieve attachments",
            description = "Retrieve attachments for a task")
    @GetMapping
    public List<AttachmentDto> getAllComments(@RequestParam Long taskId) {
        return attachmentService.getAttachments(taskId);
    }
}
