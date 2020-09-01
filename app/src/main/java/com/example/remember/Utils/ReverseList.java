package com.example.remember.Utils;

import com.example.remember.Model.Notes.java.Notes;

import java.util.ArrayList;
import java.util.List;

public class ReverseList {
    public static List<Notes> reverseArrayList(List<Notes> alist)
    {
        // Arraylist for storing reversed elements
        List<Notes> revArrayList = new ArrayList<Notes>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arrayList
        return revArrayList;
    }
}
