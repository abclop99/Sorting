package Sorting.Sorting;

import java.util.Arrays;
import java.util.Stack;

public class BinaryRadixMsdSort extends SortingAlgorithm {

	private static final String NAME = "Binary Radix MSD";
	
	private static final int MAX_DATA = 2048;

	int[] bin0, bin1, low, high, digit;
	Stack<Integer>[] stack;
	State[] state;

	@SuppressWarnings("unchecked")
	public BinaryRadixMsdSort(SortOperations op, int dataSize, int setBeg,
			int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize variables
		// set up variables for sorting
		bin0 = new int[setEnd - setBeg];
		Arrays.fill(bin0, -1);
		bin1 = new int[setEnd - setBeg];
		Arrays.fill(bin1, dataSize);
		low = new int[setEnd - setBeg];
		Arrays.fill(low, 0);
		high = new int[setEnd - setBeg];
		Arrays.fill(high, dataSize - 1);
		digit = new int[setEnd - setBeg];
		Arrays.fill(digit, (int)Math.ceil((Math.log(MAX_DATA)/Math.log(2))));

		stack = new Stack[setEnd - setBeg];
		for (int set = 0; set < stack.length; set++) {
			stack[set] = new Stack<Integer>();
			stack[set].push(digit[set]);
			stack[set].push(low[set]);
			stack[set].push(high[set]);
		}

		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.BEGIN);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

//			System.err.println(state[0] + " " + stack[0].size() + " " + low[0]
//					+ " " + high[0] + " " + bin0[0] + " " + bin1[0] + " "
//					+ digit[0]);

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				switch (state[set]) {
				case BEGIN:

					high[set] = stack[set].pop();
					low[set] = stack[set].pop();
					digit[set] = stack[set].pop();

					if (high[set] - low[set] < 1 || digit[set] < 0) {
						if (stack[set].isEmpty()) {
							state[set] = State.DONE;
						}
						continue;
					}

					// set up bins
					bin0[set] = low[set] - 1;
					bin1[set] = high[set] + 1;

					state[set] = State.COMPARE;

					break;
				case COMPARE:

					// if (set == 0) {
					// System.err.println(bin0[set] + " " + bin1[set]);
					// }

					if (bin1[set] - bin0[set] <= 1) {

						// set up stack
						stack[set].push(digit[set] - 1);
						stack[set].push(bin0[set] + 1);
						stack[set].push(high[set]);

						stack[set].push(digit[set] - 1);
						stack[set].push(low[set]);
						stack[set].push(bin0[set]);

						state[set] = State.BEGIN;
						continue;
					}

					int value = op.valueOf(data, set, toIndex(set, bin0[set] + 1));

//					if (set == 0) {
//						System.err.println(digit[set] + " " + (1 << digit[set])
//								+ " " + Integer.toBinaryString(value)
//								+ " " + Integer.toBinaryString(1 << (digit[set])) + " "
//								+ (value & (1 << (digit[set]))));
//					}

					if ((value & (1 << (digit[set]))) == 0) {

						bin0[set]++;

					} else {

						state[set] = State.SWAP;

					}

					break;
				case SWAP:
					// if (set == 0) {
					// System.err.println(bin0[set] + " " + bin1[set]);
					// }
					SortOperations.swap(data, toIndex(set, bin0[set] + 1),
							toIndex(set, bin1[set] - 1));
					bin1[set]--;
					state[set] = State.COMPARE;
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
		BEGIN, COMPARE, SWAP, NEXT, DONE;
	}

}
