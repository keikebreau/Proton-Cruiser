package com.keikebreau.proton_cruiser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {
    private Game game;
    /** The current player. */
    private Player player;

    /** Black hole (if any) in frame. */
    private BlackHoleEnemy blackHole;

    /** A list of all objects in the game. */
    private LinkedBlockingQueue<GameObject> objects;

    /** A list of all objects to be removed in the next tick. */
    private LinkedList<GameObject> removeList;

    /** A list of all objects to be added in the next tick. */
    private LinkedList<GameObject> addList;

    /** Array of booleans whose elements are true when the corresponding
     * key is pressed.
     */
    private boolean[] keyDown = new boolean[6];

    private enum KEYS {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        SPACE,
        SHIFT;
    }

    public Controller(Game game) {
        this.game = game;
        objects = new LinkedBlockingQueue<GameObject>();
        removeList = new LinkedList<GameObject>();
        addList = new LinkedList<GameObject>();
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
        boolean blackHoleFound = false;
        for (Iterator<GameObject> iter = getObjectIterator(); iter.hasNext();) {
            // Remove objects that are off-screen and moving away from it.
            GameObject obj = iter.next();
            float objX = obj.getX();
            float objY = obj.getY();
            float objDX = obj.getVelX();
            float objDY = obj.getVelY();
            boolean offLeft = objX + obj.getWidth() < 0 && objDX < 0;
            boolean offRight = objX - obj.getWidth() > Game.WIDTH && objDX > 0;
            boolean offTop = objY + obj.getHeight() < 0 && objDY < 0;
            boolean offBottom = objY - obj.getHeight() > Game.HEIGHT && objDY > 0;
            if (offLeft || offRight || offTop || offBottom) {
                requestRemove(obj);
                continue;
            }
            switch (obj.getId()) {
                case BLACK_HOLE:
                    blackHoleFound = true;
                    blackHole = (BlackHoleEnemy)obj;
                    break;
                case PLAYER:
                    player = (Player)obj;
                case ELECTRON:
                case DUST:
                case MOLECULE:
                case BG_STAR:
                default:
            }
            if (blackHole != null
                    && obj != blackHole
                    && obj.getId() != ID.BG_STAR
                    && obj.getId() != ID.ANTIPROTON) {
                float scale = 1e5f;
                float diffX = (blackHole.getCenterX() - obj.getCenterX());
                float diffY = (blackHole.getCenterY() - obj.getCenterY());
                float forceX = diffX / scale;
                float forceY = diffY / scale;
                obj.addAccelX(forceX);
                obj.addAccelY(forceY);
                if (obj.getId() != ID.PLAYER) {
                    if (obj.getBounds().overlaps(blackHole.getBounds())) {
                        requestRemove(obj);
                    }
                }
            }
            obj.tick();
        }
        // Don't allow obsolete reference to black hole
        if (!blackHoleFound) {
            blackHole = null;
        }
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

    /** Removes all non-player objects from the game. */
    public void clearEnemies() {
        objects.forEach(obj -> {if (obj.getId() != ID.PLAYER && obj.getId() != ID.BG_STAR) requestRemove(obj);});
        blackHole = null;
    }

    public Player getPlayer() {
        return player;
    }

    public BlackHoleEnemy getBlackHole() {
        return blackHole;
    }

    public Iterator<GameObject> getObjectIterator() {
        return objects.iterator();
    }

    public void handleKeyPress(int key) {
        // key events for player
        // Scale factor for acceleration: higher is smoother
        final int scale = 20;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            // Close half the difference between max velocity and current velocity
            float diff = -5 - player.getVelY();
            player.addAccelY(diff / scale);
            keyDown[KEYS.UP.ordinal()] = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            // Close half the difference between max velocity and current velocity
            float diff = 5 - player.getVelY();
            player.addAccelY(diff / scale);
            keyDown[KEYS.DOWN.ordinal()] = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            float diff = -5 - player.getVelX();
            player.addAccelX(diff / scale);
            keyDown[KEYS.LEFT.ordinal()] = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            float diff = 5 - player.getVelX();
            player.addAccelX(diff / scale);
            keyDown[KEYS.RIGHT.ordinal()] = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.speedUp();
            keyDown[KEYS.SPACE.ordinal()] = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            player.slowDown();
            keyDown[KEYS.SHIFT.ordinal()] = true;
        }
    }

    public void handleKeyRelease(int key) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            keyDown[KEYS.UP.ordinal()] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            keyDown[KEYS.DOWN.ordinal()] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            keyDown[KEYS.LEFT.ordinal()] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            keyDown[KEYS.RIGHT.ordinal()] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            keyDown[KEYS.SPACE.ordinal()] = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            keyDown[KEYS.SHIFT.ordinal()] = false;
        }

        // Stop vertical acceleration if no vertical keys are pressed.
        if (!(keyDown[KEYS.UP.ordinal()] || keyDown[KEYS.DOWN.ordinal()])) {
            player.addAccelY(-player.getAccelY());
        }
        // Stop horizontal acceleration if no horizontal keys are pressed.
        if (!(keyDown[KEYS.LEFT.ordinal()] || keyDown[KEYS.RIGHT.ordinal()])) {
            player.addAccelX(-player.getAccelX());
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
                    case BLACK_HOLE:
                    case ANTIPROTON:
                        player.loseHP(Player.MAX_HP);
                        return;
                    case ELECTRON:
                        player.loseHP(2);
                        break;
                    case DUST:
                        player.loseHP(10);
                        player.loseFrameSpeed();
                        break;
                    case MOLECULE:
                        player.loseFrameSpeed();
                        break;
                    default:
                }
            }
        }
        if (particleCount > 0) {
            particleX /= particleCount;
            particleY /= particleCount;
            float diffX = particleX - player.getX();
            float diffY = particleY - player.getY();
            // Keep differences from getting to close to zero, for fear of a
            // force explosion.
            if (Math.abs(diffX) < 1) {
                if (diffX > 0) {
                    diffX = 1;
                } else {
                    diffX = -1;
                }
            }
            if (Math.abs(diffY) < 1) {
                if (diffY > 0) {
                    diffY = 1;
                } else {
                    diffY = -1;
                }
            }
            float forceX = (float) (particleCount * netCharge / Math.pow(diffX, 2));
            float forceY = (float) (particleCount * netCharge / Math.pow(diffY, 2));
            player.addAccelX(forceX);
            player.addAccelY(forceY);
        }
    }
}
