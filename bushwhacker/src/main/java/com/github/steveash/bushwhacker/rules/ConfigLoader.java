package com.github.steveash.bushwhacker.rules;

import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Steve Ash
 */
public class ConfigLoader {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ConfigLoader.class);

  private static class Holder {

    private static final ConfigLoader instance = new ConfigLoader();
  }

  public static final ConfigLoader getInstance() {
    return Holder.instance;
  }

  private final XStream xStream;

  public ConfigLoader() {
    xStream = new XStream();
    xStream.processAnnotations(XmlRules.class);
  }

  public XmlRules loadRulesFromClasspath() throws IOException {
    return loadRulesFromClasspath("bushwhacker.xml");
  }

  public XmlRules loadRulesFromClasspath(String name) throws IOException {
    return loadRules(ConfigLoader.class.getClassLoader(), name);
  }

  public XmlRules loadRules(ClassLoader classLoader, String fileName) throws IOException {
    URL maybe = classLoader.getResource(fileName);
    if (maybe != null) {
      return loadRulesFrom(maybe);
    }
    return new XmlRules();
  }

  private XmlRules loadRulesFrom(URL url) throws IOException {
    log.debug("Loading bushwhacker rules from {}", url);
    try (InputStream is = url.openStream()) {
      return (XmlRules) xStream.fromXML(is);
    }
  }
}
