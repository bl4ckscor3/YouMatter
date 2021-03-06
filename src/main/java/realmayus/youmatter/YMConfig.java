package realmayus.youmatter;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class YMConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final YMConfig CONFIG;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> filterItems;
    public final ForgeConfigSpec.BooleanValue filterMode;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> overrides;
    public final ForgeConfigSpec.ConfigValue<Integer> defaultAmount;

    static {
        Pair<YMConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(YMConfig::new);
        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    YMConfig(ForgeConfigSpec.Builder builder) {
        filterMode = builder
                .comment("Use the filterItems list as blacklist (true) or as whitelist (false). Whitelist means, that you can only duplicate those items in that list. Blacklist is vice-versa.")
                .define("filterMode", true);
        filterItems = builder
                .comment("List of items that are being treated specially. See filterMode for further details.")
                .define("filterItems", Lists.newArrayList("youmatter:umatter_bucket", "youmatter:stabilizer_bucket"), e -> e instanceof String);
        overrides = builder
                .comment("Overrides: Set your desired required U-Matter values for each item. These do not apply when you e.g. have whitelist on but it doesn't include the desired override.")
                .define("overrides", Lists.newArrayList("minecraft:diamond=2500", "minecraft:nether_star=5000"), e -> e instanceof String);
        defaultAmount = builder
                .comment("The default amount that is required to duplicate an item if it is not overridden.")
                .define("defaultAmount", 1000);
    }

    public Object[] getOverride(String registryName) {
        for(String s : overrides.get()) {
            String foundName = s.substring(0, s.indexOf('='));
            String foundValue = s.substring(s.indexOf('=')).substring(1);
            if (foundName.equalsIgnoreCase(registryName)) {
                return new Object[]{foundName, foundValue};
            }
        }
        return null;
    }
}
