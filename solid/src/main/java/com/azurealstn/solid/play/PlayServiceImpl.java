package com.azurealstn.solid.play;

import com.azurealstn.solid.guitarkinds.AcousticGuitar;
import com.azurealstn.solid.guitarkinds.Guitar;

public class PlayServiceImpl implements PlayService {

//    Guitar guitar = new AcousticGuitar();
    private Guitar guitar;

    public PlayServiceImpl(Guitar guitar) {
        this.guitar = guitar;
    }

    @Override
    public Play createPlay(Long id, String player, String song) {
        String guitarKindsName = guitar.guitarKinds(song);
        return new Play(id, player, song, guitarKindsName);
    }
}
