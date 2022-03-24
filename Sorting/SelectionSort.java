package Sorting.Sorting;

import java.util.Arrays;

public class SelectionSort extends SortingAlgorithm {

	private static String NAME = "Selection Sort";

	int[] i, j, bestJ;
	State[] state;

	public SelectionSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize variables
		// set up variables for sorting
		i = new int[setEnd - setBeg];
		Arrays.fill(i, getDataSize() - 1);
		j = new int[setEnd - setBeg];
		Arrays.fill(j, getDataSize() - 1);
		bestJ = new int[setEnd - setBeg];
		Arrays.fill(bestJ, getDataSize() - 1);
		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.COMPARE);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				int iInd = toIndex(set, i[set]);
				int jInd = toIndex(set, j[set]);
				int bestInd = toIndex(set, bestJ[set]);

				switch (state[set]) {
				case COMPARE:

					if (j[set] >= 0) {
						if (op.compare(data, jInd, bestInd) > 0) {
							// set best
							bestInd = jInd;
							bestJ[set] = j[set];
						}
						j[set]--;
					} else {
						state[set] = State.SWAP;
					}

					break;
				case SWAP:
					// swap i and j
					SortOperations.swap(data, bestInd, iInd);

					// go to find next item
					i[set]--;
					j[set] = i[set];
					bestJ[set] = j[set];

					// mark as done if done.
					if (i[set] == 0) {
						state[set] = State.DONE;
					} else {
						// go to compare state
						state[set] = State.COMPARE;
					}
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

	private enum State {
		COMPARE, SWAP, DONE;
	}

}
