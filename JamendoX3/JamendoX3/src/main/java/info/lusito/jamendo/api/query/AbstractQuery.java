/*
 * Copyright (c) 2014 Santo Pfingsten
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose, including commercial
 * applications, and to alter it and redistribute it freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not claim that you wrote the
 *    original software. If you use this software in a product, an acknowledgment in the product
 *    documentation would be appreciated but is not required.
 *
 * 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being
 *    the original software.
 *
 * 3. This notice may not be removed or altered from any source distribution.
 */

package info.lusito.jamendo.api.query;

import info.lusito.jamendo.android.utils.JLog;
import info.lusito.jamendo.api.annotations.JamQueryIgnore;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.annotations.JamResultList;
import info.lusito.jamendo.api.annotations.JamResultMap;
import info.lusito.jamendo.api.annotations.JamResultSingleValue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import info.lusito.jamendo.api.results.Header;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractQuery<RT, QT extends AbstractQuery<RT, QT>> {

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd");
    protected static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    private static final String API_URL_BASE = "http://api.jamendo.com/v3.0/";
    protected Integer limit;
    protected Integer offset;
    @JamQueryIgnore
    protected boolean ignoreCache;

    @SuppressWarnings("unchecked")
    public QT limit(int value) {
        limit = value;
        return (QT) this;
    }

    @SuppressWarnings("unchecked")
    public QT offset(int value) {
        offset = value;
        return (QT) this;
    }

    @SuppressWarnings("unchecked")
    public QT ignoreCache(boolean value) {
        ignoreCache = value;
        return (QT) this;
    }

    public boolean isIgnoreCache() {
        return ignoreCache;
    }

    protected void parseHeader(Header header, JsonParser parser) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        readObject(header, Header.class, parser);
    }

    protected <T> LinkedList<T> readList(Class<T> clazz, JsonParser parser) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, ParseException {
        if (clazz == null) {
            throw new RuntimeException("readList: null clazz");
        }
        if (parser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
            throw new AssertionError(parser.getCurrentToken().name());
        }

        LinkedList<T> list = new LinkedList<T>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            list.add(readObject(clazz, parser));
        }
        return list;
    }

    protected <T> Map<String, T> readMap(Class<T> clazz, String defaultKey, JsonParser parser) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, ParseException {
        if (clazz == null) {
            throw new RuntimeException("readMap: null clazz");
        }
        JsonToken currentToken = parser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_NULL) {
            return null;
        }
        Map<String, T> map = new HashMap<String, T>();
        if (currentToken != JsonToken.START_OBJECT) {
            map.put(defaultKey, readObject(clazz, parser));
            System.out.println("Using default key '" + defaultKey + "' for map " + currentToken.name());
        } else {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String headerField = parser.getCurrentName();
                parser.nextToken();

                map.put(headerField, readObject(clazz, parser));
            }
        }
        return map;
    }

    private void ignoreObject(JsonParser parser) throws IOException {
        JsonToken token = parser.getCurrentToken();
        if (token == JsonToken.START_OBJECT) {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                parser.nextToken();
                ignoreObject(parser);
            }
        } else if (token == JsonToken.START_ARRAY) {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                ignoreObject(parser);
            }
        }
    }

    protected <T> void readObject(T object, Class<T> clazz, JsonParser parser) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = parser.getCurrentName();
            parser.nextToken();

            try {
                Field field = clazz.getField(fieldname);
                JamResultList listAnnotation = (JamResultList) field.getAnnotation(JamResultList.class);
                JamResultMap mapAnnotation = (JamResultMap) field.getAnnotation(JamResultMap.class);
                if (listAnnotation != null) {
                    field.set(object, readList(listAnnotation.value(), parser));
                } else if (mapAnnotation != null) {
                    field.set(object, readMap(mapAnnotation.type(), mapAnnotation.defaultKey(), parser));
                } else {
                    field.set(object, readObject(field.getType(), parser));
                }
            } catch (NoSuchFieldException e) {
                // Ignore the field
                ignoreObject(parser);
                System.out.println("Unknown field: " + fieldname + " (" + clazz.getName() + ")");
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T readObject(Class<T> clazz, JsonParser parser) throws InstantiationException, IllegalAccessException, IOException, NoSuchFieldException, ParseException {
        T object;
        switch (parser.getCurrentToken()) {
            case START_OBJECT:
                object = clazz.newInstance();
                readObject(object, clazz, parser);
                return object;
            case VALUE_STRING:
                if (clazz.isEnum()) {
                    String name = parser.getText().toUpperCase();
                    try {
                        return (T) Enum.valueOf((Class<Enum>) clazz, name);
                    } catch(IllegalArgumentException e) {
                        JLog.w("Could not find enum value for: " + name);
                        return null;
                    }
                } else if (clazz.equals(String.class)) {
                    return (T) parser.getText();
                } else if (clazz.equals(Date.class)) {
                    String text = parser.getText();
                    if (text.length() > 10) {
                        return (T) DATE_TIME_FORMAT.parse(text);
                    } else {
                        return (T) DATE_FORMAT.parse(text);
                    }
                } else if (clazz.equals(Integer.class)) {
                    return (T) Integer.valueOf(parser.getText());
                } else if (clazz.equals(Float.class)) {
                    return (T) Float.valueOf(parser.getText());
                } else if (clazz.equals(Boolean.class)) {
                    String text = parser.getText();
                    return (T) ("1".equals(text) ? Boolean.valueOf(true) : Boolean.valueOf(text));
                } else {
                    JamResultSingleValue valueAnnotation = (JamResultSingleValue) clazz.getAnnotation(JamResultSingleValue.class);
                    if (valueAnnotation != null) {
                        object = clazz.newInstance();
                        Field field = clazz.getField(valueAnnotation.value());
                        field.set(object, parser.getText());
                        return object;
                    }
                }
                throw new AssertionError(parser.getCurrentToken().name());
            case VALUE_NUMBER_INT:
                if (clazz.equals(Integer.class)) {
                    return (T) Integer.valueOf(parser.getIntValue());
                }
                if (clazz.equals(Float.class)) {
                    return (T) Float.valueOf(parser.getIntValue());
                }
                throw new AssertionError(parser.getCurrentToken().name());
            case VALUE_NUMBER_FLOAT:
                if (clazz.equals(Float.class)) {
                    return (T) Float.valueOf(parser.getFloatValue());
                }
                throw new AssertionError(parser.getCurrentToken().name());
            case VALUE_TRUE:
                if (clazz.equals(Boolean.class)) {
                    return (T) Boolean.TRUE;
                }
                throw new AssertionError(parser.getCurrentToken().name());
            case VALUE_FALSE:
                if (clazz.equals(Boolean.class)) {
                    return (T) Boolean.FALSE;
                }
                throw new AssertionError(parser.getCurrentToken().name());
            case VALUE_NULL:
                return null;
            default:
                throw new AssertionError(parser.getCurrentToken().name());
        }
    }

    public abstract LinkedList<RT> get(Header header, Class<RT> clazz) throws IOException, UnsupportedEncodingException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException;

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        for (Field field : type.getDeclaredFields()) {
            fields.add(field);
        }

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public String createUrl() throws UnsupportedEncodingException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        @SuppressWarnings("rawtypes")
        Class<? extends AbstractQuery> clazz = getClass();
        StringBuilder sb = new StringBuilder(API_URL_BASE);

        JamQuery pathAnnotation = (JamQuery) clazz.getAnnotation(JamQuery.class);
        sb.append(pathAnnotation.value());
        sb.append("?client_id=");
        sb.append(QuerySettings.CLIENT_ID);
        sb.append("&format=json");

        // Check all declared, non-static fields
        for (Field field : getAllFields(new LinkedList<Field>(), clazz)) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(JamQueryIgnore.class)) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value != null) {
                    sb.append("&");
                    sb.append(field.getName());
                    sb.append("=");
                    appendEncodedValue(sb, value);
                }
            }
        }

        return sb.toString();
    }

    private void appendEncodedValue(StringBuilder sb, Object value) throws UnsupportedEncodingException {
        if (value instanceof List) {
            // A list separated by '+'
            List<?> list = (List<?>) value;
            for (Object o : list) {
                sb.append(URLEncoder.encode(o.toString(), "UTF-8"));
                sb.append('+');
            }

            // Remove last '+'
            int len = sb.length() - 1;
            if (len >= 0) {
                sb.setLength(len);
            }
        } else {
            sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
    }
}
