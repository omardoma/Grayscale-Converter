package com.guc.media.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.guc.media.model.ImageConvergence;
import com.guc.media.view.Frame;
import com.guc.media.view.ImagePreview;

import de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel;

public class Controller implements ActionListener
{
	private ImageConvergence program;
	private Frame gui;
	private final JFileChooser chooser;
	private final JFileChooser saver;
	private final JFileChooser saverTxt;

	public Controller()
	{
		chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("JPG, PNG & GIF Images", "jpg", "gif", "png"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle("Choose the Image");
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setAccessory(new ImagePreview(chooser));
		saver = new JFileChooser();
		saver.setAcceptAllFileFilterUsed(false);
		saver.addChoosableFileFilter(new FileNameExtensionFilter("JPG", "jpg"));
		saver.addChoosableFileFilter(new FileNameExtensionFilter("PNG", "png"));
		saver.addChoosableFileFilter(new FileNameExtensionFilter("GIF", "gif"));
		saver.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saver.setDialogTitle("Choose Your Saving Directory");
		saver.setMultiSelectionEnabled(false);
		saverTxt = new JFileChooser();
		saverTxt.setAcceptAllFileFilterUsed(false);
		saverTxt.setFileFilter(new FileNameExtensionFilter("Text File (.txt)", "txt"));
		saverTxt.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saverTxt.setDialogTitle("Choose Your Saving Directory");
		saverTxt.setMultiSelectionEnabled(false);
		program = new ImageConvergence();
		gui = new Frame();
		addListeners();
		gui.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == gui.getMainPanel().getChooseImage())
		{
			int returnVal = chooser.showDialog(gui, "Choose");
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				if (!program.isFirstRun() && gui.getMainPanel().getOutputImageBorder().getImage() != null)
				{
					gui.getMainPanel().getSave().setEnabled(false);
					gui.getMainPanel().getSaveValues().setEnabled(false);
					gui.getMainPanel().getChoice8().setSelected(true);
					gui.getMainPanel().switchButtons();
				}
				gui.getMainPanel().getOutputImageBorder().setImage(null);
				gui.getMainPanel().getOutputImageBorder().repaint();
				gui.getMainPanel().getOutputImageBorder().revalidate();
				program.reloadImage(chooser.getSelectedFile().getPath());
				gui.getMainPanel().getImageBorder().setImage(program.getImage());
				gui.getMainPanel().getImageBorder().repaint();
				gui.getMainPanel().getImageBorder().revalidate();
				gui.getMainPanel().getConvert().setEnabled(true);
			}
		}
		else
			if (e.getSource() == gui.getMainPanel().getConvert())
			{
				gui.getMainPanel().getConvert().setEnabled(false);
				gui.getMainPanel().getChooseImage().setEnabled(false);
				gui.getMainPanel().getProgress().setVisible(true);
				new Thread(new Runnable()
				{
					public void run()
					{
						while (gui.getMainPanel().getProgress()
								.getValue() < gui.getMainPanel().getProgress().getMaximum() / 2)
						{
							gui.getMainPanel().getProgress().setValue(gui.getMainPanel().getProgress().getValue() + 10);
							try
							{
								Thread.sleep(50);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						program.convertToGraylevel();
						if (gui.getMainPanel().getChoice3().isSelected())
							program.convertToGraylevel3Bits();

						while (gui.getMainPanel().getProgress().getValue() < gui.getMainPanel().getProgress()
								.getMaximum())
						{
							gui.getMainPanel().getProgress().setValue(gui.getMainPanel().getProgress().getValue() + 10);
							try
							{
								Thread.sleep(50);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						gui.getMainPanel().getOutputImageBorder().setImage(program.getOutputImage());
						gui.getMainPanel().getOutputImageBorder().repaint();
						gui.getMainPanel().getProgress().setValue(0);
						gui.getMainPanel().getProgress().setVisible(false);
						gui.getMainPanel().getSave().setEnabled(true);
						gui.getMainPanel().getSaveValues().setEnabled(true);
						gui.getMainPanel().getChooseImage().setEnabled(true);
						gui.getMainPanel().getHistogram().setEnabled(true);
						gui.getMainPanel().switchButtons();
					}
				}).start();

			}
			else
				if (e.getSource() == gui.getMainPanel().getSave())
				{
					saver.setSelectedFile(new File("Grayscale_" + removeExtension(program.getImageName())));
					int returnVal = saver.showSaveDialog(gui);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						gui.getMainPanel().getSave().setEnabled(false);
						gui.getMainPanel().getSaveValues().setEnabled(false);
						gui.getMainPanel().getChooseImage().setEnabled(false);
						gui.getMainPanel().getHistogram().setEnabled(false);
						new Thread(new Runnable()
						{
							public void run()
							{
								try
								{
									if (new File(saver.getSelectedFile().getPath() + "."
											+ saver.getFileFilter().getDescription().toLowerCase()).exists())
									{
										int n = JOptionPane.showConfirmDialog(gui,
												"Do you really want to overwrite the file "
														+ saver.getSelectedFile().getName() + "?",
												"Overwrite File ?", JOptionPane.WARNING_MESSAGE,
												JOptionPane.YES_NO_OPTION);
										if (n != JOptionPane.YES_OPTION)
										{
											gui.getMainPanel().getSave().setEnabled(true);
											gui.getMainPanel().getSaveValues().setEnabled(true);
											gui.getMainPanel().getChooseImage().setEnabled(true);
											gui.getMainPanel().getHistogram().setEnabled(true);
											return;
										}
										saver.getSelectedFile().delete();
									}
									gui.getMainPanel().getProgressSaves().setVisible(true);
									while (gui.getMainPanel().getProgressSaves()
											.getValue() < gui.getMainPanel().getProgressSaves().getMaximum() / 2)
									{
										gui.getMainPanel().getProgressSaves()
												.setValue(gui.getMainPanel().getProgressSaves().getValue() + 10);
										try
										{
											Thread.sleep(50);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									ImageIO.write(program.getOutputImage(),
											saver.getFileFilter().getDescription().toLowerCase(),
											new File(saver.getSelectedFile().getPath() + "."
													+ saver.getFileFilter().getDescription().toLowerCase()));
									while (gui.getMainPanel().getProgressSaves().getValue() < gui.getMainPanel()
											.getProgressSaves().getMaximum())
									{
										gui.getMainPanel().getProgressSaves()
												.setValue(gui.getMainPanel().getProgressSaves().getValue() + 10);
										try
										{
											Thread.sleep(50);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									gui.getMainPanel().getProgressSaves().setValue(0);
									gui.getMainPanel().getProgressSaves().setVisible(false);
									gui.getMainPanel().getSave().setEnabled(true);
									gui.getMainPanel().getSaveValues().setEnabled(true);
									gui.getMainPanel().getChooseImage().setEnabled(true);
									gui.getMainPanel().getHistogram().setEnabled(true);
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
							}
						}).start();
					}
				}
				else
					if (e.getSource() == gui.getMainPanel().getSaveValues())
					{
						saverTxt.setSelectedFile(
								new File("Grayscale_" + removeExtension(program.getImageName()) + "_Values.txt"));
						int returnVal = saverTxt.showSaveDialog(gui);
						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							gui.getMainPanel().getSave().setEnabled(false);
							gui.getMainPanel().getSaveValues().setEnabled(false);
							gui.getMainPanel().getChooseImage().setEnabled(false);
							gui.getMainPanel().getHistogram().setEnabled(false);
							new Thread(new Runnable()
							{
								public void run()
								{
									if (new File(saverTxt.getSelectedFile().getPath()).exists())
									{
										int n = JOptionPane.showConfirmDialog(gui,
												"Do you really want to overwrite the file "
														+ saverTxt.getSelectedFile().getName() + "?",
												"Overwrite File ?", JOptionPane.WARNING_MESSAGE,
												JOptionPane.YES_NO_OPTION);
										if (n != JOptionPane.YES_OPTION)
										{
											gui.getMainPanel().getSave().setEnabled(true);
											gui.getMainPanel().getSaveValues().setEnabled(true);
											gui.getMainPanel().getChooseImage().setEnabled(true);
											gui.getMainPanel().getHistogram().setEnabled(true);
											return;
										}
										saverTxt.getSelectedFile().delete();
									}
									gui.getMainPanel().getProgressSaves().setVisible(true);
									while (gui.getMainPanel().getProgressSaves()
											.getValue() < gui.getMainPanel().getProgressSaves().getMaximum() / 2)
									{
										gui.getMainPanel().getProgressSaves()
												.setValue(gui.getMainPanel().getProgressSaves().getValue() + 10);
										try
										{
											Thread.sleep(50);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									program.writeToTxt(saverTxt.getSelectedFile().getPath());
									while (gui.getMainPanel().getProgressSaves().getValue() < gui.getMainPanel()
											.getProgressSaves().getMaximum())
									{
										gui.getMainPanel().getProgressSaves()
												.setValue(gui.getMainPanel().getProgressSaves().getValue() + 10);
										try
										{
											Thread.sleep(50);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									gui.getMainPanel().getProgressSaves().setValue(0);
									gui.getMainPanel().getProgressSaves().setVisible(false);
									gui.getMainPanel().getSave().setEnabled(true);
									gui.getMainPanel().getSaveValues().setEnabled(true);
									gui.getMainPanel().getChooseImage().setEnabled(true);
									gui.getMainPanel().getHistogram().setEnabled(true);
								}
							}).start();
						}
					}
					else
					{
						saverTxt.setSelectedFile(
								new File("Grayscale_" + removeExtension(program.getImageName()) + "_Histogram.txt"));
						int returnVal = saverTxt.showSaveDialog(gui);
						if (returnVal == JFileChooser.APPROVE_OPTION)
						{
							gui.getMainPanel().getSave().setEnabled(false);
							gui.getMainPanel().getSaveValues().setEnabled(false);
							gui.getMainPanel().getChooseImage().setEnabled(false);
							gui.getMainPanel().getHistogram().setEnabled(false);
							new Thread(new Runnable()
							{
								public void run()
								{
									if (new File(saverTxt.getSelectedFile().getPath()).exists())
									{
										int n = JOptionPane.showConfirmDialog(gui,
												"Do you really want to overwrite the file "
														+ saverTxt.getSelectedFile().getName() + "?",
												"Overwrite File ?", JOptionPane.WARNING_MESSAGE,
												JOptionPane.YES_NO_OPTION);
										if (n != JOptionPane.YES_OPTION)
										{
											gui.getMainPanel().getSave().setEnabled(true);
											gui.getMainPanel().getSaveValues().setEnabled(true);
											gui.getMainPanel().getChooseImage().setEnabled(true);
											gui.getMainPanel().getHistogram().setEnabled(true);
											return;
										}
										saverTxt.getSelectedFile().delete();
									}
									gui.getMainPanel().getProgressSaves().setVisible(true);
									while (gui.getMainPanel().getProgressSaves()
											.getValue() < gui.getMainPanel().getProgressSaves().getMaximum() / 2)
									{
										gui.getMainPanel().getProgressSaves()
												.setValue(gui.getMainPanel().getProgressSaves().getValue() + 10);
										try
										{
											Thread.sleep(50);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									program.histogram(saverTxt.getSelectedFile().getPath());
									while (gui.getMainPanel().getProgressSaves().getValue() < gui.getMainPanel()
											.getProgressSaves().getMaximum())
									{
										gui.getMainPanel().getProgressSaves()
												.setValue(gui.getMainPanel().getProgressSaves().getValue() + 10);
										try
										{
											Thread.sleep(50);
										}
										catch (InterruptedException e)
										{
											e.printStackTrace();
										}
									}
									gui.getMainPanel().getProgressSaves().setValue(0);
									gui.getMainPanel().getProgressSaves().setVisible(false);
									gui.getMainPanel().getSave().setEnabled(true);
									gui.getMainPanel().getSaveValues().setEnabled(true);
									gui.getMainPanel().getChooseImage().setEnabled(true);
									gui.getMainPanel().getHistogram().setEnabled(true);
								}
							}).start();
						}
					}
	}

	private void addListeners()
	{
		gui.getMainPanel().getChooseImage().addActionListener(this);
		gui.getMainPanel().getSave().addActionListener(this);
		gui.getMainPanel().getConvert().addActionListener(this);
		gui.getMainPanel().getSaveValues().addActionListener(this);
		gui.getMainPanel().getHistogram().addActionListener(this);
	}

	private String removeExtension(String s)
	{
		String ex = s.substring(s.length() - 4);
		if (s.contains(".jpg") || s.contains(".png") || s.contains(".gif"))
			s = s.replaceAll(ex, "");
		return s;
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(new SyntheticaWhiteVisionLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException | ParseException e)
		{
			e.printStackTrace();
		}
		new Controller();
	}
}