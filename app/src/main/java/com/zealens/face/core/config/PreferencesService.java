package com.zealens.face.core.config;

import java.util.HashMap;

public interface PreferencesService {
    void put(final String key, final Object value);

    Object get(final String key, final Object defaultValue);

    void remove(final String key);

    void clear();

    boolean contains(final String key);

    HashMap<String, ?> getAll();
}
