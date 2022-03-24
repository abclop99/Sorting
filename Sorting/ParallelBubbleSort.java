package Sorting.Sorting;

import java.util.Arrays;

public class ParallelBubbleSort extends SortingAlgorithm {

	private static String NAME = "Parallel Bubble";

	private int[] swapSet;
	private int dataSize;
	private State[] state;

	public ParallelBubbleSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// set up variables for sorting
		this.dataSize = dataSize;
		swapSet = new int[setEnd - setBeg];
		Arrays.fill(swapSet, 0);
		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.COMPARE);
	}

	@Override
	public void nextOperation(int[] data) {

		if (!isDone()) {
			
			for (int set = 0; set < getEnd()-getBeg(); set++) {

				switch (state[set]) {
				case COMPARE:
					
					boolean setIsDone =  true;	// marker for doneness
					
					// loop over pairs in data
					for (int i = swapSet[set]; i < dataSize-1; i+=2 ) {
						
						int ind1 = toIndex(set, i);
						int ind2 = toIndex(set, i+1);
						
						// compare
						if( op.compare(data, ind1, ind2) > 0 ) {
							
							setIsDone = false;
							
						}
						
					}
					
					if ( setIsDone ) {
						state[set] = State.DONE;
					}
					else {
						state[set] = State.SWAP;
					}
					
					break;
				case SWAP:
					
					// loop over pairs in data
					for (int i = swapSet[set]; i < dataSize-1; i+=2 ) {
						
						int ind1 = toIndex(set, i);
						int ind2 = toIndex(set, i+1);
						
						// compare and swap
						if( op.compare(data, ind1, ind2) > 0 ) {
							
							SortOperations.swap(data, ind1, ind2);
							
						}
						
					}
					
					// move to comparing next set
					swapSet[set] = (swapSet[set] + 1) % 2;
					state[set] = State.COMPARE;
					
					break;
				default:
					break;
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
