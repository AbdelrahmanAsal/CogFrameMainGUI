package Distributions;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class ExponentialDistribution extends ProbabilityDistribution{
	public double lamda;
	public JTextArea lamdaText;
	public JButton setData;
	public JFrame frame;
	public ExponentialDistribution (){
		lamda = 0;
	}
	
	public void showPanel(){
		frame = new JFrame("Exponential Distribution");
		
		frame.setLayout(new FlowLayout());
		frame.setLocationByPlatform(true);

		lamdaText = new JTextArea(1, 15);
		lamdaText.setBorder(BorderFactory.createLoweredBevelBorder());
		lamdaText.setText(lamda + "");
		
		setData = new JButton("Set Data");
		setData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lamda = Double.parseDouble(lamdaText.getText());
				frame.setVisible(false); 
			}
		});
		
		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(1, 1, 5, 5));
		labels.add(new Label("Lamda:"));
		
		JPanel texts = new JPanel();
		texts.setLayout(new GridLayout(1, 1, 5, 5));
		texts.add(lamdaText);
		
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		controls.add(setData);
		
		JPanel gui = new JPanel();
		gui.setLayout(new BorderLayout(3, 3));
		gui.add(labels, BorderLayout.WEST);
		gui.add(texts, BorderLayout.EAST);
		gui.add(controls, BorderLayout.SOUTH);
		
		frame.add(gui);
		
		frame.setVisible(true);
		frame.setSize(250, 105);
	}
	
	public String toString(){
		return String.format("Exponential %.2f", lamda);
	}
	
	public String type(){
		return "Exponential";
	}
}
