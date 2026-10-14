import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CaesarCipherUnified {

    public static void main(String[] args) {
        // =====================================================================
        // QUESTION 1: Encrypt a phrase with a single key
        // =====================================================================
        System.out.println("--- 1. Single Key Encryption ---");
        String phraseToEncrypt1 = "Can you imagine life WITHOUT the internet AND computers in your pocket?";
        int singleKey = 15;
        CaesarCipher ccSingle = new CaesarCipher(singleKey);
        System.out.println("Encrypted: " + ccSingle.encrypt(phraseToEncrypt1) + "\n");

        // =====================================================================
        // QUESTION 2: Encrypt a phrase with two keys
        // =====================================================================
        System.out.println("--- 2. Two Key Encryption ---");
        String phraseToEncrypt2 = "Can you imagine life WITHOUT the internet AND computers in your pocket?";
        int key1 = 21;
        int key2 = 8;
        CaesarCipher ccDouble = new CaesarCipher(key1, key2);
        System.out.println("Encrypted: " + ccDouble.encrypt(phraseToEncrypt2) + "\n");

        // =====================================================================
        // QUESTION 3 & 4: Most common word length in a file
        // =====================================================================
        System.out.println("--- 3 & 4. Most Common Word Length ---");
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get("manywords.txt")));
            int mostCommonLength = WordLengths.findMostCommonWordLength(fileContent);
            System.out.println("Most common word length: " + mostCommonLength + "\n");
        }
        catch (IOException e) {
            System.out.println("Could not read file.");
        }

        // =====================================================================
        // QUESTION 5: Decrypt a phrase with two known keys
        // =====================================================================
        System.out.println("--- 5. Two Key Decryption (Known Keys) ---");
        String encryptedPhrase = "Hfs cpwewloj loks cd Hoto kyg Cyy.";
        int knownKey1 = 14;
        int knownKey2 = 24;
        CaesarCipher ccDecryptKnown = new CaesarCipher(knownKey1, knownKey2);
        System.out.println("Decrypted: " + ccDecryptKnown.decrypt(encryptedPhrase) + "\n");

        // =====================================================================
        // QUESTION 6: Break a two-key encrypted phrase (Unknown keys)
        // =====================================================================
        System.out.println("--- 6. Break Two-Key Encryption (String) ---");
        String unknownEncryptedPhrase = "Aal uttx hm aal Qtct Fhljha pl Wbdl. Pvxvxlx!";
        CaesarBreaker.breakTwoKeys(unknownEncryptedPhrase);
        System.out.println();

        // =====================================================================
        // QUESTION 7 & 8: Break a two-key encrypted file and print first 5 words
        // =====================================================================
        System.out.println("--- 7 & 8. Break Two-Key Encryption (File) ---");
        try {
            // Replace "encrypted_data.txt" with the path to your encrypted file
            String encryptedFileContent = new String(Files.readAllBytes(Paths.get("mysteryTwoKeysQuiz.txt")));
            String fullDecryptedText = CaesarBreaker.breakTwoKeys(encryptedFileContent);
            
            String[] words = fullDecryptedText.split("\\s+");
            System.out.print("First five decrypted words: ");
            for(int i = 0; i < Math.min(5, words.length); i++) {
                System.out.print(words[i] + (i < 4 ? " " : ""));
            }
            System.out.println("\n");
        }
        catch (IOException e) {
            System.out.println("Could not read encrypted file.");
        }
    }

    // =========================================================================
    // CORE CLASSES FOR OOP IMPLEMENTATION
    // =========================================================================

    /**
     * Handles encryption and decryption for both 1-key and 2-key Caesar Ciphers.
     * A single key cipher is simply treated as a 2-key cipher where both keys are identical.
     */
    static class CaesarCipher {
        private int key1;
        private int key2;

        public CaesarCipher(int key) {
            this.key1 = key;
            this.key2 = key;
        }

        public CaesarCipher(int key1, int key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public String encrypt(String input) {
            StringBuilder encrypted = new StringBuilder(input);
            for(int i = 0; i < encrypted.length(); i++) {
                char currChar = encrypted.charAt(i);
                int currentKey = (i % 2 == 0) ? key1 : key2;
                encrypted.setCharAt(i, shiftChar(currChar, currentKey));
            }
            return encrypted.toString();
        }

        public String decrypt(String input) {
            // Decryption is encryption with the complement of the keys
            CaesarCipher cc = new CaesarCipher(26 - key1, 26 - key2);
            return cc.encrypt(input);
        }

        private char shiftChar(char c, int key) {
            if(Character.isUpperCase(c)) {
                return (char) ('A' + (c - 'A' + key) % 26);
            }
            else if (Character.isLowerCase(c)) {
                return (char) ('a' + (c - 'a' + key) % 26);
            }
            return c; // Return unmodified if it's punctuation or space
        }
    }

    /**
     * Provides frequency analysis tools to break the Caesar Cipher automatically.
     */
    static class CaesarBreaker {
        public static String breakTwoKeys(String encrypted) {
            String firstHalf = halfOfString(encrypted, 0);
            String secondHalf = halfOfString(encrypted, 1);

            int key1 = getKey(firstHalf);
            int key2 = getKey(secondHalf);

            System.out.println("Calculated Keys -> Key 1: " + key1 + ", Key 2: " + key2);
            
            CaesarCipher cc = new CaesarCipher(key1, key2);
            String decrypted = cc.decrypt(encrypted);
            System.out.println("Decrypted message:\n" + decrypted);
            
            return decrypted;
        }

        private static int getKey(String s) {
            int[] freqs = countLetters(s);
            int maxDex = maxIndex(freqs);
            int dkey = maxDex - 4; // Shift relative to 'e' (index 4)
            if(maxDex < 4) {
                dkey = 26 - (4 - maxDex);
            }
            return dkey;
        }

        private static String halfOfString(String message, int start) {
            StringBuilder half = new StringBuilder();
            for(int i = start; i < message.length(); i += 2) {
                half.append(message.charAt(i));
            }
            return half.toString();
        }

        private static int[] countLetters(String message) {
            int[] counts = new int[26];
            for(int i = 0; i < message.length(); i++) {
                char ch = Character.toLowerCase(message.charAt(i));
                if(ch >= 'a' && ch <= 'z') {
                    counts[ch - 'a']++;
                }
            }
            return counts;
        }

        private static int maxIndex(int[] vals) {
            int maxDex = 0;
            for(int i = 0; i < vals.length; i++) {
                if(vals[i] > vals[maxDex]) {
                    maxDex = i;
                }
            }
            return maxDex;
        }
    }

    /**
     * Handles string analysis, specifically calculating word lengths based on assignment rules.
     */
    static class WordLengths {
        public static int findMostCommonWordLength(String fileContent) {
            String[] words = fileContent.split("\\s+");
            Map<Integer, Integer> lengthCounts = new HashMap<>();
            
            for(String word: words) {
                if(word.isEmpty()) continue;
                
                int length = word.length();
                // Ignore first character punctuation
                if(!Character.isLetter(word.charAt(0))) {
                    length--;
                }
                // Ignore last character punctuation (only if there are characters left to check)
                if(word.length() > 1 && !Character.isLetter(word.charAt(word.length() - 1))) {
                    length--;
                }
                
                // Prevent negative lengths for strings entirely composed of punctuation
                length = Math.max(0, length);
                
                if(length > 0) {
                    lengthCounts.put(length, lengthCounts.getOrDefault(length, 0) + 1);
                }
            }

            int mostCommon = 0;
            int maxFrequency = 0;
            
            for(Map.Entry<Integer, Integer> entry: lengthCounts.entrySet()) {
                if(entry.getValue() > maxFrequency) {
                    maxFrequency = entry.getValue();
                    mostCommon = entry.getKey();
                }
            }
            return mostCommon;
        }
    }
}
