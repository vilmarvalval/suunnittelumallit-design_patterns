package org.template_method;

public class Die {
    private int value;
    private boolean locked;

    public Die(){
        this.value=0;
    }
    public int getValue(){
        return this.value;
    }
    public void setValue(int value){
        this.value=value;
    }

    public void lock(){
        this.locked=true;
    }
    public boolean isLocked(){
        return this.locked;
    };
}