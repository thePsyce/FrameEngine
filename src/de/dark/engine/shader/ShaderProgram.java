package de.dark.engine.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram {
	
	private static FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
	
	private int programId;
	private int vertexShaderId;
	private int fragmentShaderId;
	
	public ShaderProgram(String verFile, String fragFile) {
		this.vertexShaderId = ShaderProgram.loadShader(verFile, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderId = ShaderProgram.loadShader(fragFile, GL20.GL_FRAGMENT_SHADER);
		this.programId = GL20.glCreateProgram();
		GL20.glAttachShader(this.programId, this.vertexShaderId);
		GL20.glAttachShader(this.programId, this.fragmentShaderId);
		this.bindAttributes();
		GL20.glLinkProgram(this.programId);
		GL20.glValidateProgram(this.programId);
		this.getAllUniformLocations();
	}
	
	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(this.programId, name);
	}
	
	protected abstract void getAllUniformLocations();
	
	protected void loadUniformInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void loadUniformFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadUniformVec3f(int location, Vector3f vec) {
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
	}
	
	protected void loadUniformVec4f(int location, Vector4f vec) {
		GL20.glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
	}
	
	protected void loadUniformMat4(int location, Matrix4f mat) {
		GL20.glUniformMatrix4(location, false, ShaderProgram.convertToFloatBuffer(mat));
	}
	
	public void start() {
		GL20.glUseProgram(this.programId);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanMemory() {
		this.stop();
		GL20.glDetachShader(this.programId, this.vertexShaderId);
		GL20.glDetachShader(this.programId, this.fragmentShaderId);
		GL20.glDeleteShader(this.vertexShaderId);
		GL20.glDeleteShader(this.fragmentShaderId);
		GL20.glDeleteProgram(this.programId);
	}
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(this.programId, attribute, variableName);
	}
	
	protected abstract void bindAttributes();
	
	private static int loadShader(String filePath, int type) {
		StringBuilder shaderSource = ShaderProgram.loadFileFormated(filePath);
		int shaderId = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderId, shaderSource);
		GL20.glCompileShader(shaderId);
		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderId, 512));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		return shaderId;
	}
	
	private static FloatBuffer convertToFloatBuffer(Matrix4f mat) {
		mat.store(matBuffer);
		matBuffer.flip();
		return matBuffer;
	}
	
	public static StringBuilder loadFileFormated(String filePath) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line;
			while((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file: " + filePath);
			e.printStackTrace();
			System.exit(-1);
		}
		return result;
	}
}
