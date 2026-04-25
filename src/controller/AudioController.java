package controller;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import view.assets.ResourceManager;

public class AudioController {
    private Clip musicClip;
    private boolean musicMuted;
    private boolean sfxMuted;

    // Nhận trạng thái từ SettingsModel khi khởi tạo
    public AudioController(boolean musicMuted, boolean sfxMuted) {
        this.musicMuted = musicMuted;
        this.sfxMuted = sfxMuted;
    }

    // --- XỬ LÝ MUSIC (Nhạc nền) ---
    public void playMusic(String filePath) {
        if (musicMuted)
            return; // Nếu đang tắt nhạc thì không phát

        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }

        // DÙNG HÀM MỚI Ở ĐÂY
        musicClip = ResourceManager.loadClip(filePath);

        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }

    public void toggleMusic(boolean isMuted, String currentMusicPath) {
        this.musicMuted = isMuted;
        if (isMuted) {
            stopMusic();
        } else {
            playMusic(currentMusicPath);
        }
    }

    // --- XỬ LÝ SFX (Hiệu ứng âm thanh) ---
    public void playSFX(String filePath) {
        if (sfxMuted)
            return; // Nếu đang tắt sfx thì không phát

        Clip sfxClip = ResourceManager.loadClip(filePath);

        if (sfxClip != null) {
            sfxClip.start();
        }
    }

    public void toggleSFX(boolean isMuted) {
        this.sfxMuted = isMuted;
    }
}