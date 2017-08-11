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
package net.darmo_creations.unicode_converter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController extends KeyAdapter implements ActionListener {
  private static final Pattern LANG_PATTERN = Pattern.compile("lang-(\\w+)");

  private MainFrame frame;
  private Language language;

  public MainController(MainFrame frame, Language language) {
    this.frame = frame;
    this.language = language;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    Matcher m = LANG_PATTERN.matcher(cmd);

    if ("about".equals(cmd)) {
      this.frame.showAboutDialog();
    }
    else if (m.matches()) {
      changeLanguage(Language.fromCode(m.group(1)));
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    String name = ((Component) e.getSource()).getName();

    switch (name) {
      case MainFrame.CHARACTER_FLD_NAME:
        char c = this.frame.getCharacter();
        this.frame.setDecimalCode(c);
        this.frame.setHexadecimalCode(Integer.toHexString(c));
        break;
      case MainFrame.DECIMAL_CODE_FLD_NAME:
        try {
          int i = Integer.parseInt(this.frame.getDecimalCode());
          this.frame.setCharacter((char) i);
          this.frame.setHexadecimalCode(Integer.toHexString(i));
        }
        catch (NumberFormatException ex) {}
        break;
      case MainFrame.HEXA_CODE_FLD_NAME:
        try {
          int i = Integer.parseInt(this.frame.getHexadecimalCode(), 16);
          this.frame.setCharacter((char) i);
          this.frame.setDecimalCode(i);
        }
        catch (NumberFormatException ex) {}
        break;
    }
  }

  private void changeLanguage(Language desired) {
    if (desired != this.language) {
      // TODO
    }
  }
}
