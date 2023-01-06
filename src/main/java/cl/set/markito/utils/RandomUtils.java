package cl.set.markito.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils implements RandomDataManagement {
    

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

    public int RandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
