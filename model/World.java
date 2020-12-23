package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import exceptions.MismatchedSizeException;

public class World {
    private int rows;
    private int columns;

    private boolean[][] grid;
    private boolean[][] buffer;

    public World(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        grid = new boolean[rows][columns];
        buffer = new boolean[rows][columns];
    }

    public boolean getCell(int row, int col) {
        return grid[row][col];
    }

    public void setCell(int row, int col, boolean status) {
        grid[row][col] = status;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void randomize() {
        Random random = new Random();

        for (int i = 0; i < rows * columns / 5; i++) {
            int row = random.nextInt(rows);
            int col = random.nextInt(columns);

            setCell(row, col, true);
        }
    }

    public void clear() {
        for (int row = 0; row < rows; row++) {
            // fill all cells with false
            Arrays.fill(grid[row], false);
        }
    }

    public void next() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int neighbours = countNeighbours(row, col);

                boolean status = false;
                if(neighbours < 2) status = false;
                else if(neighbours > 3) status = false;
                else if(neighbours == 3) status = true;
                else status = getCell(row, col); // if neighbours == 2

                buffer[row][col] = status;
            }
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                grid[row][col] = buffer[row][col];
            }
        }
    }

    public int countNeighbours(int row, int col) {
        int neighbours = 0;
        int rowStart = row - 1 >= 0 ? -1 : 0;
        int rowEnd = row + 1 < rows ? 1 : 0;
        int colStart = col - 1 >= 0 ? -1 : 0;
        int colEnd = col + 1 < columns ? 1 : 0;

        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                if(row + i == row && col + j == col) continue;
                if(getCell(row + i, col + j)) neighbours++;
            }
        }
        return neighbours;
    }

	public void save(File selectedFile) throws IOException{
		try(var dos = new DataOutputStream(new FileOutputStream(selectedFile))){
			dos.writeInt(rows);
			dos.writeInt(columns);
			for(int row = 0; row < rows; row++) {
				for(int col = 0; col < columns; col++) {
					dos.writeBoolean(grid[row][col]);
				}
			}
		}
	}

	public void load(File selectedFile) throws IOException, MismatchedSizeException{
		try(var dis = new DataInputStream(new FileInputStream(selectedFile))){
			int fileRows = dis.readInt();
			int fileColumns = dis.readInt();
			
			for(int row = 0; row < fileRows; row++) {
				for(int col = 0; col < fileColumns; col++) {
					boolean status = dis.readBoolean();
					
					// if the window is resized
					if(row >= rows || col >= columns) {
						continue;
					}
					
					grid[row][col] = status;
				}
			}
			
			if(fileRows != this.rows || fileColumns != this.columns) {
				throw new MismatchedSizeException();
			}
		}
	}
}
