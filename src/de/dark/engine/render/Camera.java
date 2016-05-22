package de.dark.engine.render;

import org.lwjgl.util.vector.Vector3f;

public class Camera {	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {
	}
	
	public Camera(Vector3f position, float pitch, float yaw, float roll) {
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}
	
	public void invertPitchAndRoll() {
		this.pitch = -this.pitch;
		this.roll = -this.roll;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public void move(Vector3f amt) {
		this.position.x += amt.x;
		this.position.y += amt.y;
		this.position.z += amt.z;
	}
}
