package Sorting.Sorting;

import java.util.Arrays;

public class RadixSort extends SortingAlgorithm {

	private static final String NAME = "Radix Sort";

	private static final int KEY_LENGTH = 4;
	private final int MASK = (1 << (KEY_LENGTH)) - 1;

	int[] digit, index;
	int[][] bucketSizes, bucketIndex;
	int[][] copy;
	boolean[] done;
	State[] state;

	public RadixSort(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize variables
		// set up variables for sorting
		digit = new int[setEnd - setBeg];
		Arrays.fill(digit, 0);

		index = new int[setEnd - setBeg];
		Arrays.fill(index, 0);

		bucketSizes = new int[setEnd - setBeg][1 << (KEY_LENGTH)];
		for (int[] b : bucketSizes) {
			Arrays.fill(b, 0);
		}
		bucketIndex = new int[setEnd - setBeg][1 << (KEY_LENGTH)];
		for (int[] b : bucketIndex) {
			Arrays.fill(b, 0);
		}
		copy = new int[setEnd - setBeg][dataSize];

		done = new boolean[setEnd - setBeg];
		Arrays.fill(done, false);

		state = new State[setEnd - setBeg];
		Arrays.fill(state, State.SCAN);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			// System.err.println(state[0] + " " + digit[0] + " " + index[0]);

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				int bucket;

				switch (state[set]) {
				case SCAN: // Scan to find the bucket sizes

					bucket = findBucket(data, set, index[set], digit[set]);
					bucketSizes[set][bucket]++;

//					if (set == 0 && bucket == 7) {
//						System.out.println("Bucket: " + bucket + " value: "
//								+ Integer.toOctalString(op.valueOf(
//										data[toIndex(set, index[set])]))
//								+ " value?: " + Integer.toOctalString(
//										op.valueOf(data, set, index[set])));
//					}

					// Increment to check next number
					index[set]++;

					// Check if done
					if (bucketSizes[set][0] == getDataSize()) {
						state[set] = State.DONE;
					}

					// Move to next step if done scanning bucket sizes
					else if (index[set] >= getDataSize()) {
						index[set] = 0;
						state[set] = State.COPY;

//						System.out.print("bucket sizes: ");
//						if (set == 0) {
//							for (int b : bucketSizes[set]) {
//								System.out.print(b + ", ");
//							}
//							System.out.println();
//						}

					}

					break;
				case COPY: // Copy all elements to another array

					// Copy index-th element to copy array
					copy[set][index[set]] = data[toIndex(set, index[set])];

					// Increment to check next number
					index[set]++;

					// Move to next step if done copying
					if (index[set] >= getDataSize()) {
						index[set] = 0;
						state[set] = State.BUCKETING;
					}

					break;
				case BUCKETING:

					int color = copy[set][index[set]];

					bucket = findBucket(op.valueOf(color), digit[set]);

					int ind = 0;
					for (int i = 0; i < bucket; i++) {
						ind += bucketSizes[set][i];
					}
					ind += bucketIndex[set][bucket];
//					if (set == 0 && bucket == 7) {
//						System.err.println("ind: " + ind + " datasize: "
//								+ getDataSize() + " bucket: " + bucket
//								+ " Bucket index: " + bucketIndex[set][bucket]
//								+ " value: "
//								+ Integer.toOctalString(op.valueOf(color)));
//					}
					bucketIndex[set][bucket]++;

					data[toIndex(set, ind)] = color;

					// Increment to check next number
					index[set]++;

					// Restart on next digit if done
					if (index[set] >= getDataSize()) {

//						System.out.print("bucket indeces: ");
//						if (set == 0) {
//							for (int b : bucketIndex[set]) {
//								System.out.print(b + ", ");
//							}
//							System.out.println();
//						}

						index[set] = 0;
						digit[set]++;

						Arrays.fill(bucketSizes[set], 0);
						Arrays.fill(bucketIndex[set], 0);

						state[set] = State.SCAN;

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

	private int findBucket(int[] data, int set, int index, int digit) {

		int value = op.valueOf(data[toIndex(set, index)]);

		return findBucket(value, digit);
	}

	private int findBucket(int value, int digit) {

		value >>= (digit * KEY_LENGTH);

		value &= MASK;

		return value;

	}

	private enum State {
		SCAN, COPY, BUCKETING, DONE;
	}

}
