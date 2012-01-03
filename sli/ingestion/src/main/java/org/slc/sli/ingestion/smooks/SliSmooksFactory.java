package org.slc.sli.ingestion.smooks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.milyn.Smooks;
import org.milyn.delivery.Visitor;
import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.NeutralRecordFileWriter;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.xml.sax.SAXException;

/**
 * Factory class for Smooks
 *
 * @author dduran
 *
 */
public class SliSmooksFactory {

    private Map<FileType, SliSmooksConfig> sliSmooksConfigMap;
    private String beanId;

    public Smooks createInstance(FileType fileType, NeutralRecordFileWriter fileWriter, ErrorReport errorReport)
            throws IOException, SAXException {

        SliSmooksConfig sliSmooksConfig = sliSmooksConfigMap.get(fileType);
        if (sliSmooksConfig != null) {

            return createSmooksFromConfig(sliSmooksConfig, fileWriter, errorReport);

        } else {
            throw new IllegalArgumentException("File type not supported : " + fileType);
        }
    }

    private Smooks createSmooksFromConfig(SliSmooksConfig sliSmooksConfig, NeutralRecordFileWriter fileWriter,
            ErrorReport errorReport) throws IOException, SAXException {
        Smooks smooks = new Smooks(sliSmooksConfig.getConfigFileName());

        // based on target selectors for this file type, add visitors
        List<String> targetSelectorList = sliSmooksConfig.getTargetSelectors();
        if (targetSelectorList != null) {

            // just one visitor instance that can be added with multiple target selectors
            Visitor smooksEdFiVisitor = SmooksEdFiVisitor.createInstance(beanId, fileWriter, errorReport);

            for (String targetSelector : targetSelectorList) {
                smooks.addVisitor(smooksEdFiVisitor, targetSelector);
            }
        }
        return smooks;
    }

    public void setSliSmooksConfigMap(Map<FileType, SliSmooksConfig> sliSmooksConfigMap) {
        this.sliSmooksConfigMap = sliSmooksConfigMap;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
}
