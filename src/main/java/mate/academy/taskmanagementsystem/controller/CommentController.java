package mate.academy.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.comment.CommentDto;
import mate.academy.taskmanagementsystem.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementsystem.model.User;
import mate.academy.taskmanagementsystem.service.CommentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment managing", description = "Endpoints for managing comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Create comment", description = "Add a new comment to a task")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentDto createComment(Authentication authentication,
                                    @RequestBody @Valid CreateCommentRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return commentService.create(user.getId(), requestDto);
    }

    @Operation(summary = "Retrieve comments for a task",
            description = "Retrieve all comments for a task")
    @GetMapping
    public List<CommentDto> getAllComments(@RequestParam Long taskId,
                                           @ParameterObject @PageableDefault Pageable pageable) {
        return commentService.getAllComments(taskId, pageable);
    }
}
