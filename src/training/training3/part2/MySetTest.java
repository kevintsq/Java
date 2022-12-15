package training.training3.part2;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class MySetTest {
    private static MySet mySet = new MySet();

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.out.println("Test Start!");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        System.out.println("Test End!");
    }

    @Before
    public void setUp() throws Exception {
        mySet = new MySet();
    }

    @Test
    public void insert() {
        System.out.println("----------");
        System.out.println("Test insert!");

        int tempSize = 0;
        for (int i = 0; i < 5; ++i) {
            assertEquals(mySet.size(), tempSize);
            assertFalse(mySet.isIn(i));
            mySet.insert(i);
            assertTrue(mySet.repOK());
            assertTrue(mySet.isIn(i));
            System.out.println("mySet.size(): " + mySet.size());
            System.out.println("tempSize: " + tempSize);
            assertEquals(mySet.size(), ++tempSize);
        }

        for (int i = 9; i >= 0; --i) {
            assert mySet.size() == tempSize;
            boolean isInI = mySet.isIn(i);

            mySet.insert(i);
            assertTrue(mySet.repOK());
            assertTrue(mySet.isIn(i));
            System.out.println("mySet.size(): " + mySet.size());
            if (isInI) {
                System.out.println("tempSize: " + tempSize);
                assertEquals(mySet.size(), tempSize);
            } else {
                System.out.println("tempSize: " + (tempSize + 1));
                assertEquals(mySet.size(), ++tempSize);
            }
        }

        for (int i = 0; i < 100; ++i) {
            mySet.insert((int) (Math.random() * (20 - 10 + 1)));
            mySet.insert((int) (Math.random() * (20 - 10 + 1)));
            assertTrue(mySet.repOK());
        }

        System.out.println("Test insert finish!");
        System.out.println("----------");
    }

    @Test
    public void delete() {
        System.out.println("Test delete!");
        System.out.println("----------");

        int tempSize = 0;
        mySet.delete(0);
        assertTrue(mySet.repOK());
        assertEquals(mySet.size(), tempSize);

        for (int i = 0; i < 5; ++i) {
            mySet.insert(i);
            ++tempSize;
        }

        for (int i = 9; i >= 0; --i) {
            assert mySet.size() == tempSize;
            boolean isInI = mySet.isIn(i);

            mySet.delete(i);
            assertTrue(mySet.repOK());
            assertFalse(mySet.isIn(i));
            System.out.println("mySet.size(): " + mySet.size());
            if (isInI) {
                System.out.println("tempSize: " + (tempSize - 1));
                assertEquals(mySet.size(), --tempSize);
            } else {
                System.out.println("tempSize: " + tempSize);
                assertEquals(mySet.size(), tempSize);
            }
        }

        for (int i = 0; i < 100; ++i) {
            int random = (int) (Math.random() * (20 - 10 + 1));
            mySet.insert(random);
            assertTrue(mySet.repOK());
            mySet.delete(random);
            mySet.delete(random);
            assertTrue(mySet.repOK());
        }

        System.out.println("Test delete finish!");
        System.out.println("----------");
    }
}