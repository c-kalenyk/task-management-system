package mate.academy.taskmanagementsystem.util;

import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.model.Label;

public class LabelTestUtil {
    public static Label createTestLabel() {
        Label label = new Label();
        label.setId(1L);
        label.setName("name");
        label.setColor("color");
        return label;
    }

    public static LabelRequestDto createTestLabelRequestDto(Label label) {
        LabelRequestDto requestDto = new LabelRequestDto();
        requestDto.setName(label.getName());
        requestDto.setColor(label.getColor());
        return requestDto;
    }
}
