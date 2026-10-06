package org.strategy;
import java.util.Arrays;

//Source: https://www.geeksforgeeks.org/dsa/pigeonhole-sort/

public class PigeonHole implements SortingStrategy {
    public static void pigeonhole_sort(long[] arr,
                                       int n)
    {
        long min = arr[0];
        long max = arr[0];
        int range, i, j, index;

        for(int a=0; a<n; a++)
        {
            if(arr[a] > max)
                max = arr[a];
            if(arr[a] < min)
                min = arr[a];
        }

        range = Math.toIntExact(max - min + 1);
        int[] phole = new int[range];
        Arrays.fill(phole, 0);

        for(i = 0; i<n; i++)
            phole[Math.toIntExact(arr[i] - min)]++;


        index = 0;

        for(j = 0; j<range; j++)
            while(phole[j]-->0)
                arr[index++]=j+min;

    }

    @Override
    public long[] sort(long[] arr) {
        int n = arr.length;
        pigeonhole_sort(arr,n);
        return arr;
    }
}