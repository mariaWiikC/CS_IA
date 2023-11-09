package com.company;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class Tags
{
    // ok, each song can have tags, so I need to be able to add them to the songs, so a parameter for the songs class
    // can be a list of tags
    LinkedList<String> allTags = new LinkedList<>();

    public Tags()
    {
        allTags.add("sad");
        allTags.add("energetic");
        allTags.add("happy");
        allTags.add("relaxed");
        allTags.add("morning");
        allTags.add("afternoon");
        allTags.add("evening");
        allTags.add("guitar");
        allTags.add("piano");
        allTags.add("vocal");
        allTags.add("Independence");
        allTags.add("Easter");
        allTags.add("Christmas");
    }

    /*
    public void addTag(String newTag)
    {
        allTags.add(newTag);
    }

    public void removeTag(String toRemoveTag)
    {
        allTags.remove(toRemoveTag);
    }
     */
}
