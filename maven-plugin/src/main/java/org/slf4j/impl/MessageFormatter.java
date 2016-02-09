/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package org.slf4j.impl;

/**
 * Formats messages according to very simple substitution rules. Substitutions
 * can be made 1, 2 or more arguments.
 * <p>
 * For example,
 * 
 * <pre>
 * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;);
 * </pre>
 * 
 * will return the string "Hi there.".
 * <p>
 * The {} pair is called the <em>formatting anchor</em>. It serves to designate
 * the location where arguments need to be substituted within the message
 * pattern.
 * <p>
 * In the rare case where you need to place the '{' or '}' in the message
 * pattern itself but do not want them to be interpreted as a formatting
 * anchors, you can espace the '{' character with '\', that is the backslash
 * character. Only the '{' character should be escaped. There is no need to
 * escape the '}' character. For example,
 * 
 * <pre>
 * MessageFormatter.format(&quot;File name is \\{{}}.&quot;, &quot;App folder.zip&quot;);
 * </pre>
 * 
 * will return the string "File name is {App folder.zip}.". See
 * {@link #format(String, Object)}, {@link #format(String, Object, Object)} and
 * {@link #arrayFormat(String, Object[])} methods for more details.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MessageFormatter {
    static final char DELIM_START = '{';

    static final char DELIM_STOP = '}';

    /**
     * 
     */
    private MessageFormatter() {
    	// Utility class
    }
    
    /**
     * Performs single argument substitution for the 'messagePattern' passed as
     * parameter.
     * <p>
     * For example,
     * 
     * <pre>
     * MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;);
     * </pre>
     * 
     * will return the string "Hi there.".
     * <p>
     * 
     * @param messagePattern The message pattern which will be parsed and
     *            formatted
     * @return The formatted message
     */
    public static String format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg });
    }

    /**
     * Performs a two argument substitution for the 'messagePattern' passed as
     * parameter.
     * <p>
     * For example,
     * 
     * <pre>
     * MessageFormatter.format(&quot;Hi {}. My name is {}.&quot;, &quot;Alice&quot;, &quot;Bob&quot;);
     * </pre>
     * 
     * will return the string "Hi Alice. My name is Bob.".
     * 
     * @param messagePattern The message pattern which will be parsed and
     *            formatted
     * @param arg1 The argument to be substituted in place of the first
     *            formatting anchor
     * @param arg2 The argument to be substituted in place of the second
     *            formatting anchor
     * @return The formatted message
     */
    public static String format(String messagePattern, Object arg1, Object arg2) {
        return arrayFormat(messagePattern, new Object[]{arg1, arg2 });
    }

    /**
     * Same principle as the {@link #format(String, Object)} and
     * {@link #format(String, Object, Object)} methods except that any number of
     * arguments can be passed in an array.
     * 
     * @param messagePattern The message pattern which will be parsed and
     *            formatted
     * @param argArray An array of arguments to be substituted in place of
     *            formatting anchors
     * @return The formatted message
     */
    public static String arrayFormat(String messagePattern, Object[] argArray) {
        if (messagePattern == null) {
            return null;
        }
        int i = 0;
        final int len = messagePattern.length();
        int j = messagePattern.indexOf(DELIM_START);

        final StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

        for (int L = 0; L < argArray.length; L++) {

            char escape = 'x';

            j = messagePattern.indexOf(DELIM_START, i);

            if (j == -1 || j + 1 == len) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return messagePattern;
                } else { // add the tail string which contains no variables and
                    // return the result.
                    sbuf.append(messagePattern.substring(i, messagePattern.length()));
                    return sbuf.toString();
                }
            } else {
                final char delimStop = messagePattern.charAt(j + 1);
                if (j > 0) {
                    escape = messagePattern.charAt(j - 1);
                }

                if (escape == '\\') {
                    L--; // DELIM_START was escaped, thus should not be
                    // incremented
                    sbuf.append(messagePattern.substring(i, j - 1));
                    sbuf.append(DELIM_START);
                    i = j + 1;
                } else if (delimStop != DELIM_STOP) {
                    // invalid DELIM_START/DELIM_STOP pair
                    sbuf.append(messagePattern.substring(i, messagePattern.length()));
                    return sbuf.toString();
                } else {
                    // normal case
                    sbuf.append(messagePattern.substring(i, j));
                    sbuf.append(argArray[L]);
                    i = j + 2;
                }
            }
        }
        // append the characters following the second {} pair.
        sbuf.append(messagePattern.substring(i, messagePattern.length()));
        return sbuf.toString();
    }
}
