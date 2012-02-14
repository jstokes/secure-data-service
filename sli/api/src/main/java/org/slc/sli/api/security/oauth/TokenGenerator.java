package org.slc.sli.api.security.oauth;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 
 * @author pwolf
 *
 */
public class TokenGenerator {

    private static char[] validChars = null;
    
    private static Random random = new SecureRandom();

    static {
        validChars = new char[62];

        // upper case
        for (int i = 0; i < 26; i++) {
            validChars[i] = (char) (97 + i);
        }

        // lower case
        for (int i = 0; i < 26; i++) {
            validChars[i + 26] = (char) (65 + i);
        }

        // digits
        for (int i = 0; i < 10; i++) {
            validChars[i + 52] = (char) (48 + i);
        }

    }

    /**
     * Generates a random string containing characters a-z, A-Z, 0-9
     * 
     * @param length length of string
     * @return a securely random string
     */
    public static String generateToken(int length) {
        
        StringBuffer id = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            id.append(validChars[random.nextInt(validChars.length)]);
        }
        return id.toString();
    }

}
