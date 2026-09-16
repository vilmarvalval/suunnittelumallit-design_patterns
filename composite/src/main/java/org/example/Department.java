package org.example;

public class Department extends Organization {
    public Department(String name) {
        super(name);
    }

    /*@Override
    public void add(Organization deptmt) {
        if (deptmt.getClass().isInstance(Department.class))
            super.add(deptmt);
        else if (deptmt.getClass().isInstance(Employee.class)) {
            super.add(deptmt);
        }else {
            throw new UnsupportedOperationException("Unsupported Class");
        }
    }

    @Override
    public void remove(Organization deptmt) {
        if (deptmt.getClass().isInstance(Department.class))
            super.remove(deptmt);
        else if (deptmt.getClass().isInstance(Employee.class)) {
            super.remove(deptmt);
        }else {
            throw new UnsupportedOperationException("Unsupported Class");
        }
    }*/
}
