package com.wipfli.training.model;


public enum State {
    IS("ILLINOIS", 1),
    IN("INDIANA", 2),
    MN("MINNESOTA", 3);
    private final String fullForm;
    private final int code;

    //  The constructor for an enum type must be package-private or private access. It automatically creates
    //  the constants that are defined at the beginning of the enum body.
    //  You cannot invoke an enum constructor yourself.

    State(String fullForm, int code ) {
        this.fullForm = fullForm;
        this.code = code;
    }

    public int getCode() {
        return code;
    }


    public String getFullForm() {
        return fullForm;
    }

    public static State getStateFromChoice(int choice){
        for(State state: values()){
            if(state.code == choice) return state;
        }

        throw new IllegalArgumentException("Invalid state code provided!");
    }


}


