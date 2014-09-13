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

package info.lusito.jamendo.api.query.radio;

import info.lusito.jamendo.api.query.AbstractQueryList;
import info.lusito.jamendo.api.results.radio.Radio;
import info.lusito.jamendo.api.annotations.JamQuery;
import info.lusito.jamendo.api.enums.ImageSize;
import info.lusito.jamendo.api.enums.RadioOrder;
import info.lusito.jamendo.api.enums.RadioType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JamQuery(value = "radios")
public class RadioQuery extends AbstractQueryList<Radio, RadioQuery> {

    protected List<RadioOrder> order;
    protected Integer id;
    protected RadioType type;
    protected String name;
    protected ImageSize imagesize;

    public RadioQuery order(RadioOrder... values) {
        if (order == null) {
            order = new ArrayList<RadioOrder>();
        }
        order.addAll(Arrays.asList(values));
        return this;
    }

    public RadioQuery id(Integer value) {
        id = value;
        return this;
    }

    public RadioQuery type(RadioType value) {
        type = value;
        return this;
    }

    public RadioQuery name(String value) {
        name = value;
        return this;
    }

    /**
     * @param value Currently only _150 and 30 is supported
     */
    public RadioQuery imagesize(ImageSize value) {
        imagesize = value;
        return this;
    }
}
