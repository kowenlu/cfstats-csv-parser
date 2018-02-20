package com.jl.CfStatsParser.utils;

import com.jl.CfStatsParser.ImportCFStats;
import com.jl.CfStatsParser.ParseCFStats;

import java.io.File;
import java.util.*;

import static com.jl.CfStatsParser.utils.FileUtils.writeCFStatsToFile;

public class ScanCFStatsFolder {

    public static final List<String> CF_ANALYSIS_LIST = Collections.unmodifiableList(new ArrayList<String>(){{
        add("ColumnFamilyA"); // Add your columnfamily here
    }});

    public static void main(String[] args){
        if (args.length < 1 || args[0].equals("")){
            System.out.println("Please pass in a fully qualified path to the csfstats source folder");
            return;
        }

        String inputFolderPath = args[0];
        String outputFolderPath = inputFolderPath + "output/";

        System.out.println("Loading folder: "+ inputFolderPath);
        final File folder = new File(inputFolderPath);

        ArrayList<File> fileList = getFiles(folder);
        createOutputFolder(outputFolderPath);



        HashMap<String, List<String[]>> analysisResultMap = new HashMap<String, List<String[]>>();
        for (String CFName : CF_ANALYSIS_LIST) {
            List<String[]> result = new ArrayList<String[]>();
            String[] header = new String[4];
            header[0] = "Timestamp";
            header[1] = CFName + " Read Count";
            header[2] = CFName + " Read Latency";
            header[3] = CFName + " Pending task";
            result.add(header);
            analysisResultMap.put(CFName, result);
        }

        // Iterate all input files
        for (final File inputFile : fileList) {
            // Given a CF name, get the line number
            HashMap<String, Integer> CFAndLineMapping = new HashMap<String, Integer>();
            String filePath = inputFile.getAbsolutePath();

            String timestamp = filePath.split("-")[(filePath.split("-").length-1)];

            // Analysis per file
            ImportCFStats iCfs = new ImportCFStats();
            ParseCFStats pCfs = new ParseCFStats(iCfs.importFile(filePath));
            pCfs.parseCFStats(CFAndLineMapping);

            List<String[]> result =  pCfs.getCsvList();
            writeCFStatsToFile(result,outputFolderPath + inputFile.getName() + ".csv"); // Save to each output csv file


            if (result.size()< 100) { // Too small file - may be invalid
                System.out.println("Can not parse:" + filePath + ". Skipped.");
                continue;
            }

            // Analysis per CF
            for (String CFName : CF_ANALYSIS_LIST) {
                List<String[]> csvResult = analysisResultMap.get(CFName);

                String[] csvRow = new String[4];
                csvRow[0] = timestamp;

                Integer indexInFile = CFAndLineMapping.get(CFName);
                if (indexInFile <= 0) {
                    System.out.println("Can not find a line for CF '" + CFName + "'. Skipped");
                    continue;
                }

                String CFNameInFile = result.get(indexInFile)[1];
                if (!CFNameInFile.equals(CFName)) {
                    System.out.println("Wrong line for CF '" + CFName + "'. Skipped");
                    continue;
                }

                csvRow[1] = result.get(indexInFile)[2]; // readCount
                csvRow[2] = result.get(indexInFile)[3]; // readLatency
                csvRow[3] = result.get(indexInFile)[6]; // pending task
                csvResult.add(csvRow);
            }

        }

        // Save Analysis per CF
        for (String CFName : CF_ANALYSIS_LIST) {
            List<String[]> csvResult = analysisResultMap.get(CFName);

            if (csvResult == null) {
                System.out.println("Error: No result for CF '" + CFName + "'");
                continue;
            }

            String analysisOutputFile = outputFolderPath + CFName + "_summary_readcount.csv";
            writeCFStatsToFile(csvResult,analysisOutputFile);
            System.out.println("Analysis file saved: " + analysisOutputFile);
        }
    }

    private static ArrayList<File> getFiles(File folder) {
        ArrayList<File> fileList = new ArrayList<File>();
        File[] files = folder.listFiles();
        Arrays.sort(files);
        for (final File fileEntry : files) {

            if (fileEntry.isHidden() || fileEntry.isDirectory()) continue;

            System.out.println("Found: " + fileEntry.getAbsolutePath());
            fileList.add(fileEntry);
        }

        return fileList;
    }

    private static void createOutputFolder(String outputFolderPath) {
        new File(outputFolderPath).mkdir();
    }
}
