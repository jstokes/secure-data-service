package org.slc.sli.shtick;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.DoubleNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.LongNode;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Intentionally package-protected.
 */
final class JacksonRestEntityDeserializer extends StdDeserializer<Entity> {
    
    public JacksonRestEntityDeserializer() {
        super(Entity.class);
    }
    
    private Map<String, Object> processObject(final ObjectNode obj) {
        Map<String, Object> rval = new HashMap<String, Object>();
        
        Iterator<String> it = obj.getFieldNames();
        
        while (it.hasNext()) {
            String key = it.next();
            JsonNode node = obj.get(key);
            rval.put(key, processElement(key, node));
        }
        
        return rval;
    }
    
    private Object processElement(final String key, final JsonNode element) {
        Object rval = null;
        if (element instanceof ObjectNode) {
            Map<String, Object> r2 = processObject((ObjectNode) element);
            rval = r2;
        } else if (element instanceof NullNode) {
            rval = null;
        } else if (element instanceof ArrayNode) {
            rval = processArray(key, (ArrayNode) element);
        } else {
            rval = processPrimitive(element);
        }
        
        return rval;
    }
    
    private List<Object> processArray(final String key, final ArrayNode asJsonArray) {
        List<Object> list = new LinkedList<Object>();
        
        Iterator<JsonNode> it = asJsonArray.getElements();
        while (it.hasNext()) {
            list.add(processElement(key, it.next()));
        }
        return list;
    }
    
    private Object processPrimitive(final JsonNode prim) {
        Object val;
        
        if (prim instanceof BooleanNode) {
            val = prim.getBooleanValue();
        } else if (prim instanceof DoubleNode) {
            val = prim.getDoubleValue();
        } else if (prim instanceof IntNode) {
            val = prim.getIntValue();
        } else if (prim instanceof LongNode) {
            val = prim.getLongValue();
        } else {
            val = prim.getTextValue();
        }
        return val;
    }
    
    @Override
    public Entity deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        ObjectNode root = (ObjectNode) mapper.readTree(parser);
        
        String entityType = null;
        
        if (root.has(Constants.ENTITY_TYPE_KEY)) {
            entityType = root.get(Constants.ENTITY_TYPE_KEY).getTextValue();
            root.remove(Constants.ENTITY_TYPE_KEY);
        }
        
        Map<String, Object> data = processObject(root);
        if (entityType != null) {
            return new Entity(entityType, data);
        } else {
            return new Entity("Generic", data);
        }
    }
}