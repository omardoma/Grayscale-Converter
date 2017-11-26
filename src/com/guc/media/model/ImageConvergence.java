package com.guc.media.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

public class ImageConvergence
{
	private BufferedImage image;
	private BufferedImage outputImage;
	private String imageName;
	private boolean firstRun;

	public ImageConvergence()
	{
		firstRun = true;
	}

	public boolean isFirstRun()
	{
		return firstRun;
	}

	public String getImageName()
	{
		return imageName;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public BufferedImage getOutputImage()
	{
		return outputImage;
	}

	public void reloadImage(String imageLocation)
	{
		try
		{
			image = ImageIO.read(new File(imageLocation));
			imageName = new File(imageLocation).getName();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void histogram(String path)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
			int[] histogram = new int[256];
			for (int i = 0; i < outputImage.getHeight(); i++)
				for (int j = 0; j < outputImage.getWidth(); j++)
					histogram[new Color(outputImage.getRGB(j, i)).getRed()]++;
			for(int i : histogram)
					writer.write("[" + i + "] ");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void convertToGraylevel()
	{
		outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		firstRun = false;
		for (int i = 0; i < image.getHeight(); i++)
			for (int j = 0; j < image.getWidth(); j++)
			{
				int rgb = image.getRGB(j, i);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				int grayLevel = (r + g + b) / 3;
				int gray = (grayLevel << 16) + (grayLevel << 8) + (grayLevel);
				outputImage.setRGB(j, i, gray);
			}
	}

	public void convertToGraylevel3Bits()
	{
		firstRun = false;
		for (int i = 0; i < outputImage.getHeight(); i++)
			for (int j = 0; j < outputImage.getWidth(); j++)
			{
				Color color = new Color(outputImage.getRGB(j, i));
				int colorNumber = 0;
				if (color.getRed() >= 0 && color.getRed() <= 32)
					colorNumber = 0;
				else
					if (color.getRed() > 32 && color.getRed() <= 64)
						colorNumber = 32;
					else
						if (color.getRed() > 64 && color.getRed() <= 96)
							colorNumber = 64;
						else
							if (color.getRed() >96 && color.getRed() <= 128)
								colorNumber = 96;
							else
								if (color.getRed() > 128 && color.getRed() <= 160)
									colorNumber = 128;
								else
									if (color.getRed() > 160 && color.getRed() <= 192)
										colorNumber = 160;
									else
										if (color.getRed() > 192 && color.getRed() <= 224)
											colorNumber = 192;
										else
											if (color.getRed() > 224 && color.getRed() <= 256)
												colorNumber = 224;
				outputImage.setRGB(j, i, new Color(colorNumber, colorNumber, colorNumber).getRGB());
			}
	}

	public void writeToTxt(String path)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path))));
			for (int i = 0; i < outputImage.getHeight(); i++)
			{
				for (int j = 0; j < outputImage.getWidth(); j++)
				{
					int rgb = outputImage.getRGB(j, i);
					int r = (rgb >> 16) & 0xFF;
					writer.write("[" + r + "]");
				}
				writer.write("\n");
			}
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}