import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener; // the mouse listener i dont really understand mouse listener may you can explain?
import java.util.*;
//Tamim
public class GUI extends JFrame {

    //randomizer to pick  cards randomly
    Random rand = new Random();

    //temporary integer used for used status of players
    int tempC;

    //boolean that indicates whether the dealer is thinking or not
    boolean dealerHit = false;

    //list of cards
    ArrayList<Card> Cards = new ArrayList<Card>();

    //list of messages
    ArrayList<Message> Log = new ArrayList<Message>();

    //fonts used in cards design
    Font fontCard = new Font("Times New Roman", Font.PLAIN, 40);
    Font fontQuest = new Font("Times New Roman", Font.BOLD, 40);
    Font fontButton = new Font("Times New Roman", Font.PLAIN, 25);
    Font fontLog = new Font("Times New Roman", Font.ITALIC, 30);

    //game message colors
    Color cDealer = Color.yellow;
    Color cPlayer = new Color(67, 255, 25);

    //strings in buttons
    String questHitStay = new String("Hit or Stay?");
    String questPlayMore = new String("More cards?");

    //colors for backgrounds and buttons
    Color colorBackground = new Color(80,80,80);
    Color colorButton = new Color(0, 85, 255);

    //buttons in the game
    JButton bHit = new JButton();
    JButton bStay = new JButton();
    JButton bYes = new JButton();
    JButton bNo = new JButton();

    //screen resolution
    int sW = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int sH = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    //window resolution
    int aW = 1300;
    int aH = 800;

    //card grid position and dimensions
    int gridX = 50;
    int gridY = 50;
    int gridW = 900;
    int gridH = 400;

    //card spacing and dimensions
    int spacing = 10;
    int rounding = 10;
    int tCardW = (int) gridW/6;
    int tCardH = (int) gridH/2;
    int cardW = tCardW - spacing*2;
    int cardH = tCardH - spacing*2;

    //booleans checking who's playing
    boolean switchturns = true;
    boolean dealer_turn = false;
    boolean play_more_q = false;

    //player and dealer card arraylist
    ArrayList<Card> playerCards = new ArrayList<Card>();
    ArrayList<Card> dCards = new ArrayList<Card>();

    //player and dealer totals
    int playerMinTotal = 0;
    int playerMaxPoints = 0;
    int dMinTotal = 0;
    int dMaxTotal = 0;

    //polygons for diamond shapes
    int[] polyX = new int[4];
    int[] polyY = new int[4];

    public GUI() {
        this.setTitle("Blackjack Tamim"); // title in the window
        this.setBounds((sW-aW-6)/2, (sH-aH-29)/2, aW+6, aH+29);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        Board board = new Board();
        this.setContentPane(board);
        board.setLayout(null);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);

        //button stuff

        ActHit actHit = new ActHit();
        bHit.addActionListener(actHit);
        bHit.setBounds(1000, 200, 100, 50); // button positionning
        bHit.setBackground(colorButton);
        bHit.setFont(fontButton);
        bHit.setText("HIT");
        board.add(bHit);
        // Stay button
        ActStay actStay = new ActStay();
        bStay.addActionListener(actStay);
        bStay.setBounds(1150, 200, 100, 50);
        bStay.setBackground(colorButton);
        bStay.setFont(fontButton);
        bStay.setText("STAY");
        board.add(bStay);

        ActYes actYes = new ActYes();
        bYes.addActionListener(actYes);
        bYes.setBounds(1000, 600, 100, 50);
        bYes.setBackground(colorButton);
        bYes.setFont(fontButton);
        bYes.setText("YES");
        board.add(bYes);

        ActNo actNo = new ActNo();
        bNo.addActionListener(actNo);
        bNo.setBounds(1150, 600, 100, 50);
        bNo.setBackground(colorButton);
        bNo.setFont(fontButton);
        bNo.setText("NO");
        board.add(bNo);

        //creating all cards

