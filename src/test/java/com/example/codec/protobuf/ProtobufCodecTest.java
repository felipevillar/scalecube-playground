package com.example.codec.protobuf;

import com.example.codec.protobuf.MessageHeadersProtos.MessageHeaders;
import com.example.protobuf.GreetingProtos.Greeting;
import io.scalecube.services.api.ServiceMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

class ProtobufCodecTest {

    private ProtobufCodec protobufCodec;

    @BeforeEach
    void setUp() {
        this.protobufCodec = new ProtobufCodec();
    }

    @Test
    void shouldEncodeAndDecodeProtoPayload() throws IOException {
        Greeting greeting = Greeting.newBuilder().setMessage("Hello world.").build();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        protobufCodec.encode(os, greeting);
        byte[] serializedBytes = os.toByteArray();
        assertThat(serializedBytes).isEqualTo(greeting.toByteArray());

        ByteArrayInputStream is = new ByteArrayInputStream(serializedBytes);
        Greeting deserializedGreeting = (Greeting) protobufCodec.decode(is, Greeting.class);
        assertThat(deserializedGreeting).isEqualTo(greeting);
    }

    @Test
    void shouldEncodeAndDecodeStringPayload() throws IOException {
        String name = "Jack";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        protobufCodec.encode(os, name);
        byte[] serializedBytes = os.toByteArray();
        assertThat(serializedBytes).isEqualTo(name.getBytes(StandardCharsets.UTF_8));

        ByteArrayInputStream is = new ByteArrayInputStream(serializedBytes);
        String deserializedString = (String) protobufCodec.decode(is, String.class);
        assertThat(deserializedString).isEqualTo(name);
    }

    @Test
    void shouldEncodeAndDecodeHeaders() throws IOException {
        final Map<String, String> map = ServiceMessage.builder()
            .dataFormat(ProtobufCodec.CONTENT_TYPE)
            .qualifier("/com.example/testService")
            .build().headers();

        final MessageHeaders headers = MessageHeaders.newBuilder()
            .putAllHeaders(map)
            .build();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        protobufCodec.encode(os, map);

        byte[] serializedBytes = os.toByteArray();
        assertThat(serializedBytes).isEqualTo(headers.toByteArray());

        ByteArrayInputStream is = new ByteArrayInputStream(serializedBytes);
        Map<String, String> deserializedMap = protobufCodec.decode(is);
        assertThat(deserializedMap).isEqualTo(map);
    }
}