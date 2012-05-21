package org.slc.sli.sample.transform;

import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;

public class CcsCsvReader {

    private static Pattern commaAndSpaces = Pattern.compile("^[\\s,]*$");

    private CSVParser csvParser;
    private Map<String, String> currentRecord;
    private String[] firstLine;
    private String copyright;

    CcsCsvReader(String filename) throws Exception {
        File file = new File(filename);
        file = removeEmptyLinesFromCsv(file);
        copyright = removeTrailingCharacters(tail(file), ',');
        file = removeLastLine(file);
        csvParser = new CSVParser(new FileReader(file), CSVStrategy.EXCEL_STRATEGY);

        firstLine = csvParser.getLine();
        getNextRecord();
    }
    
    void getNextRecord() throws IOException {
        String[] line = csvParser.getLine();
        if(line == null) {
            currentRecord = null;
            return;
        }
        Map<String, String> record = new HashMap<String, String>();
        for(int i=0; i<firstLine.length; i++) {
            record.put(firstLine[i], line[i]);
        }
        currentRecord = record;
    }
    
    Map<String, String> getCurrentRecord() {
        return currentRecord;
    }
    
    String getCopyright() {
        return copyright;
    }
    
    private String tail(File file) {
        try {
            RandomAccessFile fileHandler = new RandomAccessFile( file, "r" );
            long fileLength = file.length() - 1;
            StringBuilder sb = new StringBuilder();

            for( long filePointer = fileLength; filePointer != -1; filePointer-- ) {
                fileHandler.seek( filePointer );
                int readByte = fileHandler.readByte();

                if( readByte == 0xA ) {
                    if( filePointer == fileLength ) {
                        continue;
                    } else {
                        break;
                    }
                } else if( readByte == 0xD ) {
                    if( filePointer == fileLength - 1 ) {
                        continue;
                    } else {
                        break;
                    }
                }

                sb.append( ( char ) readByte );
            }
            fileHandler.close();

            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch( java.io.FileNotFoundException e ) {
            e.printStackTrace();
            return null;
        } catch( java.io.IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    private String removeTrailingCharacters(String s, char c) {
        int lastNonCommaIndex = -1;
        for(int i = s.length() - 1; i >= 0; i--) {
            if(s.charAt(i) != c) {
                lastNonCommaIndex = i;
                break;
            }
        }
        if(lastNonCommaIndex != -1) {
            return s.substring(0, lastNonCommaIndex + 1);
        }
        return "";
    }

    private File removeLastLine(File fileIn) throws Exception {
        File tempFile = File.createTempFile("temp", "txt");
        tempFile.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        BufferedReader reader = new BufferedReader(new FileReader(fileIn));
        
        String previousLine = reader.readLine();
        if(previousLine != null) {
            for(;;) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }
                else {
                    writer.write(previousLine);
                    writer.write('\n');
                    previousLine = line;
                }
            }
        }
        reader.close();
        writer.close();
        return tempFile;
    }

    private File removeEmptyLinesFromCsv(File csvFileIn) throws Exception {
        File tempFile = File.createTempFile("temp", "txt");
        tempFile.deleteOnExit();
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        BufferedReader reader = new BufferedReader(new FileReader(csvFileIn));
        for(;;) {
            String line = reader.readLine();
            if(line == null) {
                break;
            }
            else if("".equals(line.trim())) {
                continue;
            }
            else if(!commaAndSpaces.matcher(line).matches()){
                writer.write(line);
                writer.write('\n');
            }
        }
        reader.close();
        writer.close();
        return tempFile;
    }
}
