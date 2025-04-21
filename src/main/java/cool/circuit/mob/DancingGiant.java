package cool.circuit.mob;

import com.mojang.datafixers.util.Pair;
import cool.circuit.goal.DanceGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cool.circuit.DancingGiants.getEntityIds;

public class DancingGiant extends Giant {
    public DancingGiant(Location location) {
        super(EntityType.GIANT, ((CraftWorld) location.getWorld()).getHandle());

        final List<Color> colors = Arrays.asList(
                Color.fromRGB(0, 100, 255),   // Blue
                Color.fromRGB(0, 200, 0),     // Green
                Color.fromRGB(255, 230, 0),   // Yellow
                Color.fromRGB(255, 0, 0)      // Red
        );

        final List<List<Pair<EquipmentSlot, ItemStack>>> outfits = new ArrayList<>();

        for (Color color : colors) {
            final org.bukkit.inventory.ItemStack helmet = new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET);
            final org.bukkit.inventory.ItemStack chest = new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE);
            final org.bukkit.inventory.ItemStack legs = new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS);
            final org.bukkit.inventory.ItemStack boots = new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS);

            final LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
            meta.setColor(color);
            helmet.setItemMeta(meta);
            chest.setItemMeta(meta);
            legs.setItemMeta(meta);
            boots.setItemMeta(meta);

            outfits.add(List.of(
                    Pair.of(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(helmet)),
                    Pair.of(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(chest)),
                    Pair.of(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(legs)),
                    Pair.of(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(boots))
            ));
        }

        final List<Pair<EquipmentSlot, ItemStack>> selectedOutfit = outfits.get((int) (Math.random() * outfits.size()));

        for (Pair<EquipmentSlot, ItemStack> armorPiece : selectedOutfit) {
            this.setItemSlot(armorPiece.getFirst(), armorPiece.getSecond());
        }

        setPos(location.getX(), location.getY(), location.getZ());

        this.goalSelector.getAvailableGoals().clear();
        this.goalSelector.addGoal(0, new DanceGoal(this));
        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);

        getEntityIds().add(this.getId());
    }

    public void makeHostile() {
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 10));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 10, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
