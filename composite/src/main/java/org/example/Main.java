package org.example;

public class Main {
    static void main(String[] args) {
        System.out.print("Hello and welcome!\n");

        Organization testOrg = new Organization("testOrg");
        Department testDept1 = new Department("testDep1");
        Department testDept2 = new Department("testDep2");
        Employee empl1 = new Employee("John", 15_000);
        Employee empl2 = new Employee("Alice", 12_500);
        Employee empl3 = new Employee("Andy", 9_000);
        Employee empl4 = new Employee("Marisa", 20_000);

        testOrg.add(testDept1);
        testOrg.add(testDept2);
        testDept1.add(empl1);
        testDept1.add(empl2);
        testDept2.add(empl3);
        testDept2.add(empl4);

        System.out.println(testDept1.getSalary());
        System.out.println(testDept2.getSalary());
        System.out.println(testOrg.getSalary());
    }
}