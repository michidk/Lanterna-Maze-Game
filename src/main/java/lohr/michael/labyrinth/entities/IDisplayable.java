package lohr.michael.labyrinth.entities;

import lohr.michael.labyrinth.lanterna.TextStyle;

/**
 * Created by Michael Lohr on 13.01.2016.
 *
 * an entity has to to implement this interface, if it wants to be displayed
 */
public interface IDisplayable {
    char getChar();
    TextStyle getTextStyle();
}
