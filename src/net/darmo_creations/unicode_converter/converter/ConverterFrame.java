package net.darmo_creations.unicode_converter.converter;

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
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Darmo
 */
public class ConverterFrame extends JFrame implements KeyListener, ActionListener {
    private static final long serialVersionUID = 3742949793870868995L;

    public static final Font font = new Font("Tahoma", Font.PLAIN, 13);
    
    private AboutDialog aboutDialog;
    private Locale locale;
    
    public ConverterFrame() {
        aboutDialog = new AboutDialog(this);
        locale = getPreferredLanguage();
        
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
        menuBar = new JMenuBar();
        langsMenu = new JMenu();
        langItems = new LinkedHashMap<>();
        helpMenu = new JMenu();
        aboutItem = new JMenuItem();
        
        labelsPanel = new JPanel();
        fieldsPanel = new JPanel();
        
        charLbl = new JTextField(1);
        decLbl = new JTextField(1);
        hexLbl = new JTextField(1);
        charField = new JTextField();
        decField = new JTextField();
        hexField = new JTextField();
        // </editor-fold>
        
        // <editor-fold desc="Menu bar" defaultstate="collapsed">
        langItems.put("en", new JRadioButtonMenuItem("English"));
        langItems.get("en").setActionCommand("lang en");
        langItems.get("en").setSelected(locale.getLanguage().equals("en"));
        langItems.put("fr", new JRadioButtonMenuItem("fran�ais"));
        langItems.get("fr").setActionCommand("lang fr");
        langItems.get("fr").setSelected(locale.getLanguage().equals("fr"));
        
        ButtonGroup group = new ButtonGroup();
        
        langsMenu.setFont(font);
        for (JRadioButtonMenuItem i : langItems.values()) {
            i.setFont(font);
            i.addActionListener(this);
            group.add(i);
            langsMenu.add(i);
        }
        
        aboutItem.setFont(font);
        aboutItem.setActionCommand("about");
        aboutItem.addActionListener(this);
        
        helpMenu.setFont(font);
        helpMenu.add(aboutItem);
        
        menuBar.add(langsMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        // </editor-fold>
        
        // <editor-fold desc="Labels" defaultstate="collapsed">
        charLbl.setBackground(new Color(239, 239, 239));
        charLbl.setBorder(null);
        charLbl.setFont(font);
        charLbl.setDisabledTextColor(Color.BLACK);
        charLbl.setEnabled(false);
        
        decLbl.setBackground(new Color(239, 239, 239));
        decLbl.setBorder(null);
        decLbl.setFont(font);
        decLbl.setDisabledTextColor(Color.BLACK);
        decLbl.setEnabled(false);
        
        hexLbl.setBackground(new Color(239, 239, 239));
        hexLbl.setBorder(null);
        hexLbl.setFont(font);
        hexLbl.setDisabledTextColor(Color.BLACK);
        hexLbl.setEnabled(false);
        // </editor-fold>
        
        // <editor-fold desc="Fields" defaultstate="collapsed">
        charField.setFont(font);
        charField.setActionCommand("char");
        charField.addKeyListener(this);
        
        decField.setFont(font);
        decField.setActionCommand("dec");
        decField.addKeyListener(this);
        
        hexField.setFont(font);
        hexField.setActionCommand("hex");
        hexField.addKeyListener(this);
        // </editor-fold>
        
        setFieldsText();
        
        // <editor-fold desc="Labels panel layout" defaultstate="collapsed">
        GroupLayout labelsPanelLayout = new GroupLayout(labelsPanel);
        
        labelsPanel.setLayout(labelsPanelLayout);
        labelsPanelLayout.setAutoCreateGaps(true);
        labelsPanelLayout.setAutoCreateContainerGaps(true);
        labelsPanelLayout.setHorizontalGroup(
            labelsPanelLayout.createParallelGroup()
                .addComponent(charLbl)
                .addComponent(decLbl)
                .addComponent(hexLbl)
        );
        labelsPanelLayout.setVerticalGroup(
            labelsPanelLayout.createSequentialGroup()
                .addComponent(charLbl)
                .addComponent(decLbl)
                .addComponent(hexLbl)
        );
        // </editor-fold>
        
        // <editor-fold desc="Fields panel layout" defaultstate="collapsed">
        GroupLayout fieldsPanelLayout = new GroupLayout(fieldsPanel);
        
        fieldsPanel.setLayout(fieldsPanelLayout);
        fieldsPanelLayout.setAutoCreateGaps(true);
        fieldsPanelLayout.setAutoCreateContainerGaps(true);
        fieldsPanelLayout.setHorizontalGroup(
            fieldsPanelLayout.createParallelGroup()
                .addComponent(charField)
                .addComponent(decField)
                .addComponent(hexField)
        );
        fieldsPanelLayout.setVerticalGroup(
            fieldsPanelLayout.createSequentialGroup()
                .addComponent(charField)
                .addComponent(decField)
                .addComponent(hexField)
        );
        // </editor-fold>
        
        // <editor-fold desc="General layout" defaultstate="collapsed">
        GroupLayout layout = new GroupLayout(getContentPane());
        
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addComponent(labelsPanel)
                .addComponent(fieldsPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addComponent(labelsPanel)
                .addComponent(fieldsPanel)
        );
        // </editor-fold>
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean langChanged = false;
        
        switch (e.getActionCommand()) {
            case "lang en": locale = new Locale("en"); langChanged = true; break;
            case "lang fr": locale = new Locale("fr"); langChanged = true; break;
            case "about": aboutDialog.setVisible(true); break;
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
        String[] texts = {
            charField.getText(),
            decField.getText(),
            hexField.getText()
        };
        
        if (src == charField) {
            if (texts[0].length() == 1) {
                decField.setText(Integer.toString(Util.charToInt(texts[0].charAt(0))));
                hexField.setText(Util.charToHex(texts[0].charAt(0)));
            }
            else {
                decField.setText(null);
                hexField.setText(null);
            }
        }
        else if (src == decField) {
            try {
                int code = Integer.parseInt(texts[1]);
                
                if (code >= 0) {
                    charField.setText("" + Util.intToChar(code));
                    hexField.setText(Util.intToHex(code));
                }
                else {
                    charField.setText(null);
                    hexField.setText(null);
                }
            }
            catch (NumberFormatException ex) {
                charField.setText(null);
                hexField.setText(null);
            }
        }
        else if (src == hexField) {
            if (texts[2] != null && texts[2].matches("[a-fA-F0-9]+")) {
                charField.setText("" + Util.hexToChar(texts[2]));
                decField.setText("" + Util.hexToInt(texts[2]));
            }
            else {
                charField.setText(null);
                decField.setText(null);
            }
        }
    }
    
    private void setFieldsText() {
        ResourceBundle msg = ResourceBundle.getBundle("net/darmo/resources/langs/lang", locale);
        
        setTitle(msg.getString("title"));
        charLbl.setText(msg.getString("label.character"));
        decLbl.setText(msg.getString("label.decimal"));
        hexLbl.setText(msg.getString("label.hexadecimal"));
        langsMenu.setText(msg.getString("menu.langs"));
        helpMenu.setText(msg.getString("menu.help"));
        aboutItem.setText(msg.getString("menu.help.about"));
        aboutDialog.setFieldsText(locale);
    }
    
    @SuppressWarnings("unchecked")
    private void writePreferredLanguage() {
        try (FileWriter f = new FileWriter(Util.getJarPath() + "config.json")) {
            JSONObject obj = new JSONObject();
            
            obj.put("lang", locale.getLanguage());
            f.write(obj.toJSONString());
            f.flush();
        }
        catch (IOException ex) { }
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
