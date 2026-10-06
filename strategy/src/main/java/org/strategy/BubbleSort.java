package org.strategy;

public class BubbleSort implements SortingStrategy{
    @Override
    public long[] sort(long[] array){
        long n= array.length;
        long i, j, temp;
        boolean swpped;
        for (i = 0; i < n-1; i++) {
            swpped=false;
            for (j = 0; j < n-i-1; j++) {
                if (array[Math.toIntExact(j)]>array[Math.toIntExact(j + 1)]){
                    temp=array[Math.toIntExact(j)];
                    array[Math.toIntExact(j)]=array[Math.toIntExact(j + 1)];
                    array[Math.toIntExact(j + 1)]= Math.toIntExact(temp);
                    swpped=true;
                }
            }
            if (!swpped) break;
        }
        return array;
    }
}