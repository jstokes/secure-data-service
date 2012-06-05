package org.slc.sli.api.resources.config;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.xml.XmlMapper;

import org.codehaus.jackson.map.SerializationConfig;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.springframework.stereotype.Component;


/**
 * Custom Context Resolver that will generate XML for objects
 * other than entities
 *
 * */
@SuppressWarnings("rawtypes")
@Provider
@Component
@Produces({ MediaType.APPLICATION_XML+";charset=utf-8", HypermediaType.VENDOR_SLC_XML+";charset=utf-8" })
public class ObjectXMLWriter implements MessageBodyWriter {

    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (type.getName().equals("org.slc.sli.api.representation.Home")
                || type.getName().equals("org.slc.sli.api.representation.ErrorResponse")) {
            return true;
        }

        return false;
    }

    @Override
    public long getSize(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object o, Class type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        xmlMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        xmlMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        xmlMapper.writeValue(entityStream, o);
    }
}
