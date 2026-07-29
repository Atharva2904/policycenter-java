package com.wipfli.training.model;


public enum State {
    IS("ILLINOIS", 1),
    IN("INDIANA", 2),
    MN("MINNESOTA", 3);
    private final String fullForm;
    private final int code;


    State(String fullForm, int code ) {
        this.fullForm = fullForm;
        this.code = code;
    }

    public static State getStateFromChoice(int choice){
        for(State state: values()){
            if(state.code == choice) return state;
        }

        throw new IllegalArgumentException("Invalid state code provided!");
    }


}


