import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Answer {

    public static void main(String[] args) {
        System.out.println("=== Duke Course Unified Driver ===");
        System.out.println("Note: Ensure the required files (errors.txt, dnaMystery2, and the 7 play texts) are in the project root.\n");

        // --- 1-5. WordFrequencies ---
        System.out.println("--- WordFrequencies Analysis ---");
        WordFrequencies wf = new WordFrequencies();
        try {
            wf.findUnique("errors.txt");
            System.out.println("Total unique words in errors.txt: " + wf.myWords.size());
            int maxIdx = wf.findIndexOfMax();
            if (maxIdx != -1) {
                System.out.println("Most frequent word: '" + wf.myWords.get(maxIdx) + "'");
                System.out.println("Occurrences of most common word: " + wf.myFreqs.get(maxIdx));
            }
        } catch (IOException e) {
            System.out.println("File errors.txt not found.");
        }
        System.out.println();

        // --- 6-8. CharactersInPlay ---
        System.out.println("--- CharactersInPlay Analysis ---");
        CharactersInPlay cip = new CharactersInPlay();
        try {
            cip.findAllCharacters("errors.txt");
            cip.analyzeQuestions();
        } catch (IOException e) {
            System.out.println("File errors.txt not found.");
        }
        System.out.println();

        // --- 9-11. CodonCount ---
        System.out.println("--- CodonCount Analysis ---");
        CodonCount cc = new CodonCount();
        try {
            String dna = new String(Files.readAllBytes(Paths.get("dnaMystery2"))).trim().toUpperCase();
            
            // Q9: Frame 1
            cc.buildCodonMap(1, dna);
            System.out.println("Unique codons (Frame 1): " + cc.codonMap.size());
            
            // Q10: Frame 2
            cc.buildCodonMap(2, dna);
            String mostCommon2 = cc.getMostCommonCodon();
            System.out.println("Most common codon (Frame 2): " + mostCommon2 + " with count " + cc.codonMap.get(mostCommon2));
            
            // Q11: Frame 0, count 7
            cc.buildCodonMap(0, dna);
            System.out.println("Codons occurring exactly 7 times (Frame 0): ");
            cc.printCodonsWithCount(7);
            
        } catch (IOException e) {
            System.out.println("File dnaMystery2 not found.");
        }
        System.out.println();

        // --- 12-15. WordsInFiles ---
        System.out.println("--- WordsInFiles Analysis ---");
        WordsInFiles wif = new WordsInFiles();
        try {
            wif.buildWordFileMap(); // Ensure the 7 txt files exist
            
            // Q12: In all 7 files
            List<String> in7 = wif.wordsInNumFiles(7);
            System.out.println("Words in all 7 files: " + in7.size());
            
            // Q13: In exactly 4 files
            List<String> in4 = wif.wordsInNumFiles(4);
            System.out.println("Words in exactly 4 files: " + in4.size());
            
            // Q14: "sea" NOT appear
            System.out.print("Files where 'sea' does NOT appear: ");
            wif.printFilesWithoutWord("sea");
            
            // Q15: "tree" appears
            System.out.print("Files where 'tree' appears: ");
            wif.printFilesIn("tree");
            
        } catch (Exception e) {
            System.out.println("Error reading the 7 text files for WordsInFiles.");
        }
    }

    // =========================================================================
    // WordFrequencies
    // =========================================================================
    static class WordFrequencies {
        ArrayList<String> myWords = new ArrayList<>();
        ArrayList<Integer> myFreqs = new ArrayList<>();

        public void findUnique(String filename) throws IOException {
            myWords.clear();
            myFreqs.clear();
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (String w : words) {
                    if (w.isEmpty()) continue;
                    w = w.toLowerCase(); // Includes punctuation per assignment rules
                    int index = myWords.indexOf(w);
                    if (index == -1) {
                        myWords.add(w);
                        myFreqs.add(1);
                    } else {
                        myFreqs.set(index, myFreqs.get(index) + 1);
                    }
                }
            }
        }

        // Method findIndexOfMax that has no parameters[cite: 1]
        public int findIndexOfMax() {
            if (myFreqs.isEmpty()) return -1;
            int maxIdx = 0;
            for (int i = 0; i < myFreqs.size(); i++) {
                if (myFreqs.get(i) > myFreqs.get(maxIdx)) {
                    maxIdx = i;
                }
            }
            return maxIdx;
        }
    }

    // =========================================================================
    // CharactersInPlay
    // =========================================================================
    static class CharactersInPlay {
        ArrayList<String> myCharacters = new ArrayList<>();
        ArrayList<Integer> myFreqs = new ArrayList<>();

        public void update(String person) {
            int index = myCharacters.indexOf(person);
            if (index == -1) {
                myCharacters.add(person);
                myFreqs.add(1);
            } else {
                myFreqs.set(index, myFreqs.get(index) + 1);
            }
        }

        // void method called findAllCharacters[cite: 1]
        public void findAllCharacters(String filename) throws IOException {
            myCharacters.clear();
            myFreqs.clear();
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                int periodIndex = line.indexOf('.');
                // A period is used to indicate the end of a character's name[cite: 1]
                if (periodIndex != -1) {
                    String possibleName = line.substring(0, periodIndex).trim();
                    update(possibleName);
                }
            }
        }

        public void analyzeQuestions() {
            int maxBelow100 = 0;
            String charMaxBelow100 = "";
            int count10to15 = 0;

            // Sort for 3rd most speaking parts
            List<CharacterStat> stats = new ArrayList<>();

            for (int i = 0; i < myCharacters.size(); i++) {
                int lines = myFreqs.get(i);
                stats.add(new CharacterStat(myCharacters.get(i), lines));

                // Q6: Most speaking parts under 100 lines
                if (lines < 100 && lines > maxBelow100) {
                    maxBelow100 = lines;
                    charMaxBelow100 = myCharacters.get(i);
                }

                // Q8: Between 10 and 15
                if (lines >= 10 && lines <= 15) {
                    count10to15++;
                }
            }

            stats.sort((a, b) -> Integer.compare(b.lines, a.lines)); // Descending

            System.out.println("Character with most parts (<100 lines): " + charMaxBelow100 + " (" + maxBelow100 + " parts)");
            
            if (stats.size() >= 3) {
                System.out.println("3rd most speaking parts: " + stats.get(2).name + " with " + stats.get(2).lines + " parts");
            }
            
            System.out.println("Characters with 10-15 speaking parts: " + count10to15);
        }

        class CharacterStat {
            String name;
            int lines;
            CharacterStat(String name, int lines) { this.name = name; this.lines = lines; }
        }
    }

    // =========================================================================
    // CodonCount
    // =========================================================================
    static class CodonCount {
        HashMap<String, Integer> codonMap = new HashMap<>();

        public void buildCodonMap(int start, String dna) {
            codonMap.clear();
            // Reading frame starting with 2 results in different codons
            for (int i = start; i <= dna.length() - 3; i += 3) {
                String codon = dna.substring(i, i + 3);
                codonMap.put(codon, codonMap.getOrDefault(codon, 0) + 1);
            }
        }

        public String getMostCommonCodon() {
            String mostCommon = "";
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : codonMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostCommon = entry.getKey();
                }
            }
            return mostCommon;
        }

        public void printCodonsWithCount(int count) {
            for (Map.Entry<String, Integer> entry : codonMap.entrySet()) {
                if (entry.getValue() == count) {
                    System.out.println(entry.getKey());
                }
            }
        }
    }

    // =========================================================================
    // WordsInFiles
    // =========================================================================
    static class WordsInFiles {
        HashMap<String, ArrayList<String>> wordToFileMap = new HashMap<>();
        String[] fileNames = {"caesar.txt", "confucius.txt", "errors.txt", "hamlet.txt", "likeit.txt", "macbeth.txt", "romeo.txt"};

        public void buildWordFileMap() throws IOException {
            wordToFileMap.clear();
            for (String fName : fileNames) {
                addWordsFromFile(new File(fName));
            }
        }

        // private void method named addWordsFromFile
        private void addWordsFromFile(File f) throws IOException {
            if (!f.exists()) return;
            String fName = f.getName();
            Scanner sc = new Scanner(f);
            while (sc.hasNext()) {
                String w = sc.next();
                if (!wordToFileMap.containsKey(w)) {
                    wordToFileMap.put(w, new ArrayList<>());
                }
                if (!wordToFileMap.get(w).contains(fName)) {
                    wordToFileMap.get(w).add(fName);
                }
            }
            sc.close();
        }

        public List<String> wordsInNumFiles(int number) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, ArrayList<String>> entry : wordToFileMap.entrySet()) {
                if (entry.getValue().size() == number) {
                    list.add(entry.getKey());
                }
            }
            return list;
        }

        public void printFilesIn(String word) {
            ArrayList<String> files = wordToFileMap.get(word);
            if (files != null) {
                for (String f : files) {
                    System.out.print(f + " ");
                }
            }
            System.out.println();
        }

        public void printFilesWithoutWord(String word) {
            ArrayList<String> filesWithWord = wordToFileMap.getOrDefault(word, new ArrayList<>());
            for (String fName : fileNames) {
                if (!filesWithWord.contains(fName)) {
                    System.out.print(fName + " ");
                }
            }
            System.out.println();
        }
    }
}
