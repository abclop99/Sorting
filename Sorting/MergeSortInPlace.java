package Sorting.Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MergeSortInPlace extends SortingAlgorithm {

	private static String NAME = "In-Place Merge Sort";

	int[] ins, cmp;
	State[] state;

	ArrayList<LinkedList<ArrayList<Integer>>> intervals;

	public MergeSortInPlace(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize
		ins = new int[getEnd() - getBeg()];
		cmp = new int[getEnd() - getBeg()];
		state = new State[getEnd() - getBeg()];
		Arrays.fill(state, State.COMPARE);

		// set up intervals for sorting
		intervals = new ArrayList<LinkedList<ArrayList<Integer>>>();
		for (int set = 0; set < getEnd() - getBeg(); set++) {
			LinkedList<ArrayList<Integer>> interval = new LinkedList<ArrayList<Integer>>();

			// add first layer
			ArrayList<Integer> firstLayer = new ArrayList<Integer>();
			firstLayer.add(getDataSize() - 1);
			firstLayer.add(-1);
			interval.add(firstLayer);

			// add new layers
			boolean done = false;
			while (!done) {
				// get current layer
				ArrayList<Integer> currentLayer = interval.getLast();
				// next layer
				ArrayList<Integer> nextLayer = new ArrayList<Integer>();

				// loop through current layer and add split layers to next layer
				// skip last one: using pairs of values.
				for (int i = 0; i < currentLayer.size() - 1; i++) {
					// if interval greater than 1, split and add to next layer.
					if (currentLayer.get(i) - currentLayer.get(i + 1) > 1) {
						// add beginning
						nextLayer.add(currentLayer.get(i));

						// add middle
						nextLayer.add((currentLayer.get(i + 1) + currentLayer.get(i)) / 2);

						// add end number
						nextLayer.add(currentLayer.get(i + 1));
					}
				}

				// if next layer has stuff, add it, and move on.
				if (!nextLayer.isEmpty()) {
					interval.addLast(nextLayer);
				}
				// done if next layer is empty.
				else {
					done = true;
				}

			}

			// remove top layer (no action layer)
			interval.removeFirst();

			intervals.add(interval);

			// print layers
			// if (set == 0) {
			// for (ArrayList<Integer> layer : interval) {
			// for (int i : layer) {
			// System.out.print(i + " ");
			// }
			// System.out.println();
			// }
			// }

			// ins <- middle
			ins[set] = interval.getLast().get(1);

			// cmp <- ins
			cmp[set] = ins[set];

		}

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				if (state[set] != State.DONE) {
					ArrayList<Integer> layer = intervals.get(set).getLast();

					// current merging intervals.
					int beg = layer.get(0);
					int mid = layer.get(1);
					int end = layer.get(2);

					int iInd = toIndex(set, ins[set]);
					int jInd = toIndex(set, cmp[set]);
					int jPostInd = toIndex(set, cmp[set] + 1);

					switch (state[set]) {
					case COMPARE:
						// to swap
						if (cmp[set] < beg && op.compare(data, jPostInd, jInd) < 0) {
							state[set] = State.SWAP;
						}
						// move to next thing?
						else {
							ins[set]--;
							cmp[set] = ins[set];

							// move on to next set
							if (ins[set] == end) {

								// remove this merging interval
								layer.remove(0);
								layer.remove(0);
								layer.remove(0);

								// move to next layer
								if (layer.isEmpty()) {
									intervals.get(set).removeLast();
									if (intervals.get(set).isEmpty()) {
										state[set] = State.DONE;
									} else {
										layer = intervals.get(set).getLast();
									}
								}

								if (state[set] != State.DONE) {

									// ins <- middle
									ins[set] = layer.get(1);

									// cmp <- ins
									cmp[set] = ins[set];

								}
							}
						}
						break;
					case SWAP:
						// swap
						op.swap(data, jInd, jPostInd);
						// go to next comparison
						cmp[set]++;
						// go to compare state
						state[set] = State.COMPARE;
						break;
					}

				}

				boolean done = true;
				for (State s : state) {
					if (s != State.DONE)
						done = false;
				}
				if (done) {
					setDone(true);
				}
			}
		}
	}

	private enum State {
		COMPARE, SWAP, DONE;
	}

}
