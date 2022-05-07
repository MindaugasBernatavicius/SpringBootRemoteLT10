package cf.mindaugas.springbootremotelt10.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class BlogpostSerializer extends StdSerializer<BlogPost> {
    public BlogpostSerializer() {
        this(null);
    }
    public BlogpostSerializer(Class<BlogPost> t) {
        super(t);
    }

    @Override
    public void serialize(
            BlogPost blogPost,
            JsonGenerator jgen,
            SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        jgen.writeStartObject();
        jgen.writeNumberField("id", blogPost.getId());
        jgen.writeStringField("title", blogPost.getTitle());
        // jgen.writeStringField("text", blogPost.getText());
        jgen.writeEndObject();
    }
}