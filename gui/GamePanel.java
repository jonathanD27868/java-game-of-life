package gui;

import model.World;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import exceptions.MismatchedSizeException;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final static int CELLSIZE = 20;

    private final Color backgroundColor = Color.BLACK;
    private final Color foregroundColor = Color.GREEN;
    private final Color gridColor = Color.GRAY;

    private int topBottomMargin;
    private int leftRightMargin;

    private World world;



    public GamePanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int row = (e.getY() - topBottomMargin) / CELLSIZE;
                int col = (e.getX() - leftRightMargin) / CELLSIZE;

                if (row >= world.getRows() || col >= world.getColumns()) {
                    return;
                }
                boolean status = world.getCell(row, col);
                world.setCell(row, col, !status);
                System.out.println(row + ", " + col);

                repaint();
            }
        });
        // execute automatically
        // Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> next(), 500, 50, TimeUnit.MILLISECONDS);
    }

    public void randomize() {
        world.randomize();
        repaint();
    }

    public void clear() {
        world.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        // resize the margins
        leftRightMargin = ((width % CELLSIZE) + CELLSIZE) / 2;
        topBottomMargin = ((height % CELLSIZE) + CELLSIZE) / 2;

        int rows = (height - 2 * topBottomMargin) / CELLSIZE;
        int columns = (width - 2 * leftRightMargin) / CELLSIZE;

        if (world == null) {
            world = new World(rows, columns);
        } else {
            if (world.getRows() != rows || world.getColumns() != columns) {
                world = new World(rows, columns);
            }
        }

        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, width, height);

        drawGrid(g2, width, height);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                fillCell(g2, row, col, world.getCell(row, col));
            }
        }
    }

    private void fillCell(Graphics2D g2, int row, int col, boolean status) {
        Color color = status ? foregroundColor : backgroundColor;
        g2.setColor(color);
        int x = leftRightMargin + col * CELLSIZE;
        int y = topBottomMargin + row * CELLSIZE;
        g2.fillRect(x + 1, y + 1, CELLSIZE - 1, CELLSIZE - 1);
    }

    private void drawGrid(Graphics2D g2, int width, int height) {
        g2.setColor(gridColor);

        for (int x = leftRightMargin; x <= width - leftRightMargin; x += CELLSIZE) {
            g2.drawLine(x, topBottomMargin, x, height - topBottomMargin);
        }

        for (int y = topBottomMargin; y <= height - topBottomMargin; y += CELLSIZE) {
            g2.drawLine(leftRightMargin, y, width - leftRightMargin, y);
        }

    }

    public void next() {
        world.next();
        repaint();
    }

	public void save(File selectedFile) {
		try {
			world.save(selectedFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Cannot save selected file", "An error occurred", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void load(File selectedFile) {
		try {
			world.load(selectedFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Cannot load selected file", "An error occurred", 
					JOptionPane.ERROR_MESSAGE);
		} catch (MismatchedSizeException e) {
			JOptionPane.showMessageDialog(this, "Loading grid size from a larger or smaller grid", "Warning", 
					JOptionPane.WARNING_MESSAGE);
		}
		repaint();
	}
}
