package controller;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import view.assets.ResourceManager;

public class AudioController {

    // --- ĐỊNH NGHĨA CÁC ID CHO DỄ GỌI ---
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

        // Cập nhật trạng thái mute ngay khi vừa load xong
        applyMusicMute();
    }

    private void loadMusic() {
        // Thay mảng tên này bằng tên file thực tế trong thư mục res/audio của bạn
        String[] names = { "t", "pinduphong" };
        musicClips = new Clip[names.length];

        for (int i = 0; i < names.length; i++) {
            musicClips[i] = ResourceManager.loadClip("/audio/" + names[i] + ".wav");
        }
    }

    private void loadSFX() {
        // Thay mảng tên này bằng tên file thực tế của bạn
        String[] names = { "click", "jump", "attack", "die" };
        sfxClips = new Clip[names.length];

        for (int i = 0; i < names.length; i++) {
            sfxClips[i] = ResourceManager.loadClip("/audio/" + names[i] + ".wav");
        }
    }

    // --- XỬ LÝ MUSIC (Nhạc nền) ---
    public void playMusic(int id) {
        // Dừng nhạc cũ nếu đang chạy
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
        for (Clip c : musicClips) {
            if (c != null && c.isControlSupported(BooleanControl.Type.MUTE)) {
                BooleanControl muteControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
                muteControl.setValue(musicMuted);
            }
        }
    }

    // --- XỬ LÝ SFX (Hiệu ứng) ---
    public void playSFX(int id) {
        // Nếu đang tắt âm hiệu ứng hoặc file bị lỗi -> Bỏ qua
        if (sfxMuted || sfxClips[id] == null)
            return;

        // Tua lại đầu clip và phát
        if (sfxClips[id].getMicrosecondPosition() > 0) {
            sfxClips[id].setMicrosecondPosition(0);
        }
        sfxClips[id].start();
    }

    public void toggleSFX(boolean isMuted) {
        this.sfxMuted = isMuted;
        // Với SFX, vì chúng chỉ phát 1 lần ngắn ngủi, cờ sfxMuted chặn ở hàm playSFX là
        // đủ
    }
}