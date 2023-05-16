package ch.andre601.mathexpansion;

import ch.andre601.mathexpansion.logging.LegacyLogger;
import ch.andre601.mathexpansion.logging.LoggerUtil;
import ch.andre601.mathexpansion.logging.NativeLogger;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.NMSVersion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
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
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier){
        String placeholder = "%math_" + identifier + "%";
        
        // Parse {placeholder} and replace [prc] with % for math expressions.
        String content = PlaceholderAPI.setBracketPlaceholders(player, identifier).replace("[prc]", "%");
        
        String[] values = content.split("_", 2);
        if(values.length == 1)
            return evaluateExpression(placeholder, values[0], scale(null, placeholder), roundingMode(null));
        
        // %math_<text>_% -> Invalid placeholder format
        if(values[1].isEmpty()){
            printPlaceholderWarn(placeholder, "'%%math_<text>_%%' is not an allowed placeholder syntax.");
            
            return null;
        }
        
        String[] options = Arrays.copyOf(values[0].split(":", 2), 2);
        
        int scale = scale(options[0], placeholder);
        if(scale == -1)
            return null;
        
        RoundingMode mode = roundingMode(options[1]);
        
        return evaluateExpression(placeholder, values[1], scale, mode);
    }
    
    private String evaluateExpression(String placeholder, String exp, int scale, RoundingMode roundingMode){
        Expression expression = new Expression(exp);
        
        try{
            BigDecimal bd = expression.evaluate().getNumberValue();
            
            return bd.setScale(scale, roundingMode).toPlainString();
        }catch(EvaluationException | ParseException ex){
            printPlaceholderWarn(placeholder, "'%s' is not a valid Math expression.", exp);
            
            if(debugModeEnabled())
                ex.printStackTrace();
            
            return null;
        }
    }
    
    private int scale(String value, String placeholder){
        if(value == null || value.isEmpty())
            return Math.max(this.getInt("Decimals", 3), 0);
        
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException ex){
            printPlaceholderWarn(placeholder, "%s is not a valid integer number.", value);
            
            if(debugModeEnabled())
                ex.printStackTrace();
            
            return -1;
        }
    }
    
    private RoundingMode roundingMode(String roundingMode){
        String defRoundingMode = this.getString("Rounding", "half-up");
        if(roundingMode == null || roundingMode.isEmpty())
            roundingMode = defRoundingMode.isEmpty() ? "half-up" : defRoundingMode;
        
        switch(roundingMode.toLowerCase(Locale.ROOT)){
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
            
            default:
                return RoundingMode.HALF_UP;
        }
    }
    
    private void printPlaceholderWarn(String placeholder, String cause, Object... args){
        if(this.getBoolean("Disable-Warnings", false))
            return;
        
        if(invalidPlaceholders.getIfPresent(placeholder) == null){
            logger.warn("Invalid Placeholder detected!");
            logger.warn("Placeholder: " + placeholder);
            logger.warn(String.format("Cause:       " + cause, args));
            
            invalidPlaceholders.put(placeholder, System.currentTimeMillis());
        }
    }
    
    private boolean debugModeEnabled(){
        Object debugMode = this.get("Debug", null);
        if(debugMode == null)
            return false;
        
        if(debugMode instanceof String)
            return this.getString("Debug", "off").equalsIgnoreCase("on");
        
        if(debugMode instanceof Boolean)
            return this.getBoolean("Debug", false);
        
        return false;
    }
    
    private LoggerUtil loadLogger(){
        if(NMSVersion.getVersion("v1_18_R1") != NMSVersion.UNKNOWN)
            return new NativeLogger(this);
        
        return new LegacyLogger();
    }
}
