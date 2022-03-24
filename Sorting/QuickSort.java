package Sorting.Sorting;

import java.util.Arrays;
import java.util.Stack;

public class QuickSort extends SortingAlgorithm {

	private static String NAME = "QuickSort";

	int[] lo, hi, left, right, pivot, p;
	Stack<Integer>[] stack;
	State[] state;

	@SuppressWarnings("unchecked")
	public QuickSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize variables
		// set up variables for sorting
		lo = new int[setEnd - setBeg];
		Arrays.fill(lo, 0);
		hi = new int[setEnd - setBeg];
		Arrays.fill(hi, getDataSize() - 1);

		left = new int[setEnd - setBeg];
		right = new int[setEnd - setBeg];
		pivot = new int[setEnd - setBeg];
		p = new int[setEnd - setBeg];

		stack = new Stack[setEnd - setBeg];
		for (int set = 0; set < stack.length; set++) {
			stack[set] = new Stack<Integer>();
			stack[set].push(lo[set]);
			stack[set].push(hi[set]);
		}
		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.SETUP);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			// System.err.println(state[0] + " " + lo[0] + " " + hi[0] + " " +
			// p[0]
			// + " " + left[0] + " " + right[0] + " " + pivot[0] + " "
			// + stack[0].size() + " " + (hi[0] - lo[0]));

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				int leftInd = toIndex(set, left[set]);
				int rightInd = toIndex(set, right[set]);

				switch (state[set]) {

				case SETUP:

					// Pop hi and lo from stack
					hi[set] = stack[set].pop();
					lo[set] = stack[set].pop();

					// Stop if interval too small
					if (hi[set] - lo[set] < 1) {
						if (stack[set].isEmpty()) {
							state[set] = State.DONE;
						}
						continue;

					}

					// Set pivot, left, and right
					pivot[set] = data[toIndex(set,
							lo[set] + ((hi[set] - lo[set]) / 2))];
					left[set] = lo[set] - 1;
					right[set] = hi[set] + 1;

					// change to partitioning, from left
					state[set] = State.LEFT;

					break;
				case LEFT:
					left[set]++;
					leftInd = toIndex(set, left[set]);
					if (op.compareValue(data, leftInd, pivot[set]) >= 0) {
						state[set] = State.RIGHT;
					}
					break;

				case RIGHT:
					right[set]--;
					rightInd = toIndex(set, right[set]);
					if (op.compareValue(data, rightInd, pivot[set]) <= 0) {
						state[set] = State.SWAP;
					}
					break;

				case SWAP:
					if (left[set] >= right[set]) {
						state[set] = State.NEXT;
					} else {
						SortOperations.swap(data, leftInd, rightInd);
						state[set] = State.LEFT;
					}
					break;

				case NEXT:
					p[set] = right[set];
					
					// Set up left and right cursors
					stack[set].push(p[set] + 1);
					stack[set].push(hi[set]);

					stack[set].push(lo[set]);
					stack[set].push(p[set]);

					if (stack[set].isEmpty()) {
						state[set] = State.DONE;
					} else {
						state[set] = State.SETUP;
					}

					break;
				case DONE:
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
		SETUP, LEFT, RIGHT, SWAP, NEXT, DONE;
	}

}
