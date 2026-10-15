import java.util.*;
import edu.duke.*;

public class VigenereBreaker {
    
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder slice = new StringBuilder();
        for(int i = whichSlice; i < message.length(); i += totalSlices) {
            slice.append(message.charAt(i));
        }
        return slice.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cracker = new CaesarCracker(mostCommon);
        for(int i = 0; i < klength; i++) {
            String slice = sliceString(encrypted, i, klength);
            key[i] = cracker.getKey(slice);
        }
        return key;
    }

    public HashSet<String> readDictionary(FileResource fr) {
        HashSet<String> dictionary = new HashSet<>();
        for(String line: fr.lines()) {
            dictionary.add(line.toLowerCase());
        }
        return dictionary;
    }

    public int countWords(String message, HashSet<String> dictionary) {
        int wordCount = 0;
        String[] words = message.split("\\W+");
        for(String word: words) {
            if(dictionary.contains(word.toLowerCase())) {
                wordCount++;
            }
        }
        return wordCount;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary) {
        HashMap<Character, Integer> charCounts = new HashMap<>();
        for(String word : dictionary) {
            for(char c: word.toLowerCase().toCharArray()) {
                if (Character.isLetter(c)) {
                    charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
                }
            }
        }
        
        char mostCommon = 'e';
        int maxCount = 0;
        for(char c: charCounts.keySet()) {
            if(charCounts.get(c) > maxCount) {
                maxCount = charCounts.get(c);
                mostCommon = c;
            }
        }
        return mostCommon;
    }

    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        int maxWords = 0;
        String bestDecryption = "";
        char mostCommon = mostCommonCharIn(dictionary);
        
        for(int i = 1; i <= 100; i++) {
            int[] key = tryKeyLength(encrypted, i, mostCommon);
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int validWords = countWords(decrypted, dictionary);
            
            if(validWords > maxWords) {
                maxWords = validWords;
                bestDecryption = decrypted;
            }
        }
        return bestDecryption;
    }
    
    public void breakForAllLanguages(String encrypted, HashMap<String, HashSet<String>> languages) {
        int maxWords = 0;
        String bestDecryption = "";
        String identifiedLanguage = "";
        
        for(String lang: languages.keySet()) {
            HashSet<String> dictionary = languages.get(lang);
            String decrypted = breakForLanguage(encrypted, dictionary);
            int validWords = countWords(decrypted, dictionary);
            
            if(validWords > maxWords) {
                maxWords = validWords;
                bestDecryption = decrypted;
                identifiedLanguage = lang;
            }
        }
        
        System.out.println("Identified Language: " + identifiedLanguage);
        System.out.println("Decrypted Message:\n" + bestDecryption);
    }

    public void breakVigenere() {
        FileResource fr = new FileResource();
        String message = fr.asString();
        
        HashMap<String, HashSet<String>> languages = new HashMap<>();
        String[] dictNames = {"Danish", "Dutch", "English", "French", "German", "Italian", "Portuguese", "Spanish"};
        
        for(String dictName: dictNames) {
            FileResource dictFr = new FileResource("dictionaries/" + dictName);
            languages.put(dictName, readDictionary(dictFr));
            System.out.println("Loaded dictionary: " + dictName);
        }
        
        breakForAllLanguages(message, languages);
    }
    
    public static void main(String[] args) {
        VigenereBreaker vb = new VigenereBreaker();
        vb.breakVigenere();
    }
}
