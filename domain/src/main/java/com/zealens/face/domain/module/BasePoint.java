package com.zealens.face.domain.module;

/**
 * Created by Kyle on 11/04/2017
 */

public class BasePoint {
    public int x;
    public int y;

    public BasePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasePoint basePoint = (BasePoint) o;

        if (x != basePoint.x) return false;
        return y == basePoint.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "BasePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
