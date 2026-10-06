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
    static long[] pigon = new long[10];

    static long[] selected;
    static long[] copy;
    static long[] bench1;
    static long[] copy1;
    static long[] bench2;
    static long[] copy2;

    public static long time;
    public static long wait;

    static double[] avgTimes0 = new double[7]; //7 is the number of available sorting algos
    static double[] avgTimes1 = new double[7];
    static double[] avgTimes2 = new double[7];

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
        System.out.println("Input array size: (ideally below 1 000 000)");
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
            size=1;
        } else if (size>1_000_000) {
            System.out.println("\n<<< WARNING:Array is massive! Sorting can potentially *very* long time and the program may even crash! >>>\n");
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

        System.out.println("Generating array...");
        selected = randArray(size, range);

        copy = new long[size];

        System.out.println("Do you want to do a benchmark? Y/n");
        String ans = scan.nextLine();
        if (ans.equalsIgnoreCase("n")){
            SortingContext sortHandler = new SortingContext(new BubbleSort());
            System.arraycopy(selected,0,copy,0,selected.length);

            System.out.println("""
                Select an algorithm:
                1. Bubble sort      -  Worst at everything.
                2. Selection sort   -  Not as bad but still bad. Beats counting sort after range ~50 000 000.
                3. Counting sort    -  Ideal up to a range of ~500 000. Memory intensive.
                4. Quick sort       -  Ideal for ranges past 500 000.
                5. Radix sort       -  Beats counting sort after range ~1 000 000 but loses to quick sort.
                6. Pigeonhole sort  -  Almost like count sort. Few differences.
                N/A. Arrays.sort()  -  Dual-pivot quicksort. Best on average. Exists only as a benchmark reference.
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
                    System.out.println("Pigeonhole sort chosen.");
                    sortHandler.setSortingStrategy(new PigeonHole());
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            sortNow(copy, sortHandler, true, ans);
            average(copy);

        }else {
            String[] command = ans.split(" ");
            if (command[0].equalsIgnoreCase("s")) {
                command[0] = "";
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

            if (range > 250_000_000) {
                doCount = false;
                System.out.println("Count sort skipped due to very large range. (over 250 000 000)");
            }
            if (size > 20_000 && doBubble) {
                System.out.println("Skipping Bubble sort recommended for large array (size over 20 000)");
                System.out.println("Skip Bubble sort? Y/n");
                if (!scan.nextLine().equalsIgnoreCase("n")) {
                    System.out.println("Skipping bubble sort.\n");
                    doBubble = false;
                }
            }

            //warm up
            //java optimizes code as it runs so a 'warm up' is necessary to get more accurate results
            int warmups = 25;
            warmUp(selected, copy, warmups, ans);

            //benchmark
            benchmark(selected, copy, ans, size, range, avgTimes0);

            System.out.println("Generating array size 30...");
            int size1 = 30;
            int range1 = 234_567;
            bench1 = randArray(size1, range1);
            copy1 = new long[size1];
            warmUp(bench1, copy1, warmups, ans);
            benchmark(bench1, copy1, ans, size1, range1, avgTimes1);

            System.out.println("Generating array size 100 000...");
            doBubble=false; //skipping these two for convenience.
            doSelect=false;
            int size2 = 100_000;
            int range2 = 12_345;
            copy2 = new long[size2];
            bench2 = randArray(size2, range2);
            warmUp(bench2, copy2, warmups, ans);
            benchmark(bench2, copy2, ans, size2, range2, avgTimes2);

            System.out.println("\n\n\n\n");
            printResults(size,range,avgTimes0);
            printResults(size1,range1,avgTimes1);
            printResults(size2,range2,avgTimes2);
        }
    }

    public static void warmUp(long[] selected, long[] copy, int warmups, String ans){
        System.out.println("Warming up...");
        for (int i = 0; i < warmups; i++) {
            SortingContext speedTest;
            if (doBubble) {
                System.arraycopy(selected,0,copy,0,selected.length);
                speedTest = new SortingContext(new BubbleSort());
                sortNow(copy, speedTest, false, ans);
            }

            if (doSelect) {
                System.arraycopy(selected, 0, copy, 0, selected.length);
                speedTest = new SortingContext(new SelectionSort());
                sortNow(copy, speedTest, false, ans);
            }

            if (doCount) {
                System.arraycopy(selected, 0, copy, 0, selected.length);
                speedTest = new SortingContext(new CountingSort());
                sortNow(copy, speedTest, false, ans);
            }

            System.arraycopy(selected,0,copy,0,selected.length);
            speedTest = new SortingContext(new QuickSort());
            sortNow(copy, speedTest, false, ans);

            System.arraycopy(selected,0,copy,0,selected.length);
            speedTest = new SortingContext(new RadixSort());
            sortNow(copy, speedTest, false, ans);

            System.arraycopy(selected,0,copy,0,selected.length);
            speedTest = new SortingContext(new ArraysSort());
            sortNow(copy, speedTest, false, ans);

            System.arraycopy(selected,0,copy,0,selected.length);
            speedTest = new SortingContext(new PigeonHole());
            sortNow(copy, speedTest, false, ans);

            System.out.println("Rounds done "+(i+1)+"/"+warmups);
        }
    }

    public static void benchmark(long[] selected, long[] copy, String ans, int size, int range, double[] avgTimes){
        System.arraycopy(selected,0,copy,0,selected.length);
        System.out.println("\n=== Bubble sort === \n");
        SortingContext speedTest;
        if (!doBubble) {
            System.out.println("Bubble sort skipped.");
        } else{
            speedTest = new SortingContext(new BubbleSort());
            for (int i = 0; i < 10; i++) {
                bubble[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
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
                select[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
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
                count[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
                System.arraycopy(selected, 0, copy, 0, selected.length);
            }
            avgTimes[2] = average(count);
        }

        System.arraycopy(selected,0,copy,0,selected.length);
        System.out.println("\n=== Quick sort === \n");
        speedTest = new SortingContext(new QuickSort());
        for (int i = 0; i < 10; i++) {
            quick[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
            System.arraycopy(selected,0,copy,0,selected.length);
        }
        avgTimes[3]=average(quick);

        System.arraycopy(selected,0,copy,0,selected.length);
        System.out.println("\n=== Radix sort === \n");
        speedTest = new SortingContext(new RadixSort());
        for (int i = 0; i < 10; i++) {
            radix[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
            System.arraycopy(selected,0,copy,0,selected.length);
        }
        avgTimes[4]=average(radix);

        System.arraycopy(selected,0,copy,0,selected.length);
        System.out.println("\n=== Arrays.sort() (Dual-pivot quicksort) === \n");
        speedTest = new SortingContext(new ArraysSort());
        for (int i = 0; i < 10; i++) {
            array[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
            System.arraycopy(selected,0,copy,0,selected.length);
        }
        avgTimes[5]=average(array);

        System.arraycopy(selected,0,copy,0,selected.length);
        System.out.println("\n=== Pigeonhole sort === \n");
        speedTest = new SortingContext(new PigeonHole());
        for (int i = 0; i < 10; i++) {
            pigon[i] = Math.toIntExact(sortNow(copy, speedTest, true, ans));
            System.arraycopy(selected,0,copy,0,selected.length);
        }
        avgTimes[6]=average(pigon);

        printResults(size, range, avgTimes);
    }

    public static long sortNow(long[] copy, SortingContext sortHandler, boolean debug, String ans){
        if (debug) System.out.println("Sorting "+copy.length+" items...");
        long time = System.nanoTime();
        long[] result = sortHandler.sort(copy);
        long wait = (System.nanoTime()-time)/1_000;
        if (debug) {
            System.out.println("Sort complete in " + String.format ("%.3f",(double) wait/1000) + " ms.");
            if (ans.equals("n")) {
                if (copy.length < 10_000_000) {
                    System.out.println(Arrays.toString(result));
                } else {
                    System.out.println("Array is too large to print.");
                }
            }
        }
        return wait;
    }

    public static void printResults(int size, int range, double[] avgTimes){
        System.out.println(
                "\nAverage sorting times for size "+size+" & range "+range+":"+
                        "\n2.     Bubble sort: "+(avgTimes[0]>0? String.format ("%.3f", avgTimes[0]/1000)+" ms.": "Skipped.")+
                        "\n2.  Selection sort: "+(avgTimes[1]>0? String.format ("%.3f", avgTimes[1]/1000)+" ms.": "Skipped.")+
                        "\n3.   Counting sort: "+(avgTimes[2]>0? String.format ("%.3f", avgTimes[2]/1000)+" ms." : "Skipped.")+
                        "\n4.      Quick sort: "+String.format ("%.3f", avgTimes[3]/1000)+" ms."+
                        "\n5.      Radix sort: "+String.format ("%.3f", avgTimes[4]/1000)+" ms."+
                        "\n6. Pigeonhole sort: "+String.format ("%.3f", avgTimes[6]/1000)+" ms."+
                        "\n7.   Arrays.sort(): "+String.format ("%.3f", avgTimes[5]/1000)+" ms."
        );
    }


    public static double average(long[] array){
        double avg=0;
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