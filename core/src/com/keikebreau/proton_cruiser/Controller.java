package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {
    /** The current player. */
    private Player player;

    /** A list of all objects in the game. */
    private final LinkedBlockingQueue<GameObject> objects;

    /** A list of all objects to be removed in the next tick. */
    private final LinkedList<GameObject> removeList;

    /** A list of all objects to be added in the next tick. */
    private final LinkedList<GameObject> addList;

    private final ProtonCruiser game;
    private final GameScreen gameScreen;

    /** Array of booleans whose elements are true when the corresponding
     * key is pressed.
     */
    private final boolean[] keyDown = new boolean[6];

    private enum KEYS {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        SPACE,
        SHIFT
    }

    public Controller(final ProtonCruiser game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        objects = new LinkedBlockingQueue<>();
        removeList = new LinkedList<>();
        addList = new LinkedList<>();
    }

    /** Advance time for all game objects by one tick, adding new objects and
     * removing those requested to be removed.
     */
    public void tick() {
        // Remove objects that need to be removed first...
        removeList.forEach(this::removeObject);
        removeList.clear();
        // ...Then add objects that need to be added...
        addList.forEach(this::addObject);
        addList.clear();
        // ...Then advance time by a tick.
        for (Iterator<GameObject> iter = getObjectIterator(); iter.hasNext();) {
            // Remove objects that are off-screen and moving away from it.
            GameObject obj = iter.next();
            float objX = obj.getX();
            float objY = obj.getY();
            float objDX = obj.getVelX();
            float objDY = obj.getVelY();
            boolean offLeft = objX + obj.getWidth() < 0 && objDX < 0;
            boolean offRight = objX - obj.getWidth() > GameScreen.WIDTH && objDX > 0;
            boolean offTop = objY + obj.getHeight() < 0 && objDY < 0;
            boolean offBottom = objY - obj.getHeight() > GameScreen.HEIGHT && objDY > 0;
            if (offLeft || offRight || offTop || offBottom) {
                requestRemove(obj);
                continue;
            }
            switch (obj.getId()) {
                case PLAYER:
                    player = (Player)obj;
                case ELECTRON:
                case DUST:
                case BG_STAR:
                default:
            }
            obj.tick();
        }

        handleKeyPress();
    }

    /** Request that the given object be added during the next tick. */
    public void requestAdd(GameObject object) {
        addList.add(object);
    }

    /** Immediately remove the object from the controller. */
    private synchronized void addObject(GameObject object) {
        objects.add(object);
    }

    /** Request that the given object be removed during the next tick. */
    public void requestRemove(GameObject object) {
        removeList.add(object);
    }

    /** Immediately remove the object from the controller. */
    private synchronized void removeObject(GameObject object) {
        objects.remove(object);
    }

    public Player getPlayer() {
        return player;
    }

    public Iterator<GameObject> getObjectIterator() {
        return objects.iterator();
    }

    public void handleKeyPress() {
        // key events for player
        keyDown[KEYS.UP.ordinal()] = Gdx.input.isKeyPressed(Input.Keys.W);
        keyDown[KEYS.DOWN.ordinal()] = Gdx.input.isKeyPressed(Input.Keys.S);
        keyDown[KEYS.LEFT.ordinal()] = Gdx.input.isKeyPressed(Input.Keys.A);
        keyDown[KEYS.RIGHT.ordinal()] = Gdx.input.isKeyPressed(Input.Keys.D);
        keyDown[KEYS.SHIFT.ordinal()] = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        keyDown[KEYS.SPACE.ordinal()] = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (keyDown[KEYS.UP.ordinal()]) {
            player.velY = 5;
        }
        if (keyDown[KEYS.DOWN.ordinal()]) {
            player.velY = -5;
        }
        if (keyDown[KEYS.LEFT.ordinal()]) {
            player.velX = -5;
        }
        if (keyDown[KEYS.RIGHT.ordinal()]) {
            player.velX = 5;
        }
        if (keyDown[KEYS.SPACE.ordinal()]) {
            player.speedUp();
        }
        if (keyDown[KEYS.SHIFT.ordinal()]) {
            player.slowDown();
        }
        // Stop vertical movement if no vertical keys are pressed.
        if (!(keyDown[KEYS.UP.ordinal()] || keyDown[KEYS.DOWN.ordinal()])) {
            player.velY = 0;
        }
        // Stop horizontal acceleration if no horizontal keys are pressed.
        if (!(keyDown[KEYS.LEFT.ordinal()] || keyDown[KEYS.RIGHT.ordinal()])) {
            player.velX = 0;
        }
        // Stop frame acceleration if no frame keys are pressed.
        if (!(keyDown[KEYS.SPACE.ordinal()] || keyDown[KEYS.SHIFT.ordinal()])) {
            player.cruise();
        }
    }

    public void handlePlayerInteractions() {
        Vector2 playerCenter = new Vector2();
        player.getBounds().getCenter(playerCenter);
        // For the calculation of player acceleration.
        float particleX = 0.0f;
        float particleY = 0.0f;
        int particleCount = 0;
        float netCharge = 0.0f;
        for (Iterator<GameObject> iter = getObjectIterator(); iter.hasNext();) {
            GameObject obj = iter.next();
            if (obj.getCharge() != 0) {
                particleX += obj.getX();
                particleY += obj.getY();
                netCharge += obj.getCharge();
                ++particleCount;
            }
            Vector2 objCenter = new Vector2();
            obj.getBounds().getCenter(objCenter);
            // collision detection code
            if (playerCenter.dst2(objCenter) <= (Player.SIZE + obj.bounds.width) / 2) {
                switch (obj.getId()) {
                    case ANTIPROTON:
                    case ELECTRON:
                        game.setScreen(new GameOverScreen(game, gameScreen.score));
                        gameScreen.dispose();
                        return;
                    case DUST:
                        player.loseFrameSpeed();
                        break;
                    default:
                }
            }
        }
    }
}
