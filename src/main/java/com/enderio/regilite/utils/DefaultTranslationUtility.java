/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DefaultTranslationUtility {
    public static String getDefaultTranslationFrom(String name) {
        return Arrays.stream(name.split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }
}
