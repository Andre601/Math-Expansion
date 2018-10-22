package com.andre601.mathexpansion;

import com.udojava.evalex.Expression;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

public class MathExpansion extends PlaceholderExpansion{

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

    // When the placeholder is requested
    public String onRequest(OfflinePlayer player, String identifier){

        // We first replace any appearance of {placeholder} with the right value
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        BigDecimal result;
        try {
            // We try to evaluate the provided String. If it fails, we return a invalid value-response
            Expression expression = new Expression(identifier);
            result = expression.eval();
        }catch (Exception ex){


            identifier = "The provided value was invalid!";
            return identifier;
        }

        // We have to return it as plainString, or else it would return 10 as 1E+1 (which is just the same)
        return result.toPlainString();
    }
}
