package de.dark.engine.core;

public class Bitmap {
	private int width;
	private int height;
	private int[] pixels;
	
	public Bitmap(int width, int height, int[] pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}
	
	public int getPixel(int x, int y) {
		return this.pixels[x + y * width];
	}
	
	public void setPixel(int x, int y, int value) {
		this.pixels[x + y * width] = value;
	}
	
	public void flipX() {
		int[] tmp = new int[this.pixels.length];
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.height; y++) {
				tmp[x + y * width] = this.pixels[(this.width - x - 1) + y * this.width];
			}
		}
		this.pixels = tmp;
	}
	
	public void flipY() {
		int[] tmp = new int[this.pixels.length];
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.height; y++) {
				tmp[x + y * width] = this.pixels[x + (this.height - y - 1) * this.width];
			}
		}
		this.pixels = tmp;
	}
}
