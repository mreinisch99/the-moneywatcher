package de.mnreinisch.pp.watcher.domain;

import de.mnreinisch.pp.watcher.control.dto.ConfigurationDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = Configuration.Q_GET_ALL_CONFIGS, query = "select c from Configuration c"),
        @NamedQuery(name = Configuration.Q_GET_CONFIG, query = "select c from Configuration c where configKey = :" + Configuration.CONFIG_NAME)
})

@Entity
@Table(name = "WATCH_CONFIGURATION")
public class Configuration implements Serializable {
    private static final long serialVersionUID = -1687413687432176543L;

    public static final String Q_GET_ALL_CONFIGS = "Q_GET_ALL_CONFIGS";
    public static final String Q_GET_CONFIG = "Q_GET_CONFIG";
    public static final String CONFIG_NAME = "CONFIG_NAME";

    @Id
    @Column(name = "CONFIGKEY", nullable = false)
    private String configKey;

    @Column(name = "CONFIGVALUE", nullable = false)
    private String configValue;

    public Configuration(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public Configuration() {
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return configKey.equals(that.configKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configKey);
    }

    public ConfigurationDTO toDTO() {
        return new ConfigurationDTO(configKey, configValue);
    }
}
