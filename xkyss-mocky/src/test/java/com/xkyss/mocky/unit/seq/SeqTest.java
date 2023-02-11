package com.xkyss.mocky.unit.seq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.IntStream.range;

public class SeqTest {

    @Test
    public void testSeqNullList() {
        Assertions.assertThrows(NullPointerException.class, () -> new Seq<>(null));
    }

    @Test
    public void testEmptyList() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Seq<>(new ArrayList<>()).list(100).get());
    }

//    @Test
//    public void testSeq() {
//        List<Integer> arr = M.intSeq().max(100).cycle(false).list(100).val();
//        List<Integer> seq = M.seq(arr).cycle(false).list(10).val();
//
//        seq.forEach(i -> assertEquals(seq.get(i), i));
//    }

    @Test
    public void testTwoElementsCycle() {
        List<Integer> seq = new Seq<>(Arrays.asList(1, 2)).cycle(true).list(100).get();

        range(0, seq.size()).forEach(i -> {
            if (i%2==0)
                Assertions.assertEquals(1, (int) seq.get(i));
            else
                Assertions.assertEquals(2, (int) seq.get(i));
        });
    }

    @Test
    public void testOneElementNoCycle() {
        List<Integer> seq = new Seq<>(Arrays.asList(2)).list(10).get();

        Assertions.assertEquals(2, (int) seq.get(0));
        Assertions.assertNull(seq.get(1));
    }

    @Test
    public void testOneElementNoCycleAfterNonNull() {
        List<Integer> seq = new Seq<>(Arrays.asList(2)).after(4).list(10).get();

        Assertions.assertEquals(2, (int) seq.get(0));
        Assertions.assertEquals(4, (int) seq.get(1));
    }
}
