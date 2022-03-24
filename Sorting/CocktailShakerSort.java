package Sorting.Sorting;

import java.util.Arrays;

public class CocktailShakerSort extends SortingAlgorithm {

	private static String NAME = "Cocktail Shaker Sort";

	int[] upper, lower, inner; // loop indexes
	int[] direction;
	int[] ind1, ind2;
	boolean[] done;
	private State[] state;

	public CocktailShakerSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// set up variables for sorting
		upper = new int[setEnd - setBeg];
		Arrays.fill(upper, dataSize - 1);
		lower = new int[setEnd - setBeg];
		Arrays.fill(lower, -1);
		
		inner = new int[setEnd - setBeg];
		Arrays.fill(inner, 0);
		
		direction = new int[setEnd - setBeg];
		Arrays.fill(direction, 1);
		
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
					if (upper[set] != lower[set]) {
						ind1[set] = toIndex(set, inner[set]);
						ind2[set] = toIndex(set, inner[set] + 1);

						if (op.compare(data, ind1[set], ind2[set]) > 0) {
							state[set] = State.SWAP;
						}
						inner[set] += direction[set];

						if (inner[set] == upper[set]) {
							upper[set]--;
							inner[set]--;
							direction[set] = -1;
							if (done[set] || lower[set] == upper[set]) {
								state[set] = State.DONE;
							}
							else {
								done[set] = true;
							}
						}
						else if (inner[set] == lower[set]) {
							lower[set]++;
							inner[set]++;
							direction[set] = 1;
							if (done[set] || lower[set] == upper[set]) {
								state[set] = State.DONE;
							}
							else {
								done[set] = true;
							}
						}
					}
					else {
						System.out.println("tsoiaeytohaisnet " + set);
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
