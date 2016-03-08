package lohr.michael.labyrinth.manager;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import lohr.michael.labyrinth.AStar;
import lohr.michael.labyrinth.Level;
import lohr.michael.labyrinth.Main;
import lohr.michael.labyrinth.PlayerStats;
import lohr.michael.labyrinth.entities.StaticEntity;
import lohr.michael.labyrinth.entities.level.ExitEntity;
import lohr.michael.labyrinth.entities.level.KeyEntity;
import lohr.michael.labyrinth.entities.level.PlayerEntity;
import lohr.michael.labyrinth.lanterna.GameScreen;
import lohr.michael.labyrinth.lanterna.TextStyle;
import lohr.michael.labyrinth.math.Bounds;
import lohr.michael.labyrinth.math.Position;
import lohr.michael.labyrinth.math.Size;
import lohr.michael.labyrinth.menu.MessageWindow;
import lombok.Getter;
import lombok.val;

/**
 * Created by Michael Lohr on 07.01.2016.
 */
public class GameManager implements IManager {

    @Getter
    private final static GameManager instance = new GameManager();

    private static final int TARGET_FPS = 30;
    private static final int FRAME_DURATION = 1000 / TARGET_FPS;

    private static final int VERTICAL_SEPERATOR_HEIGHT = 6;

    private boolean initialized = false;

    @Getter
    private GameScreen screen;

    // describes the screen inside the window, in terminal space
    private Bounds screenCanvasBounds;
    // the bounds where the game is drawn, in terminal space
    private Bounds gameCanvasBounds;
    // the bounds where the ui is drawn, in terminal space
    private Bounds infoCanvasBounds;

    // the bounds of the camera, in level space
    private Bounds camera;

    // player and level coordinates are in level space
    @Getter
    private Level currentLevel;
    @Getter
    private PlayerEntity player;
    @Getter // the playerstats object isn't linked with the playerEntity, because multiple player can theoretically exist (and we can display multiple) but we can only display one livebar etc.
    private PlayerStats playerStats;

    private Key pressedLastFrame;
    private boolean shouldRedrawLevel = true;

    private boolean isRunning;
    private long gameStartTime;
    private long frameSleepTime;
    private long frameDelta;
    private float framesPerSecond;
    private float fpsCounter;

    private GameManager() {
        init();
    }

    @Override
    public void init() {
        if (initialized)
            return;

        screen = new GameScreen(Main.getTerminal());

        calcCanvasSizes();

        this.initialized = true;
    }

    private void calcCanvasSizes() {
        screenCanvasBounds = new Bounds(Position.ZERO, Size.fromTerminalSize(screen.getTerminalSize()));
        gameCanvasBounds = screenCanvasBounds.move(Position.ONE).shrink(new Size(2, VERTICAL_SEPERATOR_HEIGHT + 1));
        infoCanvasBounds = new Bounds(new Position(1, screen.getSize().getHeight() - VERTICAL_SEPERATOR_HEIGHT + 1), new Size(screen.getSize().getWidth() - 2, VERTICAL_SEPERATOR_HEIGHT - 2));
    }

    public void loadLevel(PlayerStats stats, Level level) {
        this.currentLevel = level;

        // destroy player from previous game
        if (player != null) {
            player.unregister();
            player = null;
        }

        for(val ent : level.getDynamicEntities()) {
            if (ent instanceof PlayerEntity) {
                player = (PlayerEntity) ent;
                System.out.println(ent.getPosition());
            }
        }

        if (player == null) {
            player = new PlayerEntity(level.getStartPosition());
            System.out.println("Player spawned at: " + player.getPosition().toString());
        }

        this.playerStats = stats;

        // register player listener
        player.register();

        // center player in view
        calcGameBounds();

        Main.startScreen(screen);

        startGame();
    }

    private void calcGameBounds() {
        if (camera == null)
            camera = new Bounds();

        val posBefore = camera.getPosition();
        camera.setSize(gameCanvasBounds.getSize());
        camera.setPosition(player.getPosition().subtract(gameCanvasBounds.getSize().toPosition().scale(0.5f)));
        if (!posBefore.equals(camera.getPosition())) {
            shouldRedrawLevel = true;
            currentLevel.getDynamicEntities().stream().filter(ent -> camera.contains(ent.getPosition()) || ent.getLastPosition() != null && camera.contains(ent.getLastPosition())).forEach(ent -> {
                ent.setDirty(true);
            });
        }
    }

    public void startGame() {
        this.isRunning = true;
        this.gameStartTime = System.currentTimeMillis();

        drawGameBorder();
        draw(true);

        gameLoop();

        MenuManager.showWindow(MenuManager.getPauseMenu());
    }

