package org.strategy;

public class SelectionSort implements SortingStrategy{
    @Override
    public long[] sort(long[] array){
        int n= array.length;

        for (int i = 0; i < n-1; i++) {
            //assume current pos holds minimum
            int min_idx=i;

            //iterate to find the actual minimum
            for (int j = i+1; j < n; j++) {
                if(array[j]<array[min_idx]){

                    //update if smaller element found
                    min_idx=j;
                }
            }

            //move minimum element to correct position
            long temp = array[i];
            array[i]=array[min_idx];
            array[min_idx]=temp;
        }

        return array;
    }
}