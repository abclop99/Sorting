package Sorting.Sorting;

import java.util.Arrays;

public class InsertionSort extends SortingAlgorithm {

	private static String NAME = "Insertion Sort";

	int[] i, j;
	State[] state;

	public InsertionSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize variables
		// set up variables for sorting
		i = new int[setEnd - setBeg];
		Arrays.fill(i, dataSize-2);
		j = new int[setEnd - setBeg];
		Arrays.fill(j, dataSize-2);
		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.COMPARE);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				int jInd = toIndex(set, j[set]);
				int jPostInd = toIndex(set, j[set]+1);

				switch (state[set]) {
				case COMPARE:

					if (j[set] < getDataSize()-1 && op.compare(data, jPostInd, jInd) < 0) {
						state[set] = State.SWAP;
					} else {
						i[set]--;
						j[set] = i[set];
						if (i[set] == -1) {
							state[set] = State.DONE;
						}
					}

					break;
				case SWAP:
					// swap i and j
					op.swap(data, jInd, jPostInd);
					// go to next comparison
					j[set]++;
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

	private enum State {
		COMPARE, SWAP, DONE;
	}

}
