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
        String variableReference = removeBracket(ctx.CAPITAL_IDENT().toString());
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
        String propertyName = removeBracket(ctx.LOWER_IDENT().toString());
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
        if (ctx.LOWER_IDENT() != null) astNode = new TagSelector(ctx.LOWER_IDENT().getText());
        if (ctx.ID_IDENT() != null) astNode = new IdSelector(ctx.ID_IDENT().getText());
        if (ctx.CLASS_IDENT() != null) astNode = new ClassSelector(ctx.CLASS_IDENT().getText());
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
        BoolLiteral condition = new BoolLiteral(ctx.expression().toString());
        astNode.addChild(condition);
        currentContainer.peek().addChild(astNode);
        currentContainer.push(astNode);
    }

    @Override
    public void exitIfclause(ICSSParser.IfclauseContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterExpression(ICSSParser.ExpressionContext ctx) {
    }

    @Override
    public void exitExpression(ICSSParser.ExpressionContext ctx) {
    }

    @Override
    public void enterLiteral(ICSSParser.LiteralContext ctx) {
        Expression astNode = null;
        if (ctx.CAPITAL_IDENT() != null) astNode = new VariableReference(ctx.CAPITAL_IDENT().getText());
        if (ctx.COLOR() != null) astNode = new ColorLiteral(ctx.COLOR().getText());
        if (ctx.PIXELSIZE() != null) astNode = new PixelLiteral(ctx.PIXELSIZE().getText());
        if (ctx.SCALAR() != null) astNode = new ScalarLiteral(ctx.SCALAR().getText());
        if (ctx.PERCENTAGE() != null) astNode = new PercentageLiteral(ctx.PERCENTAGE().getText());
        if (ctx.TRUE() != null) astNode = new BoolLiteral(ctx.TRUE().getText());
        if (ctx.FALSE() != null) astNode = new BoolLiteral(ctx.FALSE().getText());
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

    /*
    Removing brackets for VariableReference;
    */
    private String removeBracket(String input) {
        return input.substring(1, input.length() - 1);
    }
}
