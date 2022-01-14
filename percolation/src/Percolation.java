import java.util.Scanner;

/**
 * 
 * Percolation
 * 
 * @author Ana Paula Centeno
 * @author Haolin (Daniel) Jin
 */

public class Percolation {

	private boolean[][] grid; // gridSize by gridSize grid of sites;
								// true = open site, false = closed or blocked site
	private WeightedQuickUnionFind wquFind; //
	private int gridSize; // gridSize by gridSize is the size of the grid/system
	private int gridSquared;
	private int virtualTop; // virtual top index on WeightedQuckUnionFind arrays
	private int virtualBottom; // virtual bottom index on WeightedQuckUnionFind arrays

	/**
	 * Constructor. Initializes all instance variables
	 */
	public Percolation(int n) {
		gridSize = n;
		gridSquared = gridSize * gridSize;
		wquFind = new WeightedQuickUnionFind(gridSquared + 2);
		grid = new boolean[gridSize][gridSize]; // every site is initialized to closed/blocked
		virtualTop = gridSquared;
		virtualBottom = gridSquared + 1;
	}

	/**
	 * Getter method for GridSize
	 * 
	 * @return integer representing the size of the grid.
	 */
	public int getGridSize() {
		return gridSize;
	}

	/**
	 * Returns the grid array
	 * 
	 * @return grid array
	 */
	public boolean[][] getGridArray() {
		return grid;
	}

	/**
	 * Open the site at postion (x,y) on the grid to true and add an edge to any
	 * open neighbor (left, right, top, bottom) and/or top/bottom virtual sites
	 * Note: diagonal sites are not neighbors
	 *
	 * @param row grid row
	 * @param col grid column
	 * @return void
	 */
	public void openSite(int row, int col) {

		if (!grid[row][col]) {
			grid[row][col] = true;
			if (row == 0)
				wquFind.union(relateMatrixAndArray(row, col), virtualTop);
			if (row == gridSize - 1)
				wquFind.union(relateMatrixAndArray(row, col), virtualBottom);
			try {
				if (grid[row - 1][col])
					wquFind.union(relateMatrixAndArray(row, col), relateMatrixAndArray(row - 1, col));
			} catch (Exception e) {

			}
			try {
				if (grid[row + 1][col])
					wquFind.union(relateMatrixAndArray(row, col), relateMatrixAndArray(row + 1, col));
			} catch (Exception e) {
			}
			try {
				if (grid[row][col - 1])
					wquFind.union(relateMatrixAndArray(row, col), relateMatrixAndArray(row, col - 1));
			} catch (Exception e) {
			}
			try {
				if (grid[row][col + 1])
					wquFind.union(relateMatrixAndArray(row, col), relateMatrixAndArray(row, col + 1));
			} catch (Exception e) {
			}
		}
		return;
	}

	/**
	 * Check if the system percolates (any top and bottom sites are connected by
	 * open sites)
	 * 
	 * @return true if system percolates, false otherwise
	 */
	public boolean percolationCheck() {
		return wquFind.find(virtualTop) == wquFind.find(virtualBottom) ? true : false;
	}

	/**
	 * Iterates over the grid array openning every site. Starts at [0][0] and moves
	 * row wise
	 * 
	 * @param probability
	 * @param seed
	 */
	public void openAllSites(double probability, long seed) {

		// Setting the same seed before generating random numbers ensure that
		// the same numbers are generated between different runs
		StdRandom.setSeed(seed); // DO NOT remove this line

		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				if (StdRandom.uniform() <= probability)
					openSite(row, col);
			}
		}
	}

	/**
	 * Open up a new window and display the current grid using StdDraw library. The
	 * output will be colored based on the grid array. Blue for open site, black for
	 * closed site.
	 * 
	 * @return: void
	 */
	public void displayGrid() {
		double blockSize = 0.9 / gridSize;
		double zeroPt = 0.05 + (blockSize / 2), x = zeroPt, y = zeroPt;

		for (int i = gridSize - 1; i >= 0; i--) {
			x = zeroPt;
			for (int j = 0; j < gridSize; j++) {
				if (grid[i][j]) {
					StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
					StdDraw.filledSquare(x, y, blockSize / 2);
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.square(x, y, blockSize / 2);
				} else {
					StdDraw.filledSquare(x, y, blockSize / 2);
				}
				x += blockSize;
			}
			y += blockSize;
		}
	}

	private int relateMatrixAndArray(int row, int col) {
		return row * gridSize + col;
	}

	/**
	 * Main method, for testing only, feel free to change it.
	 */
	public static void main(String[] args) {

		double p = 0.48481876811381797;
		Percolation pl = new Percolation(6);

		/*
		 * Setting a seed before generating random numbers ensure that the same numbers
		 * are generated between runs.
		 *
		 * If you would like to reproduce Autolab's output, update the seed variable to
		 * the value Autolab has used.
		 */
		long seed = System.currentTimeMillis();
		pl.openAllSites(p, seed);
		System.out.println("The system percolates: " + pl.percolationCheck());
		pl.displayGrid();
	}
}