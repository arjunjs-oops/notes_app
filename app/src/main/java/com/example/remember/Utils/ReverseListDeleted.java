package com.example.remember.Utils;

import com.example.remember.Model.Notes.java.DeletedNotes;
import com.example.remember.Model.Notes.java.Notes;

import java.util.ArrayList;
import java.util.List;

public class ReverseListDeleted {
    public static List<DeletedNotes> reverseArrayList(List<DeletedNotes> alist)
    {
        // Arraylist for storing reversed elements
        List<DeletedNotes> revArrayList = new ArrayList<DeletedNotes>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arrayList
        return revArrayList;
    }
}
