package com.ag.assignment.insurance.model;


public enum State {
    IS("ILLINOIS"),
    IN("INDIANA"),
    MN("MINNESOTA");
    private final String fullForm;

    //  The constructor for an enum type must be package-private or private access. It automatically creates
    //  the constants that are defined at the beginning of the enum body.
    //  You cannot invoke an enum constructor yourself.

    State(String fullForm) {
        this.fullForm = fullForm;
    }

    public String getFullForm() {
        return fullForm;
    }



}


