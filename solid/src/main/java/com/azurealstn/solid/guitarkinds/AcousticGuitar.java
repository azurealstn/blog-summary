package com.azurealstn.solid.guitarkinds;

public class AcousticGuitar implements Guitar {

    private final String GUITAR_ACOUSTIC = "통기타";

    @Override
    public String guitarKinds(String song) {
        return GUITAR_ACOUSTIC;
    }
}
