package reflection;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtil {

    public static Object exec(String methodCallExpr) {
        Stack<MethodCallExpr> scopes = new Stack<>();

        MethodCallExpr exStmt = StaticJavaParser.parseExpression(methodCallExpr).asMethodCallExpr();

        if (exStmt.isMethodCallExpr()) scopes.push(exStmt);
        if (exStmt.hasScope()) {
            Expression scope = exStmt.getScope().orElse(null);
            while (scope != null) {
                if (scope.isMethodCallExpr()) {
                    scopes.push((MethodCallExpr) scope);
                    scope = ((MethodCallExpr) scope).getScope().orElse(null);
                } else if (scope.isFieldAccessExpr()) {
                    scope = ((FieldAccessExpr) scope).getScope();
                } else {
                    break;
                }
            }
        }

        if (scopes.isEmpty()) return null;
        Object object = null;
        Expression expression = scopes.get(scopes.size() - 1).getScope().orElse(null);
        if(expression == null) return null;
        String className = expression.toString();
        for (int i = scopes.size() - 1; i >= 0; i--) {
            MethodCallExpr scope = scopes.get(i);
            object = exec(object, className, scope);
            className = object == null ? null : object.getClass().getName();
        }
        return object;
    }

    public static Object exec(Object object, String className, MethodCallExpr methodCallExpr) {
        try {
            Class rclass = Class.forName(className);
            Object[] args = null;
            Class[] paramTypes = null;
            if (!methodCallExpr.getArguments().isEmpty()) {
                args = new Object[methodCallExpr.getArguments().size()];
                paramTypes = new Class[args.length];
                args = getObjects(methodCallExpr.getArguments());
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) paramTypes[i] = null;
                    else {

                        if (args[i] instanceof Integer) paramTypes[i] = Integer.TYPE;
                        else if (args[i] instanceof Double) paramTypes[i] = Double.TYPE;
                        else if (args[i] instanceof Float) paramTypes[i] = Float.TYPE;
                        else paramTypes[i] = args[i].getClass();
                    }
                }
            }
            Method method = rclass.getMethod(methodCallExpr.getName().toString(), paramTypes);
            return method.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] getObjects(List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty()) return null;
        Object[] res = new Object[expressions.size()];
        for (int i = 0; i < res.length; i++) {
            String arg = expressions.get(i).toString();
            res[i] = getObject(arg);
        }
        return res;
    }

    public static Object getObject(String arg) {
        if (arg.equals("null")) return null;
        if (arg.startsWith("\"") && arg.endsWith("\"")) return arg.substring(1, arg.length() - 1);
        try {
            return Integer.parseInt(arg);
        } catch (Exception ignored) {
        }
        try {
            return Double.parseDouble(arg);
        } catch (Exception ignored) {
        }
        try {
            return Float.parseFloat(arg);
        } catch (Exception ignored) {
        }
        try {
            return exec(arg);
        } catch (Exception e) {
        }
        return null;
    }

    public static List<String> getMethodCallExpr(String expr) {
        List<String> methodExprs = new ArrayList<>();

        int bracketCount = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            switch (ch) {
                case '(':
                    if (bracketCount == 0) {
                        methodExprs.add(sb.toString());
                        sb.setLength(0);
                    }
                    bracketCount++;
                    break;
                case ')':
                    bracketCount--;
                    if (bracketCount == 0) {
                        methodExprs.add(sb.toString());
                        sb.setLength(0);
                    }
                    break;
                case ',':
                    if(bracketCount == 1){
                        methodExprs.add(sb.toString());
                        sb.setLength(0);
                    }
                default:
                    break;
            }
            sb.append(ch);
        }
        methodExprs.add(sb.toString());
        return methodExprs;
    }
}
