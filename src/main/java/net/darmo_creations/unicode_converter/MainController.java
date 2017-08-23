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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;

/**
 * Application's main controller.
 *
 * @author Damien Vergnet
 */
public class MainController extends ApplicationController implements KeyListener {
  public MainController(MainFrame frame, WritableConfig config) {
    super(frame, config);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    String name = ((Component) e.getSource()).getName();
    MainFrame frame = (MainFrame) this.frame;

    switch (name) {
      case MainFrame.CHARACTER_FLD_NAME:
        Optional<Character> c = frame.getCharacter();
        frame.setDecimalCode(c.isPresent() ? Optional.of((int) c.get()) : Optional.empty());
        frame.setHexadecimalCode(c.isPresent() ? Integer.toHexString(c.get()) : "");
        break;
      case MainFrame.DECIMAL_CODE_FLD_NAME:
        try {
          Optional<Integer> i = frame.getDecimalCode();
          frame.setCharacter(i.isPresent() ? Optional.of((char) (int) i.get()) : Optional.empty());
          frame.setHexadecimalCode(i.isPresent() ? Integer.toHexString(i.get()) : "");
        }
        catch (NumberFormatException ex) {}
        break;
      case MainFrame.HEXA_CODE_FLD_NAME:
        try {
          Optional<Integer> i = frame.getHexadecimalCodeAsInt();
          frame.setCharacter(i.isPresent() ? Optional.of((char) (int) i.get()) : Optional.empty());
          frame.setDecimalCode(i);
        }
        catch (NumberFormatException ex) {}
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {}
}
