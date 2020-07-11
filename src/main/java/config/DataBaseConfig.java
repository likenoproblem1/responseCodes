package config;

import com.typesafe.config.Config;

public class DataBaseConfig {

    public final String host;
    public final String h2host;
    public final String username;
    public final String password;

    DataBaseConfig(Config globalConfig) {
        this.host = globalConfig.getString("host");
        this.h2host = globalConfig.getString("h2host");
        this.username = globalConfig.getString("username");
        this.password = globalConfig.getString("password");
    }
}
