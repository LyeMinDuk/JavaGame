package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import core.Game;
import model.LevelModel;
import model.state.SettingsModel;

public class SaveLoadController {
    private final String SAVE_FILE = "savegame.txt";
    private SettingsModel settingsModel;
    private Game game;

    public SaveLoadController(SettingsModel settingsModel, Game game) {
        this.settingsModel = settingsModel;
        this.game = game;
    }

    public void saveGame() {
        // 1. Gom toàn bộ dữ liệu thành 1 chuỗi văn bản (Plain text)
        StringBuilder rawData = new StringBuilder();
        rawData.append("level=").append(game.getCurMapIdx()).append("\n");
        rawData.append("music=").append(!settingsModel.isMusicMuted()).append("\n");
        rawData.append("sfx=").append(!settingsModel.isSFXMuted()).append("\n");
        rawData.append("difficult=").append(settingsModel.getDifficult()).append("\n");

        try {
            // 2. Mã hóa chuỗi bằng Base64
            String encodedString = Base64.getEncoder().encodeToString(rawData.toString().getBytes());

            // 3. Ghi cục data đã mã hóa ra file
            Files.writeString(Paths.get(SAVE_FILE), encodedString);
            System.out.println("✅ Đã xuất file save (Mã hóa Base64)!");
        } catch (IOException e) {
            System.out.println("❌ Lỗi khi xuất file save!");
        }
    }

    public void loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("⚠️ Không có file save, dùng mặc định.");
            return;
        }

        try {
            // 1. Đọc cục data mã hóa từ file
            String encodedString = Files.readString(Paths.get(SAVE_FILE));

            // 2. Giải mã Base64 về lại text bình thường
            String decodedString = new String(Base64.getDecoder().decode(encodedString));

            // 3. Cắt từng dòng để gán vào Model
            String[] lines = decodedString.split("\n");
            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length < 2)
                    continue;
                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    case "level" -> game.setCurMapIdx(Integer.parseInt(value));
                    case "music" -> settingsModel.setMusicMuted(!Boolean.parseBoolean(value));
                    case "sfx" -> settingsModel.setSFXMuted(!Boolean.parseBoolean(value));
                    case "difficult" -> settingsModel.setDifficult(Integer.parseInt(value));
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ File save bị lỗi hoặc bị can thiệp!");
        }
    }
}