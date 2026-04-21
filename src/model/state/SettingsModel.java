package model.state;

public class SettingsModel {
    private boolean musicMuted = false;
    private boolean sfxMuted = false;
    private int difficult = 0;

    public boolean isMusicMuted() {
        return musicMuted;
    }

    public void setMusicMuted(boolean musicMuted) {
        this.musicMuted = musicMuted;
    }

    public boolean isSFXMuted() {
        return sfxMuted;
    }

    public void setSFXMuted(boolean sfxMuted) {
        this.sfxMuted = sfxMuted;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficulty(int difficult) {
        this.difficult = difficult;
    }
}