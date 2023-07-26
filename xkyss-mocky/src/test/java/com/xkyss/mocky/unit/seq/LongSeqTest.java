package com.xkyss.mocky.unit.seq;

import com.xkyss.mocky.unit.types.Ints;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;
import static java.util.stream.IntStream.range;

public class LongSeqTest {
    @Test
    public void testConstructorMinBiggerMax() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> new LongSeq(0, 1, 10, 20, false));
    }

    @Test
    public void testIntSeq() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LongSeq seq = new LongSeq();
            range(0, 100).forEach(n -> Assertions.assertEquals((long) seq.get(), n));
        }
    }

    @Test
    public void testIntSeqCycle() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LongSeq seq = new LongSeq().start(10).max(15).cycle(true);
            for (int j = 0; j <TEST_COUNT; j++) {
                long val = seq.get();
                Assertions.assertTrue(10 <= val);
                Assertions.assertTrue(val <= 15);
            }
        }
    }

    @Test
    public void testIntSeqListings() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Ints ints = new Ints(random);

        for (int i = 0; i < TEST_COUNT; i++) {
            int size = ints.range(100, 1000).get();
            List<Long> lst = new LongSeq().start(1).list(size);
            int sum = lst.stream().mapToInt(Long::intValue).sum();
            Assertions.assertEquals(sum, (long) size * (size + 1) / 2);
        }
    }
}
