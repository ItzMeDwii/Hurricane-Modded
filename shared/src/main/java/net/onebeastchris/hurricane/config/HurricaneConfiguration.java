package net.onebeastchris.hurricane.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public final class HurricaneConfiguration {
    @Comment("""
            "Fixes" Bedrock players running into lagback issues on certain blocks by removing any collision detection from the given block.
            Caveats: a custom client - Java or Bedrock - could take advantage of no collision and walk right through.
             Additionally, placement of these blocks on both platforms may be buggier than usual.""")
    private CollisionFixes collisionFixes = new CollisionFixes();

    @Comment("""
            Suppresses "Mismatch in destroy block pos" warnings so they don't spam each time e.g. grass is broken.\s
            This is set to false by default, as there can be valid reasons to have the warnings.\s
            """)
    private boolean suppressWarnings = false;

    @Comment("""
            Lets Bedrock players fight with a spear. Their client can only send a plain entity attack, which the server
            drops for piercing weapons, so Hurricane runs the spear's own jab (the server-side raycast) instead.
            Caveats: only players Floodgate/Geyser reports as Bedrock are affected, and the jab still uses the spear's
            own attack cooldown, so a modified Bedrock client gains nothing a Java client does not already have.""")
    private CombatFixes combatFixes = new CombatFixes();

    @SuppressWarnings("ClassEscapesDefinedScope")
    public CollisionFixes collisionFixes() {
        return collisionFixes;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public CombatFixes combatFixes() {
        return combatFixes;
    }

    public boolean suppressWarnings() {
        return suppressWarnings;
    }

    @ConfigSerializable
    static final class CollisionFixes {
        private boolean bamboo = true;
        private boolean pointedDripstone = true;
        public boolean bamboo() {
            return bamboo;
        }
        public boolean pointedDripstone() {
            return pointedDripstone;
        }
    }

    @ConfigSerializable
    static final class CombatFixes {
        private boolean bedrockSpearStab = true;
        public boolean bedrockSpearStab() {
            return bedrockSpearStab;
        }
    }
}