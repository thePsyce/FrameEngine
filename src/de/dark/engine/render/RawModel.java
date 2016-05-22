package de.dark.engine.render;

public class RawModel {

	private int vao;
	private int vertexCount;
	
	public RawModel(int vao, int vertexCount) {
		this.vao = vao;
		this.vertexCount = vertexCount;
	}
	
	public int getVao() {
		return vao;
	}
	public int getVertexCount() {
		return vertexCount;
	}
}
