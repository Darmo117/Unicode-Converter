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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.I18n;

/**
 * Application's main frame.
 * 
 * @author Damien Vergnet
 */
public class MainFrame extends ApplicationFrame {
  private static final long serialVersionUID = 3742949793870868995L;

  public static final String CHARACTER_FLD_NAME = "character-field";
  public static final String DECIMAL_CODE_FLD_NAME = "decimal-code-field";
  public static final String HEXA_CODE_FLD_NAME = "hexa-code-field";

  private JTextField characterFld, decimalCodeFld, hexaCodeFld;

  public MainFrame(WritableConfig config) {
    super(config, true, false, true, false, new Dimension(300, 150), false);
  }

  @Override
  protected ApplicationController preInit(WritableConfig config) {
    return new MainController(this, config);
  }

  @Override
  protected void initContent(ApplicationController controller, WritableConfig config) {
    MainController c = (MainController) controller;

    this.characterFld = new JTextField(15);
    this.decimalCodeFld = new JTextField(15);
    this.hexaCodeFld = new JTextField(15);

    this.characterFld.setName(CHARACTER_FLD_NAME);
    this.characterFld.addKeyListener(c);
    this.decimalCodeFld.setName(DECIMAL_CODE_FLD_NAME);
    this.decimalCodeFld.addKeyListener(c);
    this.hexaCodeFld.setName(HEXA_CODE_FLD_NAME);
    this.hexaCodeFld.addKeyListener(c);

    JPanel content = getContentPanel();

    content.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    content.add(new JLabel(I18n.getLocalizedString("label.character.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 10, 0, 10);
    content.add(this.characterFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    content.add(new JLabel(I18n.getLocalizedString("label.decimal_code.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    content.add(this.decimalCodeFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    content.add(new JLabel(I18n.getLocalizedString("label.hexadecimal_code.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 5, 10);
    content.add(this.hexaCodeFld, gbc);
  }

  public Optional<Character> getCharacter() {
    String text = this.characterFld.getText();
    return text.length() == 1 ? Optional.of(text.charAt(0)) : Optional.empty();
  }

  public void setCharacter(Optional<Character> c) {
    if (c.isPresent())
      this.characterFld.setText("" + c.get());
    else
      this.characterFld.setText(null);
  }

  public Optional<Integer> getDecimalCode() {
    try {
      return Optional.of(Integer.parseInt(this.decimalCodeFld.getText()));
    }
    catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }

  public void setDecimalCode(Optional<Integer> i) {
    if (i.isPresent())
      this.decimalCodeFld.setText("" + i.get());
    else
      this.decimalCodeFld.setText("");
  }

  public Optional<Integer> getHexadecimalCodeAsInt() {
    try {
      return Optional.of(Integer.parseInt(this.hexaCodeFld.getText().toUpperCase(), 16));
    }
    catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }

  public void setHexadecimalCode(String hex) {
    this.hexaCodeFld.setText(hex);
  }

  @Override
  public int showConfirmDialog(String localizedString) {
    return JOptionPane.showConfirmDialog(this, localizedString, I18n.getLocalizedString("popup.confirm.title"), JOptionPane.YES_NO_OPTION);
  }

  @Override
  public void showErrorDialog(String localizedString) {
    JOptionPane.showMessageDialog(this, localizedString, I18n.getLocalizedString("popup.error.title"), JOptionPane.ERROR_MESSAGE);
  }
}
