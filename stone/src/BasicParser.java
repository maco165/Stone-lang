package stone.src;
import static stone.src.Parser.rule;
import java.util.HashSet;
import stone.src.Parser.Operators;
import stone.src.ast.*;

public class BasicParser {
    HashSet<String> reserved = new Hashset<String>();
    Operators operators = new Operators();
    Parser expr0 = rule();
    Parse primary = rule(PrimaryExpr.class)
        .or(rule().sep("(")).ast(expr0).sep(")"),
            rule().number(NumberLiteral.class),
            rule().identifier(Name.class, reserved),
            rule().string(StringLiteral.class));
    Parse factor = rule().or(rule(NegativeExpr.class).sep("-").ast(primary),primary);
    Parse expr = expr0.expression(BinaryExpr.class, factor, operators);

    Parse statement0 = rule();
    Parse block = rule(BlockStmnt.class)
        .sep("{").option(statement())
        .repeat(rule().sep(";", Token.EOL).option(statement0))
        .sep("}");
    Parse simple = rule(PrimaryExpr.class).ast(expr);
    Parse statement = statement0.or(
        rule(Ifstmnt.class).sep("if").ast(expr).ast(block).option(rule().sep("else").ast(block)),
        rule(WhileStmnt.class).sep("while").ast(expr).ast(block), simple);
    
    Parse program = rule().or(statement, rule(NullStmnt.class)).sep(";", Token.EOL);

    public BasicParser() {
        reserved.add(";");
        reserved.add("}");
        reserved.add(Token.EOL);

        operators.add("=", 1, Operators.RIGHT);
        operators.add("==", 2, Operators.LEFT);
        operators.add(">", 2, Operators.LEFT);
        operators.add("<", 2, Operators.LEFT);
        operators.add("+", 3, Operators.LEFT);
        operators.add("-", 3, Operators.LEFT);
        operators.add("*", 4, Operators.LEFT);
        operators.add("/", 4, Operators.LEFT);
        operators.add("%", 4, Operators.LEFT);
    }

    public ASTree parse(Lexer lexer) throws ParseException {
        return program.parse(lexer);
    }

}
