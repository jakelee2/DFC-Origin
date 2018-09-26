package org.dbadmin.util;

import java.util.ArrayList;
import java.util.List;

public class CandidateKeyHelper {
	
	public String[] getPrimaryKeyCombinations(List<String> keys) {
        String[] primaryKeys = keys.toArray(new String[keys.size()]);
        List<String> keyCombinations = new ArrayList<String>();
        
        for(int i=0; i <= primaryKeys.length; i++) {
            getPrimaryKeyCombinations(primaryKeys, primaryKeys.length, i, keyCombinations);
        }
        
        return keyCombinations.toArray(new String[keyCombinations.size()]);
	}
	
	/*public String[] getPercentListForKeyCombinations(String[] keyCombinations) {

        //Create %s list
        List<String> percentList = new LinkedList<>();
        for(String s : keyCombinations){
            String[] temp = s.split("~");			//Delimiter is ~
            if(temp.length == 0){
                percentList.add("%s ");
            }else{
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < temp.length; i++) {
                    sb.append("%s ");
                }
                percentList.add(sb.toString());
            }
        }
        
        return percentList.toArray(new String[percentList.size()]);
    }*/

    /**
     *  The main function that finds all combinations of size r in arr[] of size n.
     *  This function mainly uses combinationUtil() 
     */	
    private void getPrimaryKeyCombinations(String arr[], int n, int r, List<String> resultList) {
        // A temporary array to store combinations
        String data[] = new String[r];

        // Print all combination using temprary array 'data[]'
        helper(arr, data, 0, n-1, 0, r, resultList);
    }

    private void helper(String arr[], String data[], int start, int end, int index, int r, List<String> resultList) {
        //Base case: Current combination is ready to be printed, print it
        StringBuilder sb = new StringBuilder();
        if (index == r) {
            for (int j=0; j < r; j++) {
                sb.append(data[j]);
                if(j+1 < r)
                    sb.append(",");
            }
            if(sb.length() > 0) {
                resultList.add(sb.toString());
            }
            return;
        }

        // replace index with all possible elements. The condition "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++) {
            data[index] = arr[i];
            helper(arr, data, i+1, end, index+1, r, resultList);
        }
    }

}
