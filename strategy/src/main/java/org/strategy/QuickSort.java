package org.strategy;

//Source: https://www.geeksforgeeks.org/dsa/quick-sort-algorithm/

public class QuickSort implements SortingStrategy {
    static int partition(long[] arr, int low, int high){

        //choose pivot
        int pivot = Math.toIntExact(arr[high]);

        //index of smaller element and indicates
        // the right position of pivot found so far
        int i= low-1;

        //traverse arr[low..high] and move all smaller
        // elements to the left side. Elements from low to
        // i are smaller after every iteration
        for (int j = low; j <= high; j++) {
            if (arr[j]<pivot){
                i++;
                swap(arr, i, j);
            }
        }

        //move pivot after smaller elements
        // and return its position
        swap(arr, i+1, high);
        return i+1;
    }

    //swap
    static void swap(long[] arr, int i, int j){
        long temp= arr[i];
        arr[i]= arr[j];
        arr[j]=temp;
    }

    static void quickSort(long[] arr, int low, int high){
        if (low<high){
            //pi is the partition return index of pivot
            int pi = partition(arr, low, high);

            //recursion calls for smaller elements
            // and greater or equals elements
            quickSort(arr, low, pi-1);
            quickSort(arr, pi+1, high);
        }
    }

    @Override
    public long[] sort(long[] array) {
        int n= array.length;
        quickSort(array, 0, n-1);

        return array;
    }

}