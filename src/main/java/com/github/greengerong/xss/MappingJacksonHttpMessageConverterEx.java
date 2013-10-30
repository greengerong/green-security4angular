package com.github.greengerong.xss;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;

public class MappingJacksonHttpMessageConverterEx extends AbstractHttpMessageConverter<Object> {
    private static final Logger logger = Logger.getLogger(MappingJacksonHttpMessageConverterEx.class);
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private ObjectMapper objectMapper = new ObjectMapper();

    private String prefixJson = ")]}',\n";


    public MappingJacksonHttpMessageConverterEx() {
        super(new MediaType("application", "json", DEFAULT_CHARSET));
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "'objectMapper' must not be null");
        this.objectMapper = objectMapper;
    }

    public void setPrefixJson(String prefixJson) {
        this.prefixJson = prefixJson;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        JavaType javaType = getJavaType(clazz);
        return this.objectMapper.canDeserialize(javaType) && canRead(mediaType);
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.type(clazz);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return this.objectMapper.canSerialize(clazz) && canWrite(mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // should not be called, since we override canRead/Write instead
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(clazz);
        try {
            return this.objectMapper.readValue(inputMessage.getBody(), javaType);
        } catch (JsonParseException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        JsonEncoding encoding = getEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator jsonGenerator =
                this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
        try {
            if (StringUtils.isNotBlank(prefixJson)) {
                jsonGenerator.writeRaw(prefixJson);
            }
            this.objectMapper.writeValue(jsonGenerator, o);
        } catch (JsonGenerationException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    private JsonEncoding getEncoding(MediaType contentType) {
        if (contentType != null && contentType.getCharSet() != null) {
            Charset charset = contentType.getCharSet();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }
}

