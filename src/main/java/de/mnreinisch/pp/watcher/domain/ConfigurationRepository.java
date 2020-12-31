package de.mnreinisch.pp.watcher.domain;

import de.mnreinisch.pp.watcher.control.dto.ConfigurationDTO;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationRepository {
    public static final String CONFIG_STARTDAY = "CONFIG.STARTDAY";

    private static ConfigurationRepository configurationRepository;
    private EMFactory emFactory;


    public static ConfigurationRepository getInstance(){
        if(configurationRepository == null){
            configurationRepository = new ConfigurationRepository();
        }
        return configurationRepository;
    }

    private ConfigurationRepository() {
        emFactory = EMFactory.getInstance();
        dbMigration(); // Required for databases on version 1.0.0-SNAPSHOT
        checkConfiguration();
    }

    private void dbMigration() {
        EntityManager em = emFactory.getEm();
        em.getTransaction().begin();

        Query nativeQuery = em.createNativeQuery("create table if not exists WATCH_CONFIGURATION (CONFIGKEY varchar(255) not null, CONFIGVALUE varchar(255) not null, primary key (CONFIGKEY))"); // Migration for already existing databases
        nativeQuery.executeUpdate();

        em.flush();
        em.getTransaction().commit();
    }

    private void checkConfiguration() {
        Query q = emFactory.createNamedQuery(Configuration.Q_GET_ALL_CONFIGS);

        List<Configuration> configs = q.getResultList();
        List<String> collect = configs.stream().map(Configuration::getConfigKey).collect(Collectors.toList());
        if(!collect.contains(CONFIG_STARTDAY)){
            Configuration startDay = new Configuration(CONFIG_STARTDAY, "1");
            emFactory.persist(startDay);
        }
    }

    public void addConfiguration(Configuration config){
        emFactory.persist(config);
    }

    public void updateConfig(ConfigurationDTO configurationDTO) throws TechnicalException {
        Configuration byKey = getByKey(configurationDTO.getKey());
        if(byKey == null) throw new TechnicalException("Configuration with key" + configurationDTO.getKey() + " not found");
        try{
            emFactory.getEm().getTransaction().begin();
            byKey.setConfigValue(configurationDTO.getValue());
            emFactory.getEm().flush();
            emFactory.getEm().getTransaction().commit();
        } catch (Throwable e){
            emFactory.getEm().getTransaction().rollback();
        }
    }

    public Configuration getByKey(String key){
        Query q = emFactory.createNamedQuery(Configuration.Q_GET_CONFIG);
        q.setParameter(Configuration.CONFIG_NAME, key);
        List<Configuration> res = q.getResultList();
        return res.size() == 1 ? res.get(0) : null;
    }

    public List<Configuration> getConfigs() {
        Query q = emFactory.createNamedQuery(Configuration.Q_GET_ALL_CONFIGS);
        return q.getResultList();
    }
}
