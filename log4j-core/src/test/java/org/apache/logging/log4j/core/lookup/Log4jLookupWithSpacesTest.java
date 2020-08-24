/*
 * Copyright 2015 Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationAware;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.apache.logging.log4j.core.lookup.Log4jLookup.KEY_CONFIG_LOCATION;
import static org.apache.logging.log4j.core.lookup.Log4jLookup.KEY_CONFIG_PARENT_LOCATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Log4jLookupWithSpacesTest {

    private final static File EXPECT = new File(System.getProperty("user.home"), "/a a/b b/c c/d d/e e/log4j2 file.xml");
    @Mock
    private LoggerContext mockCtx;
    @Mock
    private Configuration config;
    @Mock
    private ConfigurationSource configSrc;

    @BeforeEach
    public void setup() {
        ContextAnchor.THREAD_CONTEXT.set(mockCtx);
        given(config.getConfigurationSource()).willReturn(configSrc);
        given(configSrc.getFile()).willReturn(EXPECT);
    }

    @AfterEach
    public void cleanup() {
        ContextAnchor.THREAD_CONTEXT.set(null);
    }

    @Test
    public void lookupConfigLocation_withSpaces() {
        final StrLookup log4jLookup = new Log4jLookup();
        ((ConfigurationAware) log4jLookup).setConfiguration(config);
        final String value = log4jLookup.lookup(KEY_CONFIG_LOCATION);
        assertEquals(
                new File(System.getProperty("user.home"), "/a a/b b/c c/d d/e e/log4j2 file.xml").getAbsolutePath(),
                value);
    }

    @Test
    public void lookupConfigParentLocation_withSpaces() {
        final StrLookup log4jLookup = new Log4jLookup();
        ((ConfigurationAware) log4jLookup).setConfiguration(config);
        final String value = log4jLookup.lookup(KEY_CONFIG_PARENT_LOCATION);
        assertEquals(new File(System.getProperty("user.home"), "/a a/b b/c c/d d/e e").getAbsolutePath(), value);
    }
}
