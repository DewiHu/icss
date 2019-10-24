package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import java.util.ArrayList;

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        removeIfClauseNodes(ast.root);
    }

    private void removeIfClauseNodes(ASTNode root) {
        ArrayList<ASTNode> children = root.getChildren();
        for (ASTNode child : children) {
            if (!child.getChildren().isEmpty()) removeIfClauseNodes(child);
            if (child instanceof IfClause) {
                BoolLiteral literal = (BoolLiteral) ((IfClause) child).conditionalExpression;
                root.removeChild(child);
                if (literal.value) for (ASTNode node: ((IfClause) child).body) root.addChild(node);
            }
        }
    }
}
