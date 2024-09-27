package mate.academy.taskmanagementsystem.util;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Label> fillExpectedLabelList() {
        List<Label> expected = new ArrayList<>();
        expected.add(new Label().setId(1L).setName("label 1").setColor("green"));
        expected.add(new Label().setId(2L).setName("label 2").setColor("yellow"));
        expected.add(new Label().setId(3L).setName("label 3").setColor("red"));
        return expected;
    }
}
