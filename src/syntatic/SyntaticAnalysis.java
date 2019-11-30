package syntatic;

import java.io.IOException;
import lexical.Lexeme;
import lexical.TokenType;
import lexical.LexicalAnalysis;
import semantic.*;
import java.util.HashMap;
import java.util.Map;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;
//    private Map<String, Variable> global;
    private Map<String, BasicType> global;

    public SyntaticAnalysis(LexicalAnalysis lex) throws IOException {
        this.lex = lex;
        this.current = lex.nextToken();
        this.global = new HashMap<String, BasicType>();
    }

    public void start() throws IOException {
        procProgram();
        matchToken(TokenType.END_OF_FILE);
    }

    private void matchToken(TokenType type) throws IOException {
//	System.out.println("match token: " + type + " == " + current.type);
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            System.out.printf("%3d: token esperado: %s\n", lex.getLine(), type);
//            System.out.println(lex.getLine() + ": token esperado: " + type);
            showError();
            do{
                current = lex.nextToken();
            }while (current.type != type &&
                    current.type != TokenType.END_OF_FILE &&
                    current.type != TokenType.UNEXPECTED_EOF);
//            System.out.println("match token: " + type + " == " + current.type);
            current = lex.nextToken();
        }
    }

    private void showError() {
        switch (current.type) {
            case INVALID_TOKEN:
                // Imprimir erro lexico (1)
//                System.out.println(lex.getLine() + ": Token inválido: "+current.token);
                System.out.printf("   : Token inválido: %s\n", current.token);
//                System.exit(1);
                return;
//                break;
            case UNEXPECTED_EOF:
                // Imprimir erro lexico (2)
//                System.out.println(lex.getLine() + ": Fim de arquivo inesperado: ");
                System.out.printf("   : Fim de arquivo inesperado\n");
//                System.exit(2);
                return;
//                break;
            case END_OF_FILE:
                // imprimir erro sintático (2)
//                System.out.println(lex.getLine() + ": Fim de arquivo inesperado: ");
                System.out.printf("   : Fim de arquivo inesperado\n");
//                System.exit(2);
                return;
//                break;
            default:
                // imprimir erro sintático (1)
                break;
        }
//	System.out.println("ERROR: " + current.token + ": " + current.type);
        System.out.printf("   : Token inesperado: %s\n", current.token);
//        System.out.println(lex.getLine() + ": Token inesperado: "+current.token);
//        System.exit(1);
//        return;
    }

    //program ::= start [decl-list] stmt-list exit
    private void procProgram() throws IOException {
        matchToken(TokenType.START);
        while (current.type != TokenType.INT &&
            current.type != TokenType.FLOAT &&
            current.type != TokenType.STRING &&
            current.type != TokenType.ID &&
            current.type != TokenType.IF &&
            current.type != TokenType.DO &&
            current.type != TokenType.SCAN &&
            current.type != TokenType.PRINT){
            if (current.type == TokenType.END_OF_FILE)
                return;
            System.out.printf("%3d: esperado comando ou declaração\n", lex.getLine());
            System.out.printf("     Token encontrado: %s\n", current.token);
//            System.out.println(lex.getLine() + ": esperado comando "+
//                               "ou declaração");
//            System.out.println(lex.getLine() + ": Token encontrado: " +
//                               current.token);
            current = lex.nextToken();
        }
            
        if (current.type == TokenType.INT ||
            current.type == TokenType.FLOAT ||
            current.type == TokenType.STRING)
            procDeclList();
        procStmtList();
        matchToken(TokenType.EXIT);
    }

    //decl-list ::= decl {decl}
    private void procDeclList() throws IOException {
        procDecl();
        while(true){
            switch (current.type) {
            case INT:
            case FLOAT:
            case STRING:
                procDecl();
                break;
            case ID:
            case IF:
            case DO:
            case SCAN:
            case PRINT:
            case END_OF_FILE:
                return;
            default:
                System.out.printf("%3d: esperado comando ou declaração\n", lex.getLine());
                System.out.printf("     Token encontrado: %s\n", current.token);
                current = lex.nextToken();
            }
        }
    }

    //decl ::= type ident-list ";"
    private void procDecl() throws IOException {
        BasicType type;
        type = procType();
        procIdentList(type);
        matchToken(TokenType.DOT_COMMA);
    }
    
    //ident-list ::= identifier {"," identifier}
    private void procIdentList(BasicType type) throws IOException {
        if (!global.containsKey(current.token))
            global.put(current.token, type);
        else
            System.out.printf("%3d: variável já declarada\n", lex.getLine());
        matchToken(TokenType.ID);
        while(true){
            switch (current.type) {
            case COMMA:
                matchToken(TokenType.COMMA);
                if (!global.containsKey(current.token))
                    global.put(current.token, type);
                else
                    System.out.printf("%3d: variável já declarada\n", lex.getLine());
                matchToken(TokenType.ID);
                break;
            case DOT_COMMA:
            case END_OF_FILE:
                return;
            default:
                System.out.printf("%3d: esperado , \n", lex.getLine());
                System.out.printf("   : Token encontrado: %s\n", current.token);
                current = lex.nextToken();
                continue;
            }
        }
    }
    
    //type ::= int | float | string
    private BasicType procType() throws IOException {
        switch (current.type){
        case INT:
            matchToken(TokenType.INT);
            return BasicType.intValue;
        case FLOAT:
            matchToken(TokenType.FLOAT);
            return BasicType.floatValue;
        case STRING:
            matchToken(TokenType.STRING);
            return BasicType.literal;
        }
    }
    
    //stmt-list ::= stmt {stmt}
    private void procStmtList() throws IOException {
        procStmt();
        while(true){
            if (current.type == TokenType.ID ||
                current.type == TokenType.IF ||
                current.type == TokenType.DO ||
                current.type == TokenType.SCAN ||
                current.type == TokenType.PRINT)
                procStmt();
            else if (current.type == TokenType.EXIT ||
                     current.type == TokenType.ELSE ||
                     current.type == TokenType.END ||
                     current.type == TokenType.WHILE ||
                     current.type == TokenType.END_OF_FILE)
                return;
            else if (current.type != TokenType.EXIT){
                System.out.printf("%3d: Token esperado: próximo comando ou fim de arquivo\n", lex.getLine());
                System.out.printf("   : Token encontrado: %s\n", current.token);
//                System.out.println(lex.getLine() + ": Token esperado: " +
//                                 "próximo comando ou fim de arquivo");
//                System.out.println(lex.getLine() + ": Token encontrado: " +
//                                   current.token);
                current = lex.nextToken();
                continue;
            }
        }
    }
    
    //stmt ::= assign-stmt ";" | if-stmt | while-stmt
    //         | read-stmt ";" | write-stmt ";"
    private void procStmt() throws IOException {
        switch(current.type){
        case ID:
            procAssignStmt();
            matchToken(TokenType.DOT_COMMA);
            break;
        case IF:
            procIfStmt();
            break;
        case DO:
            procWhileStmt();
            break;
        case SCAN:
            procReadStmt();
            matchToken(TokenType.DOT_COMMA);
            break;
        case PRINT:
            procWriteStmt();
            matchToken(TokenType.DOT_COMMA);
            break;
        }
    }
    
    //assign-stmt ::= identifier "=" simple_expr
    private void procAssignStmt() throws IOException {
//        procIdentifier();
        matchToken(TokenType.ID);
        matchToken(TokenType.ASSIGN);
        procSimpleExpr();
    }
    
    //if-stmt ::=   if condition then stmt-list end
    //            | if condition then stmt-list else stmt-list end
    private void procIfStmt() throws IOException {
        matchToken(TokenType.IF);
        procCondition();
        matchToken(TokenType.THEN);
        procStmtList();
        if(current.type == TokenType.ELSE){
            matchToken(TokenType.ELSE);
            procStmtList();
        }
        matchToken(TokenType.END);
    }
    
    //condition ::= expression
    private void procCondition() throws IOException {
        procExpression();
    }
    
    //while-stmt ::= do stmt-list stmt-sufix
    private void procWhileStmt() throws IOException {
        matchToken(TokenType.DO);
        procStmtList();
        procStmtSufix();
    }
    
    //stmt-sufix ::= while condition end
    private void procStmtSufix() throws IOException {
        matchToken(TokenType.WHILE);
        procCondition();
        matchToken(TokenType.END);
    }
    
    //read-stmt ::= scan "(" identifier ")"
    private void procReadStmt() throws IOException {
        matchToken(TokenType.SCAN);
        matchToken(TokenType.OPEN_PAR);
        matchToken(TokenType.ID);
        matchToken(TokenType.CLOSE_PAR);
    }
    
    //write-stmt ::= print "(" writable ")"
    private void procWriteStmt() throws IOException {
        matchToken(TokenType.PRINT);
        matchToken(TokenType.OPEN_PAR);
        procWritable();
        matchToken(TokenType.CLOSE_PAR);
    }
    
    //writable ::= simple-expr
    private void procWritable() throws IOException {
            procSimpleExpr();
    }

    //expression  ::= simple-expr [relop simple-expr]
    private void procExpression() throws IOException {
        procSimpleExpr();
        if(current.type == TokenType.EQUAL ||
           current.type == TokenType.GREATER ||
           current.type == TokenType.GREATER_EQ ||
           current.type == TokenType.LESS ||
           current.type == TokenType.LESS_EQ ||
           current.type == TokenType.DIFF){
            procRelop();
            procSimpleExpr();
        }
    }
    
    //simple-expr ::= term s-expr-cont
    private void procSimpleExpr() throws IOException {
        procTerm();
        procSExprCont();
    }
    
    //s-expr-cont ::= [addop term s-expr-cont]
    private void procSExprCont() throws IOException {
        if (current.type == TokenType.ADD ||
            current.type == TokenType.SUB ||
            current.type == TokenType.OR){
            procAddop();
            procTerm();
            procSExprCont();
        }
    }
    
    //term ::= factor-a term-cont
    private void procTerm() throws IOException {
        procFactorA();
        procTermCont();
    }
    
    //term-cont ::= [mulop factor-a term-cont]
    private void procTermCont() throws IOException {
        if (current.type == TokenType.MUL ||
            current.type == TokenType.DIV ||
            current.type == TokenType.AND){
            procMulop();
            procFactorA();
            procTermCont();
        }
    }
    
    //fator-a ::= factor 
    //          | not factor 
    //          | "-" factor
    private void procFactorA() throws IOException {
        do{
            switch (current.type) {
            case ID:
            case INTV:
            case FLOATV:
            case STRING:
            case OPEN_PAR:
                procFactor();
                return;
            case NOT:
                matchToken(TokenType.NOT);
                procFactor();
                return;
            case SUB:
                matchToken(TokenType.SUB);
                procFactor();
                return;
            default:
                System.out.printf("%3d: Token esperado: identificador, número, string\n", lex.getLine());
                System.out.printf("   : Token encontrado: %s\n", current.token);

    //            System.out.println(lex.getLine() + ": Token esperado: " +
    //                             "identificador, número, string,");
    //            System.out.println(lex.getLine() + ": Token encontrado: " +
    //                               current.token);
                while(current.type != TokenType.ID &&
                       current.type != TokenType.INTV &&
                       current.type != TokenType.FLOATV &&
                       current.type != TokenType.STRING &&
                       current.type != TokenType.OPEN_PAR &&
                       current.type != TokenType.END_OF_FILE){
                    current = lex.nextToken();
                }
                if (current.type == TokenType.END_OF_FILE)
                    return;
                break;
            }
        }while(true);
    }
    
    //factor ::= identifier 
    //         | constant 
    //         | "(" expression ")"
    private BasicType procFactor() throws IOException {
        BasicType type;
        switch (current.type) {
        case ID:
            type = global.get(current.type);
            matchToken(TokenType.ID);
            return type;
        case INTV:
        case FLOATV:
        case STRING:
            type = procConstant();
            return type;
        case OPEN_PAR:
            matchToken(TokenType.OPEN_PAR);
            type = procExpression();
            matchToken(TokenType.CLOSE_PAR);
            return type;
            break;
        default:
//            System.out.println(lex.getLine() + ": Token esperado: identificador, numero ou string");
            break;
        }
    }
    
    //relop ::= "==" 
    //        | ">" 
    //        | ">=" 
    //        | "<" 
    //        | "<=" 
    //        | "<>"
    private OperatorType procRelop() throws IOException {
        switch (current.type) {
        case EQUAL:
            matchToken(TokenType.EQUAL);
            return OperatorType.eq;
        case GREATER:
            matchToken(TokenType.GREATER);
            return OperatorType.gt;
        case GREATER_EQ:
            matchToken(TokenType.GREATER_EQ);
            return OperatorType.ge;
        case LESS:
            matchToken(TokenType.LESS);
            return OperatorType.lt;
        case LESS_EQ:
            matchToken(TokenType.LESS_EQ);
            return OperatorType.le;
        case DIFF:
            matchToken(TokenType.DIFF);
            return OperatorType.ne;
        default:
            return OperatorType.error;
        }
    }
    
    //addop ::= "+" 
    //        | "-" 
    //        | or
    private OperatorType procAddop() throws IOException {
        switch (current.type) {
        case ADD:
            matchToken(TokenType.ADD);
            return OperatorType.add;
        case SUB:
            matchToken(TokenType.SUB);
            return OperatorType.sub;
        case OR:
            matchToken(TokenType.OR);
            return OperatorType.or;
        default:
            return OperatorType.error;
        }
    }
    
    //mulop ::= "*" 
    //        | "/" 
    //        | and
    private OperatorType procMulop() throws IOException {
        switch (current.type) {
        case MUL:
            matchToken(TokenType.MUL);
            return OperatorType.mul;
        case DIV:
            matchToken(TokenType.DIV);
            return OperatorType.div;
        case AND:
            matchToken(TokenType.AND);
            return OperatorType.and;
        default:
            return OperatorType.error;
        }
    }
    
    //constant ::= integer_const 
    //           | float_const 
    //           | literal
    private BasicType procConstant() throws IOException {
        switch (current.type) {
        case INTV:
            matchToken(TokenType.INTV);
            return BasicType.intValue;
        case FLOATV:
            matchToken(TokenType.FLOATV);
            return BasicType.floatValue;
        case STRING:
            matchToken(TokenType.STRING);
            return BasicType.literal;
        default:
            return BasicType.erro;
        }
    }
}