package de.mnreinisch.pp.watcher.control;

import de.mnreinisch.pp.watcher.control.dto.ConfigurationDTO;
import de.mnreinisch.pp.watcher.domain.Configuration;
import de.mnreinisch.pp.watcher.domain.ConfigurationRepository;
import de.mnreinisch.pp.watcher.domain.exceptions.CustomException;
import de.mnreinisch.pp.watcher.domain.exceptions.TechnicalException;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationControl {
    private ConfigurationRepository configurationRepository = ConfigurationRepository.getInstance();

    public int getStartDay() throws TechnicalException {
        Configuration startDay = configurationRepository.getByKey(ConfigurationRepository.CONFIG_STARTDAY);
        if(startDay == null) {
            setStartday(1);
            return 1;
        }
        return Integer.parseInt(startDay.getConfigValue());
    }

    private boolean validateConfig(ConfigurationDTO configurationDTO){
        boolean result = false;
        if (ConfigurationRepository.CONFIG_STARTDAY.equals(configurationDTO.getKey())) {
            boolean numeric = GlobalHelper.isNumeric(configurationDTO.getValue());
            if (numeric) {
                int i = Integer.parseInt(configurationDTO.getValue());
                result = i > 0 && i < 32;
            }
        }
        return result;
    }

    public void editConfiguration(ConfigurationDTO configurationDTO) throws CustomException, TechnicalException {
        if(configurationDTO == null) throw new CustomException("Please provide a configuration!");
        if(!validateConfig(configurationDTO)) throw new CustomException("Provided value for key " + configurationDTO.getKey() + " not supported!"); // Todo impl. description for supported values?

        Configuration byKey = configurationRepository.getByKey(configurationDTO.getKey());
        if(byKey == null) throw new CustomException("Couldn't find configuration with id " + configurationDTO.getKey());
        configurationRepository.updateConfig(configurationDTO);
    }

    public void setStartday(int dayOfMonth) throws TechnicalException {
        Configuration startDay = configurationRepository.getByKey(ConfigurationRepository.CONFIG_STARTDAY);
        if(startDay == null) {
            configurationRepository.addConfiguration(new Configuration(ConfigurationRepository.CONFIG_STARTDAY, String.valueOf(dayOfMonth)));
        } else {
            ConfigurationDTO configurationDTO = startDay.toDTO();
            configurationDTO.setValue(String.valueOf(dayOfMonth));
            configurationRepository.updateConfig(configurationDTO);
        }
    }

    public List<ConfigurationDTO> getAllConfigs() {
        List<Configuration> configs = configurationRepository.getConfigs();
        return configs
                .stream()
                .map(Configuration::toDTO)
                .collect(Collectors.toList());

    }

}
