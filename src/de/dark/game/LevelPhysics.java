package de.dark.game;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.render.Entity;
import de.dark.game.Door.DoorState;

public class LevelPhysics {
	
	public static Vector3f checkCollision(Level level, Vector3f oldPos, Vector3f newPos, float objWidth, float objLength) {
		Vector2f collVec = new Vector2f(1, 1);
		Vector3f movVec = Vector3f.sub(newPos, oldPos, null);
		
		Vector2f oldPos2 = new Vector2f(oldPos.getX(), oldPos.getZ());
		Vector2f newPos2 = new Vector2f(newPos.getX(), newPos.getZ());
		
		Vector2f objSize = new Vector2f(objWidth, objLength);
		
		if(movVec.length() > 0) {
			Vector2f blockSize = new Vector2f(LevelGenerator.TILE_LENGTH, LevelGenerator.TILE_WIDTH);
			
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
		
		List<List<Entity>> entities = new ArrayList<>(level.getEntities().values());
		List<Entity> doors = entities.get(1);
		for(int x=0; x<doors.size(); x++) {
			Door door = (Door)doors.get(x);
			Vector2f doorSize = getDoorSize(door);
			Vector2f doorPos = new Vector2f(doors.get(x).getPosition().getX(), doors.get(x).getPosition().getZ());
			Vector2f rectCollVec = rectCollision(oldPos2, newPos2, objSize, doorSize, doorPos);
			collVec.x = collVec.x * rectCollVec.x;
			collVec.y = collVec.y * rectCollVec.y;
			
			updateDoor(door, oldPos);
		}
		
		return new Vector3f(collVec.getX(), 0, collVec.getY());
	}
		
	private static void updateDoor(Door door, Vector3f playerPos) {
		Vector3f distVec = Vector3f.sub(playerPos, door.getPosition(), null);
		float distance = distVec.length();
		if(distance < 1.4f) {
			if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
				door.setDoorState(DoorState.STATE_CHANGING);
			}
		}
		
		if(door.getDoorState() == DoorState.STATE_CHANGING) {
			door.move(door.getMoveVector());
			if(door.opened()) {
				door.setDoorState(DoorState.STATE_OPEN);
				door.updateDoorMoveVector();
			}
			if(door.closed()) {
				door.setDoorState(DoorState.STATE_CLOSE);
				door.updateDoorMoveVector();
			}
		}
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
	
	private static Vector2f getDoorSize(Door door) {
		if(door.getRotation().getY() == 90) {
			return new Vector2f(LevelGenerator.DOOR_LENGTH, LevelGenerator.DOOR_WIDTH);
		}
		return new Vector2f(LevelGenerator.DOOR_WIDTH, LevelGenerator.DOOR_LENGTH);
	}	

}
