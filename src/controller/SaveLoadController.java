package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import core.Game;
import model.state.SettingsModel;

public class SaveLoadController {
    private final String SAVE_PATH = "/resources/save/savegame.txt";
    private SettingsModel settingsModel;
    private Game game;

    public SaveLoadController(SettingsModel settingsModel, Game game) {
        this.settingsModel = settingsModel;
        this.game = game;
    }

    public void saveGame() {
        String raw = "";
        raw += "level=" + game.getCurMapIdx() + '\n';
        raw += "music=" + !settingsModel.isMusicMuted() + '\n';
        raw += "sfx=" + !settingsModel.isSFXMuted() + '\n';
        raw += "difficult=" + settingsModel.getDifficult() + '\n';

        try {
            String encodedString = Base64.getEncoder().encodeToString(raw.getBytes());
            Files.writeString(Paths.get(SAVE_PATH), encodedString);
            System.out.println("Save completed");
        } catch (IOException e) {
            System.out.println("Save error");
        }
    }

    public void loadGame() {
        File file = new File(SAVE_PATH);
        if (!file.exists()) {
            System.out.println("Not found save");
            return;
        }

        try {
            String encodedString = Files.readString(Paths.get(SAVE_PATH));
            String decodedString = new String(Base64.getDecoder().decode(encodedString));
            String[] lines = decodedString.split("\n");
            for (String line : lines) {
                String[] parts = line.split("=");
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
            System.out.println("Load error");
        }
    }
}