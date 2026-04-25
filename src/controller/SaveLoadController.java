package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import model.LevelModel;
import model.state.SettingsModel;

public class SaveLoadController {
    private final String SAVE_FILE = "savegame.txt";
    
    private SettingsModel settingsModel;
    private LevelModel levelModel; 

    // Inject các Model vào Controller
    public SaveLoadController(SettingsModel settingsModel, LevelModel levelModel) {
        this.settingsModel = settingsModel;
        this.levelModel = levelModel;
    }

    public void saveGame() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            
            // Lưu Level
            writer.write("level=" + levelModel.getCurrentLevel() + "\n");
            
            // Chuyển Muted (boolean) sang On/Off (String)
            String soundStr = settingsModel.isSFXMuted() ? "off" : "on";
            String musicStr = settingsModel.isMusicMuted() ? "off" : "on";
            
            writer.write("sound=" + soundStr + "\n");
            writer.write("music=" + musicStr + "\n");
            
            // Tùy theo việc bạn đổi tên hàm getter là gì (getDifficult hay getDifficulty)
            writer.write("difficult=" + settingsModel.getDifficult() + "\n");
            
            System.out.println("✅ Đã lưu cấu hình vào " + SAVE_FILE);
            
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi lưu file!");
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length < 2) continue; 
                
                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "level":
                        levelModel.setCurrentLevel(Integer.parseInt(value));
                        break;
                    case "sound":
                        // Nếu trong file là "off" thì sfxMuted = true
                        settingsModel.setSFXMuted(value.equals("off"));
                        break;
                    case "music":
                        // Nếu trong file là "off" thì musicMuted = true
                        settingsModel.setMusicMuted(value.equals("off"));
                        break;
                    case "difficult":
                        settingsModel.setDifficult(Integer.parseInt(value));
                        break;
                }
            }
            System.out.println("✅ Đã tải file save thành công! Đang ở Level: " + levelModel.getCurrentLevel());

        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Không tìm thấy file save hoặc file bị lỗi. Sử dụng cấu hình mặc định.");
            // Nếu không có file save, LevelModel vẫn giữ nguyên giá trị 1 mặc định
        }
    }
}