        String temp_str = "starting_temp_str_name";
        for (int i = 0; i < 52; i++) {
            if (i % 4 == 0) {
                temp_str = "Spades";
            } else if (i % 4 == 1) {
                temp_str = "Hearts";
            } else if (i % 4 == 2) {
                temp_str = "Diamonds";
            } else if (i % 4 == 3) {
                temp_str = "Clubs";
            }
            Cards.add(new Card((i/4) + 1, temp_str, i));
        }

        //randomly selecting initial cards for player and dealer

        tempC = rand.nextInt(52);
        playerCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();

        tempC = rand.nextInt(52);
        while (Cards.get(tempC).used == true) {
            tempC = rand.nextInt(52);
        }
        dCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();


        tempC = rand.nextInt(52);
        while (Cards.get(tempC).used == true) {
            tempC = rand.nextInt(52);
        }
        playerCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();


        tempC = rand.nextInt(52);
        while (Cards.get(tempC).used == true) {
            tempC = rand.nextInt(52);
        }
        dCards.add(Cards.get(tempC));
        Cards.get(tempC).setUsed();

    }

    public void totalsScores() {

        int Counter;

        //calculation of player's totals
        playerMinTotal = 0;
        playerMaxPoints = 0;
        Counter = 0;
           //ace is the strongest card of the game so its value is 1 if card superior to max point
        for (Card c : playerCards) {
            playerMinTotal += c.value;
            playerMaxPoints += c.value;
            if (c.name == "Ace")
                Counter++;

        }

        if (Counter > 0)
            playerMaxPoints += 10;

        dMinTotal = 0;
        dMaxTotal = 0;
        Counter = 0;

        for (Card c : dCards) {
            dMinTotal += c.value;
            dMaxTotal += c.value;
            if (c.name == "Ace")
                Counter++;

        }

        if (Counter > 0)
            dMaxTotal += 10;
    }

    public void setWinner() {
        int pPoints = 0;
        int dPoints = 0;

        if (playerMaxPoints > 21) {
            pPoints = playerMinTotal;
        } else {
            pPoints = playerMaxPoints;
        }

        if (dMaxTotal > 21) {
            dPoints = dMinTotal;
        } else {
            dPoints = dMaxTotal;
        }
           // this is what will show on the log also from message files
        if (pPoints > 21 && dPoints > 21) {
            Log.add(new Message("Nobody won", "Dealer"));
        } else if (dPoints > 21) {
            Log.add(new Message("You win", "Player"));
            Main.pWins++;
        } else if (pPoints > 21) {
            Log.add(new Message("Dealer wins", "Dealer"));
            Main.dWins++;
        } else if (pPoints > dPoints) {
            Log.add(new Message("You win", "Player"));
            Main.pWins++;
        } else {
            Log.add(new Message("Dealer wins", "Dealer"));
            Main.dWins++;
        }

    }

      public void dealerHitStay() {
        dealerHit = true; // hitter hit is a boolean
           //the points
        int dAvailable = 0;
        if (dMaxTotal > 21) {
            dAvailable = dMinTotal;
        } else {
            dAvailable = dMaxTotal;
        }

        int pAvailable = 0;
        if (playerMaxPoints > 21) {
            pAvailable = playerMinTotal;
        } else {
            pAvailable = playerMaxPoints;
        }

        repaint();



        if ((dAvailable < pAvailable && pAvailable <= 21) || dAvailable < 16) {
            int tempMax = 0;
            if (dMaxTotal <= 21) {
                tempMax = dMaxTotal;
            } else {
                tempMax = dMinTotal;
            }
            String mess = ("Dealer decided to hit (total: " + Integer.toString(tempMax) + ")");
            Log.add(new Message(mess, "Dealer"));

            tempC = rand.nextInt(52);
            while (Cards.get(tempC).used == true) {
                tempC = rand.nextInt(52);
            }
            dCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();

        } else {
            int tempMax = 0;
            if (dMaxTotal <= 21) {
                tempMax = dMaxTotal;
            } else {
                tempMax = dMinTotal;
            }
            String mess = ("Dealer decided to stay total: " + Integer.toString(tempMax) + "");
            Log.add(new Message(mess, "Dealer"));
            setWinner();
            dealer_turn = false;
            play_more_q = true;
        }
        dealerHit = false;
    }

    public void refresher() {

        if (switchturns == true) {
            bHit.setVisible(true);
            bStay.setVisible(true);
        } else {
            bHit.setVisible(false);
            bStay.setVisible(false);
        }

        if (dealer_turn == true) {
            if (dealerHit == false)
                dealerHitStay();
        }

        if (play_more_q == true) {
            bYes.setVisible(true);
            bNo.setVisible(true);
        } else {
            bYes.setVisible(false);
            bNo.setVisible(false);
        }

        totalsScores();

        if ((playerMaxPoints == 21 || playerMinTotal >= 21) && switchturns == true) {
            int tempMax = 0;
            if (playerMaxPoints <= 21) {
                tempMax = playerMaxPoints;
            } else {
                tempMax = playerMinTotal;
            }
            String mess = ("total: " + Integer.toString(tempMax) + ")");
            Log.add(new Message(mess, "Player"));
            switchturns = false;
            dealer_turn = true;
        }

        if ((dMaxTotal == 21 || dMinTotal >= 21) && dealer_turn == true) {
            int tempMax = 0;
            if (dMaxTotal <= 21) {
                tempMax = dMaxTotal;
            } else {
                tempMax = dMinTotal;
            }
            String mess = ("Dealer pass,total: " + Integer.toString(tempMax) + ")");
            Log.add(new Message(mess, "Dealer"));
            setWinner();
            dealer_turn = false;
            play_more_q = true;
        }

        repaint();
    }
          // the visor design
    public class Board extends JPanel {

        public void paintComponent(Graphics g) {
            //background
            g.setColor(colorBackground);
            g.fillRect(0, 0, aW, aH);

            //questions

			g.drawRect(gridX+gridW+50, gridY, 250, 400);
			g.drawRect(gridX, gridY+gridH+50, gridW, 250);

            g.setColor(Color.black);
            g.fillRect(gridX, gridY+gridH+50, gridW, 600);

            //Log
            g.setFont(fontLog);
            int logIndex = 0;
            for (Message L : Log) {
                if (L.getWho().equalsIgnoreCase("Dealer")) {
                    g.setColor(cDealer);
                } else {
                    g.setColor(cPlayer);
                }
                g.drawString(L.getMessage(), gridX+20, gridY+480+logIndex*35); // this will position the message inside the rectangle /visor
                logIndex++;
            }

            //score
            g.setColor(Color.white);
            g.setFont(fontQuest);
            String score = ("Score: " + Integer.toString(Main.pWins) + " - " + Integer.toString(Main.dWins));
            g.drawString(score, gridX+gridW+70, gridY+gridH+300);

            //player cards and designs
            int cards = 0;
            for (Card c : playerCards) {
                g.setColor(Color.white);
                g.fillRect(gridX+spacing+tCardW*cards+rounding, gridY+spacing, cardW-rounding*2, cardH);
                g.fillRect(gridX+spacing+tCardW*cards, gridY+spacing+rounding, cardW, cardH-rounding*2);
                g.fillOval(gridX+spacing+tCardW*cards, gridY+spacing, rounding*2, rounding*2);
                g.fillOval(gridX+spacing+tCardW*cards, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
                g.fillOval(gridX+spacing+tCardW*cards+cardW-rounding*2, gridY+spacing, rounding*2, rounding*2);
                g.fillOval(gridX+spacing+tCardW*cards+cardW-rounding*2, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
                 // equal ignorecase wont care about the caracter size
                g.setFont(fontCard);
                if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }

                g.drawString(c.symbol, gridX+spacing+tCardW*cards+rounding, gridY+spacing+cardH-rounding);

                if (c.shape.equalsIgnoreCase("Hearts")) {
                    g.fillOval(gridX+tCardW*cards+42, gridY+70, 35, 35);
                    g.fillOval(gridX+tCardW*cards+73, gridY+70, 35, 35);
                    g.fillArc(gridX+tCardW*cards+30, gridY+90, 90, 90, 51, 78);
                } else if (c.shape.equalsIgnoreCase("Diamonds")) {
                    polyX[0] = gridX+tCardW*cards+75;
                    polyX[1] = gridX+tCardW*cards+50;
                    polyX[2] = gridX+tCardW*cards+75;
                    polyX[3] = gridX+tCardW*cards+100;
                    polyY[0] = gridY+60;
                    polyY[1] = gridY+100;
                    polyY[2] = gridY+140;
                    polyY[3] = gridY+100;
                    g.fillPolygon(polyX, polyY, 4);
                } else if (c.shape.equalsIgnoreCase("Spades")) {
                    g.fillOval(gridX+tCardW*cards+42, gridY+90, 35, 35);
                    g.fillOval(gridX+tCardW*cards+73, gridY+90, 35, 35);
                    g.fillArc(gridX+tCardW*cards+30, gridY+15, 90, 90, 51+180, 78);
                    g.fillRect(gridX+tCardW*cards+70, gridY+100, 10, 40);
                } else {
                    g.fillOval(gridX+tCardW*cards+40, gridY+90, 35, 35);
                    g.fillOval(gridX+tCardW*cards+75, gridY+90, 35, 35);
                    g.fillOval(gridX+tCardW*cards+58, gridY+62, 35, 35);
                    g.fillRect(gridX+tCardW*cards+70, gridY+75, 10, 70);
                }

                //-------------------------
                cards++;
            }

            if (dealer_turn == true || play_more_q == true) {
                //dealer cards
                cards = 0;
                for (Card c : dCards) {
                    g.setColor(Color.white);
                    g.fillRect(gridX+spacing+tCardW*cards+rounding, gridY+spacing+200, cardW-rounding*2, cardH);
                    g.fillRect(gridX+spacing+tCardW*cards, gridY+spacing+rounding+200, cardW, cardH-rounding*2);
                    g.fillOval(gridX+spacing+tCardW*cards, gridY+spacing+200, rounding*2, rounding*2);
                    g.fillOval(gridX+spacing+tCardW*cards, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);
                    g.fillOval(gridX+spacing+tCardW*cards+cardW-rounding*2, gridY+spacing+200, rounding*2, rounding*2);
                    g.fillOval(gridX+spacing+tCardW*cards+cardW-rounding*2, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);

                    g.setFont(fontCard);
                    if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.black);
                    }

                    g.drawString(c.symbol, gridX+spacing+tCardW*cards+rounding, gridY+spacing+cardH-rounding+200);

                    if (c.shape.equalsIgnoreCase("Hearts")) {
                        g.fillOval(gridX+tCardW*cards+42, gridY+70+200, 35, 35);
                        g.fillOval(gridX+tCardW*cards+73, gridY+70+200, 35, 35);
                        g.fillArc(gridX+tCardW*cards+30, gridY+90+200, 90, 90, 51, 78);
                    } else if (c.shape.equalsIgnoreCase("Diamonds")) {
                        polyX[0] = gridX+tCardW*cards+75;
                        polyX[1] = gridX+tCardW*cards+50;
                        polyX[2] = gridX+tCardW*cards+75;
                        polyX[3] = gridX+tCardW*cards+100;
                        polyY[0] = gridY+60+200;
                        polyY[1] = gridY+100+200;
                        polyY[2] = gridY+140+200;
                        polyY[3] = gridY+100+200;
                        g.fillPolygon(polyX, polyY, 4);
                    } else if (c.shape.equalsIgnoreCase("Spades")) {
                        g.fillOval(gridX+tCardW*cards+42, gridY+90+200, 35, 35);
                        g.fillOval(gridX+tCardW*cards+73, gridY+90+200, 35, 35);
                        g.fillArc(gridX+tCardW*cards+30, gridY+15+200, 90, 90, 51+180, 78);
                        g.fillRect(gridX+tCardW*cards+70, gridY+100+200, 10, 40);
                    } else {
                        g.fillOval(gridX+tCardW*cards+40, gridY+90+200, 35, 35);
                        g.fillOval(gridX+tCardW*cards+75, gridY+90+200, 35, 35);
                        g.fillOval(gridX+tCardW*cards+58, gridY+62+200, 35, 35);
                        g.fillRect(gridX+tCardW*cards+70, gridY+75+200, 10, 70);
                    }

                    //-------------------------
                    cards++;
                }

                g.setColor(Color.black);
                g.setFont(fontQuest);
                g.drawString("Your points: ", gridX+gridW+60, gridY+40);
                if (playerMaxPoints <= 21) {
                    g.drawString(Integer.toString(playerMaxPoints), gridX+gridW+60, gridY+120);
                } else {
                    g.drawString(Integer.toString(playerMinTotal), gridX+gridW+60, gridY+120);
                }
                g.drawString("Dealer's points: ", gridX+gridW+60, gridY+240);
                if (dMaxTotal <= 21) {
                    g.drawString(Integer.toString(dMaxTotal), gridX+gridW+60, gridY+320);
                } else {
                    g.drawString(Integer.toString(dMinTotal), gridX+gridW+60, gridY+320);
                }
            }

        }

    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent arg0) {


        }

        @Override
        public void mouseMoved(MouseEvent arg0) {


        }

    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent arg0) {


        }

        @Override
        public void mouseEntered(MouseEvent arg0) {


        }

        @Override
        public void mouseExited(MouseEvent arg0) {


        }

        @Override
        public void mousePressed(MouseEvent arg0) {


        }

        @Override
        public void mouseReleased(MouseEvent arg0) {


        }

    }
     // this is hit action
    public class ActHit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (switchturns == true) {
               // hit button is pressed these happen:

                int tempMax = 0;
                if (playerMaxPoints <= 21) {
                    tempMax = playerMaxPoints;
                } else {
                    tempMax = playerMinTotal;
                }
                String mess = ("You decided to hit total: " + Integer.toString(tempMax) + "");
                Log.add(new Message(mess, "Player"));

                tempC = rand.nextInt(52);
                while (Cards.get(tempC).used == true) {
                    tempC = rand.nextInt(52);
                }
                playerCards.add(Cards.get(tempC));
                Cards.get(tempC).setUsed();

            }
        }

    }
           //button stay action and its method
    public class ActStay implements ActionListener {  //stay will turn off player turn

        @Override
        public void actionPerformed(ActionEvent e) {
            if (switchturns == true) {


                int tempMax = 0;
                if (playerMaxPoints <= 21) {
                    tempMax = playerMaxPoints;
                } else {
                    tempMax = playerMinTotal;
                }
                String mess = ("You stay total: " + Integer.toString(tempMax) + "");
                Log.add(new Message(mess, "Player"));

                switchturns = false;
                dealer_turn = true; // dealer turn
            }
        }

    }

    public class ActYes implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // yes will restart the and clear the log

            playerCards.clear();
            dCards.clear();
            Log.clear();
            // if player stay he cant play more so i put boolean
            play_more_q = false;
            switchturns = true;

            tempC = rand.nextInt(52);
            playerCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();


            tempC = rand.nextInt(52);
            while (Cards.get(tempC).used == true) {
                tempC = rand.nextInt(52);
            }
            dCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();


            tempC = rand.nextInt(52);
            while (Cards.get(tempC).used == true) {
                tempC = rand.nextInt(52);
            }
            playerCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();


            tempC = rand.nextInt(52);
            while (Cards.get(tempC).used == true) {
                tempC = rand.nextInt(52);
            }
            dCards.add(Cards.get(tempC));
            Cards.get(tempC).setUsed();


        }

    }
        // No will shutdown the game
    public class ActNo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Main.closed = true;
            dispose();
        }

    }

}