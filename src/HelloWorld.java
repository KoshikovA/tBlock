import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class HelloWorld {
    private long window;
    private float squareX = -0.3f;
    private float squareY = 0.5f;
    private float squareSize = 0.2f;
    private float squareSpeed = 0.01f;
    private int backgroundTextureID;
    private String fileName;


    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(800, 600, "Tower Blocks", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Set up a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();



        float centerX = 0.0f;
        float centerY = 0.5f;
        float radius = 0.4f;
        float startAngle = (float) Math.PI; //((Math.PI*3)/2); // Half-circle starts from 180 degrees
        float endAngle = 0.0f;
        float angle = startAngle;
        float angularVelocity = 0.02f;
        float direction = 1.0f;


        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Load the background image as a texture
        backgroundTextureID = loadTexture("C:\\Users\\Алимжан\\Desktop\\histEnd\\dallas_texas_skyscrapers_62556_800x600.jpg"); // Replace "background.jpg" with your actual image file name

        int towerBlockTextureId = loadTexture("C:\\Users\\Алимжан\\Desktop\\histEnd\\windows-high-rise-tower-block-260nw-8905072.jpg");
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Enable texturing
            glEnable(GL_TEXTURE_2D);

            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, backgroundTextureID);

            // Render the background
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

            glBindTexture(GL_TEXTURE_2D, towerBlockTextureId);
            /*glBegin(GL_QUADS);
            glColor3f(1.0f, 1.0f, 1.0f);
            glTexCoord2f(0.0f, 0.0f);
            glVertex2f(squareX, squareY);
            glTexCoord2f(1.0f, 0.0f);
            glVertex2f(squareX + squareSize, squareY);
            glTexCoord2f(1.0f, 1.0f);
            glVertex2f(squareX + squareSize, squareY + squareSize);
            glTexCoord2f(0.0f, 1.0f);
            glVertex2f(squareX, squareY + squareSize);
            glEnd();
*/
            float x = centerX + radius * (float) Math.cos(angle);
            float y = centerY + radius * (float) Math.sin(angle);

            // Render the square at the calculated position
            glBegin(GL_QUADS);
            glColor3f(1.0f, 1.0f, 1.0f);
            glTexCoord2f(0.0f, 0.0f);
            glVertex2f(x, y);
            glTexCoord2f(1.0f, 0.0f);
            glVertex2f(x + squareSize, y);
            glTexCoord2f(1.0f, 1.0f);
            glVertex2f(x + squareSize, y + squareSize);
            glTexCoord2f(0.0f, 1.0f);
            glVertex2f(x, y + squareSize);
            glEnd();

            // Disable texturing
            glDisable(GL_TEXTURE_2D);

            // Update the angle for the next frame
            angle += direction * angularVelocity;
            if (angle > startAngle) {
                angle = startAngle;
                direction *= -1.0f; // Reverse the direction
            } else if (angle < endAngle) {
                angle = endAngle;
                direction *= -1.0f; // Reverse the direction
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

            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4); // 4 channels (RGBA)
            for (int y = height - 1; y >= 0; y--) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red channel
                    buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green channel
                    buffer.put((byte) (pixel & 0xFF)); // Blue channel
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha channel
                }
            }
            buffer.flip();

            // Generate an OpenGL texture
            int textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            return textureId;
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }

        return -1;
    }


    public static void main(String[] args) {
        new HelloWorld().run();
    }

}