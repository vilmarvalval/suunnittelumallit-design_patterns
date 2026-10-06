package org.strategy;

import java.util.Arrays;
import java.util.Scanner;

public class SortingAlgorithm {
    static long[] test = {10,2,3,-987,4,5,6,-9,7,123,8,9,0, 23456, -23449,2,6,4,6,78,54,3,4,5,6,4,4,5,6,4,-33,4,53,5,-4,35,36,-54,7,73,53,-4,5,450,0};
    static long[] bubble= {144309, 137064, 68845, 67732, 45077, 243049, 140136, 59937, 95546, 46298};
    static long[] select= {103433, 71757, 25240, 30765, 139476, 46989, 140092, 35759, 164377, 96049};
    static long[] count = {30706, 95667, 98649, 150281, 141472, 78094, 32203, 121563, 62445, 127423};
    static long[] quick = {89798, 39019, 55458, 93817, 34507, 131346, 88675, 88025, 27093, 26068};
    static long[] radix = new long[10];
    static long[] array = new long[10];

    static long[] selected;
    static long[] copy;
    public static long time;
    public static long wait;

    static long[] avgTimes= new long[6];

    static boolean doBubble= true;
    static boolean doSelect= true;
    static boolean doCount = true;
    static boolean doQuick = true;

    static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int size = 15_000;
        int range= 240_000;
        String[] stringArr;
        StringBuilder stringInt = new StringBuilder();
        System.out.println("Input array size: (ideally below 10 000 000)");
        stringArr = scan.nextLine().split(" ");
        for (String string : stringArr) {
            try {
                stringInt.append(string);
                Integer.parseInt(String.valueOf(stringInt));
            } catch (Throwable e){
                System.out.println("Invalid string: '"+string+"', discarding...");
                stringInt.setLength(stringInt.length() - 1);
            }

        }
        size= Integer.parseInt(String.valueOf(stringInt));
        System.out.println("Array size set to "+size);
        stringInt.setLength(0);
        if (size<1){
            System.out.println("Array size must be at least 1. Overwriting...");
            //size=1;
        } else if (size>10_000_000) {
            System.out.println("\n<<< WARNING:Array is massive! Sorting can take *very* long time and the program may even crash! >>>\n");
        }

        System.out.println("Input integer range: (ideally below 250 000 000)");
        System.out.println("From 0 to..:");
        stringArr = scan.nextLine().split(" ");
        for (String s : stringArr) {
            try {
                stringInt.append(s);
                Integer.parseInt(String.valueOf(stringInt));
            } catch (Throwable e){
                System.out.println("Invalid string: '"+s+"', discarding...");
                stringInt.setLength(stringInt.length() - 1);
            }
        }
        range = Integer.parseInt(String.valueOf(stringInt));
        System.out.println("Array range set to 0-"+range);
        if (range<1){
            System.out.println("Array range must be at least 0-1. Overwriting...");
            //range=1;
        }
        System.out.println("Saving array...");
        selected = randArray(size, range);

        copy = new long[size];
        //System.out.println(Arrays.toString(selected));
        SortingContext speedTest;

