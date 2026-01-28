package tr.com.havelsan.dynamicobject.common.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonDeserialize(using = GenericEnumDeserializer.class)
public enum EComponentType implements JsonEnum {
    SelectComponent("SelectComponent", "Select"),
    ToggleComponent("ToggleComponent", "Toggle"),
    IntegerInputComponent("IntegerInputComponent", "IntegerInput"),
    FloatInputComponent("FloatInputComponent", "FloatInput"),
    PositionComponent("PositionComponent", "Position"),
    PositionWithHeightComponent("PositionWithHeightComponent", "PositionWithHeight"),
    RadioGroupComponent("RadioGroupComponent", "RadioGroup"),
    SliderComponent("SliderComponent", "Slider"),
    SwitchComponent("SwitchComponent", "Switch"),
    SegmentedSelectionComponent("SegmentedSelectionComponent", "SegmentedSelection"),
    TextInputComponent("TextInputComponent", "TextInput"),
    TimeComponent("TimeComponent", "Time"),
    HiddenComponent("HiddenComponent", "Hidden"),
    DirectionComponent("DirectionComponent", "Direction"),
    BuildingComponent("BuildingComponent", "Building");

    public final String code;
    public final String label;

    @Override
    public String getJsonValue() {
        return code;
    }

    @Override
    public String getJsonLabel() {
        return label;
    }
}