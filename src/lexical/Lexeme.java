package lexical;

public class Lexeme {

    public String token;
    public TokenType type;

    public Lexeme(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }
    public void print(){
        System.out.printf("%13s", type);
        System.out.println("\t" + token);
    }

}