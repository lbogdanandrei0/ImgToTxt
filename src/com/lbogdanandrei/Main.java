package com.lbogdanandrei;

import java.io.File;

import javax.swing.JOptionPane;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Main {

	public static void main(String[] args) {
		int nrOfRetry;
		int nrOfConvertedFiles=0;
		boolean isCompleted;
		
		File sourceDir = new File("Input");
		if(!sourceDir.exists() || !sourceDir.isDirectory())
		{
			JOptionPane.showMessageDialog(null,
				    "Input folder not found",
				    "Input error",
				    JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		File resultDir = new File("Output");
		File[] toConvert = sourceDir.listFiles();
		if(toConvert.length == 0)
		{
			JOptionPane.showMessageDialog(null,
				    "Input folder is empty",
				    "Input error",
				    JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.onlineocr.net/");
		
		Actions action = new Actions(driver);
		
		for(File f:toConvert)
		{
			if(f.getName().toUpperCase().endsWith(".PNG") || f.getName().toUpperCase().endsWith(".JPG"))
			{
				System.out.println(f.getAbsolutePath());
				Select outputType = new Select(driver.findElement(By.id("MainContent_comboOutput")));
				outputType.selectByIndex(outputType.getOptions().size()-1);
				driver.findElement(By.id("fileupload")).sendKeys(f.getAbsolutePath());
				action.moveToElement(driver.findElement(By.id("MainContent_btnOCRConvert"))).click();
				action.perform();
				String s = null;
				isCompleted = false;
				nrOfRetry = 0;
				do {
					try {
						nrOfRetry++;
						Thread.sleep(1000);
						s = driver.findElement(By.id("MainContent_txtOCRResultText")).getText();
						isCompleted = true;
						System.out.println(nrOfRetry);
					}catch(Exception e)
					{
						isCompleted = false;
						if(nrOfRetry >= 2)
						{
							action.moveToElement(driver.findElement(By.id("MainContent_btnOCRConvert"))).click();
							action.perform();
						}
					}
				}while(!isCompleted);
				
				FileExport fe = new FileExport(s, resultDir.getName(), f.getName()+".txt");
				fe.start();
				nrOfConvertedFiles++;
				
				driver.get("https://www.onlineocr.net/");
			}
		}
		driver.close();
		JOptionPane.showMessageDialog(null,
			    "Converted " + nrOfConvertedFiles + " files",
			    "Completed",
			    JOptionPane.INFORMATION_MESSAGE);
	}

}
