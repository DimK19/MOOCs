import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

public class AssignmentDriver {

    // ==========================================================
    // EXPORT DATA METHODS (Questions 1 - 3)
    // ==========================================================

    public static void listExportersTwoProducts(CSVParser parser, String exportItem1, String exportItem2) {
        int count = 0;
        for (CSVRecord record : parser) {
            String exports = record.get("Exports");
            if (exports.contains(exportItem1) && exports.contains(exportItem2)) {
                count++;
                if (count == 2) {
                    System.out.println("Q1 (Second country exporting " + exportItem1 + " and " + exportItem2 + "): " + record.get("Country"));
                }
            }
        }
    }

    public static void numberOfExporters(CSVParser parser, String exportItem) {
        int count = 0;
        for (CSVRecord record : parser) {
            String exports = record.get("Exports");
            if (exports.contains(exportItem)) {
                count++;
            }
        }
        System.out.println("Q2 (Number of countries exporting " + exportItem + "): " + count);
    }

    public static void bigExporters(CSVParser parser, String amount) {
        int count = 0;
        for (CSVRecord record : parser) {
            String value = record.get("Value (dollars)");
            if (value.length() > amount.length()) {
                count++;
                if (count == 3) {
                    System.out.println("Q3 (Third country with exports > " + amount + "): " + record.get("Country"));
                }
            }
        }
    }

    // ==========================================================
    // WEATHER DATA METHODS (Questions 4 - 11)
    // ==========================================================

    public static CSVRecord getSmallestOfTwo(CSVRecord currentRow, CSVRecord smallestSoFar, String column) {
        if (smallestSoFar == null) {
            return currentRow;
        }
        
        String currentValStr = currentRow.get(column);
        String smallestValStr = smallestSoFar.get(column);
        
        if (currentValStr.equals("N/A")) return smallestSoFar;
        if (smallestValStr.equals("N/A")) return currentRow;
        
        double currentVal = Double.parseDouble(currentValStr);
        double smallestVal = Double.parseDouble(smallestValStr);
        
        // Ignore bogus temperature values (-9999)
        if (column.equals("TemperatureF") && currentVal == -9999) return smallestSoFar;
        
        if (currentVal < smallestVal) {
            return currentRow;
        }
        return smallestSoFar;
    }

    public static CSVRecord lowestHumidityInFile(CSVParser parser) {
        CSVRecord lowestSoFar = null;
        for (CSVRecord currentRow : parser) {
            lowestSoFar = getSmallestOfTwo(currentRow, lowestSoFar, "Humidity");
        }
        return lowestSoFar;
    }

