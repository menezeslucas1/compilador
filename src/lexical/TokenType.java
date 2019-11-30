package lexical;

public enum TokenType {
    // special tokens
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,

    // symbols
    ASSIGN,
    DOT_COMMA,
    COMMA,
//    OPEN_CUR,
 //   CLOSE_CUR,
    OPEN_PAR,
    CLOSE_PAR,

    // keywords
    START,
    EXIT,
    INT,
    FLOAT,
    STRING,
    IF,
    THEN,
    ELSE,
    END,
    DO,
    WHILE,
    SCAN,
    PRINT,
    NOT,
    OR,
    AND,

    // operators
    EQUAL,
    GREATER, //HIGHER_THAN,
    GREATER_EQ, //HIGHER_EQUAL,
    LESS,//LOWER_THAN,
    LESS_EQ, //LOWER_EQUAL,
    DIFF,
    ADD,
    SUB,
    MUL,
    DIV,

    // others
    ID,
    INTV,
    FLOATV
};