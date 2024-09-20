package mate.academy.taskmanagementsystem.service;

import java.util.List;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.model.Label;

public interface LabelService {
    Label create(LabelRequestDto requestDto);

    List<Label> getAll();

    Label update(Long id, LabelRequestDto requestDto);

    void delete(Long id);
}