    private void gameLoop() {
        long lastFrameTime = gameStartTime;

        while(this.isRunning) {
            long frameStart = System.currentTimeMillis();
            long lastFrameDelta = frameStart - lastFrameTime;
            lastFrameTime = frameStart;

            if (screen.updateScreenSize()) {
                onResize();
            }

            readInput();
            update(lastFrameDelta / 1000.0f);
            draw(false);

            this.frameDelta = frameStart - System.currentTimeMillis();
            this.frameSleepTime = FRAME_DURATION - frameDelta;
            if (this.frameSleepTime > 0) {
                try {
                    Thread.sleep(this.frameSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void pause() {
        isRunning = false;
    }

    public void resume() {
        Main.startScreen(screen);
        startGame();
    }

    private void readInput() {
        val key = Main.getTerminal().readInput();

        // if a key is pressed
        if (key != null) {

            // check if it was presed last time, to ensure that you have to press the button again to move
            if (this.pressedLastFrame != null && this.pressedLastFrame.getCharacter() == key.getCharacter())
                return;

            //System.out.println("Pressed: " + key.getCharacter());

            // and now, call the appropriate event to the pressed key
            val action = ActionManager.getInstance();
            switch(key.getKind()) {
                // first, handle the special buttons
                case Escape:
                    pause();
                    break;
                case ArrowUp:
                    action.onUp();
                    break;
                case ArrowRight:
                    action.onRight();
                    break;
                case ArrowDown:
                    action.onDown();
                    break;
                case ArrowLeft:
                    action.onLeft();
                    break;
                case NormalKey:
                    // and now the normal ones
                    // note, that we have to check for the capitalized key, too, otherwise you can't move with 'wasd' when capslock is enabled
                    switch (key.getCharacter()) {
                        case 'w':
                        case 'W':
                            action.onUp();
                            break;
                        case 'd':
                        case 'D':
                            action.onRight();
                            break;
                        case 's':
                        case 'S':
                            action.onDown();
                            break;
                        case 'a':
                        case 'A':
                            action.onLeft();
                            break;
                    }
            }
        }

        this.pressedLastFrame = key;
    }

    // delta time in seconds
    private void update(float deltaTime) {
        this.fpsCounter += (deltaTime - this.fpsCounter) * 0.1f;
        this.framesPerSecond = 1/fpsCounter;

        // update dynamic entities
        currentLevel.getDynamicEntities().forEach(e -> e.onUpdate(deltaTime));

        calcGameBounds();
    }

    private void draw(boolean force) {
        drawUI(force);

        // draw level
        drawLevel(force);

        // draw dynamic entities
        drawEntities(force);

        screen.hideCursor();
        screen.refresh();
    }

    private int healthLastFrame = -1;
    private int keyLengthLastFrame = -1;
    private void drawUI(boolean force) {
        int sizeX = infoCanvasBounds.getSize().getWidth();
        Bounds gameStats = infoCanvasBounds.shrink(new Size((sizeX - 2) / 2, 0)).move(new Position(5, 0));
        Bounds engineStats = infoCanvasBounds.move(new Position((sizeX / 2) + 5, 0));

        if (playerStats != null) {
            screen.setCursor(gameStats.getPosition());
            screen.moveCursor(Position.DOWN);

            int maxHealth = PlayerStats.MAX_LIVES;
            int health = playerStats.getLives();

            if (health != healthLastFrame || force) {
                int empty = maxHealth - health;
                StringBuilder fullLives = new StringBuilder(health);
                StringBuilder emptyLives = new StringBuilder(empty);
                for (int i = 0; i < playerStats.getLives(); i++) {
                    fullLives.append("\u2665");
                }
                for (int i = 0; i < empty; i++) {
                    emptyLives.append("\u2665");
                }
                screen.putString(fullLives.toString(), new TextStyle(Terminal.Color.RED));
                val offset = new Position(fullLives.length(), 0);
                screen.moveCursor(offset);
                screen.putString(emptyLives.toString(), new TextStyle(Terminal.Color.WHITE));
                screen.moveCursor(offset.scale(-1));

                healthLastFrame = health;
            }

            screen.moveCursor(Position.DOWN);

            int keyLength = playerStats.getKeys().size();
            if (keyLengthLastFrame != keyLength || force) {
                for (val key : playerStats.getKeys()) {
                    screen.putString("\u26B7", key.getColor());
                    screen.moveCursor(Position.RIGHT);
                }
                keyLengthLastFrame = keyLength;
            }

        }

        screen.setCursor(engineStats.getPosition());
        screen.putString("FPS: " + framesPerSecond);
        screen.moveCursor(Position.DOWN);
        screen.putString("MS: " + frameDelta);
        screen.moveCursor(Position.DOWN);
        screen.putString("Sleep: " + frameSleepTime);
    }

    private void drawLevel(boolean force) {
        if (force || this.shouldRedrawLevel) {
            this.shouldRedrawLevel = false;

            if (!gameCanvasBounds.getSize().equals(camera.getSize())) {
                throw new IllegalArgumentException("canvas and game bounds must have the same size");
            }

            int canvasWidth = gameCanvasBounds.getSize().getWidth();
            int canvasHeight = gameCanvasBounds.getSize().getHeight();
            int cameraX = camera.getPosition().getX();
            int cameraY = camera.getPosition().getY();

            // level space
            val entities = this.currentLevel.getLevel();

            // x, y are in level space
            for (int x = cameraX; x < cameraX + canvasWidth; x++) {
                for (int y = cameraY; y < cameraY + canvasHeight; y++) {
                    // search for entities in camera bounds
                    StaticEntity ent = null;
                    if (x >= 0 && y >= 0 && x < entities.length && y < entities[x].length)
                        ent = entities[x][y];   // entities are in level space, too

                    screen.setCursor(levelToCanvasSpace(new Position(x, y)));
                    if (ent != null && ent.onRender()) {
                        screen.putCharacter(ent.getChar(), ent.getTextStyle());
                    } else {
                        screen.putCharacter(' ');
                    }
                }
            }
        }
    }

    private void drawEntities(boolean force) {
        // draw dynamic entities only when dirty == true
        currentLevel.getDynamicEntities().forEach(ent -> {
            if (force || ent.isDirty()) {

                // restore the field where the entity was standing on
                if (ent.getLastPosition() != null && camera.contains(ent.getLastPosition()) && !levelToCanvasSpace(ent.getPosition()).equals(levelToCanvasSpace(ent.getLastPosition()))) {
                    screen.setCursor(levelToCanvasSpace(ent.getLastPosition()));
                    StaticEntity oldEnt = currentLevel.getStaticEntity(ent.getLastPosition());
                    if (oldEnt != null && oldEnt.onRender()) {
                        screen.putCharacter(oldEnt.getChar(), oldEnt.getTextStyle());
                    } else {
                        screen.putCharacter(' ');
                   }
                }

                // check if new position is on screen
                if (camera.contains(ent.getPosition()) && ent.onRender()) {
                    screen.setCursor(levelToCanvasSpace(ent.getPosition()));
                    screen.putCharacter(ent.getChar(), ent.getTextStyle());
                }

                ent.setDirty(false);
            }
        });
    }

    private void drawGameBorder() {
        drawBorder(screenCanvasBounds);
        drawSeperator(screenCanvasBounds.getSize().getHeight() - VERTICAL_SEPERATOR_HEIGHT, screenCanvasBounds.getSize().getWidth());
    }

    private void drawBorder(Bounds bounds) {
        int posX = bounds.getPosition().getX();
        int posY = bounds.getPosition().getY();
        int sizeX = bounds.getSize().getWidth();
        int sizeY = bounds.getSize().getHeight();

        // horizontal
        val horzSb = new StringBuilder(sizeX - 2);
        for (int x = 0; x < sizeX - 2; x++) {
            horzSb.append('\u2550');
        }
        val horzStr = horzSb.toString();
        screen.setCursor(posX + 1, posY);
        screen.putString(horzStr);
        screen.setCursor(posX + 1, sizeY);
        screen.putString(horzStr);

        // vertical
        for (int y = 0; y < sizeY - 2; y++) {
            screen.moveCursor(posX, y + 1);
            screen.putCharacter('\u2551');
            screen.moveCursor(sizeX, y + 1);
            screen.putCharacter('\u2551');
        }

        // corners
        screen.setCursor(bounds.getPosition());
        screen.putCharacter('\u2554');
        screen.setCursor(sizeX, posY);
        screen.putCharacter('\u2557');
        screen.setCursor(posX, sizeY);
        screen.putCharacter('\u255A');
        screen.setCursor(sizeX, sizeY);
        screen.putCharacter('\u255D');
    }

    // draws a vertical ui seperator
    private void drawSeperator(int posY, int length) {
        val sepSb = new StringBuilder(length);
        sepSb.append('\u2560');
        for (int x = 0; x < length - 2; x++) {
            sepSb.append('\u2550');
        }
        sepSb.append('\u2563');
        screen.moveCursor(0, posY);
        screen.putString(sepSb.toString());
    }

    private void onResize() {
        if (!initialized)
            return;

        screen.clear();
        calcCanvasSizes();
        calcGameBounds();
        drawGameBorder();
        draw(true);
    }

    public void gameOver(boolean won) {
        int time = Math.round((System.currentTimeMillis() - gameStartTime) / 1000);
        SoundManager.getInstance().play(won ? SoundManager.winClip : SoundManager.gameoverClip);
        MenuManager.showWindow(new MessageWindow(won ? "You Won!" : "Game Over!", won ? "You successfully finished the level after " + time + " seconds." : "You died after " + time + " seconds.", MenuManager.getMainMenu()));
    }

    private Position levelToCanvasSpace(Position levelSpace) {
        val cameraSpace = levelSpace.subtract(camera.getPosition());
        val canvasSpace = cameraSpace.add(gameCanvasBounds.getPosition());
        return canvasSpace;
    }

    private Position canvasToLevelSpace(Position canvasSpace) {
        val screenSpace = canvasSpace.subtract(gameCanvasBounds.getPosition());
        val levelSpace = screenSpace.add(camera.getPosition());
        return levelSpace;
    }
}
