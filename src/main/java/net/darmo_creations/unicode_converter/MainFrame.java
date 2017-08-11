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

import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import net.darmo_creations.utils.I18n;

/**
 * Application's main frame.
 * 
 * @author Damien Vergnet
 */
public class MainFrame extends JFrame {
  private static final long serialVersionUID = 3742949793870868995L;

  public static final String CHARACTER_FLD_NAME = "character-field";
  public static final String DECIMAL_CODE_FLD_NAME = "decimal-code-field";
  public static final String HEXA_CODE_FLD_NAME = "hexa-code-field";

  private AboutDialog aboutDialog;
  private final JTextField characterFld, decimalCodeFld, hexaCodeFld;

  public MainFrame(Language language) {
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setIconImage(new ImageIcon(getClass().getResource("/icons/icon.png")).getImage());

    MainController controller = new MainController(this, language);
    this.aboutDialog = new AboutDialog(this);

    setJMenuBar(getMenuBar(language, controller));

    JLabel characterLbl = new JLabel(I18n.getLocalizedString("label.character.text"));
    JLabel decimalCodeLbl = new JLabel(I18n.getLocalizedString("label.decimal_code.text"));
    JLabel hexaCodeLbl = new JLabel(I18n.getLocalizedString("label.hexadecimal_code.text"));
    this.characterFld = new JTextField();
    this.decimalCodeFld = new JTextField();
    this.hexaCodeFld = new JTextField();

    this.characterFld.setName(CHARACTER_FLD_NAME);
    this.characterFld.addKeyListener(controller);
    this.decimalCodeFld.setName(DECIMAL_CODE_FLD_NAME);
    this.decimalCodeFld.addKeyListener(controller);
    this.hexaCodeFld.setName(HEXA_CODE_FLD_NAME);
    this.hexaCodeFld.addKeyListener(controller);

    GroupLayout labelsPanelLayout = new GroupLayout(this.labelsPanel);

    this.labelsPanel.setLayout(labelsPanelLayout);
    labelsPanelLayout.setAutoCreateGaps(true);
    labelsPanelLayout.setAutoCreateContainerGaps(true);
    labelsPanelLayout.setHorizontalGroup(
        labelsPanelLayout.createParallelGroup().addComponent(this.charLbl).addComponent(this.decLbl).addComponent(this.hexLbl));
    labelsPanelLayout.setVerticalGroup(
        labelsPanelLayout.createSequentialGroup().addComponent(this.charLbl).addComponent(this.decLbl).addComponent(this.hexLbl));

    GroupLayout fieldsPanelLayout = new GroupLayout(this.fieldsPanel);

    this.fieldsPanel.setLayout(fieldsPanelLayout);
    fieldsPanelLayout.setAutoCreateGaps(true);
    fieldsPanelLayout.setAutoCreateContainerGaps(true);
    fieldsPanelLayout.setHorizontalGroup(
        fieldsPanelLayout.createParallelGroup().addComponent(this.characterFld).addComponent(this.decimalCodeFld).addComponent(
            this.hexaCodeFld));
    fieldsPanelLayout.setVerticalGroup(
        fieldsPanelLayout.createSequentialGroup().addComponent(this.characterFld).addComponent(this.decimalCodeFld).addComponent(
            this.hexaCodeFld));

    GroupLayout layout = new GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(this.labelsPanel).addComponent(this.fieldsPanel));
    layout.setVerticalGroup(layout.createParallelGroup().addComponent(this.labelsPanel).addComponent(this.fieldsPanel));

    pack();
    setLocationRelativeTo(null);
  }

  private JMenuBar getMenuBar(Language language, ActionListener actionListener) {
    JMenuBar menuBar = new JMenuBar();
    JMenu langMenu = new JMenu(I18n.getLocalizedString("menu.lang.text"));
    langMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.lang"));
    JMenuItem i;
    ButtonGroup group = new ButtonGroup();
    for (Language l : Language.values()) {
      langMenu.add(i = new JRadioButtonMenuItem(l.getName()));
      i.setActionCommand("lang-" + l.getCode());
      i.addActionListener(actionListener);
      i.setSelected(l == language);
      group.add(i);
    }

    JMenu helpMenu = new JMenu(I18n.getLocalizedString("menu.help.text"));
    helpMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.help"));
    helpMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.about.text")));
    i.addActionListener(actionListener);

    return menuBar;
  }

  public char getCharacter() {
    return this.characterFld.getText().charAt(0);
  }

  public void setCharacter(char c) {
    this.characterFld.setText("" + c);
  }

  public String getDecimalCode() {
    return this.decimalCodeFld.getText();
  }

  public void setDecimalCode(int i) {
    this.decimalCodeFld.setText("" + i);
  }

  public String getHexadecimalCode() {
    return this.hexaCodeFld.getText();
  }

  public void setHexadecimalCode(String hex) {
    this.hexaCodeFld.setText(hex);
  }

  public void showAboutDialog() {
    this.aboutDialog.setVisible(true);
  }
}
