package com.guc.media.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class MainPanel extends JPanel
{
	private JPanel antiResize;
	private JPanel convertPanel;
	private JPanel centerContainer;
	private JPanel centerContainerGrid;
	private JPanel savePanel;
	private PaintedPanel imageBorder;
	private PaintedPanel outputImageBorder;
	private JLabel logo;
	private JButton convert;
	private JButton chooseImage;
	private JButton save;
	private JButton saveValues;
	private JButton histogram;
	private JProgressBar progress;
	private JProgressBar progressSaves;
	private JRadioButton choice8;
	private JRadioButton choice3;
	private ButtonGroup grp;
	
	public MainPanel()
	{
		super(new BorderLayout(10,10));
		preparePanel();
	}
	
	public ButtonGroup getGrp()
	{
		return grp;
	}

	public JButton getHistogram()
	{
		return histogram;
	}

	public JRadioButton getChoice8()
	{
		return choice8;
	}

	public JRadioButton getChoice3()
	{
		return choice3;
	}

	public JPanel getSavePanel()
	{
		return savePanel;
	}

	public JProgressBar getProgressSaves()
	{
		return progressSaves;
	}

	public JProgressBar getProgress()
	{
		return progress;
	}

	public JButton getSaveValues()
	{
		return saveValues;
	}

	public JPanel getAntiResize()
	{
		return antiResize;
	}

	public JPanel getConvertPanel()
	{
		return convertPanel;
	}

	public JPanel getCenterContainer()
	{
		return centerContainer;
	}

	public JPanel getCenterContainerGrid()
	{
		return centerContainerGrid;
	}

	public PaintedPanel getImageBorder()
	{
		return imageBorder;
	}

	public PaintedPanel getOutputImageBorder()
	{
		return outputImageBorder;
	}
	
	public JLabel getLogo()
	{
		return logo;
	}

	public JButton getConvert()
	{
		return convert;
	}

	public JButton getChooseImage()
	{
		return chooseImage;
	}

	public JButton getSave()
	{
		return save;
	}

	private void preparePanel()
	{
		setOpaque(false);
		logo = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("res/MainPanel/logo.png")));
		add(logo, BorderLayout.NORTH);
		centerContainer = new JPanel(new BorderLayout());
		centerContainer.setOpaque(false);
		centerContainerGrid=new JPanel(new GridLayout(1,2));
		centerContainerGrid.setOpaque(false);
		imageBorder=new PaintedPanel();
		imageBorder.setOpaque(false);
		imageBorder.setBorder(new LineBorder(Color.CYAN.darker(), 3, true));
		imageBorder.setPreferredSize(new Dimension(300, 300));
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(imageBorder);
		centerContainerGrid.add(antiResize);
		outputImageBorder=new PaintedPanel();
		outputImageBorder.setOpaque(false);
		outputImageBorder.setBorder(new LineBorder(Color.CYAN.darker(), 3, true));
		outputImageBorder.setPreferredSize(new Dimension(300, 300));
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(outputImageBorder);
		centerContainerGrid.add(antiResize);
		centerContainer.add(centerContainerGrid);
		chooseImage = new JButton("Choose Image");
		chooseImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
		chooseImage.setToolTipText("Choose the image to be converted.");
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.setPreferredSize(new Dimension(0, 40));
		antiResize.add(chooseImage);
		centerContainer.add(antiResize, BorderLayout.NORTH);
		add(centerContainer, BorderLayout.CENTER);
		convert=new JButton("Convert");
		convert.setCursor(new Cursor(Cursor.HAND_CURSOR));
		convert.setEnabled(false);
		progress = new JProgressBar(0, 100);
		progress.setVisible(false);
		convertPanel=new JPanel(new GridLayout(3, 1));
		convertPanel.setOpaque(false);
		convertPanel.setPreferredSize(new Dimension(0, 110));
		choice8 = new JRadioButton("8 bit Grayscale image");
		choice8.setSelected(true);
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(choice8);
		choice3 = new JRadioButton("3 bit Grayscale image");
		grp=new ButtonGroup();
		grp.add(choice3);
		grp.add(choice8);
		antiResize.add(choice3);
		convertPanel.add(antiResize);
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(progress);
		convertPanel.add(antiResize);
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(convert);
		convertPanel.add(antiResize);
		save=new JButton("Save Image");
		save.setEnabled(false);
		save.setCursor(new Cursor(Cursor.HAND_CURSOR));
		save.setToolTipText("Save the converted image to the Hard Disk.");
		saveValues=new JButton("Save Values to txt");
		saveValues.setEnabled(false);
		saveValues.setCursor(new Cursor(Cursor.HAND_CURSOR));
		saveValues.setToolTipText("Save the grayscale image pixel values to a txt file.");
		histogram=new JButton("Histogram");
		histogram.setEnabled(false);
		histogram.setCursor(new Cursor(Cursor.HAND_CURSOR));
		histogram.setToolTipText("Creates a 1D array Histogram for this image.");
		progressSaves = new JProgressBar(0, 100);
		progressSaves.setVisible(false);
		savePanel=new JPanel(new GridLayout(2, 1));
		savePanel.setOpaque(false);
		savePanel.setPreferredSize(new Dimension(0, 110));
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(progressSaves);
		savePanel.add(antiResize);
		antiResize=new JPanel();
		antiResize.setOpaque(false);
		antiResize.add(save);
		antiResize.add(saveValues);
		antiResize.add(histogram);
		savePanel.add(antiResize);
		add(convertPanel, BorderLayout.SOUTH);
	}
	
	public void switchButtons()
	{
		if(convertPanel.isVisible())
		{
			convertPanel.setVisible(false);
			remove(convertPanel);
			add(savePanel, BorderLayout.SOUTH);
		}
		else
		{
			convertPanel.setVisible(true);
			remove(savePanel);
			add(convertPanel, BorderLayout.SOUTH);
		}
		repaint();
		revalidate();
	}
}