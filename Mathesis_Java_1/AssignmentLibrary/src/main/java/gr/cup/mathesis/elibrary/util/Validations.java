package gr.cup.mathesis.elibrary.util;

import java.util.Set;

/**
 *
 * @author mathesis
 */
public class Validations {
    
    private Validations() {}
    
    /**
     * @param s Set<String> to validate
     * @return {@code true} if the set isn't null or empty and it doesn't
     * contain any null or empty entries.
     */
    public static boolean isSetValid(Set<String> s) {
        boolean result =  s != null && !s.isEmpty();
        if (result)
            for (String str : s) {
                if (str == null || str.trim().isBlank()) {
                    result = false;
                }
            }
        return result;
    }
    
    public static boolean isValid(int value, int minValue, int maxValue){
        return value >= minValue && value <= maxValue;
    }
    
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }
    
}
