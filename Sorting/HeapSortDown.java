package Sorting.Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class HeapSortDown extends SortingAlgorithm {

	private static final String NAME = "Heap Sort (siftDown)";
	private static final int DIVISION_SIZE = 10;
	private static final int INS_DIVISION_MARKER = Integer.MIN_VALUE;

	int[] numInHeap, elementInd, heapifyLocation;
	State[] state;
	State[] returnTo;

	public HeapSortDown(SortOperations op, int dataSize, int setBeg, int setEnd) {
		super(op, dataSize, setBeg, setEnd, NAME);

		// initialize
		numInHeap = new int[getEnd() - getBeg()];
		Arrays.fill(numInHeap, getDataSize());

		elementInd = new int[getEnd() - getBeg()];

		heapifyLocation = new int[getEnd() - getBeg()];
		Arrays.fill(heapifyLocation, parent(getDataSize() - 1) + 1);

		state = new State[getEnd() - getBeg()];
		Arrays.fill(state, State.HEAPIFY);

		returnTo = new State[getEnd() - getBeg()];
		Arrays.fill(returnTo, State.DONE);

	}

	@Override
	public void nextOperation(int[] data) {
		if (!isDone()) {

			for (int set = 0; set < getEnd() - getBeg(); set++) {

				if (state[set] != State.DONE) {

					switch (state[set]) {
					case HEAPIFY:
						heapifyLocation[set]--;
						elementInd[set] = heapifyLocation[set];
						// loop part: Sift down each parent
						if (elementInd[set] >= 0) {
							returnTo[set] = State.HEAPIFY;
							state[set] = State.SIFTDOWN;
						}
						// done heapifying: move on to sorting
						else {
							elementInd[set] = numInHeap[set];
							if (numInHeap[set] == getDataSize()) {
								state[set] = State.SORT;
							}
						}
						break;
					case SORT:
						if (numInHeap[set] > 1) {
							// root with next spot, and update numInHeap
							numInHeap[set]--;
							SortOperations.swap(data, toIndex(set, 0),
									toIndex(set, numInHeap[set]));
							// set up for heap Down.
							elementInd[set] = 0;
							returnTo[set] = State.SORT;
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
							state[set] = returnTo[set];
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
							state[set] = returnTo[set];
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
		HEAPIFY, SORT, SIFTDOWN, CMPDOWN, SWAPDOWN, DONE;
	}

}
