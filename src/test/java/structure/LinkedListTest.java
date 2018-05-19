package structure;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Test;
import structure.helper.RandomString;
import structure.helper.Tuple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedListTest {

    private static final RandomString randomStringGenerator = new RandomString();

    private static final String KEY = "RomanKleimenov123456 -";
    private static final int NUMBER_OF_ELEMENTS = 10000;
    private static final int NUMBER_OF_EXPIREMENTS = 5000;

    @Test
    public void testElementsSpeed() {
        LinkedList<Tuple> randomStrings = getRandomStrings(NUMBER_OF_ELEMENTS);
        long objectSize = ObjectSizeCalculator.getObjectSize(randomStrings);
        System.out.println("Object size is: [bytes]" + objectSize);
        List<Long> resultTimes = new ArrayList<Long>(NUMBER_OF_EXPIREMENTS);
        for (int i = 0; i < NUMBER_OF_EXPIREMENTS; i++) {
            int randomPosition = (int)(Math.random() * ((NUMBER_OF_ELEMENTS - 0) + 1)) + 0;
            //prepare data
            LinkedList<Tuple> structure = new LinkedList<Tuple>();
            for (int j = 0; j < NUMBER_OF_ELEMENTS; j++) {
                structure.add(randomStrings.get(j));
                if (j == randomPosition) {
                    structure.add(new Tuple(KEY, -1));
                }
            }

            long startTime = System.nanoTime();
            boolean isContains = structure.contains(new Tuple(KEY, -1));
            long endTime = System.nanoTime();
            long resultTime = endTime - startTime;
            resultTimes.add(resultTime);
            System.out.println("[" + i + "] random pos is: " + randomPosition  + " [" +isContains + "] time " + resultTime);
        }

        long sumTime = 0L;
        for (Long resultTime : resultTimes) {
            sumTime += resultTime;
        }
        System.out.println("Average time: " + sumTime / NUMBER_OF_EXPIREMENTS);
        System.out.println("Object size is: [bytes]" + objectSize);
    }

    private LinkedList<Tuple> getRandomStrings(int number) {
        LinkedList<Tuple> randomString = new LinkedList<Tuple>();
        for (int i = 0; i < number; i++) {
            randomString.add(new Tuple(randomStringGenerator.nextString(), i));
        }
        return randomString;
    }
}
