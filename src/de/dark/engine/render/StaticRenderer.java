package de.dark.engine.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.core.MathUtil;
import de.dark.engine.light.Light;
import de.dark.engine.shader.StaticShader;

public class StaticRenderer {

	private StaticShader shader;
	
	public StaticRenderer(Matrix4f projMat) {
		this.shader = new StaticShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(projMat);
		this.shader.stop();
		
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0f, 0f, 0f, 1f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, Camera camera, List<Light> lights) {
		this.shader.start();
		this.shader.loadLights(this.getNearestLights(camera, lights));
		this.shader.loadViewMatrix(camera);
		for(TexturedModel model : entities.keySet()) {
			GL30.glBindVertexArray(model.getRawModel().getVao());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
			
			for(Entity entity : entities.get(model)) {
				Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(
						entity.getPosition(), entity.getRotation(), entity.getScale());
				this.shader.loadTransformationMatrix(transformationMatrix);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
		}
		this.shader.stop();
	}
	
	private List<Light> getNearestLights(Camera camera, List<Light> lights) {
		List<Light> lightsCpy = new ArrayList<>(lights);
		List<Light> result = new ArrayList<>();
		
		if(lightsCpy.size() > 0) {
			for(int x=0; x<StaticShader.STATIC_SHADER_MAX_LIGHTS; x++) {
				Light minLight = lightsCpy.get(0);
				for(int i=0; i<lightsCpy.size(); i++) {
					Vector3f minDistVec = Vector3f.sub(minLight.getPosition(),camera.getPosition(), null);
					double minDist = Math.sqrt(Vector3f.dot(minDistVec, minDistVec));
					Vector3f currDistVec = Vector3f.sub(lightsCpy.get(i).getPosition(),camera.getPosition(), null);
					double currDist = Math.sqrt(Vector3f.dot(currDistVec, currDistVec));
					
					if(currDist < minDist) {
						minLight = lightsCpy.get(i);
					}
				}
				lightsCpy.remove(minLight);
				result.add(minLight);
			}
		}
		
		return result;
	}
}
