/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Unicode Converter.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.unicode_converter.converter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Damien Vergnet
 */
public class Util {
  /**
   * Converts an hexadecimal value into decimal. If the argument is not in a valid hexadecimal
   * format, a <code>NumberFormatException</code> will be thrown. If the argument is
   * <code>null</code>, an <code>IllegalArgumentException</code> will be thrown.
   * 
   * @param hex the value to convert
   * @return the decimal value of the argument
   * @throws NumberFormatException
   */
  public static int hexToInt(String hex) throws NumberFormatException {
    if (hex == null)
      throw new IllegalArgumentException("null");

    hex = hex.toUpperCase();
    if (!hex.matches("[A-F0-9]+"))
      throw new NumberFormatException();

    int res = 0;
    String digits = "0123456789ABCDEF";

    for (int i = 0, j = hex.length() - 1; j >= 0; i++, j--)
      res += Math.pow(16, i) * digits.indexOf(hex.charAt(j));

    return res;
  }

  /**
   * Returns th hexadecimal code of the argument character.
   * 
   * @param c the character
   * @return the hexadecimal Unicode value of the argument
   */
  public static String charToHex(char c) {
    return intToHex((int) c);
  }

  /**
   * Converts a decimal value into hexadecimal. If the argument is less than <code>0</code>, an
   * <code>IllegalArgumentException</code> will be thrown.
   * 
   * @param n the value to convert
   * @return the hexadecimal representation of the argument
   */
  public static String intToHex(int n) {
    if (n < 0)
      throw new IllegalArgumentException("< 0");

    String digits = "0123456789ABCDEF";
    List<Integer> mods = new ArrayList<>();
    StringBuilder str = new StringBuilder();

    while (n > 15) {
      int modulo, quotient;

      quotient = n / 16;
      modulo = n % 16;
      n = quotient;
      mods.add(modulo);
    }
    str.append(digits.charAt(n));

    for (int i = mods.size() - 1; i >= 0; i--)
      str.append(digits.charAt(mods.get(i)));

    return str.toString();
  }

  /**
   * Returns the character with the given Unicode value. If the argument is less than
   * <code>0</code>, an <code>IllegalArgumentException</code> will be thrown.
   * 
   * @param n the Unicode value
   * @return the character corresponding to the code
   */
  public static char intToChar(int n) {
    if (n < 0)
      throw new IllegalArgumentException("< 0");

    return (char) n;
  }

  /**
   * Returns the character with the given hexadecimal Unicode value. If the argment is not in a
   * valid hexadecimal format, a <code>NumberFormatException</code> will be thrown. If the argument
   * is <code>null</code> an <code>IllegalArgumentException </code> will be thrown.
   * 
   * @param hex the hexadecimal Unicode value
   * @return the character corresponding to the code
   * @throws NumberFormatException
   */
  public static char hexToChar(String hex) throws NumberFormatException {
    if (hex == null)
      throw new IllegalArgumentException("null");
    hex = hex.toLowerCase();
    if (!hex.matches("[a-f0-9]+"))
      throw new NumberFormatException();

    return intToChar(hexToInt(hex));
  }

  public static String getJarPath() {
    String jarPath = null;
    URL url = Util.class.getProtectionDomain().getCodeSource().getLocation();

    try {
      jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
    }
    catch (UnsupportedEncodingException ex) {}

    String parentPath = new File(jarPath).getParent();
    parentPath += File.separator;

    return parentPath;
  }

  private Util() {}
}
