package uiView.classes;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gameplay.newGame.NewPlayerPayload;
import gameplay.newGame.PlayerInitializer;
import pojos.entity.PlayerEntity;
import pojos.entity.enums.EntityClassEnum;
import pojos.entity.enums.Species;
import uiView.UIMain;
import utilities.Logs;

public class CharacterCreateWindow extends GameWindow{	
	static JFrame window;
	Container con;
	JPanel upOut, out, in;
	static JTextArea upperOutput;
	static JTextArea lowerOutput;
	static JTextField input;

	static boolean enterPressed = false;

	private PlayerInitializer playerinit = new PlayerInitializer();
	private NewPlayerPayload newPlayerPayload = new NewPlayerPayload();

	public CharacterCreateWindow() {

		window = new JFrame("Character Creation");
		window.setPreferredSize(SCREEN_DIM);
		window.setSize(SCREEN_DIM);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);
		window.setMaximumSize(window.getSize());

		con = window.getContentPane();
		con.setLayout(null);
		JPanel orig_bounds = new JPanel();
		orig_bounds.setBackground(backgroundColor);
		orig_bounds.setBounds(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);
		orig_bounds.setBorder(thiccLineBorder);
		orig_bounds.setLayout(null);
		con.add(orig_bounds);

		JPanel bounds = new JPanel();
		int half_buffer = (int)(BUFFER / 2);
		bounds.setBounds(half_buffer, half_buffer, SCREEN_WIDTH - BUFFER, SCREEN_HEIGHT -  BUFFER - BUFFER);
		bounds.setBackground(backgroundColor);
		bounds.setBorder(thinLineBorder);
		bounds.setLayout(null);
		orig_bounds.add(bounds);

		int bounds_WIDTH = bounds.getWidth();
		int bounds_HEIGHT = bounds.getHeight();		

		upOut = new JPanel();
		bounds.add(upOut);

		int upperOutWidth = (int)(bounds_WIDTH * .75);
		int upperOutHeight = (int)(bounds_HEIGHT * .375);
		int upperOutBufferWidth = (int)((bounds_WIDTH - upperOutWidth)/2);
		int upperOutBufferHeight = 0;

		upOut.setBounds(upperOutBufferWidth,upperOutBufferHeight, upperOutWidth, upperOutHeight);
		upOut.setBackground(backgroundColor);
		upOut.setBorder(thiccLineBorder);

		out = new JPanel();
		bounds.add(out);

		int outWidth = (int)(bounds_WIDTH * .875);
		int outHeight = (int)(bounds_HEIGHT * .5625);
		int outBufferWidth = (int)((bounds_WIDTH - outWidth)/2);
		int outBufferHeight = upperOutBufferHeight + upperOutHeight;

		out.setBounds(outBufferWidth,outBufferHeight,outWidth, outHeight);
		out.setPreferredSize(out.getSize());
		out.setBackground(backgroundColor);
		out.setBorder(medLineBorder);


		in = new JPanel();
		bounds.add(in);

		int inHeight = (int)(bounds_HEIGHT * .0625);
		int inBufferHeight = outBufferHeight + outHeight;

		in.setBounds(outBufferWidth,inBufferHeight,outWidth, inHeight);
		in.setBackground(backgroundColor);
		in.setBorder(medLineBorder);

		upperOutput = new JTextArea();
		lowerOutput = new JTextArea();
		input = new JTextField("Type your name to begin your adventure, hero.");

		upOut.setLayout(new GridLayout(2,3));
		out.setLayout(new FlowLayout(FlowLayout.LEADING));
		in.setLayout(new FlowLayout(FlowLayout.LEADING));

		addUpperOutputBox(upOut);
		addOutputBox(out, lowerOutput);
		addInputBox(in,input);

		window.setResizable(false);
		window.pack();
		window.setVisible(true);


