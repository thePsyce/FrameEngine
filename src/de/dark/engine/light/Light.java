package de.dark.engine.light;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	private Vector3f position;
	private Vector3f color;
	private float attenuation;
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		this.attenuation = 0;
	}
	
	public Light(Vector3f position, Vector3f color, float attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(float attenuation) {
		this.attenuation = attenuation;
	}
}
