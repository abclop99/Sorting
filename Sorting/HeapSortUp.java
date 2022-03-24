package Sorting.Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class HeapSortUp extends SortingAlgorithm {

	private static final String NAME = "Heap Sort (siftUp)";
	private static final int DIVISION_SIZE = 10;
	private static final int INS_DIVISION_MARKER = Integer.MIN_VALUE;

	int[] numInHeap, elementInd;
	State[] state;

	public HeapSortUp(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize
		numInHeap = new int[getEnd() - getBeg()];
		Arrays.fill(numInHeap, 1);
		elementInd = new int[getEnd() - getBeg()];
		Arrays.fill(elementInd, 1);
		state = new State[getEnd() - getBeg()];
		Arrays.fill(state, State.HEAPIFY);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				if (state[set] != State.DONE) {

					switch (state[set]) {
					case HEAPIFY:
						// if done inserting 1 item, move on to the next.
						// if done building, start sorting.
						if (elementInd[set] == 0) {
							numInHeap[set]++;
							elementInd[set] = numInHeap[set];
							if (numInHeap[set] == getDataSize()) {
								state[set] = State.SORT;
							}
						}

						if (state[set] == State.HEAPIFY) {
							// compare the item and its parent.
							if (op.compare(data, toIndex(set, elementInd[set]),
									toIndex(set, parent(elementInd[set]))) > 0) {
								state[set] = State.SIFTUP;
							} else {
								// move to next item. or move on to sorting
								numInHeap[set]++;
								elementInd[set] = numInHeap[set];
								if (numInHeap[set] == getDataSize()) {
									state[set] = State.SORT;
								}
							}
						}

						break;
					case SIFTUP:
						// swap element with its parent.
						SortOperations.swap(data, toIndex(set, elementInd[set]),
								toIndex(set, parent(elementInd[set])));
						// move element index to parent.
						elementInd[set] = parent(elementInd[set]);
						state[set] = State.HEAPIFY;
						break;
					case SORT:
						if (numInHeap[set] > 1) {
							// root with next spot, and update numInHeap
							numInHeap[set]--;
							SortOperations.swap(data, toIndex(set, 0),
									toIndex(set, numInHeap[set]));
							// set up for heap Down.
							elementInd[set] = 0;
							state[set] = State.SIFTDOWN;
						} else {
							state[set] = State.DONE;
						}
						break;
					case SIFTDOWN:
						// find greatest child.
						if (right(elementInd[set]) >= numInHeap[set]
								|| op.compare(data, toIndex(set, left(elementInd[set])),
										toIndex(set, right(elementInd[set]))) > 0) {
							// left greater
							elementInd[set] = left(elementInd[set]);
							state[set] = State.CMPDOWN;
						} else {
							// right greater
							elementInd[set] = right(elementInd[set]);
							state[set] = State.CMPDOWN;
						}

						// go to compare with the result.

						break;
					case CMPDOWN:
						// compare with parent to decide if a swap is necessary
						if (op.compare(data, toIndex(set, elementInd[set]),
								toIndex(set, parent(elementInd[set]))) > 0) {
							state[set] = State.SWAPDOWN;
						}
						// swap not required. Move on.
						else {
							state[set] = State.SORT;
						}
						break;
					case SWAPDOWN:
						// swap element with its parent.
						SortOperations.swap(data, toIndex(set, elementInd[set]),
								toIndex(set, parent(elementInd[set])));
						// continue if has child.
						if (left(elementInd[set]) < numInHeap[set])
							state[set] = State.SIFTDOWN;
						else
							state[set] = State.SORT;
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

	private int parent(int index) {
		return (index - 1) / 2;
	}

	private int left(int index) {
		return index * 2 + 1;
	}

	private int right(int index) {
		return index * 2 + 2;
	}

	private enum State {
		HEAPIFY, SIFTUP, SORT, SIFTDOWN, CMPDOWN, SWAPDOWN, DONE;
	}

}
