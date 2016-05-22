package de.dark.game;

import java.util.List;
import java.util.Map;

import de.dark.engine.core.Bitmap;
import de.dark.engine.light.Light;
import de.dark.engine.render.Entity;
import de.dark.engine.render.TexturedModel;

public class Level {
	private int width, height;
	private Bitmap bitmap;
	private List<Light> lights;
	private Map<TexturedModel, List<Entity>> entities;
	
	public Level(int width, int height, Bitmap bitmap, List<Light> lights, Map<TexturedModel, List<Entity>> entities) {
		this.width = width;
		this.height = height;
		this.bitmap = bitmap;
		this.lights = lights;
		this.entities = entities;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public List<Light> getLights() {
		return lights;
	}

	public Map<TexturedModel, List<Entity>> getEntities() {
		return entities;
	}
}
