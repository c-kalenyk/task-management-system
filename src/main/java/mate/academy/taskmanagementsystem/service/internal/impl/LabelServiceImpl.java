package mate.academy.taskmanagementsystem.service.internal.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.LabelMapper;
import mate.academy.taskmanagementsystem.model.Label;
import mate.academy.taskmanagementsystem.repository.label.LabelRepository;
import mate.academy.taskmanagementsystem.service.internal.LabelService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public Label create(LabelRequestDto requestDto) {
        return labelRepository.save(labelMapper.toModel(requestDto));
    }

    @Override
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label update(Long id, LabelRequestDto requestDto) {
        Label label = labelRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find label by id: " + id));
        label.setName(requestDto.getName());
        label.setColor(requestDto.getColor());
        return labelRepository.save(label);
    }

    @Override
    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
