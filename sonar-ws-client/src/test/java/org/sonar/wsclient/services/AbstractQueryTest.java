/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.wsclient.services;

import static junit.framework.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.wsclient.JdkUtils;

public class AbstractQueryTest {

  @BeforeClass
  public static void before() {
    WSUtils.setInstance(new JdkUtils());
  }

  @AfterClass
  public static void after() {
    WSUtils.setInstance(null);
  }

  @Test
  public void appendSpecialChars() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", "should escape []()");
    assertEquals("foo=should+escape+%5B%5D%28%29&", url.toString());
  }

  @Test
  public void appendSpecialCharsInArray() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", new String[] { "should escape", "[]()" });
    assertEquals("foo=should+escape,%5B%5D%28%29&", url.toString());
  }

  @Test
  public void appendUrlParameter() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", "bar");
    assertEquals("foo=bar&", url.toString());
    AbstractQuery.appendUrlParameter(url, "foo2", "bar2");
    assertEquals("foo=bar&foo2=bar2&", url.toString());
  }

  @Test
  public void appendUrlBooleanParameter() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", Boolean.TRUE);
    assertEquals("foo=true&", url.toString());
  }

  @Test
  public void appendUrlIntParameter() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", 9);
    assertEquals("foo=9&", url.toString());
  }

  @Test
  public void appendUrlArrayParameter() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", new String[] { "bar", "bar2" });
    assertEquals("foo=bar,bar2&", url.toString());
  }

  @Test
  public void appendUrlNullParameter() {
    StringBuilder url = new StringBuilder();
    AbstractQuery.appendUrlParameter(url, "foo", null);
    assertEquals("", url.toString());
  }

  @Test
  public void appendUrlDateParameter() throws ParseException {
    StringBuilder url = new StringBuilder();
    Date date = new SimpleDateFormat("dd/MM/yyyy").parse("25/12/2009");
    AbstractQuery.appendUrlParameter(url, "date", date, false);
    assertEquals("date=2009-12-25&", url.toString());
  }

  @Test
  public void appendUrlDateTimeParameter() throws ParseException {
    TimeZone defaultTimeZone = TimeZone.getDefault();
    try {
      TimeZone.setDefault(TimeZone.getTimeZone("PST"));
      StringBuilder url = new StringBuilder();
      Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("25/12/2009 15:59");
      AbstractQuery.appendUrlParameter(url, "date", date, true);
      assertEquals("date=2009-12-25T15%3A59%3A00-0800&", url.toString());

    } finally {
      TimeZone.setDefault(defaultTimeZone);
    }
  }
}
