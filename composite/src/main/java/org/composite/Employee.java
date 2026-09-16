package org.composite;


public class Employee extends Organization {
    protected String name;
    protected double salary;

    public Employee(String name, double salary){
        super(name);
        this.salary = salary;
    }

    public void printData(){
        System.out.println(this.name);
    }

    @Override
    public double getSalary(){
        return this.salary;
    }

    @Override
    public void add(Organization deptmt) {
        throw new UnsupportedOperationException("Not supported in leaf, 'Employee' can not have children.");
    }

    @Override
    public void remove(Organization deptmt) {
        throw new UnsupportedOperationException("Not supported in leaf, 'Employee' can not have children.");
    }

    @Override
    public Organization getChild(int index){
        throw new UnsupportedOperationException("Not supported in leaf, 'Employee' can not have children.");
    };
}
