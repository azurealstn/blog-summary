package com.azurealstn.solid;

import com.azurealstn.solid.guitarkinds.AcousticGuitar;
import com.azurealstn.solid.guitarkinds.Guitar;
import com.azurealstn.solid.play.PlayService;
import com.azurealstn.solid.play.PlayServiceImpl;

public class MyContainer {

    public PlayService playService() {
        return new PlayServiceImpl(guitar());
    }

    public Guitar guitar() {
        return new AcousticGuitar();
    }
}
