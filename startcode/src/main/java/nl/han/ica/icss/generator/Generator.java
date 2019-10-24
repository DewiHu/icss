package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

import java.util.ArrayList;

public class Generator {

	public String generate(AST ast) {
		return generate(ast.root);
	}

	private String generate(ASTNode root) {
		ArrayList<ASTNode> children = root.getChildren();
		StringBuilder builder = new StringBuilder();
		for (ASTNode child: children) {
			if (child instanceof Stylerule) {
				String stylerule = generateStylerule((Stylerule) child);
				builder.append(stylerule);
			}
			if (!child.getChildren().isEmpty()) generate(child);
		}
		return builder.toString();
	}

	private String generateStylerule(Stylerule astNode) {
		StringBuilder builder = new StringBuilder();
		for (Selector s : astNode.selectors) {
			builder.append(s.toString());
			builder.append(" {\n");
			for (ASTNode child : astNode.body) {
				builder.append((generateDeclaration((Declaration) child)));
			}
			builder.append("}\n\n");
		}

		return builder.toString();
	}

	private String generateDeclaration(Declaration astNode) {
		Expression expression = astNode.expression;
		StringBuilder builder = new StringBuilder();

		builder.append("\t");
		builder.append(astNode.property.name);
		builder.append(": ");
		if (expression instanceof ColorLiteral) builder.append(((ColorLiteral) expression).value);
		if (expression instanceof PercentageLiteral) {
			PercentageLiteral percentageLiteral = (PercentageLiteral) expression;
			builder.append(percentageLiteral.value);
			builder.append("%");
		}
		if (expression instanceof PixelLiteral) {
			PixelLiteral pixelLiteral = (PixelLiteral) expression;
			builder.append(pixelLiteral.value);
			builder.append("px");
		}
		builder.append(";\n");
		return builder.toString();
	}
}