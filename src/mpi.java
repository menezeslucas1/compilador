import lexical.LexicalAnalysis;
import lexical.TokenType;
import lexical.LexicalException;
//import lexical.SymbolTable;

import syntatic.SyntaticAnalysis;

import java.io.IOException;

public class mpi {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("modo de uso: java -jar mpi.jar [arquivo fonte]");
            return;
        }
        try{
            LexicalAnalysis l = new LexicalAnalysis(args[0]);
            SyntaticAnalysis s = new SyntaticAnalysis(l);
            s.start();
            
//            System.out.println("\n******************"
//                             + "\nInício da execução"
//                             + "\n******************\n");
//            c.execute();
        }
        /*
        try (LexicalAnalysis lex = new LexicalAnalysis(args[0])) {
            System.out.println("\t nome\tvalor\n");
            Lexeme current = lex.nextToken();
            do{
                current.print();
                if (current.type == TokenType.INVALID_TOKEN || current.type == TokenType.UNEXPECTED_EOF)
                    System.out.println("Erro na linha: " + lex.getLine() + "\n");
                current = lex.nextToken();
            }while(current.type != TokenType.END_OF_FILE);

            System.out.println("\nTabela de símbolos");
            lex.printTable();
        }
        */
        catch (IOException e){
            System.err.println("Internal error: " + e.getMessage());
        } catch (LexicalException le){
            System.err.println(le.getMessage());
        }
        /*
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        */
//        catch (InvalidOperationException ioe) {
//            System.err.println(ioe.getMessage());
//        }
        
    }

    private static boolean checkType(TokenType type) {
        return !(type == TokenType.END_OF_FILE ||
                 type == TokenType.INVALID_TOKEN ||
                 type == TokenType.UNEXPECTED_EOF);
    }
}
