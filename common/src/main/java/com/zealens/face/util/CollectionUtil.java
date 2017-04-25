package com.zealens.face.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created on 2016/11/9
 * in BlaBla by Kyle
 */

public class CollectionUtil {
    public static <C extends Collection> void refreshWithCollection(C old, C sourceData) throws Exception {
        if (old == null || sourceData == null) return;

        old.clear();
        old.addAll(sourceData);
    }

    public static <C extends Collection> boolean isEmpty(C c) {
        return c == null || c.size() == 0;
    }

    public static <M extends Map> boolean isEmpty(M m) {
        return m == null || m.size() == 0;
    }

    public static <O extends Object> boolean isEmptyArr(O... arr) {
        return arr == null || arr.length == 0;
    }

    public static <O extends Object> boolean notEmptyArr(O... arr) {
        return !isEmptyArr(arr);
    }

    public static boolean isEmptyArr(int... arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean notEmptyArr(int... arr) {
        return !isEmptyArr(arr);
    }

    public static <C extends Collection> boolean notEmpty(C c) {
        return !isEmpty(c);
    }

    public static <M extends Map> boolean notEmpty(M m) {
        return !isEmpty(m);
    }

    public static <C extends Collection> void clear(C... cs) {
        for (Collection c : cs)
            if (c != null) c.clear();
    }

    public static <O extends Object> void clear(O[] arr) {
        Assert.assertFalse(isEmptyArr(arr));
        int len = arr.length;
        for (int i = 0; i < len; i++)
            arr[i] = null;
    }

    public static <O extends Object> void fillWithTemplate(O template, O[] arr) {
        Assert.assertFalse(isEmptyArr(arr));
        int len = arr.length;
        for (int i = 0; i < len; i++)
            arr[i] = template;
    }

    public static <O extends Object> int usedCellCount(O[] arr) {
        return usedCellCount(arr, null);
    }

    public static <O extends Object> int usedCellCount(O[] arr, O comparatorB) {
        Assert.assertFalse(isEmptyArr(arr));
        int token = 0;
        for (O o : arr)
            if (o != comparatorB) token++;
        return token;
    }

    public static <O extends Object> int usedCellCountWithRearrange(O[] arr) {
        return usedCellCountWithRearrange(arr, null);
    }

    public static <O extends Object> int usedCellCountWithRearrange(O[] arr, O filler) {
        Assert.assertFalse(isEmptyArr(arr));
        O[] temp = arr;
        int len = temp.length;
        int it = 0;
        int ia = 0;
        int count = 0;
        for (; it < len; it++) {
            if (temp[it] != filler) {
                arr[ia++] = temp[it];
                count++;
            }
        }
        while (ia < len)
            arr[ia++] = filler;
        return count;
    }

    public static <C extends Comparable> boolean containsJudgeWithBinarySearch(C[] arr, C targetValue) {
        return findIndexWithBinarySearch(arr, targetValue) >= 0;
    }

    public static <C extends Comparable> int findIndexWithBinarySearch(C[] arr, C targetValue) {
        return Arrays.binarySearch(arr, targetValue);
    }

    public static <O extends Object> boolean containsJudgeUseLoop(O[] arr, O targetO) {
        Assert.assertNotNull(targetO);
        for (O s : arr) if (targetO.equals(s)) return true;
        return false;
    }

    public static <O extends Object> int findIndexUseLoop(O[] arr, O targetO) {
        Assert.assertNotNull(targetO);
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            if (targetO.equals(arr[i])) return i;
        }
        return -1;
    }

    public static <C extends Collection> boolean indexOutOfRange(C c, int index) {
        return !indexInRange(c, index);
    }

    public static boolean indexOutOfRange(Object[] arr, int index) {
        return !indexInRange(arr, index);
    }

    public static boolean indexInRange(Object[] arr, int index) {
        return notEmptyArr(arr) && index >= 0 && index < arr.length;
    }

    public static <C extends Collection> boolean indexInRange(C c, int index) {
        return notEmpty(c) && index >= 0 && index < c.size();
    }

    public static <C extends Collection> int getSize(C c) {
        return isEmpty(c) ? 0 : c.size();
    }

    /**
     * may throws exception if class not match, be careful
     */
    public static <C extends Collection, T> void addElements(C c, T... os) {
        if (c == null || isEmptyArr(os)) return;
        for (T o : os) if (o != null) c.add(o);
    }

    public static boolean[] expandCapacityOfBooleanArray(@Nullable boolean[] src) {
        return expandCapacityOfBooleanArray(src, 1);
    }

    public static boolean[] expandCapacityOfBooleanArray(@Nullable boolean[] src, int increment) {
        if (src == null) return new boolean[1];

        int lenTook = src.length;
        boolean[] dest = new boolean[lenTook + increment];
        System.arraycopy(src, 0, dest, 0, lenTook);
        return dest;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] assembleCapacityExpandedArray(@NonNls T[] arr, int increment) {
        int oldLen = arr.length;
        T[] dest = (T[]) Array.newInstance(arr.getClass().getComponentType(), oldLen + increment);
        System.arraycopy(arr, 0, dest, 0, oldLen);
        return dest;
    }

    public static int accumulateIntegerArray(int[] scope) {
        int res = 0;
        for (int in : scope) {
            res += in;
        }
        return res;
    }
}
