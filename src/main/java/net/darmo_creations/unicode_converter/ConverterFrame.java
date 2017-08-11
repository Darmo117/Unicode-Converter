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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Darmo
 */
public class ConverterFrame extends JFrame implements KeyListener, ActionListener {
  private static final long serialVersionUID = 3742949793870868995L;

  public static final Font font = new Font("Tahoma", Font.PLAIN, 13);

  private AboutDialog aboutDialog;
  private Locale locale;

  public ConverterFrame() {
    this.aboutDialog = new AboutDialog(this);
    this.locale = getPreferredLanguage();

    setSize(290, 150);
    setResizable(false);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setIconImage(new ImageIcon(getClass().getResource("/net/darmo/resources/images/frameIcon.png")).getImage());

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}

    // <editor-fold desc="Fields init" defaultstate="collapsed">
    this.menuBar = new JMenuBar();
    this.langsMenu = new JMenu();
    this.langItems = new LinkedHashMap<>();
    this.helpMenu = new JMenu();
    this.aboutItem = new JMenuItem();

    this.labelsPanel = new JPanel();
    this.fieldsPanel = new JPanel();

    this.charLbl = new JTextField(1);
    this.decLbl = new JTextField(1);
    this.hexLbl = new JTextField(1);
    this.charField = new JTextField();
    this.decField = new JTextField();
    this.hexField = new JTextField();
    // </editor-fold>

    // <editor-fold desc="Menu bar" defaultstate="collapsed">
    this.langItems.put("en", new JRadioButtonMenuItem("English"));
    this.langItems.get("en").setActionCommand("lang en");
    this.langItems.get("en").setSelected(this.locale.getLanguage().equals("en"));
    this.langItems.put("fr", new JRadioButtonMenuItem("fran�ais"));
    this.langItems.get("fr").setActionCommand("lang fr");
    this.langItems.get("fr").setSelected(this.locale.getLanguage().equals("fr"));

    ButtonGroup group = new ButtonGroup();

    this.langsMenu.setFont(font);
    for (JRadioButtonMenuItem i : this.langItems.values()) {
      i.setFont(font);
      i.addActionListener(this);
      group.add(i);
      this.langsMenu.add(i);
    }

    this.aboutItem.setFont(font);
    this.aboutItem.setActionCommand("about");
    this.aboutItem.addActionListener(this);

    this.helpMenu.setFont(font);
    this.helpMenu.add(this.aboutItem);

    this.menuBar.add(this.langsMenu);
    this.menuBar.add(this.helpMenu);
    setJMenuBar(this.menuBar);
    // </editor-fold>

    // <editor-fold desc="Labels" defaultstate="collapsed">
    this.charLbl.setBackground(new Color(239, 239, 239));
    this.charLbl.setBorder(null);
    this.charLbl.setFont(font);
    this.charLbl.setDisabledTextColor(Color.BLACK);
    this.charLbl.setEnabled(false);

    this.decLbl.setBackground(new Color(239, 239, 239));
    this.decLbl.setBorder(null);
    this.decLbl.setFont(font);
    this.decLbl.setDisabledTextColor(Color.BLACK);
    this.decLbl.setEnabled(false);

    this.hexLbl.setBackground(new Color(239, 239, 239));
    this.hexLbl.setBorder(null);
    this.hexLbl.setFont(font);
    this.hexLbl.setDisabledTextColor(Color.BLACK);
    this.hexLbl.setEnabled(false);
    // </editor-fold>

    // <editor-fold desc="Fields" defaultstate="collapsed">
    this.charField.setFont(font);
    this.charField.setActionCommand("char");
    this.charField.addKeyListener(this);

    this.decField.setFont(font);
    this.decField.setActionCommand("dec");
    this.decField.addKeyListener(this);

    this.hexField.setFont(font);
    this.hexField.setActionCommand("hex");
    this.hexField.addKeyListener(this);
    // </editor-fold>

    setFieldsText();

    // <editor-fold desc="Labels panel layout" defaultstate="collapsed">
    GroupLayout labelsPanelLayout = new GroupLayout(this.labelsPanel);

    this.labelsPanel.setLayout(labelsPanelLayout);
    labelsPanelLayout.setAutoCreateGaps(true);
    labelsPanelLayout.setAutoCreateContainerGaps(true);
    labelsPanelLayout.setHorizontalGroup(
        labelsPanelLayout.createParallelGroup().addComponent(this.charLbl).addComponent(this.decLbl).addComponent(this.hexLbl));
    labelsPanelLayout.setVerticalGroup(
        labelsPanelLayout.createSequentialGroup().addComponent(this.charLbl).addComponent(this.decLbl).addComponent(this.hexLbl));
    // </editor-fold>

