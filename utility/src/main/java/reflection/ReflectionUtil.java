package reflection;

import com.github.javaparser.Provider;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtil {

    public static Object run(String methodCallExpr) {
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
            object = run(object, className, scope);
            className = object.getClass().getName();
        }
        return null;
    }

    public static Object run(Object object, String className, MethodCallExpr methodCallExpr) {
        try {
            Class rclass = Class.forName(className);
            Object[] args = null;
            Class[] paramTypes = null;
            if (!methodCallExpr.getArguments().isEmpty()) {
                args = new Object[methodCallExpr.getArguments().size()];
                paramTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    args[i] = getObjects(methodCallExpr.getArguments());
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
            return run(arg);
        } catch (Exception e) {
        }
        return null;
    }

    public static Object run2(String cmd) {
        String[] scmd = cmd.split("\\.");
        int methodIndex = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scmd.length; i++) {
            if (scmd[i].contains("(") && scmd[i].contains(")")) {
                methodIndex = i;
                break;
            } else {
                if (sb.length() > 0) sb.append(".");
                sb.append(scmd[i]);
            }
        }

        try {
            Class rclass = Class.forName(sb.toString());
            Object obj = null;
            for (int i = methodIndex; i < scmd.length; i++) {
                Object[] objects = new Object[10]; //getObjects(scmd[i].substring(scmd[i].indexOf('(') + 1, scmd[i].indexOf(')')));
                Class[] paramTypes = null;
                if (objects != null) {
                    paramTypes = new Class[objects.length];
                    for (int j = 0; j < objects.length; j++) {
                        paramTypes[j] = objects[j].getClass();
                    }
                }
                Method method = rclass.getMethod(scmd[i].substring(0, scmd[i].indexOf('(')), paramTypes);
                obj = method.invoke(obj, objects);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object exec(Supplier p) {
        try {
            return p.get();
        } catch (Exception e) {
            return null;
        }
    }
}
