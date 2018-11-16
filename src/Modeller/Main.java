package src.MatadorGame;
import gui_fields.GUI_Board;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_main.GUI;
import gui_main.*;
import gui_fields.GUI_Board;
import gui_fields.GUI_Field;
import gui_fields.GUI_Player;
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
        int field_position = 0;
        Random random = new Random();
        int field_position1 = 0;
        int field_position2 = 0;
        int field_position3 = 0;
        int field_position4 = 0;

        Scanner scan = new Scanner(System.in);

        ChanceCardDeck game = new ChanceCardDeck();


        GUI.setNull_fields_allowed(true);


        GUI gui = new GUI();

        gui.showMessage("Start game");


        GUI_Player player1 = new GUI_Player("Mr. on time", 30000);
        GUI_Player player2 = new GUI_Player("Gustav", 30000);
        GUI_Player player3 = new GUI_Player("Dakka", 3000);
        GUI_Player player4 = new GUI_Player("Oplader kongen", 30);
        gui.addPlayer(player1);
        gui.addPlayer(player2);

        //add player?

        gui.addPlayer(player3);
        gui.addPlayer(player4);

        GUI_Player currentplayer = player1;


        gui.getFields()[0].setCar(player1, true);
        gui.getFields()[0].setCar(player2, true);
        gui.getFields()[0].setCar(player3, true);
        gui.getFields()[0].setCar(player4, true);


        while (true) {


             if (currentplayer == player1) {
                        currentplayer = player2;
                        field_position = field_position2;

            } else if (currentplayer == player2) {
                        currentplayer = player3;
                        field_position = field_position3;

            } else if (currentplayer == player3) {
                        currentplayer = player4;
                        field_position = field_position4;

            } else {
                        currentplayer = player1;
                       field_position = field_position1;
            }


            gui.showMessage("Roll the dice " + currentplayer.getName());

            int a = random.nextInt(5);
            int b = random.nextInt(5);

            gui.setDice((a), b);

            int old_position = field_position;

            gui.getFields()[old_position].setCar(currentplayer,false);
            field_position += a;
            field_position %= 40;
            gui.getFields()[field_position].setCar(currentplayer, true);


            // Biler skal flyttes ved set car, og

            if (field_position == 2 || field_position == 8|| field_position == 18|| field_position == 23|| field_position == 36|| field_position == 33) {

            //Gemmer spillernes lokation.

            if (currentplayer == player1) {
                field_position1 = field_position;

            } else  if (currentplayer == player2) {
                field_position2 = field_position;

            } else  if (currentplayer == player3) {
                field_position3 = field_position;

            } else
                field_position4 = field_position;
            }


            }

        }
    }