    // <editor-fold desc="Fields panel layout" defaultstate="collapsed">
    GroupLayout fieldsPanelLayout = new GroupLayout(this.fieldsPanel);

    this.fieldsPanel.setLayout(fieldsPanelLayout);
    fieldsPanelLayout.setAutoCreateGaps(true);
    fieldsPanelLayout.setAutoCreateContainerGaps(true);
    fieldsPanelLayout.setHorizontalGroup(
        fieldsPanelLayout.createParallelGroup().addComponent(this.charField).addComponent(this.decField).addComponent(this.hexField));
    fieldsPanelLayout.setVerticalGroup(
        fieldsPanelLayout.createSequentialGroup().addComponent(this.charField).addComponent(this.decField).addComponent(this.hexField));
    // </editor-fold>

    // <editor-fold desc="General layout" defaultstate="collapsed">
    GroupLayout layout = new GroupLayout(getContentPane());

    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(this.labelsPanel).addComponent(this.fieldsPanel));
    layout.setVerticalGroup(layout.createParallelGroup().addComponent(this.labelsPanel).addComponent(this.fieldsPanel));
    // </editor-fold>
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    boolean langChanged = false;

    switch (e.getActionCommand()) {
      case "lang en":
        this.locale = new Locale("en");
        langChanged = true;
        break;
      case "lang fr":
        this.locale = new Locale("fr");
        langChanged = true;
        break;
      case "about":
        this.aboutDialog.setVisible(true);
        break;
    }

    if (langChanged) {
      setFieldsText();
      writePreferredLanguage();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {
    Component src = (Component) e.getSource();
    String[] texts = {this.charField.getText(), this.decField.getText(), this.hexField.getText()};

    if (src == this.charField) {
      if (texts[0].length() == 1) {
        this.decField.setText(Integer.toString((int) texts[0].charAt(0)));
        this.hexField.setText(Util.charToHex(texts[0].charAt(0)));
      }
      else {
        this.decField.setText(null);
        this.hexField.setText(null);
      }
    }
    else if (src == this.decField) {
      try {
        int code = Integer.parseInt(texts[1]);

        if (code >= 0) {
          this.charField.setText("" + Util.intToChar(code));
          this.hexField.setText(Util.intToHex(code));
        }
        else {
          this.charField.setText(null);
          this.hexField.setText(null);
        }
      }
      catch (NumberFormatException ex) {
        this.charField.setText(null);
        this.hexField.setText(null);
      }
    }
    else if (src == this.hexField) {
      if (texts[2] != null && texts[2].matches("[a-fA-F0-9]+")) {
        this.charField.setText("" + Util.hexToChar(texts[2]));
        this.decField.setText("" + Util.hexToInt(texts[2]));
      }
      else {
        this.charField.setText(null);
        this.decField.setText(null);
      }
    }
  }

  private void setFieldsText() {
    ResourceBundle msg = ResourceBundle.getBundle("net/darmo/resources/langs/lang", this.locale);

    setTitle(msg.getString("title"));
    this.charLbl.setText(msg.getString("label.character"));
    this.decLbl.setText(msg.getString("label.decimal"));
    this.hexLbl.setText(msg.getString("label.hexadecimal"));
    this.langsMenu.setText(msg.getString("menu.langs"));
    this.helpMenu.setText(msg.getString("menu.help"));
    this.aboutItem.setText(msg.getString("menu.help.about"));
    this.aboutDialog.setFieldsText(this.locale);
  }

  @SuppressWarnings("unchecked")
  private void writePreferredLanguage() {
    try (FileWriter f = new FileWriter(Util.getJarPath() + "config.json")) {
      JSONObject obj = new JSONObject();

      obj.put("lang", this.locale.getLanguage());
      f.write(obj.toJSONString());
      f.flush();
    }
    catch (IOException ex) {}
  }

  private Locale getPreferredLanguage() {
    try {
      Object obj = new JSONParser().parse(new BufferedReader(new FileReader(new File(Util.getJarPath() + "config.json"))));
      JSONObject o = ((JSONObject) obj);

      return new Locale(o.get("lang").toString());
    }
    catch (IOException | ParseException | NullPointerException ex) {
      return new Locale("en");
    }
  }

  private final JPanel labelsPanel;
  private final JPanel fieldsPanel;

  private final JTextField charLbl;
  private final JTextField decLbl;
  private final JTextField hexLbl;
  private final JTextField charField;
  private final JTextField decField;
  private final JTextField hexField;

  private final JMenuBar menuBar;
  private final JMenu helpMenu;
  private final JMenuItem aboutItem;
  private final JMenu langsMenu;
  private final Map<String, JRadioButtonMenuItem> langItems;

  public static void main(String[] args) {
    new ConverterFrame().setVisible(true);
  }
}
