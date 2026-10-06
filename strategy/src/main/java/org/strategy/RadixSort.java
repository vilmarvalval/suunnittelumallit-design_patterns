package org.strategy;

import java.util.Arrays;

public class RadixSort implements SortingStrategy {
    @Override
    public long[] sort(long[] array) {
        int n= array.length;
        radixSort(array, n);
        return array;
    }

    static void radixSort(long[] arr, int n){
        //find max number to know number of digits
        long m= getMax(arr, n);

        //count sort for every digit.
        // instead of passing digit number, exp is passed.
        // exp is 10^i where i is current digit
        for (int exp=1; m/exp> 0; exp*=10)
            countSort(arr, n, exp);
    }

    static long getMax(long[] arr, int n){
        long mx= arr[0];
        for (int i=0; i<n; i++){
            if (arr[i]>mx)
                mx= arr[i];
        }
        return mx;
    }

    static void countSort(long[] arr, int n, int exp){
        long[] output = new long[n];
        int i;
        int[] count =new int[10];
        Arrays.fill(count,0);

        //store count of occurrences in count[]
        for (i = 0; i < n; i++) {
            count[Math.toIntExact((arr[i] / exp) % 10)]++;
        }

        //change count[i] so that it contains actual position of this digit in output[]
        for (i = 1; i < 10; i++)
            count[i]+=count[i-1];

        //build output
        for (i = n-1; i >= 0; i--) {
            output[count[Math.toIntExact((arr[i] / exp) % 10)]-1]=arr[i];
            count[Math.toIntExact((arr[i] / exp) % 10)]--;
        }

        //copy output[] to arr[], so that arr[] now
        // contains sorted numbers according to current digit
        for (i = 0; i < n; i++)
            arr[i]= Math.toIntExact(output[i]);
    }
}