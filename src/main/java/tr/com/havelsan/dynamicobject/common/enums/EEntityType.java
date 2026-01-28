package tr.com.havelsan.dynamicobject.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@JsonDeserialize(using = GenericEnumDeserializer.class)
public enum EEntityType implements JsonEnum {

    CGF("CGF","Cgf"),
    OWNSHIP("OWNSHIP","Ownship");

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
