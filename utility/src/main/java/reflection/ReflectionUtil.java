package reflection;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtil {

    public static TypeValuePair exec(String expression) throws Exception {
        expression = expression.trim();
        if (expression.endsWith(";")) expression = expression.substring(0, expression.length() - 1);
        Expression expr = StaticJavaParser.parseExpression(expression);
        if (expr.isObjectCreationExpr()) {
            return execObjectCreationExpr(expr.asObjectCreationExpr());
        } else if (expr.isMethodCallExpr()) {
            return execMethodCallExpr(expr.asMethodCallExpr());
        } else if (expr.isNameExpr()) {
            return new TypeValuePair(Class.forName(expr.toString()), null);
        }
        return null;
    }

    public static TypeValuePair execMethodCallExpr(MethodCallExpr methodCallExpr) throws Exception {
        Class<?> clazz = null;
        TypeValuePair object = null;
        if (methodCallExpr.hasScope()) {
            Expression superExpression = methodCallExpr.getScope().get();
            object = exec(superExpression.toString());
            clazz = object == null ? null : object.type;
        }

        ReflectionUtil.TypeValuePair[] params = getObjects(methodCallExpr.getArguments());
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
        Method method = clazz.getMethod(methodCallExpr.getName().toString(), paramTypes);
        return new ReflectionUtil.TypeValuePair(method.getReturnType(), method.invoke(object == null ? null : object.value, args));
    }

    public static TypeValuePair execObjectCreationExpr(ObjectCreationExpr objectCreationExpr) throws Exception {
        Class<?> clazz = Class.forName(objectCreationExpr.getType().toString());
        ReflectionUtil.TypeValuePair[] params = getObjects(objectCreationExpr.getArguments());
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
        return new ReflectionUtil.TypeValuePair(clazz, clazz.getDeclaredConstructor(paramTypes).newInstance(args));
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
