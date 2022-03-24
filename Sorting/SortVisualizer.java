package Sorting.Sorting;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class SortVisualizer extends PApplet {

	private static final boolean SAVE_FRAMES = true;

	public static final float DOT_SIZE = 2f;
	public static final int NUM_SETS = (int) (900 / DOT_SIZE);
	public static final int DATA_SIZE = (int) (650 / DOT_SIZE);
	public static final int SCREEN_WIDTH = (int) (NUM_SETS * DOT_SIZE);
	public static final int SCREEN_HEIGHT = (int) (DATA_SIZE * DOT_SIZE);

	public static final float HUE_OFFSET = (5f / 6) / DATA_SIZE;

	// for speed
	private static final int MAX_FPS = 60;
	private static final int OPS_PER_SEC = MAX_FPS * 12;
	private static final int FPS = min(OPS_PER_SEC, MAX_FPS);
	private static final int ITER_PER_FRAME = max(1, OPS_PER_SEC / FPS);

	private PImage data;
	private ArrayList<SortingAlgorithm> sortingAlgorithms;
	private SortOperations op = new SortOperations(this);

	public static void main(String[] args) {
		PApplet.main(SortVisualizer.class.getName());
	}

	@Override
	public void settings() {
		size(SCREEN_WIDTH, SCREEN_HEIGHT);
		noSmooth();
	}

	@Override
	public void setup() {

		frameRate(FPS);

		data = new PImage(NUM_SETS, DATA_SIZE);

		// fill with data
		data.loadPixels();
		// op.perlinFill(data.pixels);
		op.gradientFill(data.pixels);
		SortOperations.scramble(data.pixels);
		data.updatePixels();
		// set up sorting algorithms
		{
			// list of sorting algorighms to use
			ArrayList<Class<? extends SortingAlgorithm>> algorithms = new ArrayList<>();
//			algorithms.add(Bogosort.class);
//			algorithms.add(BubbleSort.class);
//			algorithms.add(CocktailShakerSort.class);
//			algorithms.add(InsertionSort.class);
//			algorithms.add(SelectionSort.class);
//			algorithms.add(MergeSortInPlace.class);
			algorithms.add(MergeSortOutOfPlace.class);
//			algorithms.add(MergeSortHybrid.class);
			algorithms.add(HeapSortUp.class);
			algorithms.add(HeapSortDown.class);
			// algorithms.add(ParallelBubbleSort.class);
			algorithms.add(QuickSort.class);
			algorithms.add(BinaryRadixMsdSort.class);
			algorithms.add(RadixSort.class);

			sortingAlgorithms = new ArrayList<SortingAlgorithm>();

			for (int i = 0; i < algorithms.size(); i++) {
				try {
					sortingAlgorithms.add(algorithms.get(i)
							.getConstructor(new Class[] { SortOperations.class,
									int.class, int.class, int.class })
							.newInstance(op, DATA_SIZE,
									NUM_SETS * i / algorithms.size(),
									NUM_SETS * (i + 1) / algorithms.size()));
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		image(data, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		if (SAVE_FRAMES) {
			data.save(savePath("outputs\\sorting\\") + frameCount + ".png");
		}

	}

	@Override
	public void draw() {
		background(0);

		data.loadPixels();
		for (int i = 0; i < ITER_PER_FRAME; i++) {
			for (SortingAlgorithm sa : sortingAlgorithms) {
				if (!sa.isDone()) {
					sa.nextOperation(data.pixels);
				}
			}
		}
		data.updatePixels();

		image(data, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		textSize(15);
		fill(1);
		boolean done = true;
		for (SortingAlgorithm sa : sortingAlgorithms) {
			// print name
			text(sa.getName(), sa.getBeg() * DOT_SIZE, 15);

			// print done status
			if (sa.isDone()) {
				text("Done", sa.getBeg() * DOT_SIZE, 30);
			} else {
				done = false;
			}
		}

		if (done) {
			noLoop();
		}

		if (SAVE_FRAMES) {
			data.save(savePath("outputs\\sorting\\") + frameCount + ".png");
		}

	}

	/**
	 * Starts over when the mouse is clicked.
	 * 
	 * @see processing.core.PApplet#mouseClicked()
	 */
	@Override
	public void mouseClicked() {
		setup();
		loop();
	}

}
