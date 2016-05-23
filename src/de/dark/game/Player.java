package de.dark.game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.core.DisplayManager;
import de.dark.engine.render.Camera;

public class Player {
	private static final float SENSITIVITY = 0.28F; 
	private static final Vector2f SCREEN_CENTER = new Vector2f(DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2);
	private static final float SPEED = 0.045F;
	
	private static final float PLAYER_SIZE = 0.28f;
	
	private Camera camera;
	private Vector3f movVector;
	
	public Player(Vector3f position) {
		this.camera = new Camera(position, -90, 0, 0);
		this.movVector = new Vector3f();
	}
	
	public void input() {
		Vector2f deltaPos = new Vector2f();
		Vector2f.sub(new Vector2f(Mouse.getX(), Mouse.getY()), SCREEN_CENTER, deltaPos);
		
		boolean rotY = deltaPos.getX() != 0;
		boolean rotX = deltaPos.getY() != 0;
		if(rotX) {
			camera.setPitch(camera.getPitch() - deltaPos.y * SENSITIVITY);
		}
		if(rotY) {
			camera.setYaw(camera.getYaw() + deltaPos.x * SENSITIVITY);
		}
		if(rotX || rotY) {
			Mouse.setCursorPosition((int)SCREEN_CENTER.x, (int)SCREEN_CENTER.y);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			movVector.x = -SPEED * (float)Math.sin(Math.toRadians(camera.getYaw()));
			movVector.z = SPEED * (float)Math.cos(Math.toRadians(camera.getYaw()));
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			movVector.x = SPEED * (float)Math.sin(Math.toRadians(camera.getYaw()));
			movVector.z = -SPEED * (float)Math.cos(Math.toRadians(camera.getYaw()));
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			movVector.x = -SPEED * (float)Math.sin(Math.toRadians(camera.getYaw()-90));
			movVector.z = SPEED * (float)Math.cos(Math.toRadians(camera.getYaw()-90));
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			movVector.x = -SPEED * (float)Math.sin(Math.toRadians(camera.getYaw()+90));
			movVector.z = SPEED * (float)Math.cos(Math.toRadians(camera.getYaw()+90));
		}
		else {
			movVector.x = 0;
			movVector.z = 0;
		}
	}
	
	public void update(Level level) {
		Vector3f oldPos = this.camera.getPosition();
		Vector3f newPos = Vector3f.add(oldPos, movVector, null);
		Vector3f collVec = LevelPhysics.checkCollision(level, oldPos, newPos, PLAYER_SIZE, PLAYER_SIZE);
		movVector.x = movVector.x * collVec.x;
		movVector.y = movVector.y * collVec.y;
		movVector.z = movVector.z * collVec.z;
		
		this.camera.move(movVector);
	}
	
	public Camera getCamera() {
		return this.camera;
	}
}
