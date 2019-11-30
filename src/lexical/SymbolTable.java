package lexical;

import java.util.Map;
import java.util.HashMap;

class SymbolTable {

    private Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<String, TokenType>();
        
        // FIXME: Add the tokens here.
        // st.put("???", TokenType.???);
        st.put("=", TokenType.ASSIGN);
        st.put(";", TokenType.DOT_COMMA);
        st.put(",", TokenType.COMMA);
        st.put("(", TokenType.OPEN_PAR);
        st.put(")", TokenType.CLOSE_PAR);
        st.put("start", TokenType.START);
        st.put("exit", TokenType.EXIT);
        st.put("int", TokenType.INT);
        st.put("float", TokenType.FLOAT);
        st.put("string", TokenType.STRING);
        st.put("if", TokenType.IF);
        st.put("then", TokenType.THEN);
        st.put("else", TokenType.ELSE);
        st.put("end", TokenType.END);
        st.put("do", TokenType.DO);
        st.put("while", TokenType.WHILE);
        st.put("scan", TokenType.SCAN);
        st.put("print", TokenType.PRINT);
        st.put("not", TokenType.NOT);
        st.put("or", TokenType.OR);
        st.put("and", TokenType.AND);
        st.put("==", TokenType.EQUAL);
        st.put("!=", TokenType.DIFF);
        st.put("<", TokenType.LESS);
        st.put(">", TokenType.GREATER);
        st.put("<=", TokenType.LESS_EQ);
        st.put(">=", TokenType.GREATER_EQ);
        st.put("+", TokenType.ADD);
        st.put("-", TokenType.SUB);
        st.put("*", TokenType.MUL);
        st.put("/", TokenType.DIV);
    }

    public boolean contains(String token) {
        return st.containsKey(token);
    }

    public TokenType find(String token) {
        return this.contains(token) ?
            st.get(token) : TokenType.INVALID_TOKEN;
    }
    public void put(Lexeme lex){
        st.put(lex.token, lex.type);
    }
    public void print(){
        for (String key : st.keySet()){
            System.out.printf("%13s", key);
            System.out.println("\t" + st.get(key).toString());
        }
    }
}