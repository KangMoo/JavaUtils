package reflection;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtil2 {

    public static TypeValuePair exec(String methodCallExpr) throws Exception {
        methodCallExpr = methodCallExpr.trim();

        try {
            Expression expression = StaticJavaParser.parseExpression(methodCallExpr);
            if (expression.isObjectCreationExpr()) {
                ObjectCreationExpr objectCreationExpr = expression.asObjectCreationExpr();
                Class<?> clazz = Class.forName(objectCreationExpr.getType().toString());
                TypeValuePair[] params = getObjects(objectCreationExpr.getArguments());
                Class<?>[] paramTypes = null;
                Object[] args = null;
                if (params != null) {
                    paramTypes = new Class[params.length];
                    args = new Object[params.length];
                    for (int i = 0; i < params.length; i++) {
                        paramTypes[i] = params[i].type;
                        args[i] = params[i].value;
                    }
                }
                return new TypeValuePair(clazz, clazz.getDeclaredConstructor(paramTypes).newInstance(args));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public static TypeValuePair exec(TypeValuePair object, MethodCallExpr methodCallExpr) throws Exception {
        Class<?> rclass = null;
        if (object == null || object.type == null) {
            rclass = Class.forName(methodCallExpr.getScope().get().toString());
        } else {
            rclass = Class.forName(object.type.getTypeName());
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
        return new TypeValuePair(method.getReturnType(), method.invoke(object == null ? null : object.value, args));
    }

    public static TypeValuePair[] getObjects(List<Expression> expressions) throws Exception {
        if (expressions == null || expressions.isEmpty()) return null;
        TypeValuePair[] res = new TypeValuePair[expressions.size()];
        for (int i = 0; i < res.length; i++) {
            String arg = expressions.get(i).toString();
            res[i] = getObject(arg);
        }
        return res;
    }

    public static TypeValuePair getObject(String arg) throws Exception {
        Expression expression = StaticJavaParser.parseExpression(arg);
        if (expression.isNullLiteralExpr())
            return null;
        if (expression.isDoubleLiteralExpr())
            return new TypeValuePair(double.class, expression.asDoubleLiteralExpr().asDouble());
        if (expression.isIntegerLiteralExpr())
            return new TypeValuePair(int.class, expression.asIntegerLiteralExpr().asNumber().intValue());
        if (expression.isLongLiteralExpr())
            return new TypeValuePair(long.class, expression.asLongLiteralExpr().asNumber().longValue());
        if (expression.isBooleanLiteralExpr())
            return new TypeValuePair(boolean.class, expression.asBooleanLiteralExpr().getValue());
        if (expression.isStringLiteralExpr())
            return new TypeValuePair(String.class, expression.asStringLiteralExpr().asString());
        if (expression.isCastExpr()) {
            CastExpr castExpr = expression.asCastExpr();
            String type = castExpr.getType().toString();
            String value = castExpr.getExpression().toString();
            try {
                if (castExpr.getType().isPrimitiveType()) {
                    switch (type) {
                        case "int":
                            return value.contains(".") ? new TypeValuePair(int.class, (int) Double.parseDouble(value)) : new TypeValuePair(int.class, Integer.parseInt(value));
                        case "long":
                            return value.contains(".") ? new TypeValuePair(long.class, (long) Double.parseDouble(value)) : new TypeValuePair(long.class, Long.parseLong(value));
                        case "short":
                            return value.contains(".") ? new TypeValuePair(short.class, (short) Double.parseDouble(value)) : new TypeValuePair(short.class, Short.parseShort(value));
                        case "byte":
                            return value.contains(".") ? new TypeValuePair(short.class, (short) Double.parseDouble(value)) : new TypeValuePair(byte.class, Byte.parseByte(value));
                        case "float":
                            return new TypeValuePair(float.class, Float.parseFloat(value));
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
        return exec(arg);
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
