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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * @author Darmo
 */
public class AboutDialog extends JDialog implements ActionListener {
  private static final long serialVersionUID = -2364155320780319844L;

  private static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
  public static final String dispatchWindowClosingActionMapKey = "WINDOW_CLOSING";

  private String TEXT_1;
  private String TEXT_2;
  private String TEXT_3;
  private final String TEXT_4 = ".";
  private final String CC_TEXT = "Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)";
  private final String CC_LINK = "http://creativecommons.org/licenses/by-nc-sa/4.0/";
  private final String DC_TEXT = "darmo-creations.net";
  private final String DC_LINK = "http://darmo-creations.net/show.php?id=2";

  public AboutDialog(Frame owner) {
    super(owner, true);

    setSize(new Dimension(360, 180));
    setLocationRelativeTo(owner);
    setResizable(false);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}

    this.title = new JLabel();
    this.title.setFont(this.title.getFont().deriveFont(20F));

    this.titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    this.titlePanel.add(this.title);

    this.license = new JEditorPane();
    this.license.setOpaque(false);
    this.license.setEditable(false);
    this.license.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.license.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));

    HTMLEditorKit kit = new HTMLEditorKit();
    this.license.setEditorKit(kit);

    StyleSheet styleSheet = kit.getStyleSheet();
    styleSheet.addRule("body {font-family: Tahoma; font-size: 11px; text-align: justify;}");

    this.license.addHyperlinkListener(new HyperlinkListener() {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          if (Desktop.isDesktopSupported()) {
            try {
              Desktop.getDesktop().browse(e.getURL().toURI());
            }
            catch (IOException | URISyntaxException ex) {}
          }
        }
      }
    });

    this.infosPanel = new JPanel(new BorderLayout());
    this.infosPanel.add(this.titlePanel, BorderLayout.NORTH);
    this.infosPanel.add(this.license, BorderLayout.CENTER);

    this.closeBtn = new JButton();
    this.closeBtn.setFont(ConverterFrame.font);
    this.closeBtn.addActionListener(this);
    this.closeBtn.setActionCommand("close");
    this.closeBtn.setFocusPainted(false);

    this.btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    this.btnPanel.add(this.closeBtn);

    getContentPane().add(this.infosPanel, BorderLayout.CENTER);
    getContentPane().add(this.btnPanel, BorderLayout.SOUTH);

    installEscapeCloseOperation(this);
  }

  public static void installEscapeCloseOperation(final JDialog dialog) {
    Action dispatchClosing = new AbstractAction() {
      private static final long serialVersionUID = 817778022911581351L;

      @Override
      public void actionPerformed(ActionEvent event) {
        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
      }
    };
    JRootPane root = dialog.getRootPane();
    root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
    root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "close":
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
  }

  public void setFieldsText(Locale locale) {
    ResourceBundle msg = ResourceBundle.getBundle("net/darmo/resources/langs/lang", locale);

    setTitle(msg.getString("about_dialog.title"));
    this.title.setText(msg.getString("title"));
    this.closeBtn.setText(msg.getString("about_dialog.close"));
    this.TEXT_1 = msg.getString("about_dialog.text1");
    this.TEXT_2 = msg.getString("about_dialog.text2");
    this.TEXT_3 = msg.getString("about_dialog.text3");
    this.license.setText(this.TEXT_1 + toLink(this.CC_LINK, this.CC_TEXT) + this.TEXT_2 + "<br/>" + this.TEXT_3
        + toLink(this.DC_LINK, this.DC_TEXT) + this.TEXT_4);
  }

  private String toLink(String url, String text) {
    return "<a href='" + url + "'>" + (text != null ? text : url.substring(url.indexOf("//") + 2)) + "</a>";
  }

  private final JPanel infosPanel, titlePanel, btnPanel;
  private final JButton closeBtn;
  private final JEditorPane license;
  private final JLabel title;
}
