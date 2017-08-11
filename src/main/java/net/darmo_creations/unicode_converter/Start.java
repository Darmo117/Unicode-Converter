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

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.version.Version;

/**
 * Apllication's starting class.
 *
 * @author Damien Vergnet
 */
public class Start {
  public static final Version CURRENT_VERSION = new Version(1, 0, 0, true);

  public static void main(String[] args) {
    Language language = ConfigDao.getInstance().load();

    System.out.println(language);
    try {
      I18n.init(Start.class.getResourceAsStream("/assets/langs/" + language.getLocale() + ".lang"));
    }
    catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not load lang file!", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      JOptionPane.showMessageDialog(null, I18n.getLocalizedString("popup.laf_error.text"), I18n.getLocalizedString("popup.error.title"),
          JOptionPane.ERROR_MESSAGE);
    }

    new MainFrame(language).setVisible(true);
  }
}
