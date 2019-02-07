/*
 * This class is used to print any exceptions that occur throughout the whole vulcan program.
 * Please use this class to only print exceptions ie. errors that occur. Do not use for debugging
 * 
 * 	1.	You use this class by first creating a ProblemLog object with the name of the log file you want:
 * 	1.	public static final ProblemLog troubleLog = new ProblemLog("javaLog");
 * 	2.	Then you use the ProblemLog object to send messages to the log file
 * 	2.	troubleLog.logger.info("This is a sample error message");
 * NOTES:
 * -The log file will be outputted to the home directory-- on mac click Finder then All My Files
 * --you can change the log file location by appending a path to the creation of the FileHandler
 * ---EX: fileHandler = new FileHandler("/home/escolab/ChoatePondSensor/"+logFileName+".log",true);
 * ----This will place the log file in the ChoatePondSensor Directory
 * -You can replace "info" with "severe" if you want to indicate a critical error
 * -I've tried to use fine,finer,finest but they won't output to the log file 
 * -The logger was designed to append the latest data to a existing log file if it's there.
 * --if the log file is not there then it will automatically create it.
 */



package parsing_csv;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.common.base.Throwables;

public class ProblemLog{

	public static Logger logger;
	private FileHandler fileHandler;
	private String logFileName;
	private SimpleFormatter formatter;
	
	public ProblemLog(String fileName) {
		this.logger = Logger.getLogger("vulcan.problem.logger"); //get system wide logger
		this.logFileName = fileName; //get fileName that was passed
		this.formatter = new SimpleFormatter(); //add extra details of where message was made
		try {
			fileHandler = new FileHandler(logFileName+".log",true); //"/home/escolab/ChoatePondSensor/"+
			logger.addHandler(fileHandler); //handle logger messages
			fileHandler.setFormatter(formatter); //format log records
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
//	 public void info(Exception e, Logger log) {
//		log.info(Throwables.getStackTraceAsString(e));
//	}
//
//	 public void info(String s) {
//		 this.logger.info(s);
//	 }
//	

	
	
	
}
