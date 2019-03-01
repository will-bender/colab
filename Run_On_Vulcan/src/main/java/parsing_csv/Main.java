package parsing_csv;
/*
 * NAME OF FILE: WNF-S007.csv
 * NAME OF PROGRAM LOG: choateLOG.Log
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.base.Throwables;
import com.opencsv.bean.CsvToBeanBuilder;

 public class Main {
	 //Start Logging
	public static final ProblemLog troubleLog = new ProblemLog("choateLOG");
	public static final Logger log = troubleLog.logger;
	//End Logging
	
	public static void main(String[] args) {
		
		FindDeployments Pods = new FindDeployments();
		Pods.getListOfPodsInDB();
		List<Pod> listOfPods = Pods.getListOfPods(); 
		
		for (Pod PodInList: listOfPods) {
			System.out.println(PodInList.getPod_id());
		}
		
		
		/*try {
			outboundFTP ftp = new outboundFTP();
//			ftp.diagnosticInformation();
			ftp.start_test();
			System.exit(0);
		}
		catch(Exception e) {
			System.out.println("Error: "+ e);
		}*/
		
		
		Iterator<CsvParsing> beanIterator = null;
		DatabaseConnect dbConnect = null;
		System.out.println("Test OK");
		
		try {
		beanIterator = getBeans_getCsvData();
		}
		catch (Exception e) {
			log.severe("Issue getting Bean: "+ Throwables.getStackTraceAsString(e));
		}
		
		if (beanIterator != null) {
			System.out.println("Will show you the first bean in the list");
//			printFirstBean(beanIterator);
			logFirstBean(beanIterator);
//			printAllBean(beanIterator); // this prints all the beans in the csv file.
		} else {
			System.out.println("Trouble reading in Bean Iterator");
			log.severe("Trouble reading in Bean Iterator");
			System.exit(10);
		}
		beanIterator = getBeans_getCsvData();
		dbConnect = new DatabaseConnect(beanIterator);
		dbConnect.connect();
		dbConnect.showExample();
		dbConnect.insertNewRecord();
		dbConnect.proveInserted();

		
		
		

	}// end main method
	
	
	
	
	private static Iterator<CsvParsing> getBeans_getCsvData() {
		try {
			//@SuppressWarnings({ "rawtypes", "unchecked" })
			List<CsvParsing> beans = new CsvToBeanBuilder<CsvParsing>(new FileReader("WNF-S007.csv"))
					.withType(CsvParsing.class).build().parse();
			return beans.iterator();
			}
		
			catch(IllegalStateException illegalState) {
				log.severe(Throwables.getStackTraceAsString(illegalState));
				System.exit(10);
			}
			catch(FileNotFoundException fileNotFound) {
				log.severe(Throwables.getStackTraceAsString(fileNotFound));
				System.exit(10);
			}
		
		return null; 
	}
	
	private static void logFirstBean(Iterator<CsvParsing> beanIterator_M) {
		String formattedString = "";
		CsvParsing aBean = beanIterator_M.next();
		for (int i = 3/*0*/; i < 12 /*16*/; i++) {
			Object aBeanAttribute = null;
			try {
				 aBeanAttribute = aBean.getANY(i);
				 if(aBeanAttribute == null) {
					 aBeanAttribute = -1;
				 }
			}
			catch(Exception illegalState) {
				System.out.println(illegalState.getMessage());
				log.severe(Throwables.getStackTraceAsString(illegalState));
				System.exit(10);
			}
			finally {
			formattedString = formattedString.concat(aBeanAttribute + "\n");
			}
		}
		log.info("Most Current Bean: \n" + formattedString);
	}
	

	private static void printFirstBean(Iterator<CsvParsing> beanIterator_M) {
		// while(beanIterator_M.hasNext()) { // part of printing out entire CSV file
		CsvParsing aBean = beanIterator_M.next();
		for (int i = 0; i < 16; i++) {
			Object aBeanAttribute = null;
			try {
				 aBeanAttribute = aBean.getANY(i);
				 if(aBeanAttribute == null) {
					 aBeanAttribute = -1;
				 }
			}
			catch(Exception illegalState) {
				System.out.println(illegalState.getMessage());
				log.severe(Throwables.getStackTraceAsString(illegalState));
				System.exit(10);
			}
			finally {
			System.out.print( aBeanAttribute + " | ");
			}
		}
		System.out.println("");
	}
	// }	//part of printing out entire CSV file

	@SuppressWarnings("unused")
	private static void printAllBean(Iterator<CsvParsing> beanIterator_M) {
		while (beanIterator_M.hasNext()) {
			CsvParsing aBean = beanIterator_M.next();
			for (int i = 0; i < 16; i++) {
				System.out.print(aBean.getANY(i) + " | ");
			}
			System.out.println("");
			
		}
	}

	@SuppressWarnings("unused")
	private static void printBeanBasic(List<CsvParsing> beans) {
		// manually print bean value
		System.out.println(beans.get(0).getBatt_Volt());
	}

}

/*
 * UNNEEDED STUFF AND RANDOM THINGS
 * 
 * System.out.print(aBean.getName() + " | "); System.out.print(aBean.getTime() +
 * " | "); System.out.print(aBean.getBatt_Volt() + " | ");
 * System.out.print(aBean.getTemp() + " | "); System.out.print(aBean.getpH() +
 * " | "); System.out.print(aBean.getpHmv() + " | ");
 * System.out.print(aBean.getCond() + " | "); System.out.print(aBean.getDOpct()
 * + " | "); System.out.print(aBean.getDOmgl() + " | ");
 * System.out.print(aBean.getDOgain() + " | "); System.out.print(aBean.getTurb()
 * + " | "); System.out.print(aBean.getDepth() + " | ");
 * System.out.print(aBean.getLat_dd() + " | ");
 * System.out.print(aBean.getLong_dd() + " | ");
 * System.out.print(aBean.getGPStime() + " | ");
 * System.out.print(aBean.getNum_Sats() + " | ");
 * 
 * 
 */
