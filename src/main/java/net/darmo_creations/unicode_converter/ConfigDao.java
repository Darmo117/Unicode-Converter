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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import net.darmo_creations.utils.JarUtil;

/**
 * DAO for config file.
 *
 * @author Damien Vergnet
 */
public class ConfigDao {
  private static ConfigDao instance;

  public static ConfigDao getInstance() {
    if (instance == null)
      instance = new ConfigDao();
    return instance;
  }

  /**
   * Saves the config.
   * 
   * @param locale the locale
   */
  public void save(Language language) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element root = doc.createElement("Config");
      Element localeElm = doc.createElement("Locale");

      localeElm.appendChild(doc.createTextNode(language.getCode()));
      root.appendChild(localeElm);
      doc.appendChild(root);

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new File(JarUtil.getJarDir() + "config.xml"));

      transformer.transform(new DOMSource(doc), result);
    }
    catch (ParserConfigurationException | TransformerException e) {}
  }

  /**
   * Loads the config.
   * 
   * @return the locale
   */
  public Language load() {
    try {
      File fXmlFile = new File(JarUtil.getJarDir() + "config.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);

      doc.getDocumentElement().normalize();

      Element root = (Element) doc.getElementsByTagName("Config").item(0);
      if (root != null) {
        Element locale = (Element) root.getElementsByTagName("Locale").item(0);
        if (locale != null) {
          Language language = Language.fromCode(locale.getTextContent());
          return language == null ? Language.ENGLISH : language;
        }
      }
    }
    catch (NullPointerException | ClassCastException | ParserConfigurationException | SAXException | IOException e) {}

    return Language.ENGLISH;
  }

  private ConfigDao() {}
}
