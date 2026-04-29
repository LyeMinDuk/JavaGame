package controller;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import view.assets.ResourceManager;

public class AudioController {
    public static final int BGM_MENU = 0;
    public static final int BGM_PLAYING = 1;

    public static final int SFX_CLICK = 0;
    public static final int SFX_JUMP = 1;
    public static final int SFX_ATTACK = 2;
    public static final int SFX_DIE = 3;

    private Clip[] musicClips;
    private Clip[] sfxClips;

    private int currentMusicId = -1;
    private boolean musicMuted;
    private boolean sfxMuted;

    public AudioController(boolean musicMuted, boolean sfxMuted) {
        this.musicMuted = musicMuted;
        this.sfxMuted = sfxMuted;
        loadMusic();
        loadSFX();
        applyMusicMute();
    }

    private void loadMusic() {
        String[] names = { "t", "pinduphong" };
        musicClips = new Clip[names.length];

        for (int i = 0; i < names.length; i++) {
            musicClips[i] = ResourceManager.loadClip("/audio/" + names[i] + ".wav");
        }
    }

    private void loadSFX() {
        String[] names = { "click", "jump", "attack", "die" };
        sfxClips = new Clip[names.length];

        for (int i = 0; i < names.length; i++) {
            sfxClips[i] = ResourceManager.loadClip("/audio/" + names[i] + ".wav");
        }
    }

    public void playMusic(int id) {
        if (currentMusicId == id)
            return;
        if (currentMusicId != -1 && musicClips[currentMusicId] != null && musicClips[currentMusicId].isRunning()) {
            musicClips[currentMusicId].stop();
        }

        currentMusicId = id;
        if (musicClips[id] != null) {
            musicClips[id].setMicrosecondPosition(0);
            musicClips[id].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopMusic() {
        if (currentMusicId != -1 && musicClips[currentMusicId] != null && musicClips[currentMusicId].isRunning()) {
            musicClips[currentMusicId].stop();
        }
    }

    public void toggleMusic(boolean isMuted) {
        this.musicMuted = isMuted;
        applyMusicMute();
    }

    private void applyMusicMute() {
        for (Clip clip : musicClips) {
            if (clip != null && clip.isControlSupported(BooleanControl.Type.MUTE)) {
                BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
                muteControl.setValue(musicMuted);
            }
        }
    }

    public void playSFX(int id) {
        if (sfxMuted || sfxClips[id] == null)
            return;
        if (sfxClips[id].getMicrosecondPosition() > 0) {
            sfxClips[id].setMicrosecondPosition(0);
        }
        sfxClips[id].start();
    }

    public void toggleSFX(boolean isMuted) {
        this.sfxMuted = isMuted;
    }
}