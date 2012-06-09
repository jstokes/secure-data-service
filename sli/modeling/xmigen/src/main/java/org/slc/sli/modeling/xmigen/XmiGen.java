package org.slc.sli.modeling.xmigen;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.apache.ws.commons.schema.XmlSchema;

import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.index.DefaultModelIndex;
import org.slc.sli.modeling.uml.index.ModelIndex;
import org.slc.sli.modeling.xmi.writer.XmiWriter;
import org.slc.sli.modeling.xsd.XsdReader;

/**
 * A quick-n-dirty utility for converting W3C XML Schemas to XMI (with limitations).
 */
public final class XmiGen {

    private static final List<String> ARGUMENT_HELP = asList("h", "?");
    private static final String ARGUMENT_XSD = "xsdFile";
    private static final String ARGUMENT_XMI_FILE = "xmiFile";
    private static final String ARGUMENT_XMI_FOLDER = "xmiFolder";
    private static final String ARGUMENT_PLUGIN_NAME = "plugInName";

    public static void main(final String[] args) {
        final OptionParser parser = new OptionParser();
        final OptionSpec<?> helpSpec = parser.acceptsAll(ARGUMENT_HELP, "Show help");
        final OptionSpec<File> xsdFileSpec = optionSpec(parser, ARGUMENT_XSD, "XSD (input) file", File.class);
        final OptionSpec<String> outFileSpec = optionSpec(parser, ARGUMENT_XMI_FILE, "XMI (output) file", String.class);
        final OptionSpec<File> outFolderSpec = optionSpec(parser, ARGUMENT_XMI_FOLDER, "XMI (output) folder",
                File.class);
        final OptionSpec<String> plugInNameSpec = optionSpec(parser, ARGUMENT_PLUGIN_NAME, "PlugIn name", String.class);
        final OptionSet options = parser.parse(args);
        if (options.hasArgument(helpSpec)) {
            try {
                parser.printHelpOn(System.out);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                final File xsdFile = options.valueOf(xsdFileSpec);

                final File outFolder = options.valueOf(outFolderSpec);
                final String outFile = options.valueOf(outFileSpec);
                final File outLocation = new File(outFolder, outFile);
                // The platform-specific model provides the implementation mappings.
                final String plugInName = options.valueOf(plugInNameSpec);
                final Xsd2UmlPlugin plugIn = loadPlugIn(plugInName);
                final XmlSchema schema = XsdReader.readSchema(xsdFile, new Xsd2XmlResolver(xsdFile.getParentFile()));

                final Model model = Xsd2Uml.transform("", schema, plugIn);

                final ModelIndex modelIndex = new DefaultModelIndex(model);

                XmiWriter.writeDocument(model, modelIndex, outLocation);
            } catch (final FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final Xsd2UmlPlugin loadPlugIn(final String name) {
        try {
            final Class<?> clazz = Class.forName(name);
            final Class<? extends Xsd2UmlPlugin> factory = clazz.asSubclass(Xsd2UmlPlugin.class);
            try {
                return factory.newInstance();
            } catch (final InstantiationException e) {
                throw new RuntimeException(name, e);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(name, e);
            }
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(name, e);
        }

    }

    private static final <T> OptionSpec<T> optionSpec(final OptionParser parser, final String option,
            final String description, final Class<T> argumentType) {
        return parser.accepts(option, description).withRequiredArg().ofType(argumentType).required();
    }
}
