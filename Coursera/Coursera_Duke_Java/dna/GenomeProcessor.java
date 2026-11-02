import edu.duke.*;

public class GenomeProcessor {

    // Finds the closest valid stop codon that is a multiple of 3 away from the start index
    public int findStopCodon(String dna, int startIndex, String stopCodon) {
        int currIndex = dna.indexOf(stopCodon, startIndex + 3);
        while (currIndex != -1) {
            if ((currIndex - startIndex) % 3 == 0) {
                return currIndex;
            }
            currIndex = dna.indexOf(stopCodon, currIndex + 1);
        }
        return dna.length();
    }

    // Extracts all valid genes from a DNA string, checking all 3 possible stop codons
    public StorageResource getAllGenes(String dna) {
        StorageResource geneList = new StorageResource();
        int startIndex = 0;
        String upperDna = dna.toUpperCase();

        while (true) {
            int atgIndex = upperDna.indexOf("ATG", startIndex);
            if (atgIndex == -1) {
                break;
            }

            int taaIndex = findStopCodon(upperDna, atgIndex, "TAA");
            int tagIndex = findStopCodon(upperDna, atgIndex, "TAG");
            int tgaIndex = findStopCodon(upperDna, atgIndex, "TGA");

            // Find the closest valid stop codon among the three
            int minIndex = Math.min(taaIndex, Math.min(tagIndex, tgaIndex));

            // If a valid stop codon was found
            if (minIndex != upperDna.length()) {
                geneList.add(dna.substring(atgIndex, minIndex + 3));
                startIndex = minIndex + 3;
            } else {
                // No valid stop codon found, move past this ATG and keep searching
                startIndex = atgIndex + 3;
            }
        }
        return geneList;
    }

    // Calculates the CG ratio of a specific gene/DNA string
    public double cgRatio(String dna) {
        int cgCount = 0;
        String upperDna = dna.toUpperCase();
        for (int i = 0; i < upperDna.length(); i++) {
            if (upperDna.charAt(i) == 'C' || upperDna.charAt(i) == 'G') {
                cgCount++;
            }
        }
        return (double) cgCount / dna.length();
    }

    // Counts how many times a specific codon appears in a strand
    public int countCodon(String dna, String codon) {
        int count = 0;
        int startIndex = 0;
        String upperDna = dna.toUpperCase();
        String upperCodon = codon.toUpperCase();

        while (true) {
            int index = upperDna.indexOf(upperCodon, startIndex);
            if (index == -1) {
                break;
            }
            count++;
            // Move past the current codon to find the next one
            startIndex = index + upperCodon.length();
        }
        return count;
    }

    // Processes the extracted genes against parametrized thresholds
    public void processGenes(StorageResource sr, int lengthThreshold, double cgRatioThreshold) {
        int totalGenes = 0;
        int countLongerThan = 0;
        int countHighCgRatio = 0;
        int longestGeneLength = 0;

        for (String gene : sr.data()) {
            totalGenes++; 

            if (gene.length() > lengthThreshold) {
                countLongerThan++;
            }

            if (cgRatio(gene) > cgRatioThreshold) {
                countHighCgRatio++;
            }

            if (gene.length() > longestGeneLength) {
                longestGeneLength = gene.length();
            }
        }

        System.out.println("Total number of genes: " + totalGenes);
        System.out.println("Genes longer than " + lengthThreshold + ": " + countLongerThan);
        System.out.println("Genes with CG Ratio higher than " + cgRatioThreshold + ": " + countHighCgRatio);
        System.out.println("Length of the longest gene: " + longestGeneLength);
    }

    // Drives the file selection and calculation logic
    public void runAnalysis(int lengthThreshold, double cgRatioThreshold, String targetCodon) {
        FileResource fr = new FileResource();
        String dna = fr.asString();

        // 4. How many times does the target codon appear in the entire strand
        int codonCount = countCodon(dna, targetCodon);
        System.out.println("The codon " + targetCodon + " appears " + codonCount + " times in the file.");

        StorageResource genes = getAllGenes(dna);
        processGenes(genes, lengthThreshold, cgRatioThreshold);
    }

    public static void main(String[] args) {
        GenomeProcessor processor = new GenomeProcessor();
        
        // Parametrized variables to avoid hardcoded logic
        int targetLength = 60;
        double targetCgRatio = 0.35;
        String targetCodon = "CTG";
        
        processor.runAnalysis(targetLength, targetCgRatio, targetCodon);
    }
}
