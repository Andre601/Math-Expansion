package com.andre601.mathexpansion;

import com.udojava.evalex.Expression;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MathExpansion extends PlaceholderExpansion implements Configurable {

    // We get the version from the package (or something like that)
    private final String VERSION = getClass().getPackage().getImplementationVersion();

    // We don't have any dependencies to worry about so it's always true
    @Override
    public boolean canRegister(){
        return true;
    }

    // %math_<math expression>%
    @Override
    public String getIdentifier() {
        return "math";
    }

    // *Feels proud man*
    @Override
    public String getAuthor() {
        return "Andre_601";
    }

    // Just the version
    @Override
    public String getVersion() {
        return VERSION;
    }

    /*
     * We generate and load our default settings here.
     *
     * Precision is how many digits after the . should be shown
     * Debug enables/disables printing of the StackTrace, if an invalid calculation was made.
     */
    @Override
    public Map<String, Object> getDefaults(){
        Map<String, Object> defaults = new HashMap<>();

        defaults.put("Precision", 3);
        defaults.put("Debug", "off");

        return defaults;
    }

    /*
     * This is, where the actual requests will happen.
     * We use OfflinePlayer instead of Player, because a player isn't a requirement for our expansion here
     */
    public String onRequest(OfflinePlayer player, String identifier){

        /*
         * We first replace any appearance of placeholders with the right value.
         * We need to use setBracketPlaceholders, to check for {placeholders} and we also need to use [prc]
         * as a replacement of the % symbol, because of how PlaceholderAPI handles placeholders.
         */
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        identifier = identifier.replace("[prc]", "%");

        BigDecimal result;
        try {
            /*
             * We first create a new Expression with our String identifier before then evaluating and setting it
             * as a BigDecimal.
             *
             * We use setScale, to reduce the amount of numbers after the . to the one provided in the config
             * (PlaceholderAPI/config.yml)
             *
             * BigDecimal.ROUND_HALF_UP is basically the same, like you've learned in school about rounding
             * (0-4 = rounding down, 5-9 = rounding up)
             */
            Expression expression = new Expression(identifier);
            result = expression.eval().setScale(this.getInt("Precision", 3), BigDecimal.ROUND_HALF_UP);
        }catch (Exception ex){
            // If debug is set to "on", we print the StackTrace on an error.
            if(this.getString("Debug", "off").equalsIgnoreCase("on"))
                ex.printStackTrace();

            identifier = "The provided value was invalid!";
            return identifier;
        }

        // We have to return it as plainString, or else it would return 10 as 1E+1 (which is just the same)
        return result.toPlainString();
    }
}
