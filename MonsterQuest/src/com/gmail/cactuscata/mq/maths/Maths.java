package com.gmail.cactuscata.mq.maths;

public class Maths {

	public static double arrondidouble(double value, int nbrvirgule) {

		return (double) ((int) (value * Math.pow(10, nbrvirgule) + .5)) / Math.pow(10, nbrvirgule);

	}

	public static float arrondifloat(float value, int nbrvirgule) {

		return (float) ((float) ((int) (value * Math.pow(10, nbrvirgule) + .5)) / Math.pow(10, nbrvirgule));

	}
}
