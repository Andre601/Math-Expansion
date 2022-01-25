package ch.andre601.mathexpansion.logging;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class NativeLogger implements LoggerUtil{
    
    private final PlaceholderExpansion expansion;
    
    public NativeLogger(PlaceholderExpansion expansion){
        this.expansion = expansion;
    }
    
    @Override
    public void logWarning(String text){
        expansion.warning(text);
    }
}
