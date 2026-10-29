import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

public class BabyNamesAnalysis {

    // Returns the number of unique names for a specific gender in a given year
    public int getTotalNamesByGender(int year, String gender) {
        FileResource fr = new FileResource(".\\data\\yob" + year + ".csv");
        int count = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                count++;
            }
        }
        return count;
    }

    // Returns the rank of a name for a specific gender in a given year
    public int getRank(int year, String name, String gender) {
        FileResource fr = new FileResource(".\\data\\yob" + year + ".csv");
        int rank = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                rank++;
                if (rec.get(0).equals(name)) {
                    return rank;
                }
            }
        }
        return -1;
    }

    // Returns the name at a specific rank for a specific gender in a given year
    public String getName(int year, int rank, String gender) {
        FileResource fr = new FileResource(".\\data\\yob" + year + ".csv");
        int currentRank = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                currentRank++;
                if (currentRank == rank) {
                    return rec.get(0);
                }
            }
        }
        return "NO NAME";
    }

    // Determines what a name would be in a new year based on its rank in the original year
    public String whatIsNameInYear(String name, int year, int newYear, String gender) {
        int originalRank = getRank(year, name, gender);
        return getName(newYear, originalRank, gender);
    }

    // Iterates through multiple selected files to find the year a name had its highest rank
    public int yearOfHighestRank(String name, String gender) {
        int highestRank = -1;
        int yearOfHighest = -1;
        DirectoryResource dr = new DirectoryResource();
        
        for (File f : dr.selectedFiles()) {
            String fileName = f.getName();
            int currentYear = Integer.parseInt(fileName.replaceAll("[^\\d]", ""));
            FileResource fr = new FileResource(f);
            
            int currentRank = 0;
            boolean found = false;
            for (CSVRecord rec : fr.getCSVParser(false)) {
                if (rec.get(1).equals(gender)) {
                    currentRank++;
                    if (rec.get(0).equals(name)) {
                        found = true;
                        break;
                    }
                }
            }
            
            if (found) {
                if (highestRank == -1 || currentRank < highestRank) {
                    highestRank = currentRank;
                    yearOfHighest = currentYear;
                } else if (currentRank == highestRank && currentYear < yearOfHighest) {
                    yearOfHighest = currentYear;
                }
            }
        }
        return yearOfHighest;
    }

    // Calculates the average rank of a name across all selected files
    public double getAverageRank(String name, String gender) {
        double totalRank = 0;
        int fileCount = 0;
        DirectoryResource dr = new DirectoryResource();
        
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int currentRank = 0;
            boolean found = false;
            
            for (CSVRecord rec : fr.getCSVParser(false)) {
                if (rec.get(1).equals(gender)) {
                    currentRank++;
                    if (rec.get(0).equals(name)) {
                        found = true;
                        break;
                    }
                }
            }
            
            if (found) {
                totalRank += currentRank;
            }
            // Even if not found, it counts as a processed file per course logic unless instructed otherwise.
            // Some interpretations only count files where the name exists. The Duke course expects counting 
            // only files where the name was found for the average calculation.
            if (found) fileCount++; 
        }
        
        if (fileCount == 0) return -1.0;
        return totalRank / fileCount;
    }

    // Calculates the total births of same-gender names ranked higher than the provided name
    public int getTotalBirthsRankedHigher(int year, String name, String gender) {
        FileResource fr = new FileResource(".\\data\\yob" + year + ".csv");
        int targetRank = getRank(year, name, gender);
        if (targetRank == -1) return 0;
        
        int totalBirths = 0;
        int currentRank = 0;
        
        for (CSVRecord rec : fr.getCSVParser(false)) {
            if (rec.get(1).equals(gender)) {
                currentRank++;
                if (currentRank < targetRank) {
                    totalBirths += Integer.parseInt(rec.get(2));
                } else {
                    break;
                }
            }
        }
        return totalBirths;
    }

    public static void main(String[] args) {
        BabyNamesAnalysis bna = new BabyNamesAnalysis();

        System.out.println("Q1: Number of girls' names in 1900 = " + bna.getTotalNamesByGender(1900, "F"));
        System.out.println("Q2: Number of boys' names in 1905 = " + bna.getTotalNamesByGender(1905, "M"));
        System.out.println("Q3: Rank of Emily (F) in 1960 = " + bna.getRank(1960, "Emily", "F"));
        System.out.println("Q4: Rank of Frank (M) in 1971 = " + bna.getRank(1971, "Frank", "M"));
        System.out.println("Q5: Girl's name at rank 350 in 1980 = " + bna.getName(1980, 350, "F"));
        System.out.println("Q6: Boy's name at rank 450 in 1982 = " + bna.getName(1982, 450, "M"));
        System.out.println("Q7: Susan (1972) in 2014 = " + bna.whatIsNameInYear("Susan", 1972, 2014, "F"));
        System.out.println("Q8: Owen (1974) in 2014 = " + bna.whatIsNameInYear("Owen", 1974, 2014, "M"));

        System.out.println("\n--- For Q9 to Q12, please select ALL data files (1880-2014) when prompted ---");
        System.out.println("Q9: Year of highest rank for Genevieve (F) = " + bna.yearOfHighestRank("Genevieve", "F"));
        System.out.println("Q10: Year of highest rank for Mich (M) = " + bna.yearOfHighestRank("Mich", "M"));
        System.out.println("Q11: Average rank of Susan (F) = " + String.format("%.2f", bna.getAverageRank("Susan", "F")));
        System.out.println("Q12: Average rank of Robert (M) = " + String.format("%.2f", bna.getAverageRank("Robert", "M")));

        System.out.println("\nQ13: Total girls born in 1990 ranked higher than Emily = " + bna.getTotalBirthsRankedHigher(1990, "Emily", "F"));
        System.out.println("Q14: Total boys born in 1990 ranked higher than Drew = " + bna.getTotalBirthsRankedHigher(1990, "Drew", "M"));
    }
}
