package reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtil {
    public static Object run(String cmd) {
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
                Method method = rclass.getMethod(scmd[i].substring(0, scmd[i].indexOf('(')));
                Object[] objects = getObjects(scmd[i].substring(scmd[i].indexOf('(') + 1, scmd[i].indexOf(')')));
                System.out.println("args = " + Arrays.toString(objects));
                obj = method.invoke(obj, objects);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object[] getObjects(String str) {
        boolean inString = false;
        List<String> args = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',' && !inString) {
                args.add(sb.toString());
                sb.setLength(0);
            } else if (str.charAt(i) == '"') {
                sb.append('"');
                inString = !inString;
            } else {
                sb.append(str.charAt(i));
            }
        }
        args.add(sb.toString());

        if (args.isEmpty()) return null;
        Object[] res = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            res[i] = getParsedType(args.get(i));
        }
        return res;
    }

    private static Object getParsedType(String str) {
        str = str.trim();
        if (str.startsWith("\"") && str.endsWith("\"")) return str;

        try {
            return Integer.parseInt(str);
        } catch (Exception ignored) {
        }

        try {
            return Float.parseFloat(str);
        } catch (Exception ignored) {
        }

        try {
            if(str.equals("true") || str.equals("false"))
                return Boolean.parseBoolean(str);
        } catch (Exception ignored) {
        }

        if (str.contains("(")) return run(str);
        return null;
    }
}
