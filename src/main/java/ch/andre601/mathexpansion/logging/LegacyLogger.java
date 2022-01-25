package ch.andre601.mathexpansion.logging;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

import java.util.logging.Logger;

public class LegacyLogger implements LoggerUtil{
    
    private final Logger logger;
    
    public LegacyLogger(){
        this.logger = PlaceholderAPIPlugin.getInstance().getLogger();
    }
    
    @Override
    public void logWarning(String text){
        logger.warning("[Math] " + text);
    }
}
