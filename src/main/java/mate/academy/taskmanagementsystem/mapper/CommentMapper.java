package mate.academy.taskmanagementsystem.mapper;

import java.util.Collection;
import java.util.List;
import mate.academy.taskmanagementsystem.config.MapperConfig;
import mate.academy.taskmanagementsystem.dto.comment.CommentDto;
import mate.academy.taskmanagementsystem.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementsystem.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "userId", source = "user.id")
    CommentDto toDto(Comment comment);

    Comment toModel(CreateCommentRequestDto requestDto);

    List<CommentDto> toDtoList(Collection<Comment> comments);
}
