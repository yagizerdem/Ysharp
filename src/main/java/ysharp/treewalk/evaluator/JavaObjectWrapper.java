package ysharp.treewalk.evaluator;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.Native.YPF.Container.yContainer;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

// converts native object to ysharp compatible type
public class JavaObjectWrapper {

    private static class ConverterEntry {
        public final Class<?> from;
        public final Class<?> to;

        public ConverterEntry(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
        }
    }

    private static final List<ConverterEntry> converters = new ArrayList<>();

    public static void RegisterConvertorTypes(Class<?> from, Class<?> to) {
        converters.add(new ConverterEntry(from, to));
    }

    static public Object wrap(Object obj) {
        if (obj == null) return null;

        if (
                obj instanceof Integer ||
                        obj instanceof Character ||
                        obj instanceof Boolean ||
                        obj instanceof Double
        ) {
            return obj;
        }

        if (obj instanceof Float) {
            return Double.parseDouble(Float.toString((float) obj));
        }

        if (obj instanceof Short) {
            return Integer.parseInt(Short.toString((short) obj));
        }

        if (obj instanceof String) {
            return new yString.yStringInstance(String.valueOf(obj));
        }

        if (obj instanceof RuntimeObject) {
            return obj;
        }

        ConverterEntry bestEntry = findBestConverter(obj.getClass());

        if (bestEntry != null) {
            try {
                Constructor<?> ctor = bestEntry.to.getConstructor(bestEntry.from);
                return ctor.newInstance(obj);
            } catch (Exception e) {
                throw new YsharpException(
                        YsharpException.YsharpErrorType.PROCESS,
                        -1,
                        "JavaObjectWrapper failed to wrap "
                                + obj.getClass().getName()
                                + " as "
                                + bestEntry.to.getName()
                                + ": "
                                + e.getMessage()
                );
            }
        }

        return obj;
    }

    private static ConverterEntry findBestConverter(Class<?> objClass) {
        ConverterEntry best = null;

        for (ConverterEntry entry : converters) {
            if (!entry.from.isAssignableFrom(objClass)) {
                continue;
            }

            if (best == null) {
                best = entry;
                continue;
            }

            if (isMoreSpecific(entry.from, best.from, objClass)) {
                best = entry;
            }
        }

        return best;
    }

    private static boolean isMoreSpecific(Class<?> candidate,
                                          Class<?> currentBest,
                                          Class<?> objClass) {

        if (candidate.equals(currentBest)) {
            return false;
        }

        if (currentBest.isAssignableFrom(candidate)) {
            return true;
        }

        if (candidate.isAssignableFrom(currentBest)) {
            return false;
        }

        int candidateDistance = getTypeDistance(objClass, candidate);
        int currentBestDistance = getTypeDistance(objClass, currentBest);

        return candidateDistance < currentBestDistance;
    }

    private static int getTypeDistance(Class<?> from, Class<?> target) {
        if (from.equals(target)) {
            return 0;
        }

        int best = Integer.MAX_VALUE;

        Class<?> superClass = from.getSuperclass();

        if (superClass != null && target.isAssignableFrom(superClass)) {
            int distance = getTypeDistance(superClass, target);

            if (distance != Integer.MAX_VALUE) {
                best = Math.min(best, distance + 1);
            }
        }

        for (Class<?> iface : from.getInterfaces()) {
            if (target.isAssignableFrom(iface)) {
                int distance = getTypeDistance(iface, target);

                if (distance != Integer.MAX_VALUE) {
                    best = Math.min(best, distance + 1);
                }
            }
        }

        return best;
    }
}