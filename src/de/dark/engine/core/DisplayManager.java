package de.dark.engine.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	public static final int WIDTH = 3840;
	public static final int HEIGHT = 2160;
	public static final boolean FULLSCREEN = true;
	public static final boolean VSYNC = true;
	public static final String TITLE = "Lost Chapter";
	public static final int FPS_CAP = 60;
	public static final int ANTIALIASING_LEVEL = 4;
	
	public static void createDisplay() throws LWJGLException {
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		DisplayManager.setDisplayMode(WIDTH, HEIGHT, FULLSCREEN);
		Display.setVSyncEnabled(VSYNC);
		Display.setTitle(TITLE);
		Display.create(new PixelFormat().withSamples(ANTIALIASING_LEVEL).withDepthBits(24), attribs);
		
		Mouse.setGrabbed(true);
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void update() {
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay() {
		Mouse.setGrabbed(false);
		Display.destroy();
	}
	
	public static boolean closeRequested() {
		return Display.isCloseRequested();
	}
	
	private static void setDisplayMode(int width, int height, boolean fullscreen) {
		 
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
	    (Display.isFullscreen() == fullscreen)) {
	        return;
	    }
	 
	    try {
	        DisplayMode targetDisplayMode = null;
	         
	    if (fullscreen) {
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
	        int freq = 0;
	                 
	        for (int i=0;i<modes.length;i++) {
	            DisplayMode current = modes[i];
	                     
	        if ((current.getWidth() == width) && (current.getHeight() == height)) {
	            if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
	                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
	                targetDisplayMode = current;
	                freq = targetDisplayMode.getFrequency();
	                        }
	                    }
	 
	            // if we've found a match for bpp and frequence against the 
	            // original display mode then it's probably best to go for this one
	            // since it's most likely compatible with the monitor
	            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }
	 
	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }
	 
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	             
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
}
