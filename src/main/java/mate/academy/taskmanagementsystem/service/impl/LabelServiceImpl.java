package mate.academy.taskmanagementsystem.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.exception.EntityNotFoundException;
import mate.academy.taskmanagementsystem.mapper.LabelMapper;
import mate.academy.taskmanagementsystem.model.Label;
import mate.academy.taskmanagementsystem.repository.label.LabelRepository;
import mate.academy.taskmanagementsystem.service.LabelService;
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
        if (labelRepository.existsById(id)) {
            Label label = labelMapper.toModel(requestDto);
            label.setId(id);
            return labelRepository.save(label);
        } else {
            throw new EntityNotFoundException("Can't find label by id " + id);
        }
    }

    @Override
    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
