package parsing_csv;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FindDeployments {
	
	public DatabaseConnect dbConnect;
	public Pod pod;
	List<Pod> listOfPods;
	
	
	public void getListOfPodsInDB() {
		dbConnect = new DatabaseConnect();
		dbConnect.connect();
		
		setListOfPods(dbConnect.getActivePods());
		removeDuplicatePods();
	}


	public List<Pod> getListOfPods() {
		return listOfPods;
	}


	public void setListOfPods(List<Pod> listOfPods) {
		this.listOfPods = listOfPods;
	}
	
	//When two pods with two Pod_id's are present remove the pod with the lower "id" parameter.
	//This method exists when someone forgets to switch the "active" parameter in the database. This is problematic because if there are two active pods with
	//the same pod_id's then we will unknowingly add data to both deployments.
	//After the pod is re-deployed, it's former deployment should be marked as active = 0;
	//IF YOU ARE SURE THAT THIS WON"T EVER HAPPEN THEN THIS CAN BE REMOVED. WE DON;t WANT NEW DATA IN OLD DEPLOYMENTS FOR HISTORIXCAL REASONS
	public void removeDuplicatePods() {
		//assuming that pod_id and id could be null im using a try catch
		//create an array of pod_id's
//		int[] pod_ids = new int[getListOfPods().size()];
		ArrayList<Integer> pod_ids = new ArrayList<Integer>();
		try {
			for(Pod pod: getListOfPods()) {
				pod_ids.add(pod.getPod_id());
			}
			
			//stack overflow: https://stackoverflow.com/questions/562894/java-detect-duplicates-in-arraylist
			//check the sizes of hashset and list if there is a discrepancy then there is a duplicate. This is a cheap method for looking for duplicates
			Set<Integer> set = new HashSet<Integer>(pod_ids);
			if(set.size() == pod_ids.size()) {
				//there are no duplicates, return
				return;
			}
			//There are duplicates. now to find the duplicates
			List<Pod> pods = getListOfPods();
			Iterator<Integer> iterable_pod_ids = pod_ids.iterator();
			while (iterable_pod_ids.hasNext()) {
				int temp = iterable_pod_ids.next();
				int count = 0;
				int location = 0;
				for(int i = 0; i < pod_ids.size(); i++) {
					if(temp == pod_ids.indexOf(i)) {
						count++;
						if(count == 2) {
							//we need to compare the id (deployment id) and choose the higher by removing the lower one from the pod list
							if(pods.get(location).getId() > pods.get(i).getId()) {
								System.out.println("Removing Pod ID "+ pods.get(i).getPod_id()+"with deployment id "+ pods.get(i).getId());
								//add logger statement here
								pods.remove(i);
							}
							else {
								System.out.println("Removing Pod ID "+ pods.get(location).getPod_id()+" with deployment id "+ pods.get(location).getId());
								//add logger statement here
								pods.remove(location);
							}
						}
						//one location
						location = pod_ids.indexOf(i);
					}
				}
				
			}
			//add logger statement here indicating that action needs to be taken regarding toggling the active parameter on an old deployment	
		}
		catch (Exception e){
			System.out.println("Put a logger statement here there was a null pod_id or id value");
		}
		
	}
	
	
	
}

