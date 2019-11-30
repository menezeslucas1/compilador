package lexical;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.PushbackInputStream;

import java.util.*;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis(String filename) throws LexicalException {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }

        st = new SymbolTable();
        line = 1;
    }

    public void close() throws IOException {
        input.close();
    }

    public int getLine() {
        return this.line;
    }
    public void printTable(){
        st.print();
    }
    public Lexeme nextToken() throws IOException {
//        System.out.println("nextToken");
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

         // TODO: implement me.

         // HINT: read a char.
         // int c = input.read();

         // HINT: unread a char.
         // if (c != -1)
         //     input.unread(c);
        int estado = 1;
        while (estado != 12 && estado != 13)
        {
            int c = input.read();// -1
            switch (estado)
            {
            case 1:
//                System.out.println("case 1:");
            if (c=='\n')
            {
                line++;
            }
            else if (c==' ' || c=='\t'||c=='\r')
                {
                    estado = 1;
                }
                else if (Character.isDigit(c))
                {
                    lex.token += (char)c;
                    lex.type = TokenType.INTV;
                    estado = 3;
                }
                else if(c=='!')
                {
                    lex.token += (char)c;
                    estado = 4;
                }
                else if (c=='='){
                    lex.token += (char)c;
                    estado = 5;
                }
                else if (c== '<'||c=='>')
                {
                    lex.token +=(char)c;
                    estado = 6;
                }
                else if (Character.isLetter(c))
                {
//                    System.out.println("isLetter");
                    lex.type = TokenType.ID;
                    lex.token += (char)c;
                    estado = 7;
                }
                else if(c=='"')
                {
//                    System.out.println("vai para o caso 8");
                    lex.type = TokenType.STRING;
                    estado = 8;
                }
                else if (c == ';')
                {
                    lex.token += (char)c;
                    lex.type = TokenType.DOT_COMMA;
                    estado = 12;
                }
                else if (c == ','){
                    lex.token += (char)c;
                    lex.type = TokenType.COMMA;
                    estado=12;
                }
                else if (c == '+'){
                    lex.token += (char)c;
                    lex.type = TokenType.ADD;
                    estado = 12;
                }
                else if (c == '-'){
                    lex.token += (char)c;
                    lex.type = TokenType.SUB;
                    estado = 12;
                }
                else if (c == '*'){
                    lex.token += (char)c;
                    lex.type = TokenType.MUL;
                    estado = 12;
                }
                else if (c == '/'){
                    lex.token += (char)c;
                    lex.type = TokenType.DIV;
                    estado = 2;
                }
                else if (c == '('){
                    lex.token += (char)c;
                    lex.type = TokenType.OPEN_PAR;
                    estado = 12;
                }
                else if (c == ')'){
                    lex.token += (char)c;
                    lex.type = TokenType.CLOSE_PAR;
                    estado = 12;
                }
                else if (c == -1)
                {
                    estado = 13;
                }
                else
                {
                    lex.token += (char)c;
                    lex.type = TokenType.INVALID_TOKEN;
                    estado = 13;
                }
                break;
            case 2:
                if (c=='/') //comentario de uma linha
                {
                    lex.type = TokenType.END_OF_FILE;
                    lex.token = "";
                    estado = 22;
                }
                else if (c=='*')//comentario de varias linhas
                {
                    lex.type = TokenType.END_OF_FILE;
                    lex.token = "";
                    estado = 20;
                }
                else{
                    input.unread(c);
                    estado = 13;
                }
                break;
            case 20:
                if(c=='*')//fim de comentario
                    estado = 21;
                else if (c=='\n')
                    line++;
                else if (c == -1){
                    lex.type = TokenType.UNEXPECTED_EOF;
                    estado = 13;
                }
                break;
            case 21:
                if(c=='/')
                    estado = 1;
                else
                    estado = 20;
                break;
            case 22:
                if (c=='\n'){
                    line++;
                    estado = 1;
                }
                if (c==-1)
                    estado = 13;
                break;
            case 3:
                if (Character.isDigit(c))
                {
                    lex.token += (char)c;
                    estado = 3;
                }
                else if (c == '.')
                {
                    lex.token += (char)c;
                    lex.type = TokenType.FLOATV;
                    estado = 31;
                }
                else
                {
                    input.unread(c);
                    estado = 13;
                }
                break;
            case 31:
                if (Character.isDigit(c))
                {
                    lex.token += (char)c;
                    estado = 33;
                }
                else{
                    lex.type = TokenType.INVALID_TOKEN;
                    estado = 13;
                }
                break;
            case 33:
                if (Character.isDigit(c))
                {
                    lex.token += (char)c;
                    estado = 33;
                }
                else
                {
                    input.unread(c);
                    estado = 13;
                }
                break;
            case 4:
                if (c=='=')
                {
                    lex.token += (char)c;
                    estado = 12;
                }
                else
                {
                    lex.type = TokenType.INVALID_TOKEN;
                    estado = 13;
                }
                break;
            case 5:
                if (c=='>'||c=='=')
                {
                    lex.token += (char) c;
                    estado = 12;
                }
                else
                {
//                    if (c != -1)
                        input.unread(c);
                    estado = 12;
                }
                break;
            case 6:
                if (c=='=')
                {
                    lex.token += (char)c;
                    estado = 12;
                }
                else
                {
                    input.unread(c);
                    estado = 12;
                }
                break;
            case 7:
//                System.out.println("case 7:");
                if (Character.isDigit(c) ||  Character.isLetter(c))
                {
                    lex.token += (char) c;
                    estado = 7;
                }
                else
                {
                    if (c != -1)
                        input.unread(c); //ungetc
                    estado = 12;
                }
                break;
            case 8:
//                System.out.println("case 8:");
                if (c!='"')
                {
                    if (c == '\n')
                        line++;
                    else if (c == -1){
                        lex.type = TokenType.UNEXPECTED_EOF;
                        estado = 13;
                        break;
                    }
                    lex.token += (char)c;
                    estado = 8;
                }
                else
                    estado = 13;
                break;
            case 9:
                if (Character.isLetter(c))
                {
                    lex.token += (char)c;
                    estado = 11;
                }
                else
                {
                    input.unread(c);
                    estado = 12;
                }
                break;
            case 10:
                if (Character.isDigit(c)|| Character.isLetter(c))
                {
                    lex.token += (char)c;
                    estado = 11;
                }
                break;
//                else
            case 11:
                if (!Character.isDigit(c)&&!Character.isLetter(c)){
                    input.unread(c);
                    estado = 13;
                }
                else
                    lex.token += (char)c;
                break;
            }
            
        }
        if (estado == 12)
        {
//            System.out.println("case 12: " + lex.token);
            TokenType type = st.find(lex.token);
            if (type != TokenType.INVALID_TOKEN){
                lex.type = type;
            }
            st.put(lex);
//            System.out.println("lex.type: " + lex.type);
        }
        if (estado == 13)
        {
//            System.out.println("case 13:");
        }
        return lex;
    }
}