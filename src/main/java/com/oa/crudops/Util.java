package com.oa.crudops;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static <T> List<List<T>> splitListToSubLists(List<T> parentList, int subListSize) {
        List<List<T>> subLists = new ArrayList<List<T>>();

        if (subListSize > parentList.size() || subListSize < 1) {
            subLists.add(parentList);
        } else {
            int remainingElements = parentList.size();
            int startIndex = 0;
            int endIndex = subListSize;
            do {
                List<T> subList = parentList.subList(startIndex, endIndex);
                subLists.add(subList);
                startIndex = endIndex;
                if (remainingElements - subListSize >= subListSize) {
                    endIndex = startIndex + subListSize;
                } else {
                    endIndex = startIndex + remainingElements - subList.size();
                }
                remainingElements -= subList.size();
            } while (remainingElements > 0);

        }
        return subLists;
    }

}
