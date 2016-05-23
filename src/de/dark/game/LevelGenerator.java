package de.dark.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.core.ArrayUtil;
import de.dark.engine.core.Bitmap;
import de.dark.engine.light.Light;
import de.dark.engine.render.Entity;
import de.dark.engine.render.GLTexture;
import de.dark.engine.render.Loader;
import de.dark.engine.render.TexturedModel;

public class LevelGenerator {

	public static final float TILE_WIDTH = 1;
	public static final float TILE_LENGTH = 1;
	public static final float TILE_HEIGHT = 1;
	
	public static final float DOOR_WIDTH = 0.125f;
	public static final float DOOR_LENGTH = 1;
	public static final float DOOR_HEIGHT = 1;
	
	private static final int NUM_TEX_EXP = 4;
	private static final int NUM_TEXTURES = (int)Math.pow(2, NUM_TEX_EXP);
	
	public static Level createLevelFromFile(String srcFilePath, String texFilePath, Loader loader) {
		Bitmap level = null;	
		try {
			 level = Loader.loadBitmapFromFile(srcFilePath);
			 level.flipY();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Float> vertices = new ArrayList<>();
		List<Float> texCoords = new ArrayList<>();
		List<Float> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		List<Light> lights = new ArrayList<>();
		List<Entity> doors = new ArrayList<>();
			
		for(int x=0; x<level.getWidth(); x++) {
			for(int y=0; y<level.getHeight(); y++) {
				if((level.getPixel(x, y) & 0xFFFFFF) == 0) {
					continue;
				}
				
				//Blue component
				int col = ((level.getPixel(x, y) & 0x0000FF));
				
				//Green component
				int texX = ((level.getPixel(x, y) & 0x00FF00) >> 8) / NUM_TEXTURES; 
				int texY = texX % NUM_TEX_EXP;
				texX /= NUM_TEX_EXP;
				
				float xHigher = 1.f - (float)texX/(float)NUM_TEX_EXP; 
				float xLower = xHigher - 1.f/(float)NUM_TEX_EXP;
				float yLower = 1.f - (float)texY/(float)NUM_TEX_EXP;
				float yHigher = yLower - 1.f/(float)NUM_TEX_EXP;
			
				//Generate floor
				indices.add((vertices.size() / 3) + 0);
				indices.add((vertices.size() / 3) + 1);
				indices.add((vertices.size() / 3) + 3);
				indices.add((vertices.size() / 3) + 3);
				indices.add((vertices.size() / 3) + 1);
				indices.add((vertices.size() / 3) + 2);
				
				vertices.add(x * TILE_WIDTH); vertices.add(0.f); vertices.add(y * TILE_LENGTH);
				vertices.add((x + 1) * TILE_WIDTH); vertices.add(0.f); vertices.add(y * TILE_LENGTH);
				vertices.add((x + 1) * TILE_WIDTH); vertices.add(0.f); vertices.add((y + 1) * TILE_LENGTH);
				vertices.add(x * TILE_WIDTH); vertices.add(0.f); vertices.add((y + 1) * TILE_LENGTH);
				
				texCoords.add(xLower); texCoords.add(yLower);
				texCoords.add(xHigher); texCoords.add(yLower);
				texCoords.add(xHigher); texCoords.add(yHigher);
				texCoords.add(xLower); texCoords.add(yHigher);
				
				normals.add(0.f); normals.add(1.f); normals.add(0.f);
				normals.add(0.f); normals.add(1.f); normals.add(0.f);
				normals.add(0.f); normals.add(1.f); normals.add(0.f);
				normals.add(0.f); normals.add(1.f); normals.add(0.f);
				
				//Generate Ceiling
				if(col != 60) {
					indices.add((vertices.size() / 3) + 0);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 2);
					
					vertices.add(x * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add(y * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add(y * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add(x * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add((y + 1) * TILE_LENGTH);
					
					texCoords.add(xLower); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yHigher);
					texCoords.add(xLower); texCoords.add(yHigher);
					
					normals.add(0.f); normals.add(-1.f); normals.add(0.f);
					normals.add(0.f); normals.add(-1.f); normals.add(0.f);
					normals.add(0.f); normals.add(-1.f); normals.add(0.f);
					normals.add(0.f); normals.add(-1.f); normals.add(0.f);
				}
				
				//Red component
				texX = ((level.getPixel(x, y) & 0xFF0000) >> 16) / NUM_TEXTURES; 
				texY = texX % NUM_TEX_EXP;
				texX /= NUM_TEX_EXP;
				
				xHigher = 1.f - (float)texX/(float)NUM_TEX_EXP; 
				xLower = xHigher - 1.f/(float)NUM_TEX_EXP;
				yLower = 1.f - (float)texY/(float)NUM_TEX_EXP;
				yHigher = yLower - 1.f/(float)NUM_TEX_EXP;
				
				//Generate Walls
				if((level.getPixel(x, y - 1) & 0xFFFFFF) == 0) {
					indices.add((vertices.size() / 3) + 0);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 2);
					
					vertices.add(x * TILE_WIDTH); vertices.add(0.f); vertices.add(y * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(0.f); vertices.add(y * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add(y * TILE_LENGTH);
					vertices.add(x * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add(y * TILE_LENGTH);
					
					texCoords.add(xLower); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yHigher);
					texCoords.add(xLower); texCoords.add(yHigher);
					
					normals.add(0.f); normals.add(0.f); normals.add(1.f);
					normals.add(0.f); normals.add(0.f); normals.add(1.f);
					normals.add(0.f); normals.add(0.f); normals.add(1.f);
					normals.add(0.f); normals.add(0.f); normals.add(1.f);
				}
				if((level.getPixel(x, y + 1) & 0xFFFFFF) == 0) {
					indices.add((vertices.size() / 3) + 0);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 2);
					
					vertices.add(x * TILE_WIDTH); vertices.add(0.f); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(0.f); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add(x * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add((y + 1) * TILE_LENGTH);
					
					texCoords.add(xLower); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yHigher);
					texCoords.add(xLower); texCoords.add(yHigher);
					
					normals.add(0.f); normals.add(0.f); normals.add(-1.f);
					normals.add(0.f); normals.add(0.f); normals.add(-1.f);
					normals.add(0.f); normals.add(0.f); normals.add(-1.f);
					normals.add(0.f); normals.add(0.f); normals.add(-1.f);
				}
				if((level.getPixel(x - 1, y) & 0xFFFFFF) == 0) {
					indices.add((vertices.size() / 3) + 0);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 2);
					
					vertices.add(x * TILE_WIDTH); vertices.add(0.f); vertices.add(y * TILE_LENGTH);
					vertices.add(x * TILE_WIDTH); vertices.add(0.f); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add(x * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add(x * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add(y * TILE_LENGTH);
					
					texCoords.add(xLower); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yHigher);
					texCoords.add(xLower); texCoords.add(yHigher);
					
					normals.add(1.f); normals.add(0.f); normals.add(0.f);
					normals.add(1.f); normals.add(0.f); normals.add(0.f);
					normals.add(1.f); normals.add(0.f); normals.add(0.f);
					normals.add(1.f); normals.add(0.f); normals.add(0.f);
				}
				if((level.getPixel(x + 1, y) & 0xFFFFFF) == 0) {
					indices.add((vertices.size() / 3) + 0);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 3);
					indices.add((vertices.size() / 3) + 1);
					indices.add((vertices.size() / 3) + 2);
					
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(0.f); vertices.add(y * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(0.f); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add((y + 1) * TILE_LENGTH);
					vertices.add((x + 1) * TILE_WIDTH); vertices.add(TILE_HEIGHT); vertices.add(y * TILE_LENGTH);
					
					texCoords.add(xLower); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yLower);
					texCoords.add(xHigher); texCoords.add(yHigher);
					texCoords.add(xLower); texCoords.add(yHigher);
					
					normals.add(-1.f); normals.add(0.f); normals.add(0.f);
					normals.add(-1.f); normals.add(0.f); normals.add(0.f);
					normals.add(-1.f); normals.add(0.f); normals.add(0.f);
					normals.add(-1.f); normals.add(0.f); normals.add(0.f);
				}
				
				if(col == 10) {
					lights.add(new Light(new Vector3f(x * TILE_WIDTH + (TILE_WIDTH / 2), 0.55f, y * TILE_LENGTH + (TILE_LENGTH / 2)), new Vector3f(1, 0.57f, 0.16f), 0.18f));
				}
				else if(col == 40) {
					if((level.getPixel(x, y + 1) & 0xFFFFFF) == 0 && (level.getPixel(x, y - 1) & 0xFFFFFF) == 0) {
						doors.add(new Door(new Vector3f(x * TILE_WIDTH + (TILE_WIDTH / 2), 0, y * TILE_LENGTH), new Vector3f(0, 0, 0), 1.f, 0.01f));
					}
					else {
						doors.add(new Door(new Vector3f(x * TILE_WIDTH, 0, y * TILE_LENGTH + (TILE_LENGTH / 2)), new Vector3f(0, 90, 0), 1.f, 0.01f));
					}
				}
			}
		}
		
		float[] vertArray = ArrayUtil.toFloatArray(vertices);
		float[] texCoordsArray = ArrayUtil.toFloatArray(texCoords);
		float[] normalArray = ArrayUtil.toFloatArray(normals);
		int[] indArray = ArrayUtil.toIntArray(indices);
		vertices.clear();
		texCoords.clear();
		normals.clear();
		indices.clear();
		
		Map<TexturedModel, List<Entity>> entities = new HashMap<>();
		entities.put(new TexturedModel(loader.loadToVAO(vertArray, texCoordsArray, normalArray, indArray), new GLTexture(loader.loadTextureFromFile(texFilePath))), 
				Arrays.asList(new Entity(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1.f)));
		entities.put(LevelGenerator.createDoorModel(loader, "res/texture/door1.png"), doors);
		
		return new Level(level.getWidth(), level.getHeight(), level, lights, entities);
	}
	
	public static TexturedModel createDoorModel(Loader loader, String texFilePath) {	
		
		float[] vertices = new float[] {
				//front
				0.f, 0.f, 0.f,
				0.f, 0.f, DOOR_LENGTH,
				0.f, DOOR_HEIGHT, DOOR_LENGTH,
				0.f, DOOR_HEIGHT, 0.f,
				//back
				DOOR_WIDTH, 0.f, 0.f,
				DOOR_WIDTH, 0.f, DOOR_LENGTH,
				DOOR_WIDTH, DOOR_HEIGHT, DOOR_LENGTH,
				DOOR_WIDTH, DOOR_HEIGHT, 0.f,
				//left
				0.f, 0.f, 0.f,
				DOOR_WIDTH, 0.f, 0.f,
				DOOR_WIDTH, DOOR_HEIGHT, 0.f,
				0.f, DOOR_HEIGHT, 0.f,
				//right
				0.f, 0.f, DOOR_LENGTH,
				DOOR_WIDTH, 0.f, DOOR_LENGTH,
				DOOR_WIDTH, DOOR_HEIGHT, DOOR_LENGTH,
				0.f, DOOR_HEIGHT, DOOR_LENGTH,
		};
		
		float[] texCoords = new float[] {
				//front
				0, 1,
				1, 1,
				1, 0,
				0, 0,
				//back
				0, 1,
				1, 1,
				1, 0,
				0, 0,
				//left
				0, 1,
				0.05f, 1,
				0.05f, 0,
				0, 0,
				//right
				0, 1,
				0.05f, 1,
				0.05f, 0,
				0, 0,
				
		};
		
		float[] normals = new float[] {
				//front
				1, 0, 0,
				1, 0, 0,
				1, 0, 0,
				1, 0, 0,
				//back
				-1, 0, 0,
				-1, 0, 0,
				-1, 0, 0,
				-1, 0, 0,
				//left
				0, 0, -1,
				0, 0, -1,
				0, 0, -1,
				0, 0, -1,
				//right
				0, 0, 1,
				0, 0, 1,
				0, 0, 1,
				0, 0, 1,
		};
		
		int[] indices = new int[] {
				//front
				0, 1, 3,
				3, 1, 2,
				//back
				4, 5, 7,
				7, 5, 6,
				//left
				8, 9, 11,
				11, 9, 10,
				//right
				12, 13, 15,
				15, 13, 14
		};
		
		return new TexturedModel(loader.loadToVAO(vertices, texCoords, normals, indices), 
				new GLTexture(loader.loadTextureFromFile(texFilePath)));
	}
}
