package org.slc.sli.scaffold.semantics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * Adds elements to the existing html to include links to the generated 
 * database schema documents.
 * 
 * @author jstokes
 *
 */
public class ResourceDocumenter {
            
    private static final String LINK_HTML = "<a href=\"$LINK\">$TYPE</a>";
    private static final String PROP_FILE = "/resource_mapping.properties";
    private static String baseUrl;
    private static Properties props;
    
    /**
     * Replaces schema tags with links to schema in generated html documentation
     * @param generatedHtml
     */
    public void addResourceMerge(File generatedHtml) {
        String content = "";
        content = readFile(generatedHtml);
        readPropertiesFile();
  
        for (Entry<Object, Object> entry : props.entrySet()) {
            content = addResource(content, entry);
        }
        
        writeFile(content, generatedHtml);
    }
    
    protected void readPropertiesFile() {
        try {
            props = new Properties();
            props.load(ResourceDocumenter.class.getResourceAsStream(PROP_FILE));
            baseUrl = (String) props.get("base_url");
            props.remove("base_url");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Helper function to write content to a file
     * @param content content to write
     * @param output output file
     */
    protected void writeFile(String content, File output) {
        try {
            IOUtils.write(content, new FileOutputStream(output));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to read content from a file
     * @param generatedHtml file to read from
     * @return the file as a string
     */
    protected String readFile(File generatedHtml) {
        String content = "";
        try {
            content = IOUtils.toString(new FileInputStream(generatedHtml));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    protected String addResource(String content, Entry<Object, Object> entry) {
        String value = (String) entry.getValue();
        String key = (String) entry.getKey();
        
        String link = (String) value.split(",")[0];
        String href = (String) value.split(",")[1];
        
        content = content.replace("$$" + key + "$$", createLink(link, href));
        
        return content;
    }
    
    protected String createLink(String key, String value) {
        String link = "";
        
        link = LINK_HTML.replace("$LINK", baseUrl + value);
        link = link.replace("$TYPE", key);
        
        return link;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        
        ResourceDocumenter resourceDoc = new ResourceDocumenter();
        resourceDoc.addResourceMerge(new File(args[0]));
    }
}
