package com.dreamplume.sell.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 将 Date 数据类型转换成 Long 类型
 * @Classname DateSerializer
 * @Description TODO
 * @Date 2022/4/20 9:52
 * @Created by 翊
 */
public class DateToLongSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(date.getTime() / 1000);
    }
}
