/*
	
	Rules

	if neighbouring cell count < 2 => deactivate cell
	if neighbouring cell count > 3 => deactivate cell
	if neighbouring cell count == 3 => activate cell
	if neighbouring cell count == 2 => don't mess with it
	
	
	Click on each cell to activate them or press enter to randomly fill them 
	Press the  space bar to generate the next generation
	press the backspace key to delete all the active cells
	
	You can save and load the game
	
 */


package application;

import gui.MainFrame;


import javax.swing.SwingUtilities;

public class App {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> new MainFrame());

	}

}
