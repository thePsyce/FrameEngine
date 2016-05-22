package de.dark.engine.audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioPlayer {

	private static final List<Integer> buffers = new ArrayList<>();
	
	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}
	
	public static int loadSound(String file) {
		int bfr = AL10.alGenBuffers();
		buffers.add(bfr);
		try {
			WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
			AL10.alBufferData(bfr, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return bfr;
	}
	
	public static void destroy() {
		for(int bfr : buffers) AL10.alDeleteBuffers(bfr);
		AL.destroy();
	}
}
