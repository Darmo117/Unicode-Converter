package net.darmo_creations.unicode_converter.converter;

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
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) { }
        
        title = new JLabel();
        title.setFont(title.getFont().deriveFont(20F));
        
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(title);
        
        license = new JEditorPane();
        license.setOpaque(false);
        license.setEditable(false);
        license.setBorder(new EmptyBorder(5, 5, 5, 5));
        license.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        
        HTMLEditorKit kit = new HTMLEditorKit();
        license.setEditorKit(kit);
        
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {font-family: Tahoma; font-size: 11px; text-align: justify;}");
        
        license.addHyperlinkListener(new HyperlinkListener() {
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
        
        infosPanel = new JPanel(new BorderLayout());
        infosPanel.add(titlePanel, BorderLayout.NORTH);
        infosPanel.add(license, BorderLayout.CENTER);
        
        closeBtn = new JButton();
        closeBtn.setFont(ConverterFrame.font);
        closeBtn.addActionListener(this);
        closeBtn.setActionCommand("close");
        closeBtn.setFocusPainted(false);
        
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(closeBtn);
        
        getContentPane().add(infosPanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        
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
            case "close": dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
    
    public void setFieldsText(Locale locale) {
        ResourceBundle msg = ResourceBundle.getBundle("net/darmo/resources/langs/lang", locale);
        
        setTitle(msg.getString("about_dialog.title"));
        title.setText(msg.getString("title"));
        closeBtn.setText(msg.getString("about_dialog.close"));
        TEXT_1 = msg.getString("about_dialog.text1");
        TEXT_2 = msg.getString("about_dialog.text2");
        TEXT_3 = msg.getString("about_dialog.text3");
        license.setText(TEXT_1 + toLink(CC_LINK, CC_TEXT) + TEXT_2 + "<br/>" + TEXT_3 + toLink(DC_LINK, DC_TEXT) + TEXT_4);
    }
    
    private String toLink(String url, String text) {
        return "<a href='" + url + "'>" + (text != null ? text : url.substring(url.indexOf("//") + 2)) + "</a>";
    }
    
    private final JPanel infosPanel, titlePanel, btnPanel;
    private final JButton closeBtn;
    private final JEditorPane license;
    private final JLabel title;
}
