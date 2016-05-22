package de.dark.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.audio.AudioPlayer;
import de.dark.engine.audio.Source;
import de.dark.engine.core.DisplayManager;
import de.dark.engine.core.MathUtil;
import de.dark.engine.render.Loader;
import de.dark.engine.render.SkyboxRenderer;
import de.dark.engine.render.StaticRenderer;
import de.dark.engine.render.TexturedModel;
import de.dark.engine.render.Entity;

public class Main {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	public static void main(String[] args) {
		try {
			DisplayManager.createDisplay();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Matrix4f projectionMatrix = MathUtil.createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
		
		Loader loader = new Loader();
		StaticRenderer renderer = new StaticRenderer(projectionMatrix);
		SkyboxRenderer skyRenderer = new SkyboxRenderer(loader, projectionMatrix);
		
		Player player = new Player(new Vector3f(6, 0.45f, 7));
		
		Level level = LevelGenerator.createLevelFromFile("res/level/level1.png", "res/texture/tileset1.png", loader);
		
		/*
		Map<TexturedModel, List<Entity>> entities = new HashMap<>();
		entities.put(level.getTexturedModel(), Arrays.asList(
				new Entity(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1.f)));
		entities.put(LevelGenerator.createDoorModel(loader, "res/texture/door1.png"), Arrays.asList(
				);
		*/
		
		AudioPlayer.init();
		AudioPlayer.setListenerData();
		int bfr = AudioPlayer.loadSound("res/audio/ambnt.wav");
		Source source = new Source(0.45f);
		source.play(bfr);
		
		while(!DisplayManager.closeRequested()) {
			player.input();
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
				System.out.println("Position: x: " + player.getCamera().getPosition().x + " y: " +
						player.getCamera().getPosition().y + " z: " + player.getCamera().getPosition().z);
			}
			
			player.update(level);
			
			renderer.prepare();
			renderer.render(level.getEntities(), player.getCamera(), level.getLights());
			skyRenderer.render(player.getCamera());
			
			DisplayManager.update();
		}
		
		source.delete();
		AudioPlayer.destroy();
		loader.cleanMemory();
		DisplayManager.closeDisplay();
	}
}
