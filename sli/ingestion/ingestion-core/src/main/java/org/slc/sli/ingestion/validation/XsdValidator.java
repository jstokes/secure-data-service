package org.slc.sli.ingestion.validation;

import java.io.File;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.Resource;
import org.xml.sax.ErrorHandler;

import org.slc.sli.ingestion.landingzone.IngestionFileEntry;

/**
 *
 * @author ablum
 *
 */
public class XsdValidator extends SimpleValidator<IngestionFileEntry> {

	private Map<String, Resource> xsd;

	@Override
	public boolean isValid(IngestionFileEntry ingestionFileEntry, ErrorReport errorReport){
		boolean isValid = false;
       try{
		  SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
          File schemaFile = new File("Interchange-Student.xsd");
          Schema schema = schemaFactory.newSchema(schemaFile);
          Validator validator = schema.newValidator();
          String sourceXml = "InterchangeStudent.xml";
          Source sc = new StreamSource(sourceXml);
          ErrorHandler myHandler = new XsdErrorHandler();
          validator.setErrorHandler(myHandler);
          validator.validate(sc);
          isValid = true;

          } catch (Exception ex) {
              System.out.println(ex.getMessage());
              isValid = false;
          }

       return isValid;
	}


}
