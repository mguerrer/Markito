package cl.set.markito.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Some useful random tools.
 */
public class RandomUtils implements RandomDataManagement {

    /**
     * Return a random string of max size. 
     */
    public String randomString(int size) {
        String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String randomString = "";
        for (int x = 0; x < size; x++) {
            int randomIndex = randomNumber(0, pool.length() - 1);
            char randomCharacter = pool.charAt(randomIndex);
            randomString += randomCharacter;
        }
        return randomString;
    }

    /**
     * Return a random integer in range [min,max].
     */
    public int randomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
