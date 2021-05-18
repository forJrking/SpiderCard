package com.spider.card;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        for (int i = 0; i < 4 * 2; i++) {
            System.out.println("输出;" + i % 4);
        }

        assertEquals(4, 2 + 2);
    }
}