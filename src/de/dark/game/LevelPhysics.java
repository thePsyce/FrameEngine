package de.dark.game;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class LevelPhysics {

	public static Vector3f checkCollision(Level level, Vector3f oldPos, Vector3f newPos, float objWidth, float objLength) {
		Vector2f collVec = new Vector2f(1, 1);
		Vector3f movVec = Vector3f.sub(newPos, oldPos, null);
		
		if(movVec.length() > 0) {
			Vector2f blockSize = new Vector2f(LevelGenerator.TILE_WIDTH, LevelGenerator.TILE_HEIGHT);
			Vector2f objSize = new Vector2f(objWidth, objLength);
			
			Vector2f oldPos2 = new Vector2f(oldPos.getX(), oldPos.getZ());
			Vector2f newPos2 = new Vector2f(newPos.getX(), newPos.getZ());
			
			for(int x=0; x<level.getWidth(); x++) {
				for(int y=0; y<level.getHeight(); y++) {
					if((level.getBitmap().getPixel(x, y) & 0xFFFFFF) == 0) {
						Vector2f blockPos = new Vector2f();
						blockPos.x = blockSize.x * x;
						blockPos.y = blockSize.y * y;
						Vector2f rectCollVec = rectCollision(oldPos2, newPos2, objSize, blockSize, blockPos);
						collVec.x = collVec.x * rectCollVec.x;
						collVec.y = collVec.y * rectCollVec.y;
					}
				}
			}
		}
		
		return new Vector3f(collVec.getX(), 0, collVec.getY());
	}
	
	private static Vector2f rectCollision(Vector2f oldPos, Vector2f newPos, Vector2f size1, Vector2f size2, Vector2f pos2) {
		Vector2f result = new Vector2f(0,0);
		if(newPos.getX() + size1.getX() < pos2.getX() ||
		   newPos.getX() - size1.getX() > pos2.getX() + size2.getX() * size2.getX() ||
		   oldPos.getY() + size1.getY() < pos2.getY() ||
		   oldPos.getY() - size1.getY() > pos2.getY() + size2.getY() * size2.getY())
			result.setX(1);
		
		if(oldPos.getX() + size1.getX() < pos2.getX() ||
		   oldPos.getX() - size1.getX() > pos2.getX() + size2.getX() * size2.getX() ||
		   newPos.getY() + size1.getY() < pos2.getY() ||
		   newPos.getY() - size1.getY() > pos2.getY() + size2.getY() * size2.getY())
			result.setY(1);
		
		return result;
	}
}
