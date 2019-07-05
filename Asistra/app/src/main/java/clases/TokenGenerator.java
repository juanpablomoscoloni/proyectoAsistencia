package clases;

import java.util.Random;

public class TokenGenerator {


    /**
     * Genera un token random
     */

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();

    public static String randomToken(int len) {

        StringBuilder token = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            token.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return token.toString();
    }


}
