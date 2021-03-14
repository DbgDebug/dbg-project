package icu.dbgdev.pay.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    /**
     * 出参保留两位小数
     */
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (Objects.isNull(jsonParser.getDecimalValue())) {
            return null;
        } else {
            // 这里取floor
            return jsonParser.getDecimalValue().setScale(2, RoundingMode.FLOOR);
        }
    }
}

