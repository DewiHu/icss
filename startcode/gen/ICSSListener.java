// Generated from C:/Users/dewi_/Documents/ICA ASD/ICA APP/icss/startcode/src/main/antlr4/nl/han/ica/icss/parser\ICSS.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ICSSParser}.
 */
public interface ICSSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 */
	void enterStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 */
	void exitStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(ICSSParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(ICSSParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(ICSSParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(ICSSParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#ifclause}.
	 * @param ctx the parse tree
	 */
	void enterIfclause(ICSSParser.IfclauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#ifclause}.
	 * @param ctx the parse tree
	 */
	void exitIfclause(ICSSParser.IfclauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ICSSParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(ICSSParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(ICSSParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#multiplyoperation}.
	 * @param ctx the parse tree
	 */
	void enterMultiplyoperation(ICSSParser.MultiplyoperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#multiplyoperation}.
	 * @param ctx the parse tree
	 */
	void exitMultiplyoperation(ICSSParser.MultiplyoperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#subtractoperation}.
	 * @param ctx the parse tree
	 */
	void enterSubtractoperation(ICSSParser.SubtractoperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#subtractoperation}.
	 * @param ctx the parse tree
	 */
	void exitSubtractoperation(ICSSParser.SubtractoperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#addoperation}.
	 * @param ctx the parse tree
	 */
	void enterAddoperation(ICSSParser.AddoperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#addoperation}.
	 * @param ctx the parse tree
	 */
	void exitAddoperation(ICSSParser.AddoperationContext ctx);
}