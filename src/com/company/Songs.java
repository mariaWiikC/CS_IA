package com.company;

import java.util.LinkedList;

public class Songs
{
    LinkedList<String> tagsInSong = new LinkedList<>();

    public Songs()
    {
        // my idea here is the following: when a song is selected, a window very similar to the SearchWindow is gonna
        // appear (the only difference is that there is no real time and for the date there's only month and year)
        // and the user can check the boxes of the tags they want the song to have :)

        // so I create an object for each song that is added to the program.
        // can I have the objects saved? Would I need to have them like, written in a file or smt
        // and then when the user is retrieving info from the song it checks its file
        // if it has the word that the tag corresponds to, it's tagged, if the tag is unchecked, delete it from the file
        // if it's not there and the user wants to add it (it's checked), write it into the file

        // in the songs page I'm gonna select a song and then according to the selected tags they are added or not to the song
        // so like, initially, the song doesn't have any tags. I can add the tags on the songs page
    }

    public void addSongTags(String tagToAdd)
    {
        tagsInSong.add(tagToAdd);
    }
}