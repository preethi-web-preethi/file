import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.*;
import java.nio.file.Paths;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class findkey {
	static PrintStream ps =null;
	static PrintStream errorFile = null;
	public static int noOfKeywordOccureance = 0;
	public static int linenumber = 0;
	public static String[] linearr = {};

	public static  int[] linenumarr={};

	public static  int[] arrocc={};
	public static void main(String[] args) throws Exception {
		
		String userHome = System.getProperty("user.home");
		File homeDirectory = new File(userHome);
		String destPath = homeDirectory.toString() + File.separator + "FindKeyWordsFromFiles" ;
		new File(destPath).mkdirs();
		errorFile =new PrintStream(destPath+"\\ErrorList.txt");
		if(args.length == 1) {
			
			try {
			String[] ArguValue = args[0].toString().split(",");
			String inputKeywordFile = ArguValue[0];
			String inputFile = ArguValue[1];
			String outputFileDir = ArguValue[2];
			 File f = new File(inputFile); 
	         String[] files = f.list(); 
			 ps =  new PrintStream(outputFileDir+"\\Output.txt");
			    ps.print("INPUT KEYWORD FILE LOCATION : "+inputKeywordFile);
				 ps.print("\nINPUT FILE  LOCATION : "+inputFile);
				 ps.print("\nOUTPUT FILE LOCATION : "+outputFileDir+"\n");
				
				 try (Stream<String> stream = Files.lines(Paths.get(inputKeywordFile))) {
						stream.forEach(keyword ->{
							 
							ps.print("\nKEYWORD NAME : "+keyword+"\n");
							ps.print("****************"+keyword +" START ****************\n");
							try {
								
								Files.walk(Paths.get(inputFile)).filter(Files :: isRegularFile).forEach(file ->{
													
									noOfKeywordOccureance = 0;
									linenumber=0;
									linenumarr=new int[0];
							        linearr=new String[0];
									
									
							        try (Stream<String> inputFilestream = Files.lines(file)) {
										 inputFilestream.forEach(line ->{
												if(line.contains(keyword))
													noOfKeywordOccureance++;
												
											});
										 final Map<String, Integer> map = new HashMap<>();
										 
									        Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");

									        try (BufferedReader reader = Files.newBufferedReader(file)) {
									            String line = null;
									            while ((line = reader.readLine()) != null) {
									                linenumber++;
									                if (pattern.matcher(line).find()) {
									                    map.put(line, linenumber);
												        linenumarr=ArrayUtils.add(linenumarr, linenumber);
												        linearr=ArrayUtils.add(linearr, line);

									                }
									            }
									        } catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										 
										} 
									 catch (IOException e) {
											errorFile.print(e.getMessage());
											e.printStackTrace();
										}
									
									 arrocc=ArrayUtils.add(arrocc, noOfKeywordOccureance);
									 
									 if(noOfKeywordOccureance != 0){
										 ps.print(file.toString()+" - "+keyword+" OCCURS "+
												 noOfKeywordOccureance+" times\n");
										System.out.println(file.toString()+" - "+keyword+" OCCURS "+
										 noOfKeywordOccureance+" times");
										for(int i=0;i<linenumarr.length;i++){
										System.out.println(file.toString()+" -  LINE  "+linenumarr[i]+" - "+linearr[i]);
										ps.print(file.toString()+" -  LINE  "+linenumarr[i]+" - "+linearr[i]+"\n");
										}
										ps.print("\n");
										System.out.println();
										      }	
								}

								); 
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							ps.print("****************"+keyword +" END ******************\n");
							System.out.println("---------------------");
							
							 XSSFWorkbook workbook = new XSSFWorkbook(); 
						        XSSFSheet sheet = workbook.createSheet("Employee Data");
						          
						        Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
						        data.put(1, new Object[] {"FILE NAME", keyword});
						       for(int i=0;i<arrocc.length;i++){
						        data.put(2+i, new Object[] {files[i], arrocc[i]});
						       }
						        Set<Integer> keyset = data.keySet();
						        int rownum = 0;
						        for (Integer key : keyset)
						        {
						            Row row = sheet.createRow(rownum++);
						            Object [] objArr = data.get(key);
						            int cellnum = 0;
						            for (Object obj : objArr)
						            {
						               Cell cell = row.createCell(cellnum++);
						               if(obj instanceof String)
						                    cell.setCellValue((String)obj);
						                else if(obj instanceof Integer)
						                    cell.setCellValue((Integer)obj);
						            }
						        }
						        try
						        {
						           
						            FileOutputStream out = new FileOutputStream(new File(outputFileDir+"\\exl5.xls"));
						            workbook.write(out);
						            out.close();
						           
						        } 
						        catch (Exception e) 
						        {
						            e.printStackTrace();
						        }
								
						});

					} catch (IOException e) {
						errorFile.print(e.getMessage());
						e.printStackTrace();
					}

				}catch (Exception e) {
					errorFile.print(e.getMessage());
				}
			}else {
				errorFile.print("Arugument Mismatch Error");
				throw new Exception();
			}
		    
		}
	private static Integer[] append(Integer[] values, int noOfKeywordOccureance2) {
		// TODO Auto-generated method stub
		return null;
	}

	}
