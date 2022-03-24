package Sorting.Sorting;

import java.util.function.IntBinaryOperator;

import processing.core.PApplet;
import processing.core.PConstants;

public class SortOperations {

	private final SortVisualizer parent;

	public SortOperations(SortVisualizer parent) {
		this.parent = parent;
	}

	public static void scramble(int[] data) {

		IntBinaryOperator toIndex = (set, index) -> set + index * SortVisualizer.NUM_SETS;

		for (int set = 0; set < SortVisualizer.NUM_SETS; set++) {
			for (int ind = 0; ind < SortVisualizer.DATA_SIZE; ind++) {

				// select random item from remaining
				int selected = (int) (Math.random() * (SortVisualizer.DATA_SIZE - ind) + ind);

				// swap
				swap(data, toIndex.applyAsInt(set, ind), toIndex.applyAsInt(set, selected));

			}
		}

	}

	public void gradientFill(int[] data) {
		// fill with gradient
		parent.colorMode(PConstants.HSB, 1);
		for (int i = 0; i < data.length; i++) {
			data[i] = parent.color(((i / SortVisualizer.NUM_SETS) * SortVisualizer.HUE_OFFSET) % 1,
					1, 1);
			// adjust brightness
//			data[i] = parent.color(parent.hue(data[i]), 1, (float)(0.587/(0.299 * parent.red(data[i]) + 0.587 * parent.green(data[i]) + 0.114 * parent.blue(data[i]))));
		}
	}

	public void perlinFill(int[] data) {
		parent.colorMode(PConstants.HSB, 1);
		for (int i = 0; i < SortVisualizer.DATA_SIZE; i++) {
			int iInd = i * SortVisualizer.NUM_SETS;
			for (int j = 0; j < SortVisualizer.NUM_SETS; j++) {
				// set pixel to perlin noise
				data[iInd + j] = parent.color(PApplet.map(parent.noise(i * 0.02f, j * 0.02f),0,1,0,0.8f)%1, 1, 1);
				// System.out.println(i + " " + j + " " + (iInd+j));
				// 249999
			}
		}
	}

	public static void swap(int[] data, int ind1, int ind2) {
		int tmp = data[ind1];
		data[ind1] = data[ind2];
		data[ind2] = tmp;
	}

	public float compare(int[] data, int ind1, int ind2) {
		return parent.hue(data[ind1]) - parent.hue(data[ind2]);
	}

	public float compareValue(int[] data, int ind1, int value) {
		return parent.hue(data[ind1]) - parent.hue(value);
	}

	public int valueOf(int[] data, int set, int index) {

		float hue = parent.hue(data[index]);

		return (int)(hue*2048);
	}

	public int valueOf(int color) {

		float hue = parent.hue(color);

		return (int)(hue*2048);
	}

}
