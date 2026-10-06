package org.strategy;


import java.util.Arrays;

public class ArraysSort implements SortingStrategy {
    @Override
    public long[] sort(long[] array) {

        Arrays.sort(array);

        return array;
    }
}