package config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigService {
    private static final Config defaultConfig = ConfigFactory.load();
    private static final DataBaseConfig dbConfig = new DataBaseConfig(defaultConfig.getConfig("db"));

    public static DataBaseConfig getDbConfig() {
        return dbConfig;
    }
}
