import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.slf4j.Logger;


import java.util.logging.Level;
import java.util.logging.LogManager;

import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author kangmoo Heo
 */
public class JnativeHookSample {
    private static final Logger logger = getLogger(JnativeHookSample.class);

    public static void main(String[] args) {
        try {
            // Clear previous logging configurations.
            LogManager.getLogManager().reset();

            // Get the logger for "org.jnativehook" and set the level to off.
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeMouseMotionListener(new MouseMotionHooker());
        GlobalScreen.addNativeMouseListener(new MouseHooker());
        GlobalScreen.addNativeKeyListener(new KeyHooker());
    }

    public static class KeyHooker implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent e) {
            System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
        }
    }

    public static class MouseHooker implements NativeMouseListener {
        @Override
        public void nativeMouseClicked(NativeMouseEvent e) {
            System.out.println("Mouse Clicked: " + e.getPoint());
        }

        @Override
        public void nativeMousePressed(NativeMouseEvent e) {
            System.out.println("Mouse Pressed: " + e.getPoint());
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent e) {
            System.out.println("Mouse Released: " + e.getPoint());
        }
    }

    public static class MouseMotionHooker implements NativeMouseMotionListener {
        @Override
        public void nativeMouseMoved(NativeMouseEvent e) {
            System.out.println("Mouse Released: " + e.getPoint());
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent e) {
            System.out.println("Mouse Released: " + e.getPoint());
        }
    }
}

