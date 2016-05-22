package de.dark.engine.core;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.dark.engine.render.Camera;

public class MathUtil {

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		Matrix4f.translate(translation, mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.scale(new Vector3f(scale, scale, scale), mat, mat);
		return mat;
	}
	
	public static Matrix4f createProjectionMatrix(float fov, float farPlane, float nearPlane) {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = farPlane - nearPlane;

		Matrix4f mat = new Matrix4f();
		mat.m00 = x_scale;
		mat.m11 = y_scale;
		mat.m22 = -((farPlane + nearPlane) / frustum_length);
		mat.m23 = -1;
		mat.m32 = -((2 * nearPlane * farPlane) / frustum_length);
		mat.m33 = 0;
		return mat;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), mat, mat);
		Vector3f camPos = camera.getPosition();
		Vector3f negCamPos = new Vector3f(-camPos.x, -camPos.y, -camPos.z);
		Matrix4f.translate(negCamPos, mat, mat);
		return mat;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
}
