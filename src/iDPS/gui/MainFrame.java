package iDPS.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import iDPS.Application;
import iDPS.gear.Gem;
import iDPS.gear.Armor;
import iDPS.gui.menu.MenuBar;
import iDPS.gui.sidepanel.SidePanel;

public class MainFrame extends JFrame {
		
	private Application app;
	private MenuBar menuBar;
	private ImportProfileDialog importFrame = null;
	private CenterPanel centerP;
	private InventoryButton[] buttons;
	private SidePanel sidePanel;
	
	public MainFrame(Application app) {
		super("iDPS");
		if (app.getSetup() != null && app.getSetup().getName() != null)
			setTitle("iDPS - "+app.getSetup().getName());	
	
		this.app = app;
		
		menuBar = new MenuBar(this);
		setJMenuBar(menuBar);
		
		buttons = new InventoryButton[19];
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		
		JPanel invPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		
		c.insets = new Insets(0,0,0,0);
		
		// Inv 0-7
		for (int i=0;i<=21;i+=3) {
			buttons[i/3] = new InventoryButton(this, i/3);
			c.gridx = 0; c.gridy = i; c.gridheight = 3;
			invPane.add(buttons[i/3],c);
			c.gridx = 1; c.gridheight = 1;
			invPane.add(buttons[i/3].getSocketButton(0),c);
			c.gridy = i+1;
			invPane.add(buttons[i/3].getSocketButton(1),c);
			c.gridy = i+2;
			invPane.add(buttons[i/3].getSocketButton(2),c);
		}
		
		centerP = new CenterPanel(this);
		
		c.gridx = 2; c.gridy = 0; c.gridwidth = 9; c.gridheight = 20;
		c.insets = new Insets(0,0,0,0);
		c.anchor = GridBagConstraints.CENTER;
		invPane.add(centerP, c);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridwidth = 1;
		c.gridheight = 1;
		
		// Inv 8-15
		for (int i=0;i<=21;i+=3) {
			buttons[i/3+8] = new InventoryButton(this, i/3+8);
			c.gridx = 12; c.gridy = i; c.gridwidth = 1; c.gridheight = 3;
			invPane.add(buttons[i/3+8],c);
			c.gridx = 11; c.gridheight = 1;
			invPane.add(buttons[i/3+8].getSocketButton(0),c);
			c.gridy = i+1;
			invPane.add(buttons[i/3+8].getSocketButton(1),c);
			c.gridy = i+2;
			invPane.add(buttons[i/3+8].getSocketButton(2),c);
		}
		
		// Inv 16-18
		for (int i=0;i<=6;i+=3) {
			if (i == 0)
				c.insets = new Insets(0,40,0,0);
			else if (i == 6)
				c.insets = new Insets(0,0,0,40);
			else
				c.insets = new Insets(0,0,0,10);
			buttons[i/3+16] = new InventoryButton(this, i/3+16);
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.gridy = 21; c.gridx = 2+i; c.gridwidth = 3; c.gridheight = 3;
			invPane.add(buttons[i/3+16],c);
			if (i == 0)
				c.insets = new Insets(0,40,0,0);
			else
				c.insets = new Insets(0,0,0,0);
			c.anchor = GridBagConstraints.LAST_LINE_START;
			c.gridy = 20; c.gridx = 2+i; c.gridwidth = 1; c.gridheight = 1;
			invPane.add(buttons[i/3+16].getSocketButton(0),c);
			c.gridx = 2+i+1; c.insets = new Insets(0,0,0,0);
			invPane.add(buttons[i/3+16].getSocketButton(1),c);
			c.gridx = 2+i+2;
			invPane.add(buttons[i/3+16].getSocketButton(2),c);

		}
		Border b1 = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border b2 = new EmptyBorder(new Insets(0,0,0,0));
		Border b3 = new CompoundBorder(b1,b2);
		invPane.setBorder(b3);
		invPane.setPreferredSize(new Dimension(440,490));
		add(invPane, BorderLayout.CENTER);
		
		sidePanel = new SidePanel(this, app.getCycleController());
		add(sidePanel, BorderLayout.LINE_END);
		
		pack();
		
	    Dimension d1 = getToolkit().getScreenSize();
	    Dimension d2 = getSize();
	    setLocation((int)((d1.width-d2.width)/3),(int)((d1.height-d2.height)/3));
		
		ActionListener escListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSidePanel();
			}
	    };

	    getRootPane().registerKeyboardAction(escListener,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
	    
		setResizable(false);
		setVisible(true);
	}
	
	public void refreshItem(int slot) {
		Armor item = app.getSetup().getGear().getItem(slot);
		Gem[] gems = app.getSetup().getGear().getGems(slot);
		buttons[slot].changeToItem(item, gems);
	}
	
	public void showItem(Armor item, int slot) {
		buttons[slot].changeToItem(item);
		centerP.showStats();
		pack();
	}
	
	public void showGem(Gem gem, int slot, int index) {
		buttons[slot].getSocketButton(index).socketGem(gem);
		centerP.showStats();
	}
	
	public void showGear() {
		for (int i=0; i<=18; i++)
			buttons[i].changeToItem(app.getSetup().getGear().getItem(i), app.getSetup().getGear().getGems(i));
		centerP.showStats();
	}
	
	public void showStats() {
		centerP.showStats();
	}

	public SidePanel getSideScroll() {
		return sidePanel;
	}
	
	public void showSidePanel() {
		sidePanel.showDefaultPanel();
	}
	
	
	public MenuBar getMyMenuBar() {
		return menuBar;
	}
	
	public void createAndShowImportFrame() {
		if (importFrame == null)
			importFrame = new ImportProfileDialog(this);
		importFrame.setVisible(true);
	}

	public Application getApp() {
		return app;
	}
	
	public void dispose() {
		app.exit();
	}

}
