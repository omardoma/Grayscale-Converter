package com.guc.media.view;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame
{
	private MainPanel mainPanel;
	
	public Frame()
	{
		super("Grayscale Converter");
		prepareFrame();
	}

	public MainPanel getMainPanel()
	{
		return mainPanel;
	}
	
	private void prepareFrame()
	{
		setMinimumSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("res/MainPanel/icon.png")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new MainPanel();
		add(mainPanel);
	}
}