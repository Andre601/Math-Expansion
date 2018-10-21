package com.andre601.mathexpansion;

import com.udojava.evalex.Expression;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;


public class MathExpansion extends PlaceholderExpansion {

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

    public String onRequest(OfflinePlayer player, String identifier){

        // It's really that simple... yeah.
        identifier = PlaceholderAPI.setBracketPlaceholders(player, identifier);
        BigDecimal result;

        try {
            Expression expression = new Expression(identifier);
            result = expression.eval();
        }catch (Exception ex){
            return "The provided value was invalid!";
        }

        return result.toPlainString();
    }
}
