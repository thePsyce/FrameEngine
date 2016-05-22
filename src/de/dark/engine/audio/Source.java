package de.dark.engine.audio;

import org.lwjgl.openal.AL10;

public class Source {
	
	private int source;
	
	public Source(float gain) {
		this.source = AL10.alGenSources();
		AL10.alSourcef(this.source, AL10.AL_GAIN, gain);
		AL10.alSourcef(this.source, AL10.AL_PITCH, 1);
		AL10.alSource3f(this.source, AL10.AL_POSITION, 0, 0, 0);
	}
	
	public void play(int buffer) {
		AL10.alSourcei(this.source, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(this.source);
	}
	
	public void delete() {
		AL10.alDeleteSources(this.source);
	}
}
