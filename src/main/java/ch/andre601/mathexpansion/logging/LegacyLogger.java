package ch.andre601.mathexpansion.logging;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

import java.util.logging.Logger;

public class LegacyLogger implements LoggerUtil{
    
    private final Logger logger;
    private final String prefix = "[Math] ";
    
    public LegacyLogger(){
        this.logger = PlaceholderAPIPlugin.getInstance().getLogger();
    }
    
    @Override
    public void warn(String text){
        logger.warning(prefix + text);
    }
    
    @Override
    public void info(String text){
        logger.info(prefix + text);
    }
}
