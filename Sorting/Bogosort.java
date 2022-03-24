package Sorting.Sorting;

import java.util.Arrays;

public class Bogosort extends SortingAlgorithm {

	private static String NAME = "Bogosort";

	int[] i;
	State[] state;

	public Bogosort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize variables
		// set up variables for sorting
		i = new int[setEnd - setBeg];
		Arrays.fill(i, 0);
		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.SCRAMBLE);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				int iInd = toIndex(set, i[set]);
				int iPlusOneInd = toIndex(set, i[set]+1);

				switch (state[set]) {
				case SCRAMBLE:
					
					// Select element to swap with
					int swap = (int) (Math.random() * (SortVisualizer.DATA_SIZE - i[set]) + i[set]);
					
					// swap
					SortOperations.swap(data, iInd, toIndex(set, swap));
					
					// move to next step
					if ( i[set] < getDataSize() - 2 ) {
						i[set]++;
					}
					else {
						i[set] = 0;
						state[set] = State.TEST;
					}
					
					break;
				case TEST:
					
					// check if ith element and i+1th element are sorted
					if ( op.compare(data, iInd, iPlusOneInd) < 0 ) {
						
						// current part sorted, move on
						if ( i[set] < getDataSize() - 2 ) {
							i[set]++;
						}
						else {
							i[set] = 0;
							state[set] = State.SCRAMBLE;
						}
					} else {
						// not sorted, scramble again
						i[set] = 0;
						state[set] = State.SCRAMBLE;
					}
					
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
		SCRAMBLE, TEST, DONE;
	}

}
