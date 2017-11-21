package gui_fields;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import gui_codebehind.GUI_Center;
import gui_codebehind.JLabelRotatable;
import gui_codebehind.Observer;
import gui_codebehind.SwingComponentFactory;
import gui_fields.GUI_Player.iPlayerValidator;
import gui_resources.Attrs;

/**
 * The board
 * @author Ronnie
 */
public final class GUI_Board extends javax.swing.JFrame implements Observer {
    private static final long serialVersionUID = -2551372048143397506L;
    public static final String FONT = "Tahoma";
    public static final int FONTSIZE = 10;
    public static final Color BASECOLOR = new Color(51, 204, 0);
    public static final int MAX_PLAYER_COUNT = 6;
    
    private SwingComponentFactory factory = new SwingComponentFactory();
    public GUI_Player[] playerList = new GUI_Player[MAX_PLAYER_COUNT];
    private JLayeredPane base;
    private JLayeredPane[][] carPanes = new JLayeredPane[11][11];
    private JLabelRotatable[][] diceLabels = new JLabelRotatable[11][11];
    private JLabel[] playerLabels = new JLabel[MAX_PLAYER_COUNT];
    private JLabel[] iconLabels = new JLabel[MAX_PLAYER_COUNT];
    private JPanel inputPanel = new JPanel();
    private JTextArea messageArea = new JTextArea();
    private ImageIcon[] diceIcons = new ImageIcon[6];
    private GUI_Field[] fields = null;
    private int die1x = 1, die1y = 1, die2x = 1, die2y = 1;
    
    public static Point[] points = new Point[40];
    public static int nextPoint = 0;
    static{
        int i = 0;
        for(int x=10; x > 0; x--){
            GUI_Board.points[i++] = new Point(x, 10);
        }
        for(int y=10; y > 0; y--){
            GUI_Board.points[i++] = new Point(0, y);
        }
        for(int x=0; x < 10; x++){
            GUI_Board.points[i++] = new Point(x, 0);
        }
        for(int y=0; y < 10; y++){
            GUI_Board.points[i++] = new Point(10, y);
        }
    }
    
    public GUI_Board(GUI_Field[] fields) {
        this.fields = fields;
        nextPoint = 0;
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        this.setTitle(Attrs.getString("GUI_Board.Title")+(year%100));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        for(GUI_Field field : fields){
            if (field!=null)
                field.addMouseListener(new FieldMouseListener(field, playerList));
        }
        
        makeDice();
        makeBase();
        makeBackGroundPanels();
        makeDiceLabels();
        makePlayerAreas();
        makeCenter();
        makeFieldPanels();
        makeCarPanes();
        makeInputPanel();
        
        makeAutogeneratedCrap();
        
        playerList = new GUI_Player[MAX_PLAYER_COUNT];
        this.setVisible(true);
    }
    
