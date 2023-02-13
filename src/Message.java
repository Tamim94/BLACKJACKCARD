public class Message {
// message to the log
   String message = "this is going in the visor ";
    String who = "the person who win dealer or player";
    //m announce the win or lose, w announce player or dealer

    public Message(String m, String w) {
        this.message = m;
        this.who = w;
    }
//getter and setters
    public String getMessage() {
        return this.message;
    }

    public String getWho() {
        return this.who;
    }

}