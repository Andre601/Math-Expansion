package com.andre601.mathexpansion;

import com.udojava.evalex.Expression;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MathExpansion extends PlaceholderExpansion implements Configurable {
    
    private final String VERSION = getClass().getPackage().getImplementationVersion();

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "math";
    }

    @Override
    public String getAuthor() {
        return "Andre_601";
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public Map<String, Object> getDefaults(){
        Map<String, Object> defaults = new HashMap<>();

        defaults.put("Precision", 3);
        defaults.put("Rounding", "half_up");
        defaults.put("Debug", false);

        return defaults;
    }
    
    public String onRequest(OfflinePlayer player, String identifier){
        Logger log = PlaceholderAPIPlugin.getInstance().getLogger();
        
        // Used for warnings.
        String placeholder = "%math_" + identifier + "%";
        
        // Replace any bracket placeholder when possible and replace [prc] with the percent symbol (%)
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        identifier = identifier.replace("[prc]", "%");
        
        // Create a null-padded Array by splitting at _
        String[] values = Arrays.copyOf(identifier.split("_", 2), 2);
        
        // Placeholder is %math_<expression>% 
        if(values[1] == null)
            return evaluate(placeholder, values[0], getPrecision(), getRoundingMode(), log);
        
        //Placeholder is %math_<text>_% -> Invalid.
        if(values[1].isEmpty()){
            log.warning("[Math] Invalid Placeholder detected!");
            log.warning("[Math] Placeholder: " + placeholder);
            log.warning("[Math] Cause: Not allowed placeholder-syntax '%math_<text>_%'");
            
            return null;
        }
        
        // Split values[0] at : and put null where nothing exists.
        String[] options = Arrays.copyOf(values[0].split(":", 2), 2);
        
        int precision;
        RoundingMode roundingMode;
        
        if(isNullOrEmpty(options[0])){
            precision = getPrecision();
        }else{
            try{
                precision = Integer.parseInt(options[0]);
            }catch(NumberFormatException ex){
                // String isn't a valid number -> Invalid placeholder.
                log.warning("[Math] Invalid Placeholder detected!");
                log.warning("[Math] Placeholder: " + placeholder);
                log.warning("[Math] Cause: '" + options[0] + "' is not a valid number for precision!");
                
                if(isDebug())
                    ex.printStackTrace();
                
                return null;
            }
        }
        
        if(isNullOrEmpty(options[1])){
            roundingMode = getRoundingMode();
        }else{
            roundingMode = getRoundingMode(options[1].toLowerCase());
        }
        
        return evaluate(placeholder, values[1], precision, roundingMode, log);
    }
    
    private String evaluate(String placeholder, String expression, int precision, RoundingMode roundingMode, Logger log){
        try{
            Expression exp = new Expression(expression);
            BigDecimal result = exp.eval().setScale(precision, roundingMode);
            
            return result.toPlainString();
        }catch(Exception ex){
            log.warning("[Math] Invalid Placeholder detected!");
            log.warning("[Math] Placeholder: " + placeholder);
            log.warning("[Math] Cause: '" + expression + "' is not a valid Math-Expression.");
            
            if(isDebug())
                ex.printStackTrace();
            
            return null;
        }
    }
    
    private boolean isDebug(){
        Object debug = this.get("Debug", null);
        
        if(debug == null)
            return false;
        
        if(debug instanceof String)
            return this.getString("Debug", "off").equalsIgnoreCase("on");
        
        if(debug instanceof Boolean){
            ConfigurationSection section = this.getConfigSection();
            if(section == null)
                return false;
            
            return section.getBoolean("Debug", false);
        }
        
        return false;
    }
    
    private int getPrecision(){
        return Math.max(this.getInt("Precision", 3), 0);
    }
    
    private RoundingMode getRoundingMode(){
        return getRoundingMode(this.getString("Rounding", "half-up").toLowerCase());
    }
    
    private RoundingMode getRoundingMode(String mode){
        switch(mode){
            case "up":
                return RoundingMode.UP;
            
            case "down":
                return RoundingMode.DOWN;
            
            case "ceiling":
                return RoundingMode.CEILING;
                
            case "floor":
                return RoundingMode.FLOOR;
                
            case "half-down":
                return RoundingMode.HALF_DOWN;
            
            case "half-even":
                return RoundingMode.HALF_EVEN;
            
            case "half-up":
            default:
                return RoundingMode.HALF_UP;
        }
    }
    
    private boolean isNullOrEmpty(String value){
        return value == null || value.isEmpty();
    }
}
