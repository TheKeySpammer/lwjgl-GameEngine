package com.tks.learning.game;

import com.tks.learning.engine.IGameLogic;
import com.tks.learning.engine.MouseInput;
import com.tks.learning.engine.Window;
import com.tks.learning.engine.graph.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame2 implements IGameLogic {

    private int displayXInc = 0;

    private int displayYInc = 0;

    private int displayZInc = 0;

    private int rotationXInc = 0;
    private int rotationYInc = 0;
    private int rotationZInc = 0;

    private int scaleInc = 0;

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Camera camera;

    private static final float CAMERA_POS_STEP = 0.05f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private GameItem[] gameItems;


    public DummyGame2() {
        this.renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        float reflectance = 1.f;

//        Mesh mesh = OBJLoader.loadMesh("models/cube.obj");
//        Texture texture = new Texture("C:\\Users\\aamir\\Documents\\projects\\lwjgl-learning\\src\\texture\\grassblock.png");
        Mesh mesh = OBJLoader.loadMesh("models/spider.obj");
        Material material = new Material(new Vector4f(.2f, .5f, .5f, 1.f), reflectance);
        mesh.setMaterial(material);

        GameItem gameItem1 = new GameItem(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPosition(0, 0, -2);
        gameItems = new GameItem[]{gameItem1};

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {

//        Camera controls

        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_I)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_K)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_J)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_L)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_N)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            cameraInc.y = 1;
        }

//        Object controls

        displayXInc = 0;
        displayYInc = 0;
        displayZInc = 0;
        rotationXInc = 0;
        rotationYInc = 0;
        rotationZInc = 0;
        scaleInc = 0;

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displayYInc = 1;
        } if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displayYInc = -1;
        } if (window.isKeyPressed(GLFW_KEY_LEFT)){
            displayXInc = -1;
        } if (window.isKeyPressed(GLFW_KEY_RIGHT)){
            displayXInc = 1;
        } if (window.isKeyPressed(GLFW_KEY_Q)){
            displayZInc = -1;
        } if (window.isKeyPressed(GLFW_KEY_W)) {
            displayZInc = 1;
        } if (window.isKeyPressed(GLFW_KEY_A)) {
            scaleInc = -1;
        } if (window.isKeyPressed(GLFW_KEY_S)) {
            scaleInc = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_R)) {
            rotationXInc = 1;
        } if (window.isKeyPressed(GLFW_KEY_T)) {
            rotationYInc = 1;
        } if (window.isKeyPressed(GLFW_KEY_Y)) {
            rotationZInc = 1;
        }if (window.isKeyPressed(GLFW_KEY_F)) {
            rotationXInc = -1;
        } if (window.isKeyPressed(GLFW_KEY_G)) {
            rotationYInc = -1;
        } if (window.isKeyPressed(GLFW_KEY_H)) {
            rotationZInc = -1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        for (GameItem gameItem : gameItems) {
            Vector3f itemPosition = gameItem.getPosition();
            float posX = itemPosition.x + displayXInc * 0.1f;
            float posY = itemPosition.y + displayYInc * 0.1f;
            float posZ = itemPosition.z + displayZInc * 0.1f;
            gameItem.setPosition(posX, posY, posZ);

            float scale = gameItem.getScale();
            scale += scaleInc * 0.006f;
            if (scale < 0) {
                scale = 0;
            }
            gameItem.setScale(scale);

            float rotationX = gameItem.getRotation().x + rotationXInc;
            float rotationY = gameItem.getRotation().y + rotationYInc;
            float rotationZ = gameItem.getRotation().z + rotationZInc;
            if ( rotationX > 360 ) {
                rotationX = 0;
            }
            if ( rotationY > 360 ) {
                rotationX = 0;
            }
            if ( rotationZ > 360 ) {
                rotationZ = 0;
            }
            gameItem.setRotation(rotationX, rotationY, rotationZ);
        }
    }

    @Override
    public void render(Window window) {
        float color = 0.15f;
        window.setClearColor(color, color, color, 1.0f);
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.cleanup();
        }
    }
}
