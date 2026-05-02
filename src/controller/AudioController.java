package controller;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import view.assets.ResourceManager;

public class AudioController {
    private float musicVolume = 0.7f;
    private float sfxVolume = 0.75f;

    public static final int BGM_MENU = 0;
    public static final int LV1 = 1;
    public static final int LV2 = 2;
    public static final int LV3 = 3;
    public static final int LV4 = 4;
    public static final int SFX_CLICK = 0;
    public static final int SFX_JUMP = 1;
    public static final int SFX_KNIGHT_ATTACK = 2;
    public static final int SFX_KNIGHT_ULT = 3;
    public static final int SFX_KNIGHT_SPECIAL = 4;
    public static final int SFX_DIE = 5;
    public static final int SFX_WIN = 6;
    public static final int SFX_LOSE = 7;
    public static final int SFX_MAGE_ATTACK = 8;
    public static final int SFX_MAGE_ULT = 9;
    public static final int SFX_MAGE_SPECIAL = 10;

    private Clip[] music;
    private Clip[] sfx;

    private int currentMusicId;
    private boolean musicMuted;
    private boolean sfxMuted;
    private boolean premium = true;

    public AudioController(boolean musicMuted, boolean sfxMuted) {
        this.musicMuted = musicMuted;
        this.sfxMuted = sfxMuted;
        loadMusic();
        loadSFX();
        applyMusicMute();
    }

    private void loadMusic() {
        String[] songs = { "menu", "level1", "level2", "level3", "level4" };
        String[] vip = { "vip0", "vip1", "vip2", "vip3", "vip4" };
        String[] song;
        if (premium)
            song = vip;
        else
            song = songs;
        music = new Clip[song.length];
        for (int i = 0; i < song.length; i++) {
            music[i] = ResourceManager.loadClip("/audio/" + song[i] + ".wav");
        }
    }

    private void loadSFX() {
        String[] names = { "click", "jump", "knight_atk", "knight_ult", "knight_special", "die", "victory", "gameover",
                "mage_atk",
                "mage_ult", "mage_special" };
        sfx = new Clip[names.length];
        for (int i = 0; i < names.length; i++) {
            sfx[i] = ResourceManager.loadClip("/audio/" + names[i] + ".wav");
        }
    }

    private void applyVolume(Clip clip, float volume) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    public void playMusic(int id) {
        stopMusic();
        currentMusicId = id;
        applyVolume(music[id], musicVolume);
        music[id].setMicrosecondPosition(0);
        music[id].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pauseMusic() {
        if (music[currentMusicId].isRunning()) {
            music[currentMusicId].stop();
        }
    }

    public void resumeMusic() {
        music[currentMusicId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setLevelMusic(int lvlIndex) {
        switch (lvlIndex) {
            case 0 -> playMusic(LV1);
            case 1 -> playMusic(LV2);
            case 2 -> playMusic(LV3);
            case 3 -> playMusic(LV4);
        }
    }

    public void stopMusic() {
        if (music[currentMusicId].isRunning()) {
            music[currentMusicId].stop();
        }
    }

    private void stopSFX(int id) {
        Clip clip = sfx[id];
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setMicrosecondPosition(0);
        }
    }

    public void stopEndGameSFX() {
        stopSFX(SFX_WIN);
        stopSFX(SFX_LOSE);
    }

    public void toggleMusic(boolean isMuted) {
        this.musicMuted = isMuted;
        applyMusicMute();
    }

    private void applyMusicMute() {
        for (Clip clip : music) {
            BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            muteControl.setValue(musicMuted);
        }
    }

    public void playSFX(int id) {
        if (sfxMuted)
            return;
        applyVolume(sfx[id], sfxVolume);
        if (sfx[id].getMicrosecondPosition() > 0) {
            sfx[id].setMicrosecondPosition(0);
        }
        sfx[id].start();
    }

    public void toggleSFX(boolean isMuted) {
        this.sfxMuted = isMuted;
    }
}