    /**
     * Makes images of the dice
     */
    private void makeDice() {
        try {
            String path = Attrs.getImagePath("GUI_Board.Dice");
            BufferedImage image = ImageIO.read(getClass().getResource(path));
            for(int value = 0; value < 6; value++) {
                int x = 0;
                int y = 55 * value;
                this.diceIcons[value] = new ImageIcon(image.getSubimage(x, y, 54, 54));
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Makes a graphical representation of all the fields
     */
    private void makeFieldPanels() {
        int i = 0;
        for(GUI_Field f : fields){
            if(f != null) {
                Point point = points[i];
                JLayeredPane panel = f.getPanel();
                this.base.add(panel, this.factory.createGridBagConstraints(point.x, point.y));
            }
            i++;
        }
    }
    /**
     * Makes room for input fields
     */
    private void makeInputPanel() {
        this.inputPanel.setBackground(new Color(0, 128, 0));
        this.inputPanel.setOpaque(false);
        this.inputPanel = (JPanel) this.factory.setSize(this.inputPanel, 557, 179);
        this.inputPanel.setLayout(new FlowLayout());
        
        this.messageArea.setWrapStyleWord(true);
        this.messageArea.setLineWrap(true);
        this.messageArea.setSize(557, 10);
        this.messageArea.setOpaque(false);
        this.messageArea.setEditable(false);
        this.messageArea.setFocusable(false);
        
        this.inputPanel.add(this.messageArea);
        this.base.setLayer(this.inputPanel, 4);
        this.base.add(this.inputPanel, this.factory.createGridBagConstraints(1, 1, 9, 3));
    }
    /**
     * Adds Input components to the board
     * @param message The message for the user
     * @param components : input components (buttons, textfields, drop-down, etc.)
     */
    public void getUserInput(String message, Component... components) {
        this.messageArea.setText(message);
        for(Component c : components) {
            this.inputPanel.add(c);
        }
        this.inputPanel.validate();
        this.inputPanel.repaint();
    }
    /**
     * Resets input panel
     */
    public void clearInputPanel() {
        this.messageArea.setText("");
        this.inputPanel.removeAll();
        this.inputPanel.add(this.messageArea);
        this.inputPanel.validate();
        this.inputPanel.repaint();
    }
    /**
     * Makes the components on which cars can be placed
     */
    private void makeCarPanes() {
        int fieldNo = 0;
        for(GUI_Field f : fields) {
            if(f != null) {
                Point point = points[fieldNo];
            int x = point.x;
            int y = point.y;
            
            JLayeredPane layered = new JLayeredPane();
            this.factory.setSize(layered, GUI_Field.FIELDWIDTH, GUI_Field.FIELDHEIGHT);
            this.carPanes[x][y] = layered;
            layered.setOpaque(false);
            
            JLabel[] cars = new JLabel[MAX_PLAYER_COUNT];
            for(int i = 0; i < MAX_PLAYER_COUNT; i++) {
                JLabel label = new JLabel();
                cars[i] = label;
                label.setOpaque(false);
                this.factory.setSize(label, GUI_Field.FIELDWIDTH, GUI_Field.FIELDHEIGHT);
                label.setBounds(3 * i + 3, 6 * i + 1, GUI_Player.ICON_WIDTH, GUI_Player.ICON_HEIGHT);
                layered.setLayer(label, i + 5);
                label.setVisible(false);
                layered.add(label);
            }
            
            f.setCarIcons(cars);
            // fields are on layer 0.
            this.base.setLayer(layered, 1);
            this.base.add(layered, this.factory.createGridBagConstraints(x, y));
            }
            fieldNo++;
        }
    }
    /**
     * Makes the center
     */
    private void makeCenter() {
        this.base.setLayer(GUI_Center.getInstance().getCenterPanel(), 1);
        this.base.add(GUI_Center.getInstance().getCenterPanel(),
            this.factory.createGridBagConstraints(4, 4, 3, 3));
    }
    /**
     * Makes the base
     */
    private void makeBase() {
        this.base = new javax.swing.JLayeredPane();
        this.factory.setSize(this.base, 11 * GUI_Field.FIELDWIDTH, 11 * GUI_Field.FIELDWIDTH);
        this.base.setLayout(new GridBagLayout());
        this.base.setBackground(BASECOLOR);
        this.base.setOpaque(true);
    }
    /**
     * Makes the background
     */
    private void makeBackGroundPanels() {
        for(int x = 1; x < 10; x++) {
            for(int y = 1; y < 10; y++) {
                JPanel panel = new javax.swing.JPanel();
                panel.setBackground(GUI_Board.BASECOLOR);
                this.factory.setSize(panel, GUI_Field.FIELDWIDTH, GUI_Field.FIELDHEIGHT);
                this.base.setLayer(panel, 0);
                this.base.add(panel, this.factory.createGridBagConstraints(x, y));
            }
        }
    }
    /**
     * Makes the components on which dice can be placed
     */
    private void makeDiceLabels() {
        for(int x = 0; x < 11; x++) {
            for(int y = 0; y < 11; y++) {
                JLabelRotatable label = new JLabelRotatable();
                this.diceLabels[x][y] = label;
                label.setOpaque(false);
                this.factory.setSize(label, GUI_Field.FIELDWIDTH, GUI_Field.FIELDHEIGHT);
                this.base.setLayer(label, 3);
                this.base.add(label, this.factory.createGridBagConstraints(x, y), 0);
            }
        }
    }
    /**
     * Makes the components on which to show players
     */
    private void makePlayerAreas() {
        int x = 7;
        for(int i = 0; i < MAX_PLAYER_COUNT; i++) {
            int y = 9 - i;
            
            JLabel iconLabel = new JLabel();
            this.factory.setSize(iconLabel, 1 * GUI_Field.FIELDWIDTH, 1 * GUI_Field.FIELDWIDTH);
            this.base.setLayer(iconLabel, 1);
            this.base.add(iconLabel, this.factory.createGridBagConstraints(x, y));
            this.iconLabels[i] = iconLabel;
            
            JLabel playerLabel = new JLabel();
            this.factory.setSize(playerLabel, 2 * GUI_Field.FIELDWIDTH, 1 * GUI_Field.FIELDWIDTH);
            this.base.setLayer(playerLabel, 1);
            this.base.add(playerLabel, this.factory.createGridBagConstraints(x + 1, y, 2, 1));
            this.playerLabels[i] = playerLabel;
        }
    }
    /**
     * Basic getter
     * @return fields ref
     */
    public GUI_Field[] getFields() { return fields; }
    /**
     * Places dice on the board
     * @param x1 x-coordinate for die 1
     * @param y1 y-coordinate for die 1
     * @param facevalue1 value of die 1
     * @param rotation1 the angle [0:359] of die 1
     * @param x2 x-coordinate for die 2
     * @param y2 y-coordinate for die 2
     * @param facevalue2 value of die 2
     * @param rotation2 the angle [0:359] of die 2
     */
    public void setDice(int x1, int y1, int facevalue1, int rotation1,
        int x2, int y2, int facevalue2, int rotation2) {
        this.diceLabels[this.die1x][this.die1y].setIcon(null);
        this.diceLabels[this.die2x][this.die2y].setIcon(null);
        this.die1x = x1;
        this.die1y = y1;
        this.die2x = x2;
        this.die2y = y2;
        
        this.diceLabels[x1][y1].setRotation(rotation1);
        this.diceLabels[x1][y1].setHorizontalAlignment(SwingConstants.CENTER);
        this.diceLabels[x1][y1].setVerticalAlignment(SwingConstants.CENTER);
        this.diceLabels[x1][y1].setIcon(this.diceIcons[facevalue1 - 1]);
        this.diceLabels[x2][y2].setRotation(rotation2);
        this.diceLabels[x2][y2].setHorizontalAlignment(SwingConstants.CENTER);
        this.diceLabels[x2][y2].setVerticalAlignment(SwingConstants.CENTER);
        this.diceLabels[x2][y2].setIcon(this.diceIcons[facevalue2 - 1]);
    }	
    /**
     * Add a player to the board
     * @param player The player must be created beforehand
     * @return true if player is added, otherwise false
     */
    public boolean addPlayer(GUI_Player player) {
        //Check if out of room
        if(playerList[MAX_PLAYER_COUNT - 1] != null) { return false; }
        
        int i = 0;
        for(; i < MAX_PLAYER_COUNT; i++) {
            if(playerList[i] != null) {
                // No duplicate player names
                if(playerList[i].getName().equals(player.getName())) {
                    return false;
                }
            } else {
                break;
            }
        }
        player.setNumber(i);
        player.addObserver(this);
        player.setValidator(new iPlayerValidator() {
            @Override
            public boolean checkName(String name) {
                if(name == null || name.isEmpty()) return false;
                for(GUI_Player p : playerList){
                    if(p != null && name.equals(p.getName())) return false;
                }
                return true;
            }
        });
        player.getCar().addObserver(this);
        playerList[i] = player;
        updatePlayers();
        return true;
    }
    /**
     * Updates the board in order to correct player balances
     */
    public void updatePlayers() {
        int position = 0;
        for(GUI_Player p : playerList) {
            if(p != null) {
                Icon icon = new ImageIcon(p.getImage());
                
                this.iconLabels[position].setIcon(icon);
                this.playerLabels[position].setText("<html>" + p.getName() + "<br>"
                    + p.getBalance());
                position++;
            } else {
                break;
            }
        }
    }
    /**
     * Retrieves a player
     * @param name The name of the player
     * @return a Player object
     */
    public GUI_Player getPlayer(String name) {
        for(GUI_Player p : playerList) {
            if(p != null && name.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }
    /**
     * Counts how many players are in the game
     * @return number of players
     */
    public int getPlayerCount() {
        int count = 0;
        for(GUI_Player p : playerList) {
            if(p != null) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
    /**
     * Autogenerated DON'T CHANGE!
     */
    private void makeAutogeneratedCrap() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.base,
                javax.swing.GroupLayout.PREFERRED_SIZE,
                javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING).addComponent(this.base,
                javax.swing.GroupLayout.PREFERRED_SIZE,
                javax.swing.GroupLayout.DEFAULT_SIZE,
                javax.swing.GroupLayout.PREFERRED_SIZE));
        pack();
    }
    
    @Override
    public void onUpdate() {
        updatePlayers();
    }
}