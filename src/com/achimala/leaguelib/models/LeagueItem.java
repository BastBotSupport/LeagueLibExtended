/*
 *  This file is part of LeagueLib.
 *  LeagueLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LeagueLib is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LeagueLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.achimala.leaguelib.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.achimala.util.BidirectionalMap;

public class LeagueItem {

	private static Map<Integer,Map<String,Object>> itemList = new HashMap<Integer, Map<String,Object>>();
   
	
	static{
		BufferedReader br = null;

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader("lib\\itemlist.txt"));
			
			while ((sCurrentLine = br.readLine()) != null) {
				Map<String,Object> itemInfo = new HashMap<String,Object>();
				
				String[] itemID = sCurrentLine.split("=");
				String[] name = itemID[0].split("\\(");
				String itemCost = name[1].replace(")", "");
				itemInfo.put("Name", name[0]);
				itemInfo.put("Cost", itemCost.split(":")[1].trim());
				itemList.put(Integer.parseInt(itemID[1].trim()), itemInfo);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public Map<Integer,Map<String,Object>> getItemList(){
		return itemList;
	}
	
	public static String getItemNameFromId(Integer id){
		if(itemList.containsKey(id)){
			return itemList.get(id).get("Name").toString();
		}
		return "Empty";
	}

	public static void main(String[] args) throws Exception {
	
	}
}
