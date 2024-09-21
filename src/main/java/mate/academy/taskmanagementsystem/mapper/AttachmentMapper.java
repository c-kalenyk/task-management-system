package mate.academy.taskmanagementsystem.mapper;

import java.util.List;
import mate.academy.taskmanagementsystem.config.MapperConfig;
import mate.academy.taskmanagementsystem.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementsystem.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {
    @Mapping(target = "taskId", source = "task.id")
    AttachmentDto toDto(Attachment attachment);

    List<AttachmentDto> toDtoList(List<Attachment> attachments);
}