    public static CSVRecord lowestHumidityInManyFiles() {
        CSVRecord lowestSoFar = null;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
            lowestSoFar = getSmallestOfTwo(currentRow, lowestSoFar, "Humidity");
        }
        return lowestSoFar;
    }

    public static CSVRecord coldestHourInFile(CSVParser parser) {
        CSVRecord coldestSoFar = null;
        for (CSVRecord currentRow : parser) {
            coldestSoFar = getSmallestOfTwo(currentRow, coldestSoFar, "TemperatureF");
        }
        return coldestSoFar;
    }

    public static String fileWithColdestTemperature() {
        File coldestFile = null;
        CSVRecord coldestSoFar = null;
        DirectoryResource dr = new DirectoryResource();
        
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
            
            if (coldestSoFar == null) {
                coldestSoFar = currentRow;
                coldestFile = f;
            }
            else {
                double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
                double coldestTemp = Double.parseDouble(coldestSoFar.get("TemperatureF"));
                if (currentTemp != -9999 && currentTemp < coldestTemp) {
                    coldestSoFar = currentRow;
                    coldestFile = f;
                }
            }
        }
        return coldestFile.getName();
    }

    public static void averageTemperatureInFile(CSVParser parser) {
        double sum = 0;
        int count = 0;
        for (CSVRecord record : parser) {
            double temp = Double.parseDouble(record.get("TemperatureF"));
            if (temp != -9999) {
                sum += temp;
                count++;
            }
        }
        double avg = sum / count;
        // Truncate to 4 decimals
        avg = Math.floor(avg * 10000) / 10000;
        System.out.println("Q8 (Average Temperature): " + String.format("%.4f", avg));
    }

    public static void averageTemperatureWithHighHumidityInFile(CSVParser parser, int value) {
        double sum = 0;
        int count = 0;
        for (CSVRecord record : parser) {
            String humidityStr = record.get("Humidity");
            if (!humidityStr.equals("N/A")) {
                int humidity = Integer.parseInt(humidityStr);
                if (humidity >= value) {
                    double temp = Double.parseDouble(record.get("TemperatureF"));
                    if (temp != -9999) {
                        sum += temp;
                        count++;
                    }
                }
            }
        }
        if (count == 0) {
            System.out.println("No temperatures with that humidity");
        }
        else {
            double avg = sum / count;
            // Truncate to 3 decimals
            avg = Math.floor(avg * 1000) / 1000;
            System.out.println("Q9 (Average Temp with High Humidity): " + String.format("%.3f", avg));
        }
    }

    // ==========================================================
    // MAIN DRIVER
    // ==========================================================
    public static void main(String[] args) {
        System.out.println("--- EXPORT DATA ANALYSIS ---");
        System.out.println("Please select 'exportdata.csv' for the next 3 questions:");
        FileResource exportData = new FileResource();
        
        listExportersTwoProducts(exportData.getCSVParser(), "cotton", "flowers"); // Q1
        numberOfExporters(exportData.getCSVParser(), "cocoa"); // Q2
        bigExporters(exportData.getCSVParser(), "$999,999,999,999"); // Q3

        System.out.println("\n--- WEATHER DATA ANALYSIS ---");
        
        System.out.println("Please select 'weather-2014-06-29.csv' for Q4:");
        FileResource frQ4 = new FileResource();
        System.out.println("Q4 (Lowest humidity June 29): " + lowestHumidityInFile(frQ4.getCSVParser()).get("Humidity"));

        System.out.println("Please select 'weather-2014-07-22.csv' for Q5:");
        FileResource frQ5 = new FileResource();
        System.out.println("Q5 (Time of lowest humidity July 22): " + lowestHumidityInFile(frQ5.getCSVParser()).get("DateUTC"));

        System.out.println("Please select all 2013 weather files for Q6 and Q7:");
        CSVRecord lowestHum2013 = lowestHumidityInManyFiles();
        System.out.println("Q6 (Lowest humidity 2013): " + lowestHum2013.get("Humidity"));
        System.out.println("Q7 (Time of lowest humidity 2013): " + lowestHum2013.get("DateUTC"));

        System.out.println("Please select 'weather-2013-08-10.csv' for Q8:");
        FileResource frQ8 = new FileResource();
        averageTemperatureInFile(frQ8.getCSVParser());

        System.out.println("Please select 'weather-2013-09-02.csv' for Q9:");
        FileResource frQ9 = new FileResource();
        averageTemperatureWithHighHumidityInFile(frQ9.getCSVParser(), 80);

        System.out.println("Please select all 2013 weather files for Q10 and Q11:");
        String coldestFile2013 = fileWithColdestTemperature();
        System.out.println("Q10 (File with coldest temp in 2013): " + coldestFile2013);
        
        // Reload that specific file to find the exact lowest temperature for Q11
        // Assuming the file is loaded from the directory selector properly:
        System.out.println("Please select the file identified in Q10 (" + coldestFile2013 + ") to get the exact temperature for Q11:");
        FileResource coldestFr = new FileResource(); 
        CSVRecord coldestRecord = coldestHourInFile(coldestFr.getCSVParser());
        System.out.println("Q11 (Coldest temp in 2013): " + String.format("%.1f", Double.parseDouble(coldestRecord.get("TemperatureF"))));
    }
} 
