package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and other whitespace
        have been added for clarity).  Note the curly braces, square brackets, and double-quotes!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject();
            JSONArray colHeaders = new JSONArray();
            JSONArray rowHeaders = new JSONArray();
            JSONArray data = new JSONArray();
            JSONArray dataRow;
            String[] csvRow;
            
            
            /*This pulls the csv data out into an array called csvRow*/
            csvRow = full.get(0);/*gets csvRow out of full array*/
            
            for (int i = 0; i < csvRow.length; ++i){
                colHeaders.add(csvRow[i]);
            }
            
            for (int i = 1; i < full.size(); ++i){
                csvRow = full.get(i);
                rowHeaders.add(csvRow[0]);
                dataRow = new JSONArray();
                for (int j = 1; j < csvRow.length; ++j){
                    dataRow.add(Integer.parseInt(csvRow[j]));
                }
                
                data.add(dataRow);
            }
            jsonObject.put("colHeaders", colHeaders);
            jsonObject.put("rowHeaders", rowHeaders);
            jsonObject.put("data", data);
            
        
        
        
            results = JSONValue.toJSONString(jsonObject); 
            
        }
        
        catch(IOException e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        String[] csvRow;
       
        JSONArray colHeaders;
        JSONArray rowHeaders;
        JSONArray data;
        
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            colHeaders = (JSONArray)jsonObject.get("colHeaders");
            rowHeaders = (JSONArray)jsonObject.get("rowHeaders");
            data = (JSONArray)jsonObject.get("data");
            
            csvRow = new String[colHeaders.size()];
            
            // Copy column headers
            
            for (int i = 0; i < colHeaders.size(); ++i){
                String field = (String)colHeaders.get(i);
                csvRow[i] = field;
            }
            
            csvWriter.writeNext(csvRow); 
            
            // Copy row headers / data
            
            for (int i = 0; i < rowHeaders.size(); ++i) {
                
                csvRow = new String[colHeaders.size()];
                
                csvRow[0] = (String)rowHeaders.get(i);
                
                JSONArray dataRow = (JSONArray)data.get(i);
                
                for (int j = 0; j < dataRow.size(); ++j) {
                    csvRow[j+1] = Long.toString((long) dataRow.get(j));
                }
                
                csvWriter.writeNext(csvRow); 
                
                
            }
            
            
            
            results = writer.toString(); 
            
        }
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }
	
}