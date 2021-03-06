package AttributePanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import All.DrawingPanel;


public class VisualizeAttributePanel extends JPanel{
	public ButtonGroup visualiztionOptions;
	public ArrayList<JRadioButton> buttons;
	public long currentSimulationTime;
	public DrawingPanel drawingP;
	public Player player;
	public final JSlider ticker;
	public long playerStepSize = 1;
	public JTable packetsColorTable;
	public PacketsColorTableModel packetsColorModel;
	public VisualizeAttributePanel(DrawingPanel drawingPanel){
		this.drawingP = drawingPanel;
		player = new Player();
		player.start();

		buttons = new ArrayList<JRadioButton>();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Visualization Attributes Panel"));

		JRadioButton lossRatio = new JRadioButton("Loss Ratio");
		lossRatio.setActionCommand("LossRatio");
		buttons.add(lossRatio);
//		JRadioButton throughput = new JRadioButton("Throughput");
//		throughput.setActionCommand("Throughput");
//		buttons.add(throughput);
		JRadioButton nodalDelay = new JRadioButton("Nodal Delay");
		nodalDelay.setActionCommand("NodalDelay");
		buttons.add(nodalDelay);
		JRadioButton linkDelay = new JRadioButton("Link Delay");
		linkDelay.setActionCommand("LinkDelay");
		buttons.add(linkDelay);

		JRadioButton numOfSwitches = new JRadioButton("No. of switches");
		numOfSwitches.setActionCommand("NumOfSwitches");
		buttons.add(numOfSwitches);

		JButton backward = new JButton("<<");
		backward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentSimulationTime -= playerStepSize;
				currentSimulationTime = Math.max(0, currentSimulationTime);
				ticker.setValue((int)currentSimulationTime);
				System.out.println("Current Simulation time: " + currentSimulationTime);
			}
		});

		JButton run = new JButton("Run");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player.finished = false;
			}
		});

		JButton pause = new JButton("Pause");
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player.finished = true;
			}
		});

		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player.finished = true;
				currentSimulationTime = 0;
				ticker.setValue(0);
			}
		});


		JButton forward = new JButton(">>");
		forward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentSimulationTime += playerStepSize;
				currentSimulationTime = Math.min(drawingP.maxRange, currentSimulationTime);
				ticker.setValue((int)currentSimulationTime);
			}
		});

		JLabel stepLabel = new JLabel("Step:");
		final JTextField step = new JTextField("1");
		step.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			    	if(step.getText().length() > 0)
			    		playerStepSize = Integer.parseInt(step.getText().trim());
		    	}
		    }

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if(step.getText().length() > 0)
			    		playerStepSize = Integer.parseInt(step.getText().trim());
				} catch (Exception ex) {
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {}
		});

		JLabel seekLabel = new JLabel("Seek:");
		final JTextField seek = new JTextField("");
//		seek.setPreferredSize(new Dimension(getWidth()/3, 30));
		seek.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (seek.getText().length() > 0) {
						currentSimulationTime = Integer.parseInt(seek.getText()
								.trim());
						ticker.setValue((int) currentSimulationTime);
					}
				}
		    }

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if(seek.getText().length() > 0) {
			    		currentSimulationTime = Integer.parseInt(seek.getText().trim());
			    		ticker.setValue((int)currentSimulationTime);
			    	}
				} catch(Exception ex) {
					
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {}
		});

		ticker = new JSlider(JSlider.HORIZONTAL, 0, 0, 0);
		ticker.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		ticker.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int time = (int) ticker.getValue();
				currentSimulationTime = time;
				drawingP.repaint();
			}
		});
		
		JLabel expPacketsLabel = new JLabel("Experiment packets:");

		packetsColorModel = new PacketsColorTableModel();
		packetsColorTable = new JTable();
		packetsColorTable.setModel(packetsColorModel);
//		packetsColorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		packetsColorTable.setBorder(BorderFactory.createLoweredBevelBorder());
//		int[] colWidth = {235, 85};
		int[] colWidth = {215, 85};
		for (int i = 0; i < 2; i++) {
			TableColumn tcol = packetsColorTable.getColumnModel().getColumn(i);
			tcol.setPreferredWidth(colWidth[i]);
			tcol.setCellRenderer(new PacketsColorTableCellRenderer());
		}

		JScrollPane ps = new JScrollPane(packetsColorTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		ps.setPreferredSize(new Dimension(200,250));
		ps.setPreferredSize(new Dimension(getWidth(), 350));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);

		visualiztionOptions = new ButtonGroup();
		for(JRadioButton button: buttons)
			visualiztionOptions.add(button);

		lossRatio.setSelected(true);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(lossRatio)
//					.addComponent(throughput)
					.addComponent(nodalDelay)
					.addComponent(linkDelay)
					.addComponent(numOfSwitches)
					.addGroup(layout.createSequentialGroup().addComponent(backward).addComponent(run).addComponent(pause).addComponent(stop).addComponent(forward))
					.addComponent(ticker)
					.addGroup(layout.createSequentialGroup().addComponent(stepLabel).addComponent(step).addComponent(seekLabel).addComponent(seek))
					.addComponent(expPacketsLabel)
					.addComponent(ps)
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(lossRatio)
//					.addComponent(throughput)
					.addComponent(nodalDelay)
					.addComponent(linkDelay)
					.addComponent(numOfSwitches)
					.addGroup(layout.createParallelGroup().addComponent(backward).addComponent(run).addComponent(pause).addComponent(stop).addComponent(forward))
					.addComponent(ticker)
					.addGroup(layout.createParallelGroup().addComponent(stepLabel).addComponent(step).addComponent(seekLabel).addComponent(seek))
					.addComponent(expPacketsLabel)
					.addComponent(ps)
		);
	}

	class Player extends Thread{
		public boolean finished;

		public Player() {
			finished = true;
		}
		@Override
		public void run() {
			while(true) {
				if(!finished){
					currentSimulationTime += playerStepSize;
					currentSimulationTime = Math.min(drawingP.maxRange, currentSimulationTime);
					if (currentSimulationTime == drawingP.maxRange)
						finished = true;
					ticker.setValue((int)currentSimulationTime);
					drawingP.repaint();
				}
				try {
					this.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

