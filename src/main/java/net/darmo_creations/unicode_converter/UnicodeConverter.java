package net.darmo_creations.unicode_converter;

import java.io.InputStream;
import java.util.Optional;

import net.darmo_creations.gui_framework.Application;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.utils.version.Version;

public class UnicodeConverter implements Application {
  public static final Version CURRENT_VERSION = new Version(1, 0, 2, false);

  @Override
  public String getName() {
    return "Unicode Converter";
  }

  @Override
  public Version getCurrentVersion() {
    return CURRENT_VERSION;
  }

  @Override
  public Optional<String> getIcon() {
    return Optional.of("/assets/icons/icon.png");
  }

  @Override
  public String getIconsLocation() {
    return "/assets/icons/";
  }

  @Override
  public Optional<String> getLicenseIcon() {
    return Optional.of("/assets/icons/gplv3-127x51.png");
  }

  @Override
  public ApplicationFrame initFrame(WritableConfig config) {
    return new MainFrame(config);
  }

  @Override
  public InputStream getLanguageFilesStream(Language language) {
    return UnicodeConverter.class.getResourceAsStream("/assets/langs/" + language.getLocale() + ".lang");
  }

  @Override
  public boolean checkUpdates() {
    return true;
  }

  @Override
  public Optional<String> getRssUpdatesLink() {
    return Optional.of("https://github.com/Darmo117/Unicode-Converter/releases.atom");
  }

  @Override
  public boolean hasAboutDialog() {
    return true;
  }

  @Override
  public Optional<String> getAboutFilePath() {
    return Optional.of("/assets/about.html");
  }

  @Override
  public boolean hasHelpDocumentation() {
    return false;
  }

  @Override
  public Optional<String> getHelpDocumentationLink(Language language) {
    return Optional.empty();
  }
}
