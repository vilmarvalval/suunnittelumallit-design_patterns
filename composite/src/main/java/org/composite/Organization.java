package org.composite;

import java.util.*;

public class Organization {
    protected String name;
    private final List<Organization> children = new ArrayList<>();

    public Organization(String name){
        this.name = name;
    }

    public void printData(){
        System.out.println(this.name);
    }

    public String getName(){
        return this.name;
    }

    public double getSalary(){
        double sum=0;
        for (int i = 0; i < this.children.toArray().length; i++) {
            sum+=getChild(i).getSalary();
        }
        return sum;
    }

    public void add(Organization deptmt) {
        this.children.add(deptmt);
    }

    public void remove(Organization deptmt) {
        this.children.remove(deptmt);
    }

    public Organization getChild(int index){
        return this.children.get(index);
    };
}
