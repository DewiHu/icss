package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        checkChildren(ast.root);
    }

    private void checkChildren(ASTNode root) {
        ArrayList<ASTNode> children = root.getChildren();
        for (ASTNode child : children) {
            if (child instanceof VariableAssignment) addVariableToList((VariableAssignment) child);
            checkChild(child);
            if (!child.getChildren().isEmpty()) checkChildren(child);
        }
    }

    private void addVariableToList(VariableAssignment astNode) {
        HashMap<String, ExpressionType> map = new HashMap<>();
        String name = astNode.name.name;
        if (astNode.expression instanceof BoolLiteral) map.put(name, ExpressionType.BOOL);
        if (astNode.expression instanceof ColorLiteral) map.put(name, ExpressionType.COLOR);
        if (astNode.expression instanceof PercentageLiteral) map.put(name, ExpressionType.PERCENTAGE);
        if (astNode.expression instanceof PixelLiteral) map.put(name, ExpressionType.PIXEL);
        if (astNode.expression instanceof ScalarLiteral) map.put(name, ExpressionType.SCALAR);
        if (!(astNode.expression instanceof Literal)) map.put(name, ExpressionType.UNDEFINED);
        variableTypes.add(map);
    }

    private void checkChild(ASTNode astNode) {
        if (astNode instanceof VariableReference) checkForUndefinedVariableReference((VariableReference) astNode);
        if (astNode instanceof Declaration) checkDeclarationForInvalidValues((Declaration) astNode);
        if (astNode instanceof Operation) checkOperationForInvalidOperands((Operation) astNode);
        if (astNode instanceof IfClause) checkIfClauseCondition((IfClause) astNode);
    }

    private void checkForUndefinedVariableReference(VariableReference astNode) {
        if (containsVariableType(astNode, ExpressionType.UNDEFINED))
            astNode.setError("Variable is undefined");
    }

    private void checkDeclarationForInvalidValues(Declaration astNode) {
        String name = astNode.property.name;
        String error = "Style property has wrong value";
        Expression expression = astNode.expression;
        if (name.contains("color")) {
            if (!(expression instanceof ColorLiteral)) {
                if (expression instanceof VariableReference) {
                    VariableReference reference = (VariableReference) expression;
                    if (!containsVariableType(reference, ExpressionType.COLOR)) astNode.setError(error);
                } else astNode.setError(error);
            }
        } else {
            if (expression instanceof ColorLiteral
                    || expression instanceof BoolLiteral
                    || expression instanceof ScalarLiteral)
                astNode.setError(error);
            if (expression instanceof VariableReference) {
                VariableReference reference = (VariableReference) expression;
                if (containsVariableType(reference, ExpressionType.COLOR)
                        || containsVariableType(reference, ExpressionType.BOOL)
                        || containsVariableType(reference, ExpressionType.SCALAR))
                    astNode.setError(error);
            }
        }
    }

    private void checkOperationForInvalidOperands(Operation astNode) {
        Expression left = astNode.lhs;
        Expression right = astNode.rhs;
        ExpressionType leftET = getExpressionType(left);
        ExpressionType rightET = getExpressionType(right);
        String error = null;
        if (astNode instanceof AddOperation || astNode instanceof SubtractOperation) error = getErrorUnmatchedTypeOperands(leftET, rightET);
        if (astNode instanceof MultiplyOperation) error = getErrorNoScalarValue(leftET, rightET);
        if (leftET == ExpressionType.COLOR || rightET == ExpressionType.COLOR) error = getErrorForColorOperation(leftET, rightET);
        if (error != null) astNode.setError(error);
    }

    private void checkIfClauseCondition(IfClause astNode) {
        Expression condition = astNode.getConditionalExpression();
        String error = "Conditional condition is not a boolean";

        if (condition instanceof BoolLiteral) return;

        if (condition instanceof VariableReference) {
            VariableReference variable = (VariableReference) condition;
            if (containsVariableType(variable, ExpressionType.BOOL)) return;
            condition.setError(error);
        } else condition.setError(error);
    }

    private String getErrorUnmatchedTypeOperands(ExpressionType left, ExpressionType right) {
        if (left != right) return "Operands are not of the same type";
        return null;
    }

    private String getErrorNoScalarValue(ExpressionType left, ExpressionType right) {
        if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR) return "Multiplication is not with a scalar value";
        return null;
    }

    private String getErrorForColorOperation(ExpressionType left, ExpressionType right) {
        if (left == ExpressionType.COLOR || right == ExpressionType.COLOR) return "Operation has value(s) with color";
        return null;
    }

    private boolean containsVariableType(VariableReference astNode, ExpressionType type) {
        boolean output = false;
        for (HashMap map : variableTypes) {
            if (map.get(astNode.name) == type) output = true;
        }
        return output;
    }

    private ExpressionType getExpressionType(Expression astNode) {
        ExpressionType type = null;
        if (astNode instanceof Literal) type = getExpressionType((Literal) astNode);
        if (astNode instanceof VariableReference) type = getExpressionType((VariableReference) astNode);
        if (astNode instanceof Operation) type = getExpressionType((Operation) astNode);
        return type;
    }

    private ExpressionType getExpressionType(Literal astNode) {
        ExpressionType type = null;
        if (astNode instanceof BoolLiteral) type = ExpressionType.BOOL;
        if (astNode instanceof ColorLiteral) type = ExpressionType.COLOR;
        if (astNode instanceof PercentageLiteral) type = ExpressionType.PERCENTAGE;
        if (astNode instanceof PixelLiteral) type = ExpressionType.PIXEL;
        if (astNode instanceof ScalarLiteral) type = ExpressionType.SCALAR;
        return type;
    }

    private ExpressionType getExpressionType(VariableReference astNode) {
        ExpressionType type = null;
        for (HashMap map : variableTypes)
            if (map.containsKey(astNode.name)) type = (ExpressionType) map.get(astNode.name);
        return type;
    }

    private ExpressionType getExpressionType(Operation astNode) {
        Expression left = astNode.lhs;
        Expression right = astNode.rhs;
        ExpressionType leftET = getExpressionType(left);
        ExpressionType rightET = getExpressionType(right);

        /*
        It removes scalar expression type from the operation.
        When both expressions are scalar, it does not matter;
         */
        if (left instanceof ScalarLiteral) leftET = rightET;
        if (right instanceof ScalarLiteral) rightET = leftET;

        /*
        When both expression have the same type, return the expression type.
        Else return null.
         */
        if (leftET == rightET) return leftET;
        return null;
    }
}
