package stone.src.chap6;
import java.text.ParseException;

import stone.*;
import stone.src.BasicParser;
import stone.src.Lexer;
import stone.src.Token;
import stone.src.ast.ASTree;
import stone.src.ast.NullStmnt;
import stone.src.test.CodeDialog;

public class BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new BasicEnv(), new BasicEnv());
    }

    public static void run(BasicParser bp, Environment env) throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        while (lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if (!(t instanceof NullStmnt)) {
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                System.out.println("=> " + r);
            }
        }
    }

}