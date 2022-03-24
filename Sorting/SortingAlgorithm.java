package Sorting.Sorting;

public abstract class SortingAlgorithm {
	
	final SortOperations op;
	
	// beginning and end of the set to sort.
	// setBeg == first set to sort.
	// setEnd == last set to sort + 1
	private final int dataSize, setBeg, setEnd;
	private final String name;
	
	private boolean isDone = false;
	
	public SortingAlgorithm(SortOperations op, int dataSize, int setBeg, int setEnd, String name) {
		this.op = op;
		this.dataSize = dataSize;
		this.setBeg = setBeg;
		this.setEnd = setEnd;
		this.name = name;
	}
	
	public abstract void nextOperation(int[] data);
	
	public String getName() {
		return name;
	}
	
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public int getBeg() {
		return setBeg;
	}
	
	public int getEnd() {
		return setEnd;
	}
	
	public int getDataSize() {
		return dataSize;
	}
	
	public int toIndex(int set, int index) {
		return (set+getBeg()) + index * SortVisualizer.NUM_SETS;
	}

}
