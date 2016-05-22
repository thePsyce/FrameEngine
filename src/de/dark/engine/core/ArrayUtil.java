package de.dark.engine.core;

import java.util.List;

public class ArrayUtil {
	public static int[] toIntArray(List<Integer> list)  {
	    int[] ret = new int[list.size()];
	    int i = 0;
	    for (Integer e : list)  
	        ret[i++] = e.intValue();
	    return ret;
	}
	
	public static float[] toFloatArray(List<Float> list)  {
	    float[] ret = new float[list.size()];
	    int i = 0;
	    for (Float e : list)  
	        ret[i++] = e.floatValue();
	    return ret;
	}
}
