package core;

import java.awt.Graphics;

import controller.AudioController;
import controller.InputController;
import controller.SaveLoadController;
import controller.WorldController;
import controller.entity.PlayerController;
import controller.state.ClassSelectController;
import controller.state.GameOverController;
import controller.state.MenuController;
import controller.state.OptionController;
import controller.state.PausedController;
import controller.state.PlayingController;
import controller.state.VictoryController;
import model.CameraModel;
import model.MapModel;
import model.entity.KnightModel;
import model.entity.MageModel;
import model.entity.PlayerModel;
import model.state.SettingsModel;
import model.state.GameState;
import model.state.GameStateModel;
import util.AssetsPath;
import view.GameWindow;
import view.renderer.GameRenderer;
import view.GamePanel;

import static core.GameConfig.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private boolean running;
    private Thread gameThread;

    private GameStateModel gameState;
    private MenuController menuController;
    private PlayingController playingController;
    private VictoryController victoryController;
    private GameOverController gameOverController;
    private OptionController optionController;
    private PausedController pausedController;
    private SettingsModel settingsModel;

    private InputController input;
    private PlayerModel player;
    private MapModel map;
    private CameraModel camera;
    private PlayerController playerController;
    private WorldController worldController;
    private AudioController audioController;
    private SaveLoadController saveLoad;

    private GameState lastState = null;
    private GameRenderer renderer;
    private int curMapIdx = 0;
    private ClassSelectController classSelectController;
    private boolean selectedMage = false;

    public Game() {
        initInput();
        loadSaveFile();

        gameState = new GameStateModel();
        initModel();
        initController();
        audioController = new AudioController(settingsModel.isMusicMuted(), settingsModel.isSFXMuted());
        audioController.toggleMusic(settingsModel.isMusicMuted());
        audioController.toggleSFX(settingsModel.isSFXMuted());
        initRenderer();
        initGameStateController();

        initWindow();

        startGame();
    }

    private void initInput() {
        input = new InputController();
    }

    private void loadSaveFile() {
        settingsModel = new SettingsModel();
        saveLoad = new SaveLoadController(settingsModel, this);
        saveLoad.loadGame();
    }

    private void initModel() {
        map = new MapModel(AssetsPath.levelMap[curMapIdx]);
        camera = new CameraModel();

        int x = map.getPlayerLocation().x * TILE_SIZE;
        int y = map.getPlayerLocation().y * TILE_SIZE;
        if (selectedMage) {
            player = new MageModel(x, y, 100);
        } else {
            player = new KnightModel(x, y, 100);
        }
        player.applyDifficult(settingsModel.getDifficult());
    }

    private void initController() {
        playerController = new PlayerController(input);
        worldController = new WorldController(map, camera, playerController, gameState, settingsModel);
    }

    private void initRenderer() {
        renderer = new GameRenderer(map, player, this, camera, worldController.getEnemyController().getListEnemy(),
                worldController.getSpikeController().getListSpike(), gameState, settingsModel);
    }

    private void initGameStateController() {
        menuController = new MenuController(this, input, gameState, renderer.getMenuRenderer(), saveLoad);
        classSelectController = new ClassSelectController(this, input, gameState, renderer.getClassSelectRenderer());
        playingController = new PlayingController(input, gameState, worldController, player, renderer);
        victoryController = new VictoryController(this, input, gameState, renderer.getVictoryRenderer());
        gameOverController = new GameOverController(this, input, gameState, renderer.getGameOverRenderer());
        optionController = new OptionController(input, gameState, renderer.getOptionRenderer(), settingsModel,
                audioController, saveLoad);
        pausedController = new PausedController(this, input, gameState, renderer.getPausedRenderer(), settingsModel,
                saveLoad);
    }

    private void initWindow() {
        gamePanel = new GamePanel(this, input);
        gameWindow = new GameWindow(gamePanel);
    }

    private void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        GameState current = gameState.getGameState();
        if (current != lastState) {
            onEnterState(current);
            lastState = current;
        }

        switch (current) {
            case GameState.MENU -> menuController.update(audioController);
            case GameState.PLAYING -> playingController.update(audioController);
            case GameState.PAUSED -> pausedController.update(audioController);
            case GameState.GAME_OVER -> gameOverController.update(audioController);
            case GameState.OPTIONS -> optionController.update();
            case GameState.VICTORY -> victoryController.update(audioController);
            case GameState.CHOOSE_CLASS -> classSelectController.update(audioController);
            default -> throw new IllegalArgumentException("Unexpected value: " + gameState.getGameState());
        }
    }

    private void onEnterState(GameState state) {
        switch (state) {
            case GameState.MENU -> audioController.playMusic(AudioController.BGM_MENU);
            case GameState.PLAYING -> {
                if (lastState == GameState.PAUSED) {
                    audioController.resumeMusic();
                } else {
                    audioController.setLevelMusic(curMapIdx);
                }
            }
            case GameState.PAUSED -> audioController.pauseMusic();
            case GameState.GAME_OVER -> {
                audioController.stopMusic();
                audioController.playSFX(AudioController.SFX_LOSE);
            }
            case GameState.OPTIONS -> {
            }
            case GameState.CHOOSE_CLASS -> {
            }
            case GameState.VICTORY -> {
                audioController.stopMusic();
                audioController.playSFX(AudioController.SFX_WIN);
            }
        }
    }

    public void render(Graphics g) {
        renderer.render(g, curMapIdx);
    }

    public void resetPlaying() {
        initModel();
        initController();
        initRenderer();
        initGameStateController();
    }

    public void setCurMapIdx(int curMapIdx) {
        this.curMapIdx = curMapIdx;
    }

    public int getCurMapIdx() {
        return curMapIdx;
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000 / FPS;

        long prevTime = System.nanoTime();
        double deltaFrame = 0;

        while (running) {
            long curTime = System.nanoTime();
            deltaFrame += (curTime - prevTime) / timePerFrame;
            prevTime = curTime;

            if (deltaFrame >= 1) {
                update();
                gamePanel.repaint();
                deltaFrame--;
            }
        }
    }

    public WorldController getWorldController() {
        return worldController;
    }

    public void setSelectedClassMage() {
        selectedMage = true;
    }

    public void setSelectedClassKnight() {
        selectedMage = false;
    }
}
