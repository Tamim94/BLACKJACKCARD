public class Card {

    String name; // name of the cards
    int value; // values of each cards
    String shape;// shape of the cards symbol
    boolean used = false;
    int id;
    String symbol; // name of the symbols
   //each cards has a name, value,shape name,and an id and symbol
    public Card(int n, String s, int z) {
        if (n > 1 && n < 11) { // card values are between 1 and 11
            this.name = Integer.toString(n);
            this.value = n;
            this.symbol = this.name;
        } else if (n > 10) { //
            this.value = 10;
            if (n == 11) { // jack is equal to 11 and  its symbol is J
                this.name = "Jaak";
                this.symbol = "J";
            } else if (n == 12) {
                this.name = "Queen";
                this.symbol = "Q";
            } else if (n == 13) {
                this.name = "King";
                this.symbol = "K";
            }
        } else if (n == 1) {
            this.value = 1;
            this.name = "Ace";
            this.symbol = "A";
        }
        this.shape = s;
        this.id = z;

    }

    public void setUsed() {
        used = true;

    }



}