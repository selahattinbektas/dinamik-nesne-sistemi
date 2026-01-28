package tr.com.havelsan.dynamicobject.common.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Arrays;

public class GenericEnumDeserializer
        extends StdDeserializer<Enum<?>>
        implements ContextualDeserializer {

    private Class<? extends Enum<?>> enumType;

    public GenericEnumDeserializer() {
        super(Enum.class);
    }

    private GenericEnumDeserializer(Class<? extends Enum<?>> enumType) {
        super(enumType);
        this.enumType = enumType;
    }

    @Override
    public JsonDeserializer<?> createContextual(
            DeserializationContext ctxt,
            BeanProperty property
    ) {
        JavaType type = ctxt.getContextualType();
        return new GenericEnumDeserializer(
                (Class<? extends Enum<?>>) type.getRawClass()
        );
    }

    @Override
    public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        String value = p.getValueAsString();

        return Arrays.stream(enumType.getEnumConstants())
                .filter(e ->
                        e instanceof JsonEnum jsonEnum &&
                                jsonEnum.getJsonValue().equalsIgnoreCase(value)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid enum code: " + value +
                                        " for enum " + enumType.getSimpleName()
                        ));
    }
}
