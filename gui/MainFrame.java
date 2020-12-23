package gui;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private GamePanel gamePanel = new GamePanel();
	private static final String defaultFileName = "gameoflife.gol";
	
	public MainFrame() {
		super("Game of Life");
				
		setLayout(new BorderLayout());
		add(gamePanel, BorderLayout.CENTER);
		
				
		MenuItem openItem = new MenuItem("Open");
		MenuItem saveItem = new MenuItem("Save");
		
		Menu fileMenu = new Menu("File");
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		
		MenuBar menuBar = new MenuBar();
		menuBar.add(fileMenu);
		setMenuBar(menuBar);
		
		JFileChooser fileChooser = new JFileChooser();
		
		// accepted extensions (choice in open dialog => file format)
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of Life Files", "gol");
		fileChooser.addChoosableFileFilter(filter);
		// select "Game of Life Files" automatically in open dialog => file format
		fileChooser.setFileFilter(filter);
		
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int userOption = fileChooser.showOpenDialog(MainFrame.this);
				
				if(userOption == JFileChooser.APPROVE_OPTION) {
					// get the file name in case of any change in the save window dialog
					File selectedFile = fileChooser.getSelectedFile();
					gamePanel.load(selectedFile);
				}
			}
		});
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Set default filename in the save window dialog
				fileChooser.setSelectedFile(new File(defaultFileName));
				int userOption = fileChooser.showSaveDialog(MainFrame.this);
				
				if(userOption == JFileChooser.APPROVE_OPTION) {
					// get the file name in case of any change in the save window dialog
					File selectedFile = fileChooser.getSelectedFile();
					gamePanel.save(selectedFile);
				}
			}
		});
				

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				int code = e.getKeyCode();

				switch (code) {
					case 32:
						gamePanel.next();
						break;
					case 8:
						gamePanel.clear();
						break;
					case 10:
						gamePanel.randomize();
						break;
				}
			}
		});
		
		setSize(800,600);
		
		// full screen
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		// setUndecorated(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
}
