package ch.andre601.mathexpansion;

import ch.andre601.mathexpansion.logging.LegacyLogger;
import ch.andre601.mathexpansion.logging.LoggerUtil;
import ch.andre601.mathexpansion.logging.NativeLogger;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.parser.ParseException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.NMSVersion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MathExpansion extends PlaceholderExpansion implements Configurable {
    
    private final LoggerUtil logger;
    
    private final Map<String, Object> defaults = new HashMap<>();
    private final Cache<String, Long> invalidPlaceholders = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.SECONDS)
        .build();
    
    private ExpressionConfiguration defaultConfiguration = null;
    
    public MathExpansion(){
        this.logger = loadLogger();
        
        defaults.put("Decimals", 3);
        defaults.put("Rounding", "half-up");
        defaults.put("Debug", false);
        defaults.put("Disable-Warnings", false);
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
        // Check if the old "Precision" setting is present and apply the old value to the new setting.
        if(this.getInt("Precision", -1) >= 0){
            logger.info("Found old 'Precision' setting. Starting migration process...");
            
            this.defaults.put("Decimals", this.getInt("Precision", 3));
            this.defaults.put("Precision", null);
            
            logger.info("Migrated old settings. Please check the config.yml of PlaceholderAPI for problems.");
        }
        
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
        if(values[1] == null){
            if(defaultConfiguration == null)
                defaultConfiguration = createConfiguration(getPrecision(null, placeholder), null);
            
            return evaluate(placeholder, values[0], defaultConfiguration);
        }
        
        // Placeholder is %math_<text>_% -> Invalid.
        if(values[1].isEmpty()){
            printPlaceholderWarning(placeholder, "Not allowed placeholder-syntax '%%math_<text>_%%'");
            
            return null;
        }
        
        // Create a null-padded Array by splitting values[0] at :
        String[] options = Arrays.copyOf(values[0].split(":", 2), 2);
        
        int precision = getPrecision(options[0], placeholder);
        if(precision == -1)
            return null;
        
        ExpressionConfiguration configuration = createConfiguration(precision, options[1]);
        
        return evaluate(placeholder, values[1], configuration);
    }
    
    private String evaluate(String placeholder, String expression, ExpressionConfiguration configuration){
        Expression exp = new Expression(expression, configuration);
        
        try{
            return exp.evaluate().getStringValue();
        }catch(EvaluationException | ParseException ex){
            printPlaceholderWarning(placeholder, "'%s' is not a valid Math Expression", expression);
            
            if(isDebug())
                ex.printStackTrace();
            
            return null;
        }
    }
    
    private ExpressionConfiguration createConfiguration(int precision, String roundingMode){
        RoundingMode mode = getRoundingMode(roundingMode);
        
        return ExpressionConfiguration.builder()
            .mathContext(new MathContext(68, mode))
            .decimalPlacesRounding(precision)
            .build();
    }
    
    // Small utility thing to allow support for pre PAPI 2.11.0 logging
    private LoggerUtil loadLogger(){
        if(NMSVersion.getVersion("v1_18_R1") != NMSVersion.UNKNOWN) // Only PAPI 2.11.0+ has this NMSVersion entry
            return new NativeLogger(this);
        
        return new LegacyLogger();
    }
    
    // Method to print a placeholder warning every 10 seconds per placeholder.
    private void printPlaceholderWarning(String placeholder, String cause, Object... args){
        if(this.getBoolean("Disable-Warnings", false))
            return;
            
        if(invalidPlaceholders.getIfPresent(placeholder) == null){
            logger.warn("Invalid Placeholder detected!");
            logger.warn("Placeholder: " + placeholder);
            logger.warn(String.format("Cause: " + cause, args));
            
            invalidPlaceholders.put(placeholder, System.currentTimeMillis());
        }
    }
    
    private boolean isDebug(){
        Object debug = this.get("Debug", null);
        
        if(debug == null)
            return false;
        
        // Backwards compatibility for before PAPI had boolean support
        if(debug instanceof String)
            return this.getString("Debug", "off").equalsIgnoreCase("on");
        
        if(debug instanceof Boolean)
            return getBoolean("Debug", false);
        
        return false;
    }
    
    private int getPrecision(String value, String placeholder){
        if(isNullOrEmpty(value)){
            return Math.max(this.getInt("Decimals", 3), 0);
        }else{
            try{
                return Integer.parseInt(value);
            }catch(NumberFormatException ex){
                printPlaceholderWarning(placeholder, "'%s' is not a valid number for precision!", value);
                
                if(isDebug())
                    ex.printStackTrace();
                
                return -1;
            }
        }
    }
    
    private RoundingMode getRoundingMode(String mode){
        String def = this.getString("Rounding", "half-up");
        if(isNullOrEmpty(mode)){
            mode = def.isEmpty() ? "half-up" : def;
        }
        
        switch(mode.toLowerCase(Locale.ROOT)){
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
