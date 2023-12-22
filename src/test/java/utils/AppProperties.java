package utils;

import java.io.InputStream;
import java.util.Properties;

public final class AppProperties {
    private static Properties _properties;
    private static String filePath = "config.properties";
    private AppProperties() {}
    private static void LoadProperties()
    {
        try {
            _properties = new Properties();
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream != null) {
                _properties.load(inputStream);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static String Get(String key)
    {
        if (_properties == null)
        {
            LoadProperties();
        }

        return _properties.getProperty(key);
    }

}
