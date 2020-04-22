package com.example.codec.protobuf;

import com.example.codec.protobuf.MessageHeadersProtos.MessageHeaders;
import com.google.common.io.CharStreams;
import com.google.protobuf.AbstractMessageLite;
import io.scalecube.services.exceptions.MessageCodecException;
import io.scalecube.services.transport.api.DataCodec;
import io.scalecube.services.transport.api.HeadersCodec;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ProtobufCodec implements HeadersCodec, DataCodec {

    public static final String CONTENT_TYPE = "application/protobuf";

    private static final String PARSE_METHOD = "parseFrom";

    @Override
    public String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void encode(OutputStream stream, Object value) throws IOException {
        if (value instanceof String) {
            // TODO use a proto string
            stream.write(((String) value).getBytes(StandardCharsets.UTF_8));
        } else {
            stream.write(((AbstractMessageLite<?, ?>) value).toByteArray());
        }
    }

    @Override
    public void encode(OutputStream stream, Map<String, String> headers) throws IOException {
        byte[] bytes = MessageHeaders.newBuilder().putAllHeaders(headers).build().toByteArray();
        stream.write(bytes);
    }

    @Override
    public Object decode(InputStream stream, Type type) throws IOException {
        try {
            Class<?> clazz = null;
            if (type instanceof Class<?>) {
                clazz = (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                clazz = Class.forName(((ParameterizedType) type).getRawType().getTypeName());
            }
            Class<?> aClass = requireNonNull(clazz);

            if (aClass.isAssignableFrom(String.class)) {
                // TODO use a proto string
                try (final Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    return CharStreams.toString(reader);
                }
            }

            Method parseFrom = aClass.getMethod(PARSE_METHOD, InputStream.class);
            return parseFrom.invoke(null, stream);
        } catch (ClassNotFoundException | NoSuchMethodException |
            IllegalAccessException | InvocationTargetException e) {
            throw new MessageCodecException("Couldn't decode message", e);
        }
    }

    @Override
    public Map<String, String> decode(InputStream stream) throws IOException {
        return MessageHeaders.parseFrom(stream).getHeadersMap();
    }

}
