package mate.academy.taskmanagementsystem.mapper;

import mate.academy.taskmanagementsystem.config.MapperConfig;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.model.Label;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    Label toModel(LabelRequestDto requestDto);
}
