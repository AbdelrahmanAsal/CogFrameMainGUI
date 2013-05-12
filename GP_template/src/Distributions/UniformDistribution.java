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
import javax.swing.border.TitledBorder;


public class UniformDistribution extends ProbabilityDistribution{
	public double from, to;
	public JTextArea fromText, toText;
	public JButton setData;
	public JFrame frame;
	public UniformDistribution(){
		from = to = 0;
	}
	
	public void showPanel(){
		frame = new JFrame("Uniform Distribution");
		
		frame.setLayout(new FlowLayout());
		frame.setLocationByPlatform(true);
		
		fromText = new JTextArea(1, 15);
		fromText.setBorder(BorderFactory.createLoweredBevelBorder());
		fromText.setText(from + "");
		
		toText = new JTextArea(1, 15);
		toText.setBorder(BorderFactory.createLoweredBevelBorder());
		toText.setText(to + "");
		setData = new JButton("Set Data");
		setData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				from = Double.parseDouble(fromText.getText());
				to = Double.parseDouble(toText.getText());
				frame.setVisible(false); 
			}
		});
		
		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(2, 1, 5, 5));
		labels.add(new Label("From:"));
		labels.add(new Label("To:"));
		
		JPanel texts = new JPanel();
		texts.setLayout(new GridLayout(2, 1, 5, 5));
		texts.add(fromText);
		texts.add(toText);
		
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
		frame.setSize(250, 135);
	}
	
	public String toString(){
		return String.format("Uniform %.2f %.2f", from, to);
	}
	
	public String type(){
		return "Uniform";
	}
}
