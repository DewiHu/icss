package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class EvalExpressions implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    public EvalExpressions() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        evalNode(ast.root);
    }

    private void evalNode(ASTNode root) {
        ArrayList<ASTNode> children = root.getChildren();
        for (ASTNode child : children) {
            if (child instanceof VariableAssignment) addVariableToList((VariableAssignment) child);
            if (child instanceof IfClause || child instanceof Declaration) transformExpression(child);
            if (!child.getChildren().isEmpty()) evalNode(child);
        }
    }

    private void addVariableToList(VariableAssignment astNode) {
        HashMap<String, Literal> map = new HashMap<>();
        String name = astNode.name.name;
        if (!(astNode.expression instanceof Literal)) transformExpression(astNode.expression);
        if (astNode.expression instanceof BoolLiteral) map.put(name, (BoolLiteral) astNode.expression);
        if (astNode.expression instanceof ColorLiteral) map.put(name, (ColorLiteral) astNode.expression);
        if (astNode.expression instanceof PercentageLiteral) map.put(name, (PercentageLiteral) astNode.expression);
        if (astNode.expression instanceof PixelLiteral) map.put(name, (PixelLiteral) astNode.expression);
        if (astNode.expression instanceof ScalarLiteral) map.put(name, (ScalarLiteral) astNode.expression);
        variableValues.add(map);
    }

    private void transformExpression(ASTNode astNode) {
        ArrayList<ASTNode> children = astNode.getChildren();
        for (ASTNode child : children) {
            if (child instanceof VariableReference) {
                astNode.removeChild(child);
                child = transformVariableReference((VariableReference) child);
                astNode.addChild(child);
            }
            if (child instanceof Operation) {
                astNode.removeChild(child);
                child = transformOperation((Operation) child);
                astNode.addChild(child);
            }
        }

    }

    private Literal transformVariableReference(VariableReference astNode) {
        Literal value = null;
        for (HashMap map : variableValues) {
            String name = astNode.name;
            if (map.containsKey(name)) value = (Literal) map.get(name);
        }
        return value;
    }

    private Literal transformOperation(Operation astNode) {
        Expression left = astNode.lhs;
        Expression right = astNode.rhs;

        if (!(left instanceof Literal)) astNode.removeChild(left);
        if (!(right instanceof Literal)) astNode.removeChild(right);
        if (left instanceof VariableReference) astNode.lhs = transformVariableReference((VariableReference) left);
        if (right instanceof VariableReference) astNode.rhs = transformVariableReference((VariableReference) right);
        if (left instanceof Operation) astNode.lhs = transformOperation((Operation) left);
        if (right instanceof Operation) astNode.rhs = transformOperation((Operation) right);

        return outcome(astNode);
    }

    private Literal outcome(Operation operation) {
        Expression left = operation.lhs;
        Expression right = operation.rhs;
        Literal literal = null;
        int value;

        if (operation instanceof AddOperation) {
            if (left instanceof PercentageLiteral) {
                value = ((PercentageLiteral) left).value + ((PercentageLiteral) right).value;
                literal = new PercentageLiteral(value);
            }
            if (left instanceof PixelLiteral) {
                value = ((PixelLiteral) left).value + ((PixelLiteral) right).value;
                literal = new PixelLiteral(value);
            }
            if (left instanceof ScalarLiteral) {
                value = ((ScalarLiteral) left).value + ((ScalarLiteral) right).value;
                literal = new ScalarLiteral(value);
            }
        }
        if (operation instanceof SubtractOperation) {
            if (left instanceof PercentageLiteral) {
                value = ((PercentageLiteral) left).value - ((PercentageLiteral) right).value;
                literal = new PercentageLiteral(value);
            }
            if (left instanceof PixelLiteral) {
                value = ((PixelLiteral) left).value - ((PixelLiteral) right).value;
                literal = new PixelLiteral(value);
            }
            if (left instanceof ScalarLiteral) {
                value = ((ScalarLiteral) left).value - ((ScalarLiteral) right).value;
                literal = new ScalarLiteral(value);
            }
        }

        if (operation instanceof MultiplyOperation) {
            if (left instanceof ScalarLiteral && right instanceof ScalarLiteral) {
                value = ((ScalarLiteral) left).value * ((ScalarLiteral) right).value;
                literal = new ScalarLiteral(value);
            } else if (left instanceof ScalarLiteral) {
                literal = multiply((ScalarLiteral) left, right);
            } else if (right instanceof ScalarLiteral) {
                literal = multiply((ScalarLiteral) right, left);

            }
        }
        return literal;
    }

    private Literal multiply(ScalarLiteral scalar, Expression expression) {
        int value;
        Literal output = null;
        if (expression instanceof PercentageLiteral) {
            value = scalar.value * ((PercentageLiteral) expression).value;
            output = new PercentageLiteral(value);
        }
        if (expression instanceof PixelLiteral) {
            value = scalar.value * ((PixelLiteral) expression).value;
            output = new PixelLiteral(value);
        }
        return output;
    }
}

