package mate.academy.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.model.Label;
import mate.academy.taskmanagementsystem.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Label managing", description = "Endpoints for managing labels")
@RestController
@RequiredArgsConstructor
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;

    @Operation(summary = "Create label", description = "Create a new label")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Label createLabel(@RequestBody @Valid LabelRequestDto requestDto) {
        return labelService.create(requestDto);
    }

    @Operation(summary = "Retrieve labels", description = "Retrieve all labels")
    @GetMapping
    public List<Label> getAllLabels() {
        return labelService.getAll();
    }

    @Operation(summary = "Update label", description = "Update the label")
    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable Long id,
                             @RequestBody LabelRequestDto requestDto) {
        return labelService.update(id, requestDto);
    }

    @Operation(summary = "Delete label", description = "Delete the label")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.delete(id);
    }
}
