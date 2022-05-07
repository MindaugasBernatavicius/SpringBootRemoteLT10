package cf.mindaugas.springbootremotelt10.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.*;

public class CommentsSerializer extends StdSerializer<Set<Comment>> {
    public CommentsSerializer() {
        this(null);
    }
    public CommentsSerializer(Class<Set<Comment>> t) {
        super(t);
    }

    @Override
    public void serialize(
            Set<Comment> comments,
            JsonGenerator generator,
            SerializerProvider provider
    )
            throws IOException, JsonProcessingException
    {
//        Set<Long> ids = new HashSet<>();
//        for (Comment comment : comments)
//            ids.add(comment.getId());
//        generator.writeObject(ids);

        List<Map<Long, String>> idToTitleMaps = new ArrayList<>();
        for (Comment comment : comments)
            idToTitleMaps.add(new HashMap<>(){{ put(comment.getId(), comment.getText()); }});
        generator.writeObject(idToTitleMaps);
    }
}