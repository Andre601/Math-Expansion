package com.andre601.mathexpansion;

import com.udojava.evalex.Expression;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpansion extends PlaceholderExpansion implements Configurable {

    private final Pattern SETTINGS_PATTERN = Pattern.compile("\\[(?<precision>[a-zA-Z0-9:]+)]");

    /**
     * Gives back if the plugin can be registered or not.
     *
     * @return Always {@code true} since we do not have any dependencies.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * Gives the String that is used in %identifier_value%.
     *
     * @return A {@link java.lang.String String} called "math".
     */
    @Override
    public String getIdentifier() {
        return "math";
    }

    /**
     * Gives the name of the author.
     *
     * @return A {@link java.lang.String String} called "Andre_601".
     */
    @Override
    public String getAuthor() {
        return "Andre_601";
    }

    /**
     * Gives the version of the expansion.
     *
     * @return A {@link java.lang.String String} with the current version.
     */
    @Override
    public String getVersion() {
        return "1.0.7";
    }

    /**
     * Sets/Loads the default settings in the config.yml of PlaceholderAPI.
     *
     * @return A {@link java.util.HashMap HashMap<String, Object>} with the default settings for this expansion.
     *
     * @since 1.0.4
     */
    @Override
    public Map<String, Object> getDefaults(){
        Map<String, Object> defaults = new HashMap<>();

        defaults.put("Precision", 3);
        defaults.put("Debug", "off");

        return defaults;
    }

    /**
     * Will be executed, when a placeholder with our {@link #getIdentifier() identifier} is used.
     *
     * @param  player
     *         A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param  identifier
     *         A {@link java.lang.String String}.
     *
     * @return A String, depending on certain conditions.
     */
    public String onRequest(OfflinePlayer player, String identifier){

        /*
         * We first replace any appearance of placeholders with the right value.
         * We need to use setBracketPlaceholders, to check for {placeholders} and we also need to use [prc]
         * as a replacement of the % symbol, because of how PlaceholderAPI handles placeholders.
         */
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        identifier = identifier.replace("[prc]", "%");

        /*
         * We setup a matcher, that checks for any appearance of [something]
         */
        Matcher matcher = SETTINGS_PATTERN.matcher(identifier);

        /*
         * We first set a integer with value -1, that will be used later.
         * We then check, if the matcher has found something and if yes, we split that result at : as a String[]
         * That String[] will be checked, if it's bigger or equal to 2 (if it found at least 2 Strings, after splitting
         * at the :)
         * When it finds something, we will check, if the first String (before the first :) is called "precision"
         * and if it is the case, then we will try to get an integer out of the second String (after the :) and if
         * that fails, we return a message about an wrong argument.
         */
        int precision = -1;
        if(matcher.find()){
            String[] results = matcher.group("precision").split(":");

            if(results.length >= 2){
                if(results[0].equalsIgnoreCase("precision")){
                    try {
                        precision = Integer.valueOf(results[1]);
                        identifier = identifier.replace("[" + matcher.group("precision") + "]", "");
                    }catch(Exception ex){
                        identifier = "The value in the option \"Precision\" was invalid! Make sure it's a number!";

                        if(this.getString("Debug", "off").equalsIgnoreCase("on"))
                            ex.printStackTrace();

                        return identifier;
                    }
                }else{
                    identifier = "Invalid option-type \"" + matcher.group("precision") + "\"!";
                    return identifier;
                }
            }
        }

        BigDecimal result;
        try {
            /*
             * We first create a new Expression with our String identifier before then evaluating and setting it
             * as a BigDecimal.
             *
             * We use setScale, to reduce the amount of numbers after the . to the one provided in the config
             * (PlaceholderAPI/config.yml), unless there is a [precision:<number>] placeholder within the identifier
             * itself (precision is not -1)
             *
             * BigDecimal.ROUND_HALF_UP is basically the same, like you've learned in school about rounding
             * (0-4 = rounding down, 5-9 = rounding up)
             */
            Expression expression = new Expression(identifier);
            result = expression.eval().setScale(
                    precision == -1 ? this.getInt("Precision", 3) : precision,
                    BigDecimal.ROUND_HALF_UP
            );
        }catch (Exception ex){
            // If debug is set to "on", we print the StackTrace on an error.
            if(this.getString("Debug", "off").equalsIgnoreCase("on"))
                ex.printStackTrace();

            identifier = "The provided value was invalid!\nReason: " + ex.getMessage();
            return identifier;
        }

        // We have to return it as plainString, or else it would return 10 as 1E+1 (which is just the same)
        return result.toPlainString();
    }
}
