package ch.andre601.mathexpansion;

import ch.andre601.mathexpansion.logging.LegacyLogger;
import ch.andre601.mathexpansion.logging.LoggerUtil;
import ch.andre601.mathexpansion.logging.NativeLogger;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.udojava.evalex.Expression;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.NMSVersion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MathExpansion extends PlaceholderExpansion implements Configurable {
    
    private final LoggerUtil logger;
    
    private final Map<String, Object> defaults = new HashMap<>();
    private final Cache<String, Long> invalidPlaceholders = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.SECONDS)
        .build();
    
    public MathExpansion(){
        this.logger = loadLogger();
        
        defaults.put("Precision", 3);
        defaults.put("Rounding", "half_up");
        defaults.put("Debug", false);
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "math";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "Andre_601";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "VERSION";
    }

    @Override
    public Map<String, Object> getDefaults(){
        return this.defaults;
    }
    
    public String onRequest(OfflinePlayer player, @NotNull String identifier){
        
        // Used for warnings.
        String placeholder = "%math_" + identifier + "%";
        
        // Replace any bracket placeholder when possible and replace [prc] with the percent symbol (%)
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        identifier = identifier.replace("[prc]", "%");
        
        // Create a null-padded Array by splitting at _
        String[] values = Arrays.copyOf(identifier.split("_", 2), 2);
        
        // Placeholder is %math_<expression>% 
        if(values[1] == null)
            return evaluate(placeholder, values[0], getPrecision(), getRoundingMode());
        
        //Placeholder is %math_<text>_% -> Invalid.
        if(values[1].isEmpty()){
            printPlaceholderWarning(placeholder, "Not allowed placeholder-syntax '%%math_<text>_%%'");
            
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
                printPlaceholderWarning(placeholder, "'%s' is not a valid number for precision!", options[0]);
                
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
        
        return evaluate(placeholder, values[1], precision, roundingMode);
    }
    
    private String evaluate(String placeholder, String expression, int precision, RoundingMode roundingMode){
        try{
            Expression exp = new Expression(expression);
            BigDecimal result = exp.eval().setScale(precision, roundingMode);
            
            return result.toPlainString();
        }catch(Exception ex){
            // Math evaluation failed -> Invalid placeholder
            printPlaceholderWarning(placeholder, "'%s' is not a valid Math Expression.", expression);
            
            if(isDebug())
                ex.printStackTrace();
            
            return null;
        }
    }
    
    // Small utility thing to allow support for pre PAPI 2.11.0
    private LoggerUtil loadLogger(){
        if(NMSVersion.getVersion("v1_18_R1") != NMSVersion.UNKNOWN) // Only PAPI 2.11.0+ has this NMSVersion entry
            return new NativeLogger(this);
        
        return new LegacyLogger();
    }
    
    private void printPlaceholderWarning(String placeholder, String cause, Object... args){
        if(invalidPlaceholders.getIfPresent(placeholder) == null){
            logger.logWarning("Invalid Placeholder detected!");
            logger.logWarning("Placeholder: " + placeholder);
            logger.logWarning(String.format(cause, args));
            
            invalidPlaceholders.put(placeholder, System.currentTimeMillis());
        }
    }
    
    private boolean isDebug(){
        Object debug = this.get("Debug", null);
        
        if(debug == null)
            return false;
        
        if(debug instanceof String)
            return this.getString("Debug", "off").equalsIgnoreCase("on");
        
        if(debug instanceof Boolean)
            return getBoolean("Debug", false);
        
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