		input.requestFocus();


	}//end initializer

	private void addOutputBox(Container out, JTextArea box) {
		box.setOpaque(false);
		box.setForeground(textColor);
		box.setFont(gameFont);
		int margin = BUFFER/2 + MED;
		box.setSize(out.getWidth() - BUFFER, out.getHeight());
		box.setMargin(new Insets(margin,margin,margin, margin));
		box.setEditable(false);
		box.setHighlighter(null);
		box.setLineWrap(true);
		box.setWrapStyleWord(true);

		//initial text while database is loading
		box.setText("It appears the snarling tendrils of this fantasy world have not yet ensnared you, weary traveler." 
				+ "\n\nDo tell, what is your profession?");

		out.add(box);
	}

	private void addUpperOutputBox(Container out) {
		int speciesCounter = 0;
		int classCounter = 0;

		JButton classLButton = new JButton("<");
		JButton classRButton = new JButton(">");
		JTextArea className = new JTextArea(""+getClasses().get(classCounter));
		JButton speciesLButton = new JButton("<");
		JButton speciesRButton = new JButton(">");
		JTextArea speciesName = new JTextArea( ""+getSpecies().get(speciesCounter));
		JButton[] buttonArray = new JButton[] { classLButton, classRButton, speciesLButton, speciesRButton };
		JPanel classPanel = new JPanel();
		JPanel speciesPanel = new JPanel();
		classPanel.setBackground(backgroundColor);
		speciesPanel.setBackground(backgroundColor);
		className.setBackground(backgroundColor);
		className.setForeground(textColor);
		className.setFont(smallMenuFont);
		speciesName.setBackground(backgroundColor);
		speciesName.setForeground(textColor);
		speciesName.setFont(smallMenuFont);


		for(JButton button : buttonArray) {
			button.setForeground(textColor);
			button.setBackground(backgroundColor);
			button.setFont(smallMenuFont);
			button.setOpaque(true);
			button.setBorderPainted(false);	
		}
		addListeners(classLButton, classRButton, speciesLButton, speciesRButton, classCounter, speciesCounter);

		classPanel.add(classLButton);
		classPanel.add(className);
		classPanel.add(classRButton);

		//species select
		speciesPanel.add(speciesLButton);
		speciesPanel.add(speciesName);
		speciesPanel.add(speciesRButton);


		out.add(classPanel);
		out.add(speciesPanel);
	}

	private void addInputBox(Container in, JTextField box) {
		box.setOpaque(false);
		box.setBorder(null);
		box.setForeground(textColor);
		box.setFont(gameFont);
		box.setHighlighter(null);
		box.setPreferredSize(in.getSize());
		box.setFocusTraversalKeysEnabled(false);
		box.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER) {
					enterPressed = true;
					if(!input.getText().equals("/quit")) {
						UIMain.player.setName(input.getText());
						UIMain.player = playerinit.initializePlayer(true, newPlayerPayload);

						Logs.LOGGER.info("Character Creation Character Name: " + UIMain.player.getName());
						window.dispose();
					} else {
						window.dispose();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}});


		in.add(box);
	}

	public JTextArea getUpperOutputBox() {
		return upperOutput;
	}

	public JTextArea getOutputBox() {
		return lowerOutput;
	}

	public JTextField getInputBox() {
		return input;
	}

	public void outGUI(String outputString) {
		JTextArea outputBox = getOutputBox();
		outputBox.setText(outputString);
	}

	public void outTopGUI(String upperOutputString) {
		JTextArea upperOutputBox = getUpperOutputBox();
		upperOutputBox.setText(upperOutputString);
	}

	public String inGUI() {
		JTextField inputBox = getInputBox();
		String response = inputBox.getText();
		inputBox.setText("");

		return response;
	}

	public String requestInput() {
		while(!enterPressed) {
			try {
				Thread.sleep(300);       
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		enterPressed = false;
		return inGUI();

	}

	public PlayerEntity getNewPlayer() {
		Logs.LOGGER.info("NewGameWindow.getNewPlayer() " + UIMain.player);
		return UIMain.player;
	} 

	public List<EntityClassEnum> getClasses() {
		return Arrays.asList(EntityClassEnum.values());
	}

	public List<Species> getSpecies() {
		return Arrays.asList(Species.values());
	}

	public void exitWindow() {
		window.dispose();
	}
	private void addListeners(JButton classLButton, JButton classRButton, JButton speciesLButton, JButton speciesRButton, int classCounter, int speciesCounter) {
		JButton[] buttonArray = new JButton[] { classLButton, classRButton, speciesLButton, speciesRButton };
		
		classLButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int iterator = classCounter;
				//move class list back one
				iterator = backOneClass(iterator);
			}
		});

		classRButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//move class list forward one
			}

		}); 

		speciesLButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				//move species list back one
			}

		});

		speciesRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				//move species list forward one
			}
		});

		for (JButton button : buttonArray) {
			addChangeListener(button);
		}

	}

	private void addChangeListener(JButton button) {
		button.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (button.getModel().isPressed() || button.getModel().isRollover()) {
					button.setForeground(textColor.darker());
					for (Component component : button.getComponents() ) {
						component.setForeground(textColor.darker());
						button.add(component);
					}
				} else {
					button.setForeground(textColor);
					for (Component component : button.getComponents() ) {
						component.setForeground(textColor);
						button.add(component);
					}
				}
			}
		});
	}
	
	public int backOneClass(int classCounter) {
		JTextArea activeClass = new JTextArea();
		if(classCounter == 0) {
			classCounter = getClasses().size()-1;
			activeClass = new JTextArea(""+getClasses().get(getClasses().size()-1));

			Logs.LOGGER.info("Active Class Select is " + activeClass.getText());

		} else {
			classCounter = classCounter -1;
			activeClass = new JTextArea(""+getClasses().get(classCounter));
			Logs.LOGGER.info("Active Class Select is " + activeClass.getText());
		}
		return classCounter;
	}
}
