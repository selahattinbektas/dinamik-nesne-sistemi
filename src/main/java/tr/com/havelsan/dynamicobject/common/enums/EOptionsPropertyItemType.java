package tr.com.havelsan.dynamicobject.common.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonDeserialize(using = GenericEnumDeserializer.class)
public enum EOptionsPropertyItemType implements JsonEnum {
    TEAM_TYPE("teamType", "teamType"),
    OPERATION_CONDITION("operationCondition", "operationCondition"),
    ACTIVE_STATUS("activeStatus", "activeStatus"),
    FREEZE_STATUS("freezeStatus", "freezeStatus");

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