package de.dark.engine.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.core.MathUtil;
import de.dark.engine.light.Light;
import de.dark.engine.render.Camera;

public class StaticShader extends ShaderProgram {

	private static final String VERT_PATH = "res/shader/StaticShader.vert";
	private static final String FRAG_PATH = "res/shader/StaticShader.frag";
	
	public static final int STATIC_SHADER_MAX_LIGHTS = 12;
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	
	public StaticShader() {
		super(VERT_PATH, FRAG_PATH);
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");
		
		this.location_lightPosition = new int[STATIC_SHADER_MAX_LIGHTS];
		this.location_lightColor = new int[STATIC_SHADER_MAX_LIGHTS];
		this.location_attenuation = new int[STATIC_SHADER_MAX_LIGHTS];
		for(int i=0; i<STATIC_SHADER_MAX_LIGHTS; i++) {
			this.location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i +"]");
			this.location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			this.location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
		super.bindAttribute(2, "normal");
	}

	public void loadTransformationMatrix(Matrix4f mat) {
		super.loadUniformMat4(this.location_transformationMatrix, mat);
	}
	
	public void loadProjectionMatrix(Matrix4f mat) {
		super.loadUniformMat4(this.location_projectionMatrix, mat);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadUniformMat4(this.location_viewMatrix, MathUtil.createViewMatrix(camera));
	}
	
	public void loadLights(List<Light> lights) {		
		for(int i=0; i<STATIC_SHADER_MAX_LIGHTS; i++) {
			if(i<lights.size()) {
				super.loadUniformVec3f(this.location_lightPosition[i], lights.get(i).getPosition());
				super.loadUniformVec3f(this.location_lightColor[i], lights.get(i).getColor());
				super.loadUniformFloat(this.location_attenuation[i], lights.get(i).getAttenuation());
			}
			else {
				super.loadUniformVec3f(this.location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadUniformVec3f(this.location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadUniformVec3f(this.location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
}
