package org.slc.sli.ingestion.landingzone;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.MessageSource;

import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.landingzone.validation.SubmissionLevelException;
import org.slc.sli.ingestion.util.spring.MessageSourceHelper;

public class ControlFileFactory {

    public ControlFile parse(File file) throws IOException, SubmissionLevelException {
        return parse(file, null, null);
    }

    public ControlFile parse(File file, LandingZone landingZone, MessageSource messageSource) throws IOException, SubmissionLevelException {

        Scanner scanner = new Scanner(file);
        Pattern fileItemPattern = Pattern.compile("^([^\\s^,]+)\\,([^\\s^,]+)\\,([^,]+)\\,(\\w+)\\s*$");
        Matcher fileItemMatcher;
        Pattern configItemPattern = Pattern.compile("^@(.*)$");
        Matcher configItemMatcher;
        String line;
        FileFormat fileFormat;
        FileType fileType;
        int lineNumber = 0;

        String topLevelLandingZonePath = null;
        if (landingZone != null) {
            topLevelLandingZonePath = landingZone.getLZId();
        }

        Properties configProperties = new Properties();
        ArrayList<IngestionFileEntry> fileEntries = new ArrayList<IngestionFileEntry>();

        debug("parsing control file: {}", file);

        try {
            while (scanner.hasNextLine()) {

                line = scanner.nextLine();
                debug("scanned next line: {}", line);

                fileItemMatcher = fileItemPattern.matcher(line);
                if (fileItemMatcher.matches()) {
                    fileFormat = FileFormat.findByCode(fileItemMatcher.group(1));
                    fileType = FileType.findByNameAndFormat(fileItemMatcher.group(2), fileFormat);
                    fileEntries.add(new IngestionFileEntry(fileFormat, fileType, fileItemMatcher.group(3),
                            fileItemMatcher.group(4), topLevelLandingZonePath));
                    lineNumber += 1;
                    continue;
                }

                configItemMatcher = configItemPattern.matcher(line);
                if (configItemMatcher.matches()) {
                    configProperties.load(new ByteArrayInputStream(configItemMatcher.group(1).trim().getBytes()));
                    info("Control file configuration loaded: {}", configItemMatcher.group(1).trim());
                    lineNumber += 1;
                    continue;
                }

                // blank lines are ignored silently, but stray marks are not
                if (line.trim().length() > 0) {
                    // line was not parseable
                    lineNumber += 1;
                    String errorMessage;
                    if (messageSource != null) {
                         errorMessage = MessageSourceHelper.getMessage(messageSource, "SL_ERR_MSG16", lineNumber, line);
                    } else {
                        errorMessage = "Invalid control file entry at line number [" + lineNumber + "] Line:" + line;
                    }
                    throw new SubmissionLevelException(errorMessage);
                }
            }

            return new ControlFile(file, fileEntries, configProperties);

        } finally {
            scanner.close();
        }
    }

}
