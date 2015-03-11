package org.fellesprosjekt.gruppe24.common.Kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends Serializer<LocalDateTime>{
    @Override
    public void write(Kryo kryo, Output output, LocalDateTime localDateTime) {
        output.writeString(localDateTime.toString());
    }

    @Override
    public LocalDateTime read(Kryo kryo, Input input, Class<LocalDateTime> aClass) {
        return LocalDateTime.parse(input.readString());
    }
}
