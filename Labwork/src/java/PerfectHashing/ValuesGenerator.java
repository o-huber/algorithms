package PerfectHashing;

import java.util.Random;
import java.util.stream.LongStream;

public class ValuesGenerator {

    private static final Random random = new Random();
    public static final long FIRST_PRIME_AFTER_INTEGER = Long.parseLong("2147483659");

    public static long generatePrimeNumber(int lowerBound) {
        long temp = lowerBound + 1;

        while (temp < FIRST_PRIME_AFTER_INTEGER && !isPrime(temp)) {
            ++temp;
        }

        return temp;
    }

    public static long generateFirstHashingValue(long bound) {
        long temp = random.nextLong();

        while (temp >= bound || temp < 1) {
            temp = Math.abs(random.nextLong()) % bound;
        }

        return temp;
    }

    public static long generateSecondHashingValue(long bound) {
        long temp = random.nextLong();

        while (temp >= bound || temp < 0) {
            temp = Math.abs(random.nextLong()) % bound;
        }

        return temp;
    }

    public static int generateUniversalHash(int key, long firstHashingValue, long secondHashingValue,
                                            long primeDivider, int hashMapSize) {

        return (int) ((firstHashingValue * key + secondHashingValue) % primeDivider) % hashMapSize;
    }

    public static boolean isPrime(long number){
        return number > 1 && LongStream.range(2, (long) Math.sqrt(number)).noneMatch(i -> number % i == 0);
    }
}