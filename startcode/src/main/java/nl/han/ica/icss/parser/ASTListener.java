package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.Stack;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast;

    //Use this to keep track of the parent nodes when recursively traversing the ast
    private Stack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new Stack<>();
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet astNode = new Stylesheet();
        ast.setRoot(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        Stylerule astNode = new Stylerule();
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitStylerule(ICSSParser.StyleruleContext ctx) {
        currentContainer.pop();

    }

    @Override
    public void enterVariable(ICSSParser.VariableContext ctx) {
        VariableAssignment astNode = new VariableAssignment();
        String variableReference = ctx.CAPITAL_IDENT().toString();
        variableReference = variableReference.substring(1, variableReference.length() - 1);
        astNode.addChild(new VariableReference(variableReference));
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitVariable(ICSSParser.VariableContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        Declaration astNode = new Declaration();
        String propertyName = ctx.LOWER_IDENT().toString();
        propertyName = propertyName.substring(1, propertyName.length() - 1);
        astNode.property = new PropertyName(propertyName);
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterSelector(ICSSParser.SelectorContext ctx) {
        Selector astNode = null;
        if (ctx.LOWER_IDENT() != null) {
            astNode = new TagSelector(ctx.LOWER_IDENT().toString());
        }

        if (ctx.ID_IDENT() != null) {
            astNode = new IdSelector(ctx.ID_IDENT().toString());
        }

        if (ctx.CLASS_IDENT() != null) {
            astNode = new ClassSelector(ctx.CLASS_IDENT().toString());
        }
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitSelector(ICSSParser.SelectorContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterIfclause(ICSSParser.IfclauseContext ctx) {
        IfClause astNode = new IfClause();
        astNode.addChild(new BoolLiteral(ctx.expression().toString()));
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitIfclause(ICSSParser.IfclauseContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterExpression(ICSSParser.ExpressionContext ctx) {
//        Expression astNode = null;
//        if (ctx.multiplyoperation() != null) {
//            astNode = new MultiplyOperation();
//        }
//        if (ctx.addoperation() != null) {
//            astNode = new AddOperation();
//        }
//        if (ctx.subtractoperation() != null) {
//            astNode = new SubtractOperation();
//        }
//        if (ctx.literal().CAPITAL_IDENT() != null) {
//            astNode = new VariableReference(ctx.literal().CAPITAL_IDENT().toString());
//        }
//        if (ctx.literal().COLOR() != null) {
//            astNode = new ColorLiteral(ctx.literal().COLOR().toString());
//        }
//        if (ctx.literal().PIXELSIZE() != null) {
//            astNode = new PixelLiteral(ctx.literal().PIXELSIZE().toString());
//        }
//        if (ctx.literal().SCALAR() != null) {
//            astNode = new ScalarLiteral(ctx.literal().SCALAR().toString());
//        }
//        if (ctx.literal().PERCENTAGE() != null) {
//            astNode = new PercentageLiteral(ctx.literal().PERCENTAGE().toString());
//        }
//        if (ctx.literal().TRUE() != null) {
//            astNode = new BoolLiteral(ctx.literal().TRUE().toString());
//        }
//        if (ctx.literal().FALSE() != null) {
//            astNode = new BoolLiteral(ctx.literal().FALSE().toString());
//        }
//        currentContainer.peek().addChild(astNode);
//        currentContainer.push(astNode);
    }

    @Override
    public void exitExpression(ICSSParser.ExpressionContext ctx) {
//        currentContainer.pop();
    }

    @Override
    public void enterLiteral(ICSSParser.LiteralContext ctx) {
        Expression astNode = null;
        if (ctx.CAPITAL_IDENT() != null) {
            astNode = new VariableReference(ctx.CAPITAL_IDENT().toString());
        }
        if (ctx.COLOR() != null) {
            astNode = new ColorLiteral(ctx.COLOR().toString());
        }
        if (ctx.PIXELSIZE() != null) {
            astNode = new PixelLiteral(ctx.PIXELSIZE().toString());
        }
        if (ctx.SCALAR() != null) {
            astNode = new ScalarLiteral(ctx.SCALAR().toString());
        }
        if (ctx.PERCENTAGE() != null) {
            astNode = new PercentageLiteral(ctx.PERCENTAGE().toString());
        }
        if (ctx.TRUE() != null) {
            astNode = new BoolLiteral(ctx.TRUE().toString());
        }
        if (ctx.FALSE() != null) {
            astNode = new BoolLiteral(ctx.FALSE().toString());
        }
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitLiteral(ICSSParser.LiteralContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterMultiplyoperation(ICSSParser.MultiplyoperationContext ctx) {
        Expression astNode = new MultiplyOperation();
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitMultiplyoperation(ICSSParser.MultiplyoperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterSubtractoperation(ICSSParser.SubtractoperationContext ctx) {
        Expression astNode = new SubtractOperation();
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitSubtractoperation(ICSSParser.SubtractoperationContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterAddoperation(ICSSParser.AddoperationContext ctx) {
        Expression astNode = new AddOperation();
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitAddoperation(ICSSParser.AddoperationContext ctx) {
        currentContainer.pop();
    }

    public AST getAST() {
        return ast;
    }
}
