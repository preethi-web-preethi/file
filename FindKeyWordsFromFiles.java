import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FindKeyWordsFromFiles {
	static PrintStream ps =null;
	static PrintStream errorFile = null;
	public static int noOfKeywordOccureance = 0;

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
			
			 ps =  new PrintStream(outputFileDir+"\\Output.txt");
			 
			 ps.print("INPUT KEYWORD FILE LOCATION : "+inputKeywordFile);
			 ps.print("\nINPUT FILE  LOCATION : "+inputFile);
			 ps.print("\nOUTPUT FILE LOCATION : "+outputFileDir+"\n");
			 
			 try (Stream<String> stream = Files.lines(Paths.get(inputKeywordFile))) {

					stream.forEach(keyword ->{
						ps.print("****************"+keyword +" START ****************\n");
						try {
							Files.walk(Paths.get(inputFile)).filter(Files :: isRegularFile).forEach(file ->{
								
								noOfKeywordOccureance = 0;
								
								 try (Stream<String> inputFilestream = Files.lines(file)) {

									 inputFilestream.forEach(line ->{
											if(line.contains(keyword))
												noOfKeywordOccureance++;
											
										});

									} catch (IOException e) {
										errorFile.print(e.getMessage());
										e.printStackTrace();
									}
								 if(noOfKeywordOccureance != 0){
									 ps.print(file.toString() +" -  "+noOfKeywordOccureance +" time occured.\n" );
									 System.out.println(file.toString() +" -  "+noOfKeywordOccureance +" time occured." );}
							});
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						 
						ps.print("****************"+keyword +" END ******************\n");
						
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

}
