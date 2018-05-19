package structure;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Test;
import structure.helper.RandomString;

import java.util.*;

public class HashMapTest {

    private static final RandomString randomStringGenerator = new RandomString();

    private static final String KEY = "RomanKleimenov123456 -";
    private static final int NUMBER_OF_ELEMENTS = 10000;
    private static final int NUMBER_OF_EXPIREMENTS = 5000;

    @Test
    public void testElementsSpeed() {
        HashMap<String, Integer> randomStrings = getRandomStrings(NUMBER_OF_ELEMENTS);
        long objectSize = ObjectSizeCalculator.getObjectSize(randomStrings);
        System.out.println("Object size is: [bytes]" + objectSize);
        List<Long> resultTimes = new ArrayList<Long>(NUMBER_OF_EXPIREMENTS);
        for (int i = 0; i < NUMBER_OF_EXPIREMENTS; i++) {
            int randomPosition = (int)(Math.random() * ((NUMBER_OF_ELEMENTS - 0) + 1)) + 0;
            //prepare data
            HashMap<String, Integer> structure = new HashMap<String, Integer>();
            int j = 0;
            for (Map.Entry<String, Integer> entry : randomStrings.entrySet()) {
                structure.put(entry.getKey(), entry.getValue());
                if (j == randomPosition) {
                    structure.put(KEY, -1);
                }
                j++;
            }
            long startTime = System.nanoTime();
            Integer integer = structure.get(KEY);
            long endTime = System.nanoTime();
            long resultTime = endTime - startTime;
            resultTimes.add(resultTime);
            System.out.println("[" + i + "] random pos is: " + randomPosition  + " [" + integer + "] time " + resultTime);
        }

        long sumTime = 0L;
        for (Long resultTime : resultTimes) {
            sumTime += resultTime;
        }
        System.out.println("Average time: " + sumTime / NUMBER_OF_EXPIREMENTS);
        System.out.println("Object size is: [bytes]" + objectSize);
    }

    private HashMap<String, Integer> getRandomStrings(int number) {
        HashMap<String, Integer> randomString = new HashMap<String, Integer>();
        for (int i = 0; i < number; i++) {
            randomString.put(randomStringGenerator.nextString(), i);
        }
        return randomString;
    }
}
