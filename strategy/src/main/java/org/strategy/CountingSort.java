package org.strategy;

//Source: https://www.geeksforgeeks.org/dsa/counting-sort/

public class CountingSort implements SortingStrategy {
    @Override
    public long[] sort(long[] array) {
        int n = array.length;

        //find max
        int maxVal=0;
        for (long j : array) {
            if (j > maxVal) maxVal = Math.toIntExact(j);
        }

        //create and initialize count array
        long[] count = new long[maxVal+1];

        //count frequency of each element
        for (int i =0; i<n; i++)
            count[Math.toIntExact(array[i])]++;

        //build output array
        long[] ans = new long[n];
        for (int i = n - 1; i >= 0; i--) {
            ans[Math.toIntExact(count[Math.toIntExact(array[i])] - 1)] = array[i];
            count[Math.toIntExact(array[i])]--;
        }

        return ans;
    }
}