package de.dark.engine.render;

public class TexturedModel {

	private RawModel rawModel;
	private GLTexture texture;
	
	public TexturedModel(RawModel model, GLTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public GLTexture getTexture() {
		return texture;
	}
}
