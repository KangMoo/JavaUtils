package reflection;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.CastExpr;
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

    public static TypeValuePair exec(String methodCallExpr) {
        List<MethodCallExpr> scopes = new ArrayList<>();

        MethodCallExpr exStmt = StaticJavaParser.parseExpression(methodCallExpr).asMethodCallExpr();

        if (exStmt.isMethodCallExpr()) scopes.add(exStmt);
        if (exStmt.hasScope()) {
            Expression scope = exStmt.getScope().orElse(null);
            while (scope != null) {
                if (scope.isMethodCallExpr()) {
                    scopes.add((MethodCallExpr) scope);
                    scope = ((MethodCallExpr) scope).getScope().orElse(null);
                } else if (scope.isFieldAccessExpr()) {
                    scope = ((FieldAccessExpr) scope).getScope();
                } else {
                    break;
                }
            }
        }

        if (scopes.isEmpty()) return null;
        TypeValuePair object = null;
        Expression expression = scopes.get(scopes.size() - 1).getScope().orElse(null);
        if (expression == null) return null;
        for (int i = scopes.size() - 1; i >= 0; i--) {
            MethodCallExpr scope = scopes.get(i);
            object = exec(object, scope);
        }
        return object;
    }

    public static TypeValuePair exec(TypeValuePair typeValuePair, MethodCallExpr methodCallExpr) {
        try {
            Class<?> rclass = null;
            if (typeValuePair == null || typeValuePair.type == null) {
                rclass = Class.forName(methodCallExpr.getScope().get().toString());
            } else {
                rclass = Class.forName(typeValuePair.type.getTypeName());
            }
            Object[] args = null;
            Class<?>[] paramTypes = null;
            if (!methodCallExpr.getArguments().isEmpty()) {
                args = new Object[methodCallExpr.getArguments().size()];
                paramTypes = new Class[args.length];
                TypeValuePair[] tvps = getObjects(methodCallExpr.getArguments());
                if (tvps != null) {
                    for (int i = 0; i < tvps.length; i++) {
                        args[i] = tvps[i].value;
                        paramTypes[i] = tvps[i].type;
                    }
                }
            }
            Method method = rclass.getMethod(methodCallExpr.getName().toString(), paramTypes);
            return new TypeValuePair(method.getReturnType(), method.invoke(typeValuePair == null ? null : typeValuePair.value, args));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TypeValuePair[] getObjects(List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty()) return null;
        TypeValuePair[] res = new TypeValuePair[expressions.size()];
        for (int i = 0; i < res.length; i++) {
            String arg = expressions.get(i).toString();
            res[i] = getObject(arg);
        }
        return res;
    }

    public static TypeValuePair getObject(String arg) {
        Expression expression = StaticJavaParser.parseExpression(arg);
        if (expression.isDoubleLiteralExpr())
            return new TypeValuePair(Double.class, expression.asDoubleLiteralExpr().asDouble());
        if (expression.isLongLiteralExpr())
            return new TypeValuePair(Long.class, expression.asLongLiteralExpr().asNumber().longValue());
        if (expression.isBooleanLiteralExpr())
            return new TypeValuePair(Boolean.class, expression.asBooleanLiteralExpr().getValue());
        if (expression.isIntegerLiteralExpr())
            return new TypeValuePair(Integer.class, expression.asIntegerLiteralExpr().asNumber().intValue());
        if (expression.isStringLiteralExpr())
            return new TypeValuePair(String.class, expression.asStringLiteralExpr().asString());
        if (expression.isCastExpr()) {
            CastExpr castExpr = expression.asCastExpr();
            String type = castExpr.getType().toString();
            String value = castExpr.getExpression().toString();
            try {
                if (castExpr.getType().isPrimitiveType()) {
                    switch (type) {
                        case "byte":
                            return new TypeValuePair(byte.class, Byte.parseByte(value));
                        case "short":
                            return new TypeValuePair(short.class, Short.parseShort(value));
                        case "int":
                            if (value.contains("."))
                                return new TypeValuePair(int.class, (int) Double.parseDouble(value));
                            else return new TypeValuePair(int.class, Integer.parseInt(value));
                        case "float":
                            return new TypeValuePair(float.class, Float.parseFloat(value));
                        case "long":
                            if (value.contains("."))
                                return new TypeValuePair(long.class, (long) Double.parseDouble(value));
                            else return new TypeValuePair(long.class, Long.parseLong(value));
                        case "double":
                            return new TypeValuePair(double.class, Double.parseDouble(value));
                        case "char":
                            return new TypeValuePair(char.class, value.charAt(0));
                        case "boolean":
                            return new TypeValuePair(boolean.class, Boolean.parseBoolean(value));
                        default:
                            break;
                    }
                } else {
                    Class<?> typeClass = Class.forName(castExpr.getType().toString());
                    new TypeValuePair(typeClass, typeClass.cast(castExpr.getType().asTypeParameter().getName().toString()));
                }
            } catch (Exception ignored) {
            }
        }
        TypeValuePair res = exec(arg);
        if (res == null) return null;
        return new TypeValuePair(res.type, res.value);
    }

    public static class TypeValuePair {
        public final Class<?> type;
        public final Object value;

        public TypeValuePair(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}
