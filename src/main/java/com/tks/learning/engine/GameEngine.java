package com.tks.learning.engine;

public class GameEngine implements Runnable{

    public final int TARGET_FPS = 75;
    public final int TARGET_UPS = 30;

    private final Window window;
    private final Timer timer;
    private final MouseInput mouseInput;
    private final IGameLogic gameLogic;

    public GameEngine (String windowTitle, int width, int height, boolean vSync, IGameLogic logic) {
        window = new Window(windowTitle, width, height, vSync);
        mouseInput = new MouseInput();
        this.gameLogic = logic;
        timer = new Timer();
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
        mouseInput.init(window);
    }

    protected void gameLoop() {
        float elapsedTime = 0;
        float accumulator = 0;
        float interval = 1.f / TARGET_FPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;
            input();
            while(accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }


    private void sync() {
        float loopSlot = 1.f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);

    }

    private void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    private void render() {
        gameLogic.render(window);
        window.update();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            cleanup();
        }
    }

    private void cleanup() {
        gameLogic.cleanup();
    }
}
