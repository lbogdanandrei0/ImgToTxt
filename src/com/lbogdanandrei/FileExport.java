package com.lbogdanandrei;

import java.io.File;
import java.io.FileOutputStream;

public class FileExport extends Thread {
	
	private String s;
	private String resultDir;
	private String fileName;
	
	public FileExport(String data, String outputDir, String fileName)
	{
		super();
		this.s = data;
		this.resultDir = outputDir;
		this.fileName = fileName;
	}
	
	@Override
	public void run()
	{
		s = s.replaceAll(":", ":\n");
		s = s.replaceAll("elif", "elof");
		s = s.replaceAll("if", "\nif");
		s = s.replaceAll("elof", "elif");
		s = s.replaceAll("while", "\nwhile");
		s = s.replaceAll("for", "\nfor");
		s = s.replaceAll("\\)", "\\)\n");
		
		System.out.println(s.toString());
		try {
			File toOut = new File(resultDir+"//"+fileName);
			toOut.createNewFile();
			FileOutputStream out = new FileOutputStream(toOut);
			out.write(s.toString().getBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
