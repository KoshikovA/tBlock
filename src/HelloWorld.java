import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.apache.commons.lang3.ArrayUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class HelloWorld {
    private long window;
    private int score;
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\Алимжан\\Desktop\\histEnd\\wavik.wav"));
    Clip clip = AudioSystem.getClip();
    private float squareX = -0.3f;
    private float squareY = 0.5f;
    private float squareSize = 0.2f;
    private float squareSpeed = 0.01f;
    private int backgroundTextureID;
    private int blockTextureID;
    private String fileName;
    TowerBlock[] towerBlocks = new TowerBlock[10];
    boolean spacePressed = false;

    public HelloWorld() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    }

    public void run() throws LineUnavailableException, IOException {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        clip.open(audioInputStream);
        clip.start();
        loop();


        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "Tower Blocks", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);

            else if (key == GLFW_KEY_SPACE) {
                if (action == GLFW_PRESS) {
                    spacePressed = true; // Set the spacePressed variable to true when the space button is pressed
                    TowerBlock newBlock = new TowerBlock(0.0f, towerBlocks[towerBlocks.length - 1].getY() + 0.2f,
                            0.2f, 0.2f, loadTexture("C:\\Users\\Алимжан\\Desktop\\histEnd\\1000_F_213155103_fOzbjufN7Lj12wxTvNsC6GJM4Hu4EtMj (1).jpg"));
                    towerBlocks = Arrays.copyOf(towerBlocks, towerBlocks.length + 1);
                    towerBlocks[towerBlocks.length - 1] = newBlock;
                } else if (action == GLFW_RELEASE) {
                    spacePressed = false; // Set the spacePressed variable to false when the space button is released
                }
            }


        });

        for (int i = 0; i < towerBlocks.length; i++) {
            towerBlocks[i] = new TowerBlock(0.0f, 0.0f, 0.2f, 0.2f, blockTextureID);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }
    float rotationAngle = 0.0f;
    float rotationSpeed = 0.02f;
    float rotationDirection = 1.0f;
    private void loop() {
        GL.createCapabilities();

        backgroundTextureID = loadTexture("C:\\Users\\Алимжан\\Desktop\\histEnd\\cartoon-blue-sky-cityscape-cloudy-city-skyline-landscape-midday-graphic-urban-silhouette-illustration-town-building-layers-166285152.jpg");
        blockTextureID = loadTexture("C:\\Users\\Алимжан\\Desktop\\histEnd\\1000_F_213155103_fOzbjufN7Lj12wxTvNsC6GJM4Hu4EtMj (1).jpg");

        for (int i = 0; i < towerBlocks.length; i++) {
            towerBlocks[i] = new TowerBlock(0.0f, towerBlocks[towerBlocks.length - 1].getY() + 0.2f,
                    0.2f, 0.2f, loadTexture("C:\\Users\\Алимжан\\Desktop\\histEnd\\1000_F_213155103_fOzbjufN7Lj12wxTvNsC6GJM4Hu4EtMj (1).jpg"));
        }

        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, backgroundTextureID);

            glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 0.0f);
            glVertex2f(-1.0f, -1.0f);
            glTexCoord2f(1.0f, 0.0f);
            glVertex2f(1.0f, -1.0f);
            glTexCoord2f(1.0f, 1.0f);
            glVertex2f(1.0f, 1.0f);
            glTexCoord2f(0.0f, 1.0f);
            glVertex2f(-1.0f, 1.0f);
            glEnd();


            for (TowerBlock block : towerBlocks) {

                glBindTexture(GL_TEXTURE_2D, block.getTextureID());

                glBegin(GL_QUADS);
                glColor3f(1.0f, 1.0f, 1.0f);
                glTexCoord2f(0.0f, 0.0f);
                glVertex2f(block.getX(), block.getY());
                glTexCoord2f(1.0f, 0.0f);
                glVertex2f(block.getX() + block.getWidth(), block.getY());
                glTexCoord2f(1.0f, 1.0f);
                glVertex2f(block.getX() + block.getWidth(), block.getY() + block.getHeight());
                glTexCoord2f(0.0f, 1.0f);
                glVertex2f(block.getX(), block.getY() + block.getHeight());
                glEnd();


                // Update the block's position
                if (spacePressed) {
                    // Update the block's position only if space button is pressed
                    block.setY(block.getY() - squareSpeed);
                }

                // Check if the block is outside the window boundaries
                if (block.getY() + block.getHeight() <= -1.0f) {
                    // Remove the block from the array
                    /*block.setY(-1.0f);*/
                    towerBlocks = ArrayUtils.removeElement(towerBlocks, block);
                }
            }

            glDisable(GL_TEXTURE_2D);

            if (towerBlocks.length > 0 && towerBlocks[towerBlocks.length - 1].getY() > 1.0f) {
                System.out.println("Game over!");
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
    private int loadTexture(String fileName) {
        try {
            // Load the image file into a ByteBuffer
            BufferedImage image = ImageIO.read(new File(fileName));
            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = height - 1; y >= 0; y--) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            buffer.flip();

            int textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            return textureId;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        new HelloWorld().run();
    }
}

