package org.sli.orient.importer;

import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.orientechnologies.orient.graph.gremlin.OGremlinHelper;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import org.sli.orient.importer.importers.BaseImporter;
import org.sli.orient.importer.importers.CohortImporter;
import org.sli.orient.importer.importers.EdOrgImporter;
import org.sli.orient.importer.importers.ProgramImporter;
import org.sli.orient.importer.importers.SectionImporter;
import org.sli.orient.importer.importers.StaffCohortEdgeImporter;
import org.sli.orient.importer.importers.StaffProgramEdgeImporter;
import org.sli.orient.importer.importers.StudentCohortEdgeImporter;
import org.sli.orient.importer.importers.StudentImporter;
import org.sli.orient.importer.importers.StudentProgramEdgeImporter;
import org.sli.orient.importer.importers.StudentSectionEdgeImporter;
import org.sli.orient.importer.importers.TeacherImporter;
import org.sli.orient.importer.importers.TeacherSchoolEdgeImporter;
import org.sli.orient.importer.importers.TeacherSectionEdgeImporter;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger log = Logger.getLogger("Orient-Importer");
    private static int count = 0;
    private static long totalTime = 0;
    public static void main( String[] args )
    {
        Mongo m = null;
        OrientGraph g = new OrientGraph("local:graph/");
        g.createKeyIndex("mongoid", Vertex.class);
        // Clear the graph first.
        try {
            m = new Mongo("127.0.0.1", 27017);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MongoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DB db = m.getDB("sli");
        BaseImporter importer = new EdOrgImporter(db, g);
        benchmarkStamp(importer);

        importer = new SectionImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new CohortImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new ProgramImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new StudentImporter(db, g);
        benchmarkStamp(importer);

        importer = new TeacherImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new TeacherSchoolEdgeImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new StudentProgramEdgeImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new StudentCohortEdgeImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new StudentSectionEdgeImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new StaffProgramEdgeImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new StaffCohortEdgeImporter(db, g);
        benchmarkStamp(importer);
        
        importer = new TeacherSectionEdgeImporter(db, g);
        benchmarkStamp(importer);

        
        if (totalTime > 0 && count > 0) {
            
            log.log(Level.INFO, "\t Avg {0}\tTotal {1}\tTotal Records: {2}", new String[] { "" + totalTime / count,
                    "" + totalTime, "" + count });
        }
        
        OGremlinHelper.global().create();
        List<Vertex> result = g.getRawGraph().command(new OCommandGremlin("g.V[0..5]")).execute();
        
        g.shutdown();
        m.close();
    }
    
    private static void benchmarkStamp(BaseImporter stamper) {
        log.log(Level.INFO, "Starting to import {0}", stamper.getClass().getName());
        long startTime = System.currentTimeMillis();
        stamper.importCollection();
        long endTime = System.currentTimeMillis() - startTime;
        log.log(Level.INFO, "{0} in {1} ms", new String[] { stamper.getClass().getName(), "" + endTime });
        totalTime += endTime;
        count++;
    }
}
