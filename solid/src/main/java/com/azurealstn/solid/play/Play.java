package com.azurealstn.solid.play;

public class Play {

    private Long id; //연주id
    private String player; //연주자
    private String song; //곡이름
    private String guitarKinds; //기타종류

    public Play(Long id, String player, String song, String guitarKinds) {
        this.id = id;
        this.player = player;
        this.song = song;
        this.guitarKinds = guitarKinds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getGuitarKinds() {
        return guitarKinds;
    }

    public void setGuitarKinds(String guitarKinds) {
        this.guitarKinds = guitarKinds;
    }
}
