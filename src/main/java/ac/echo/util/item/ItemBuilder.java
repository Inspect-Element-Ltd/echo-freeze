package ac.echo.util.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemBuilder {

    private final ItemStack stack;

    public ItemBuilder (Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder (ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder type(Material material) {
        stack.setType(material);
        return this;
    }

    public Material getType() {
        return stack.getType();
    }

    public ItemBuilder name(String name) {
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', name));
        stack.setItemMeta(stackMeta);
        return this;
    }

    public String getName() {
        if (!stack.hasItemMeta() || !stack.getItemMeta().hasDisplayName()) return null;
        return stack.getItemMeta().getDisplayName();
    }

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public int getAmount() {
        return stack.getAmount();
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        lore.replaceAll(textToTranslate -> ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', textToTranslate));

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setLore(lore);
        stack.setItemMeta(stackMeta);
        return this;
    }

    public List<String> getLore() {
        if (!stack.hasItemMeta() || !stack.getItemMeta().hasLore()) return null;
        return stack.getItemMeta().getLore();
    }

    public ItemBuilder color(ItemDataColor color) {
        return durability(color.getValue());
    }

    public ItemBuilder data(short data) {
        return durability(data);
    }

    public ItemBuilder durability(short durability) {
        stack.setDurability(durability);
        return this;
    }

    public short getDurability() {
        return stack.getDurability();
    }

    public ItemDataColor getColor() {
        return ItemDataColor.getByValue(stack.getDurability());
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder unenchant(Enchantment enchantment) {
        stack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder flag(ItemFlag...flag) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder deflag(ItemFlag ...flag) {
        ItemMeta meta = stack.getItemMeta();
        meta.removeItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullOwner(String name) {
        if (!(stack.getItemMeta() instanceof SkullMeta)) return this;

        stack.setDurability((byte) 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(name);
        stack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder ifThen(Predicate<ItemBuilder> ifTrue, Function<ItemBuilder, Object> then) {
        if (ifTrue.test(this))
            then.apply(this);

        return this;
    }

    public ItemStack build() {
        return get();
    }

    public ItemStack get() {
        return stack;
    }

}