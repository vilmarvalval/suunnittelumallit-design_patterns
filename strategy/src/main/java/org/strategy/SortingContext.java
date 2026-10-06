package org.strategy;

public class SortingContext {
    private SortingStrategy sortingStrategy;

    public SortingContext(SortingStrategy sortingStrategy){
        this.sortingStrategy= sortingStrategy;
    }

    public void  setSortingStrategy(SortingStrategy sortingStrategy){
        this.sortingStrategy= sortingStrategy;
    }

    public long[] sort(long[] array){
        return sortingStrategy.sort(array);
    }
}