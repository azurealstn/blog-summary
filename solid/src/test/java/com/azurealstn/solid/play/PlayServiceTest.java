package com.azurealstn.solid.play;

import com.azurealstn.solid.MyContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayServiceTest {

    private PlayService playService;

    @BeforeEach
    public void beforeEach() {
        MyContainer myContainer = new MyContainer();
        playService = myContainer.playService();
    }

    @Test
    void createPlay() {
        //given
        Long id = 1L;
        String player = "sungHa";
        String song = "let it go";
        String guitarKinds = "통기타";

        //when
        Play play = playService.createPlay(1L, player, song);

        //then
        assertThat(play.getGuitarKinds()).isEqualTo(guitarKinds);
    }
}