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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import info.lusito.jamendo.api.results.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class AbstractQueryObject<RT, QT extends AbstractQueryObject<RT, QT>> extends AbstractQuery<RT, QT> {

    public LinkedList<RT> get(Header header, Class<RT> clazz) throws IOException, UnsupportedEncodingException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException {
        String url = createUrl();

        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));

            parser = factory.createParser(in);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = parser.getCurrentName();
                parser.nextToken();
                if ("headers".equals(fieldname)) {
                    parseHeader(header, parser);
                    if (clazz == null) {
                        return null;
                    }
                } else if ("results".equals(fieldname)) {
                    LinkedList<RT> list = new LinkedList<RT>();
                    list.add(readObject(clazz, parser));
                    return list;
                } else {
                    throw new IllegalStateException("Unrecognized field '" + fieldname + "'!");
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (parser != null) {
                parser.close();
            }
        }

        return null;
    }
}
