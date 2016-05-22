package de.dark.engine.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.core.MathUtil;
import de.dark.engine.render.Camera;
 
public class SkyboxShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = "res/shader/SkyboxShader.vert";
    private static final String FRAGMENT_FILE = "res/shader/SkyboxShader.frag";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor;
     
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadUniformMat4(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = MathUtil.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadUniformMat4(location_viewMatrix, matrix);
    }
     
    @Override
    protected void getAllUniformLocations() {
        this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        this.location_viewMatrix = super.getUniformLocation("viewMatrix");
        this.location_fogColor = super.getUniformLocation("fogColor");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}
