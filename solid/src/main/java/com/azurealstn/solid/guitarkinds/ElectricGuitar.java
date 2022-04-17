package com.azurealstn.solid.guitarkinds;

public class ElectricGuitar implements Guitar {

    private final String GUITAR_ELECTRIC = "일렉기타";

    @Override
    public String guitarKinds(String song) {
        return GUITAR_ELECTRIC;
    }
}
