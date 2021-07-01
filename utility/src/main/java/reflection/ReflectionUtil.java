package reflection;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtil {

    public static Object exec(String methodCallExpr) {
        Stack<MethodCallExpr> scopes = new Stack<>();

        MethodCallExpr exStmt = StaticJavaParser.parseExpression(methodCallExpr);

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
        String className = scopes.get(scopes.size() - 1).getScope().orElse(null).toString();
        for (int i = scopes.size() - 1; i >= 0; i--) {
            MethodCallExpr scope = scopes.get(i);
            object = exec(object, className, scope);
            className = object.getClass().getName();
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
                    // args[i] = getObjects(methodCallExpr.getArguments());
                    paramTypes[i] = args[i] == null ? null : args[i].getClass();
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
        if (arg.startsWith("\"") && arg.endsWith("\"")) return arg;
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
}
