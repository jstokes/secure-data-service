package org.sli.orient.importer.importers;

import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class BaseImporter implements Importer {
    private static Logger logger = Logger.getLogger("BaseImporter");
    protected DB mongo;
    protected Graph graph;
    
    public BaseImporter(DB mongo, Graph graph) {
        this.graph = graph;
        this.mongo = mongo;
    }
    
    public void importCollection() {
    }

    protected boolean vertexExists(String id) {
        boolean hasVertex = false;
        for (Vertex v : graph.getVertices("mongoid", id)) {
            hasVertex = true;
        }
        return hasVertex;
    }
    protected void extractBasicNode(String collectionName) {
        DBCursor cursor = mongo.getCollection(collectionName).find();
        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            Vertex v = graph.addVertex(null);
            v.setProperty("mongoid", item.get("id"));
        }
    }
}