        System.out.println("Do you want to do a benchmark? Y/n");
        String ans = scan.nextLine();
        if (ans.equalsIgnoreCase("n")){
            SortingContext sortHandler = new SortingContext(new BubbleSort());
            System.arraycopy(selected,0,copy,0,selected.length);

            System.out.println("""
                Select an algorithm:
                1. Bubble sort     -  Worst at everything.
                2. Selection sort  -  Not as bad but still bad. Beats counting sort after range ~50 000 000.
                3. Counting sort   -  Ideal up to a range of ~500 000. Memory intensive.
                4. Quick sort      -  Ideal for ranges past 500 000.
                5. Radix sort      -  Beats counting sort after range ~1 000 000 but loses to quick sort.
                6. Arrays.sort()   -  Dual-pivot quicksort. Best on average.
                """);
            int choice = scan.nextInt();
            switch (choice){
                case 1:
                    System.out.println("'Bubble sort' chosen.");
                    sortHandler.setSortingStrategy(new BubbleSort());
                    break;
                case 2:
                    System.out.println("'Selection sort' chosen.");
                    sortHandler.setSortingStrategy(new SelectionSort());
                    break;
                case 3:
                    System.out.println("Counting sort chosen.");
                    sortHandler.setSortingStrategy(new SelectionSort());
                    break;
                case 4:
                    System.out.println("Quick sort chosen.");
                    sortHandler.setSortingStrategy(new QuickSort());
                    break;
                case 5:
                    System.out.println("Radix sort chosen.");
                    sortHandler.setSortingStrategy(new RadixSort());
                    break;
                case 6:
                    System.out.println("Arrays.sort() chosen");
                    sortHandler.setSortingStrategy(new ArraysSort());
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            sortNow(sortHandler, true);
            average(copy);

        }else{
            String[] command= ans.split(" ");
            if (command[0].equalsIgnoreCase("s")) {
                command[0]="";
                if (Arrays.asList(command).contains("b")) {
                    doBubble = false;
                    System.out.println("You have selected to skip bubble sort.");
                }
                if (Arrays.asList(command).contains("s")) {
                    doSelect = false;
                    System.out.println("You have selected to skip select sort.");
                }
                if (Arrays.asList(command).contains("c")) {
                    doCount = false;
                    System.out.println("You have selected to skip count sort.");
                }
            }

            if (range>250_000_000){
                doCount = false;
                System.out.println("Count sort skipped due to very large range. (over 250 000 000)");
            }

            //warm up
            System.out.println("Warming up...");
            int warmups=25;
            for (int i = 0; i < warmups; i++) {
                if (doBubble) {
                    System.arraycopy(selected,0,copy,0,selected.length);
                    speedTest = new SortingContext(new BubbleSort());
                    sortNow(speedTest, false);
                }

                if (doSelect) {
                    System.arraycopy(selected, 0, copy, 0, selected.length);
                    speedTest = new SortingContext(new SelectionSort());
                    sortNow(speedTest, false);
                }

                if (doCount) {
                    System.arraycopy(selected, 0, copy, 0, selected.length);
                    speedTest = new SortingContext(new CountingSort());
                    sortNow(speedTest, false);
                }

                System.arraycopy(selected,0,copy,0,selected.length);
                speedTest = new SortingContext(new QuickSort());
                sortNow(speedTest, false);

                System.arraycopy(selected,0,copy,0,selected.length);
                speedTest = new SortingContext(new RadixSort());
                sortNow(speedTest, false);

                System.arraycopy(selected,0,copy,0,selected.length);
                speedTest = new SortingContext(new ArraysSort());
                sortNow(speedTest, false);

                System.out.println("Rounds done "+(i+1)+"/"+warmups);
            }




            System.arraycopy(selected,0,copy,0,selected.length);
            System.out.println("\n=== Bubble sort === \n");
            if (!doBubble) {
                System.out.println("Bubble sort skipped.");
            } else{
                speedTest = new SortingContext(new BubbleSort());
                for (int i = 0; i < 10; i++) {
                    bubble[i] = Math.toIntExact(sortNow(speedTest, true));
                    System.arraycopy(selected,0,copy,0,selected.length);
                }
                avgTimes[0]=average(bubble);
            }


            System.arraycopy(selected,0,copy,0,selected.length);
            System.out.println("\n=== Selection sort === \n");
            if (!doSelect){
                System.out.println("Selection sort skipped.");
            } else {
                speedTest = new SortingContext(new SelectionSort());
                for (int i = 0; i < 10; i++) {
                    select[i] = Math.toIntExact(sortNow(speedTest, true));
                    System.arraycopy(selected, 0, copy, 0, selected.length);
                }
                avgTimes[1] = average(select);
            }

            System.arraycopy(selected,0,copy,0,selected.length);
            System.out.println("\n=== Counting sort === \n");
            if (!doCount){
                System.out.println("Count sort skipped.");
            }else {
                speedTest = new SortingContext(new CountingSort());
                for (int i = 0; i < 10; i++) {
                    count[i] = Math.toIntExact(sortNow(speedTest, true));
                    System.arraycopy(selected, 0, copy, 0, selected.length);
                }
                avgTimes[2] = average(count);
            }

            System.arraycopy(selected,0,copy,0,selected.length);
            System.out.println("\n=== Quick sort === \n");
            speedTest = new SortingContext(new QuickSort());
            for (int i = 0; i < 10; i++) {
                quick[i] = Math.toIntExact(sortNow(speedTest, true));
                System.arraycopy(selected,0,copy,0,selected.length);
            }
            avgTimes[3]=average(quick);

            System.arraycopy(selected,0,copy,0,selected.length);
            System.out.println("\n=== Radix sort === \n");
            speedTest = new SortingContext(new RadixSort());
            for (int i = 0; i < 10; i++) {
                radix[i] = Math.toIntExact(sortNow(speedTest, true));
                System.arraycopy(selected,0,copy,0,selected.length);
            }
            avgTimes[4]=average(radix);

            System.arraycopy(selected,0,copy,0,selected.length);
            System.out.println("\n=== Arrays.sort() (Dual-pivot quicksort) === \n");
            speedTest = new SortingContext(new ArraysSort());
            for (int i = 0; i < 10; i++) {
                array[i] = Math.toIntExact(sortNow(speedTest, true));
                System.arraycopy(selected,0,copy,0,selected.length);
            }
            avgTimes[5]=average(array);

            System.out.println(
                    "\nAverage sorting times for size "+size+" & range "+range+":"+
                            "\n2.    Bubble sort: "+(doBubble? avgTimes[0]+" nanoseconds": "Skipped.")+
                            "\n2. Selection sort: "+(doSelect? avgTimes[1]+" nanoseconds": "Skipped.")+
                            "\n3.  Counting sort: "+(doCount? avgTimes[2]+" nanoseconds" : "Skipped.")+
                            "\n4.     Quick sort: "+avgTimes[3]+" nanoseconds"+
                            "\n4.     Radix sort: "+avgTimes[4]+" nanoseconds"+
                            "\n4.  Arrays.sort(): "+avgTimes[5]+" nanoseconds"
            );
        }
    }

    public static long sortNow(SortingContext sortHandler, boolean debug){
        if (debug) System.out.println("Sorting "+copy.length+" items...");
        time = System.nanoTime();
        long[] result = sortHandler.sort(copy);
        wait = System.nanoTime()-time;
        if (debug) {
            System.out.println("Sort complete in " + wait + " nanoseconds.");
            if (copy.length<10_000_000){
                System.out.println(Arrays.toString(result));
            } else {
                System.out.println("Array is too large to print.");
            }
        }
        return wait;
    }

    public static long average(long[] array){
        long avg=0;
        for (long j : array) {
            avg += j;
        }
        avg = avg/array.length;
        System.out.println("Average value: "+avg);
        return avg;
    }

    public static long[] randArray(int size, int range){
        long[] array= new long[size];

        for (int i = 0; i < size; i++) {
            array[i]= (int) (Math.random() *  range);
        }
        return array;
    }
}