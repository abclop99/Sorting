package Sorting.Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MergeSortOutOfPlace extends SortingAlgorithm {

	private static String NAME = "Out-Of-Place Merge Sort";

	int[] ind1, ind2;
	State[] state;
	ArrayList<ArrayList<Integer>> temp;

	ArrayList<LinkedList<ArrayList<Integer>>> intervals;

	public MergeSortOutOfPlace(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize
		ind1 = new int[getEnd() - getBeg()];
		ind2 = new int[getEnd() - getBeg()];
		state = new State[getEnd() - getBeg()];
		Arrays.fill(state, State.COMPARE);

		temp = new ArrayList<ArrayList<Integer>>();
		for (int set = 0; set < getEnd() - getBeg(); set++) {
			temp.add(new ArrayList<Integer>());
		}

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

			// ind1 <- beg
			ind1[set] = interval.getLast().get(0);

			// ind1 <- mid
			ind2[set] = interval.getLast().get(1);

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

					int i1Ind = toIndex(set, ind1[set]);
					int i2Ind = toIndex(set, ind2[set]);

					switch (state[set]) {
					case COMPARE:
						if (ind1[set] == mid) {
							if (ind2[set] == end) {
								// both reached end. Begin moving to list.
								state[set] = State.MOVE;
								ind2[set]++;
							} else {
								// first half has reached end. Add from second half here
								temp.get(set).add(data[i2Ind]);
								ind2[set]--;
							}
						} else {
							if (ind2[set] == end) {
								// second half has reached end. Add from first half here
								temp.get(set).add(data[i1Ind]);
								ind1[set]--;
							} else {
								// neither has reached end. Use comparison.
								if (op.compare(data, i1Ind, i2Ind) > 0) {
									state[set] = State.ADD1;
								} else {
									state[set] = State.ADD2;
								}
							}
						}
						break;
					case MOVE:
						// ind2 used as location to move to.

						data[i2Ind] = temp.get(set).remove(temp.get(set).size() - 1);

						if (ind2[set] == beg) {
							// finished moving. Move on to next set.

							state[set] = State.COMPARE;

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

								// ind1 <- beg
								ind1[set] = layer.get(0);

								// ind1 <- mid
								ind2[set] = layer.get(1);

								 temp.get(set).clear();

							}

						} else {
							ind2[set]++;
						}

						break;
					case ADD1:
						temp.get(set).add(data[i1Ind]);
						ind1[set]--;
						state[set] = State.COMPARE;
						break;
					case ADD2:
						temp.get(set).add(data[i2Ind]);
						ind2[set]--;
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
		COMPARE, ADD1, ADD2, MOVE, DONE;
	}

}
