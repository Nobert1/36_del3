package Modeller;
import gui_fields.GUI_Board;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_main.GUI;
import gui_main.*;
import gui_fields.GUI_Board;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
import sun.awt.SunHints;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.*;
import java.util.Scanner;


public class Main {

    ChanceCardDeck deck = new ChanceCardDeck();

    public Main() {
        new GUI();
    }

    public static void main(String[] args) {

        // Creates the variables needed for the game to run. The list contains all the player objects that are gonna be created.
        List<Object> playerlist = new ArrayList<>();
        Random random = new Random();
        Dice die1 = new Dice();

        int tempfield_position0 = 0;
        int tempfield_position1 = 0;
        int tempfield_position2 = 0;
        int tempfield_position3 = 0;
        int field_position = 0;

        int players;

        GUI gui = new GUI();



        gui.showMessage("Start game");

        // Enters number of players (have to be made into a method for later)
        while (true) {
            players = gui.getUserInteger(" How many players? Max 4 players ");
            if (players > 1 && players < 5) {
                break;
            } else {
                gui.showMessage("Not a valid number");
            }
        }

        // then produces the number of players, probably made into the same method as the above.
        for (int i = 0; i < players; i++) {
            String name = gui.getUserString("What would you like your name to be?");
            playerlist.add(new GUI_Player(name, 24-2*players));
            gui.getFields()[0].setCar((GUI_Player) playerlist.get(i), true);
        }

        // Player object that keeps changing into other objects.

        GUI_Player currentplayer = (GUI_Player) playerlist.get(0);

        //Game loop that currently never ends
        while (true) {

        //Player 1 turn
            if (currentplayer == playerlist.get(0)) {
                currentplayer = (GUI_Player) playerlist.get(1);
                field_position = tempfield_position1;


        // Player 2 turn
            } else if (currentplayer == playerlist.get(1)) {

                if (players == 2) {
                    currentplayer = (GUI_Player) playerlist.get(0);
                    field_position = tempfield_position0;
                } else {
                    currentplayer = (GUI_Player) playerlist.get(2);
                    field_position = tempfield_position2;
                }
            }

        // Player 3 turn
            else if (currentplayer == playerlist.get(2)) {

            if (players == 3) {
                currentplayer = (GUI_Player) playerlist.get(0);
                field_position = tempfield_position0;

            } else {
                currentplayer = (GUI_Player) playerlist.get(3);
                field_position = tempfield_position3; }


            // Player 4 turn
            } else if (currentplayer == playerlist.get(3)) {
                currentplayer = (GUI_Player) playerlist.get(0);
                field_position = tempfield_position0;
            }


            gui.showMessage("Roll the dice " + currentplayer.getName());

            // Variabels for the die's, use dice class instead.
            die1.roll();
            int b = random.nextInt(6) + 1;

            // Displays the dices on the board.

            gui.setDice(die1.getValue(), b);

            gui.showMessage("You rolled " + die1.getValue());


            gui.getFields()[field_position].setCar(currentplayer, false);
            // Removes the car from the current position on the board


            field_position += die1.getValue();
            field_position = field_position % 24;
            // Moves the car to the new position and takes modoulus to make sure the car will always be inside the array.






            gui.getFields()[field_position].setCar(currentplayer, true);


            //if (field_position == 3 || field_position == 9|| field_position == 15|| field_position == 22) { continue; }
            // Function to check if the car is currently on any chancecard spot currently fcks up the program even though it does nothing
            // It has a weird effect on the variables, it produces multiple cars even though the method body only consists of continue.



            //Saves location of the player till it's their turn agian.

                    if (currentplayer == playerlist.get(0)) {
                        tempfield_position0 = field_position;

            } else if (currentplayer == playerlist.get(1)) {
                        tempfield_position1 = field_position;

            } else if (currentplayer == playerlist.get(2)) {
                        tempfield_position2 = field_position;

            } else if (currentplayer == playerlist.get(3)) {
                        tempfield_position3 = field_position;
            }

        }

    }

    }


