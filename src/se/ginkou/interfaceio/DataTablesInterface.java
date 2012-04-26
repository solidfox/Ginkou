package se.ginkou.interfaceio;

import java.util.Arrays;
import java.util.TreeMap;

public class DataTablesInterface {
	
	public DataTablesInterface(String inString) {
		String[] rawCommands = inString.split("&");
		TreeMap<String, String> commands = new TreeMap<String,String>;
		for (String aCommand : rawCommands) {
			String[] commandParts = aCommand.split("=");
			assert(commandParts.length == 2);
			commands.put(commandParts[0], commandParts[1]);
		}
		System.out.println(Arrays.toString(rawCommands));
	}

	public static void main(String[] args) {
		DataTablesInterface DT = new DataTablesInterface("sEcho=2&iColumns=4&sColumns=&iDisplayStart=0&iDisplayLength=10&mDataProp_0=account&mDataProp_1=date&mDataProp_2=notice&mDataProp_3=sum&sSearch=&bRegex=false&sSearch_0=&bRegex_0=false&bSearchable_0=true&sSearch_1=&bRegex_1=false&bSearchable_1=true&sSearch_2=&bRegex_2=false&bSearchable_2=true&sSearch_3=&bRegex_3=false&bSearchable_3=true&iSortCol_0=1&sSortDir_0=desc&iSortingCols=1&bSortable_0=true&bSortable_1=true&bSortable_2=true&bSortable_3=true");
	}
}
