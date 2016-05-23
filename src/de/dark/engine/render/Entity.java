package de.dark.engine.render;

import org.lwjgl.util.vector.Vector3f;

public class Entity {
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public Entity(Vector3f position, Vector3f rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void move(Vector3f amt) {
		this.position.x += amt.x;
		this.position.y += amt.y;
		this.position.z += amt.z;
	}
}
