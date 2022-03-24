package Sorting.Sorting;

import java.util.Arrays;

public class BubbleSort extends SortingAlgorithm {

	private static String NAME = "Bubble Sort";

	int[] i, j; // loop indexes
	int[] ind1, ind2;
	boolean[] done;
	private State[] state;

	public BubbleSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// set up variables for sorting
		i = new int[setEnd - setBeg];
		Arrays.fill(i, dataSize - 1);
		j = new int[setEnd - setBeg];
		ind1 = new int[setEnd - setBeg];
		ind2 = new int[setEnd - setBeg];
		done = new boolean[setEnd - setBeg];
		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.COMPARE);
	}

	@Override
	public void nextOperation(int[] data) {

		if (!isDone()) {
			
			for (int set = 0; set < getEnd()-getBeg(); set++) {

				switch (state[set]) {
				case COMPARE:
					if (i[set] != 0) {
						ind1[set] = toIndex(set, j[set]);
						ind2[set] = toIndex(set, j[set] + 1);

						if (op.compare(data, ind1[set], ind2[set]) > 0) {
							state[set] = State.SWAP;
						}
						j[set]++;

						if (j[set] == i[set]) {
							i[set]--;
							j[set] = 0;
							if (done[set] || i[set] == 0) {
								state[set] = State.DONE;
							}
							else {
								done[set] = true;
							}
						}
					}
					break;
				case SWAP:
					
					done[set] = false;
					
					SortOperations.swap(data, ind1[set], ind2[set]);

					state[set] = State.COMPARE;
					break;
				default:
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
