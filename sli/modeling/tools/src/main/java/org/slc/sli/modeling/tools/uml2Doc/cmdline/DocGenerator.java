package org.slc.sli.modeling.tools.uml2Doc.cmdline;

import java.io.FileNotFoundException;

import org.slc.sli.modeling.uml.Type;
import org.slc.sli.modeling.uml.index.DefaultMapper;
import org.slc.sli.modeling.uml.index.Mapper;
import org.slc.sli.modeling.xmi.reader.XmiReader;

/**
 * A utility for converting XMI to Documentation.
 */
public final class DocGenerator {

    public static void main(final String[] args) {
        try {
            final Mapper model = new DefaultMapper(XmiReader.readModel("SLI.xmi"));
            final Documentation<Type> domains = DocumentationReader.readDocumentation("../data/domains.xml", model);
            DocumentationWriter.writeDocument(domains, model, "model-schema.xml");
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DocGenerator() {
        // Prevent instantiation, even through reflection.
        throw new RuntimeException();
    }
}
