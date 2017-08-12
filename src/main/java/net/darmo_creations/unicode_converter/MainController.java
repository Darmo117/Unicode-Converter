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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.JarUtil;

/**
 * Application's main controller.
 *
 * @author Damien Vergnet
 */
public class MainController extends WindowAdapter implements ActionListener, KeyListener {
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
        Optional<Character> c = this.frame.getCharacter();
        this.frame.setDecimalCode(c.isPresent() ? Optional.of((int) c.get()) : Optional.empty());
        this.frame.setHexadecimalCode(c.isPresent() ? Integer.toHexString(c.get()) : "");
        break;
      case MainFrame.DECIMAL_CODE_FLD_NAME:
        try {
          Optional<Integer> i = this.frame.getDecimalCode();
          this.frame.setCharacter(i.isPresent() ? Optional.of((char) (int) i.get()) : Optional.empty());
          this.frame.setHexadecimalCode(i.isPresent() ? Integer.toHexString(i.get()) : "");
        }
        catch (NumberFormatException ex) {}
        break;
      case MainFrame.HEXA_CODE_FLD_NAME:
        try {
          Optional<Integer> i = this.frame.getHexadecimalCodeAsInt();
          this.frame.setCharacter(i.isPresent() ? Optional.of((char) (int) i.get()) : Optional.empty());
          this.frame.setDecimalCode(i);
        }
        catch (NumberFormatException ex) {}
        break;
    }
  }

  @Override
  public void windowClosing(WindowEvent e) {
    exit();
  }

  private void changeLanguage(Language desired) {
    if (desired != this.language) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.change_language.confirm.text"));

      if (choice == JOptionPane.YES_OPTION) {
        try {
          this.language = desired;
          exit();
          JarUtil.restartApplication(".jar");
        }
        catch (IOException | URISyntaxException __) {
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.change_language.restart_error.text"));
          System.exit(0);
        }
      }

    }
  }

  private void exit() {
    ConfigDao.getInstance().save(this.language);
    this.frame.dispose();
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {}
}
