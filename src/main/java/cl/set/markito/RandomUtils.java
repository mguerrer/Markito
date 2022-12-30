package cl.set.markito;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils implements IRandomUtils {
    
    /**
     * Generates a random String of size length.
     * @param size
     * @return
     */
    public String RandomString(int size) {
        String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String randomString = "";
        for (int x = 0; x < size; x++) {
            int randomIndex = RandomNumber(0, pool.length() - 1);
            char randomCharacter = pool.charAt(randomIndex);
            randomString += randomCharacter;
        }
        return randomString;
    }

    /**
     * Generates a random integer in range [min, max].
     * @param min
     * @param max
     * @return
     */
    public int RandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
