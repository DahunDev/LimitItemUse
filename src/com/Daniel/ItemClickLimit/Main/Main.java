package com.Daniel.ItemClickLimit.Main;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;



public class Main extends JavaPlugin

		implements Listener {
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Command plugin;

	boolean useDeny_msg_inv = false;
	boolean clicker = true;

	boolean blockinvClickOP = true;
	boolean blockDisinvClickOP = true;

	boolean blockInteractOP = false;
	boolean useDeny_msg_interact = true;
	boolean useMSG_on_deny_dispensor_inv = true;

	String Deny_Msg_inv = "��b��l[ ��f��lServer ��b��l] ��c�ش� ���������� �ش� �������� ��Ŭ���� �Ұ����մϴ�.";
	String Deny_Msg_interact = "��b��l[ ��f��lServer ��b��l] ��c�ش� ���������� Ŭ�� �Ͻ� �ش� ��ƼƼ/���� ����� �Ұ����մϴ�.";

	String ArmorStandMSG = "��b��l[ ��f��lServer ��b��l] ��c���� ��ġ��� ��ġ �� ��Ŭ���� �Ұ����մϴ�.";
	String ItemFrameMSG = "��b��l[ ��f��lServer ��b��l] ��c������ ���ڴ� ��ġ�� �Ұ����մϴ�.";
	String PaintingMSG = "��b��l[ ��f��lServer ��b��l] ��c���� ��ġ��� ��ġ�� �Ұ����մϴ�.";
	String Deny_Msg_inv_All = "��b��l[ ��f��lServer ��b��l] ��c�ش� �������� ��Ŭ�� �� ��ġ�� �Ұ����մϴ�.";
	String Deny_MSG_interact_All = "��b��l[ ��f��lServer ��b��l] ��cŬ�� �Ͻ� �ش� ��ƼƼ/���� ����� �Ұ����մϴ�.";
	
	String Deny_Msg_Item_dispensor = "��b��l[ ��f��lServer ��b��l] ��c�ش� ����������  �ش� �������� �߻�⿡ ����� �Ұ����մϴ�.";
	String Deny_Msg_Item_dispensor_All = "��b��l[ ��f��lServer ��b��l] ��c�ش� �������� �߻�⿡ ����� �Ұ����մϴ�.";


	// protected List<String> crashnicknames;
	String cartMSG = "��b��l[ ��f��lServer ��b��l] ��c�ش� ���忡���� ����/�򶧱Ⱑ �Ǹ� ���� ���� ������ ����� ��Ȱ��ȭ ���ֽ��ϴ�.";

	public void onDisable() {
		PluginDescriptionFile pdFile = this.getDescription();
		System.out.println(
				String.valueOf(String.valueOf(pdFile.getName())) + " " + pdFile.getVersion() + " ��(��) ��Ȱ��ȭ �Ǿ����ϴ�.");
	}

	public void onEnable() {

		PluginDescriptionFile pdFile = this.getDescription();
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) this);

		reloadConfiguration();
		System.out.println(
				String.valueOf(String.valueOf(pdFile.getName())) + " " + pdFile.getVersion() + " ��(��) Ȱ��ȭ �Ǿ����ϴ�.");

	}

	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel,
			final String[] args) {
		if (commandLabel.equalsIgnoreCase("itemclicklimit")) {
			if (sender.hasPermission("itemclicklimit.reload") || sender.isOp()) {
				PluginDescriptionFile pdFile = this.getDescription();
				this.reloadConfiguration();
				sender.sendMessage("��b��l[ ��f��lServer ��b��l] ��c��l" + pdFile.getName() + "��(��) ���ε� �Ǿ����ϴ�.");
				

				
				
			}

			return false;
		}
		return false;
	}

	// STORAGE_MINECART

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVehicleDestroyEvent(VehicleDestroyEvent e) {

		// e.getAttacker().sendMessage("����īƮ �ı���");
		Vehicle vehicle = e.getVehicle();
		
		Entity attacker = e.getAttacker();

		if (e.getVehicle().getType().equals(EntityType.MINECART_CHEST)) {

			if (attacker == null || !attacker.hasPermission("ItemClickLimit.bypass.cart"))
				if (getConfig().getStringList("Disabled-MineCart_ItemDrop-worlds")
						.contains(vehicle.getLocation().getWorld().getName())) {

					StorageMinecart cart = (StorageMinecart) e.getVehicle();
					Inventory inv = cart.getInventory();
					inv.clear();
					if(attacker != null) {
					e.getAttacker().sendMessage(cartMSG);
					}
					vehicle.remove();

				}

		} else if (vehicle.getType().equals(EntityType.MINECART_HOPPER)) {

			if (attacker == null || !e.getAttacker().hasPermission("ItemClickLimit.bypass.cart"))
				if (getConfig().getStringList("Disabled-MineCart_ItemDrop-worlds")
						.contains(vehicle.getLocation().getWorld().getName())) {

					HopperMinecart cart = (HopperMinecart) e.getVehicle();
					Inventory inv = cart.getInventory();
					inv.clear();
					if(attacker != null ) {
						e.getAttacker().sendMessage(cartMSG);

					}
					vehicle.remove();

				}

		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlace(PlayerInteractEvent e) {

		Action action = e.getAction();
		Player p = e.getPlayer();
		String material = "AIR";
		if (e.hasItem()) {
			material = e.getMaterial().toString();
		} else {
			return;
		}

		if (action == Action.RIGHT_CLICK_BLOCK) {

			if (getConfig().getBoolean("Ban-ArmorStand-Place", true)) {

				if (!e.getPlayer().hasPermission(
						getConfig().getString("bypassBanArmorstandPerm", "ItemClickLimit.bypass.ArmorStand"))) {

					if (getConfig().getString("ArmorStand", "ARMOR_STAND").equalsIgnoreCase(material)) {
						e.setCancelled(true);
						p.sendMessage(ArmorStandMSG);
					}

				}
			}

			if (getConfig().getBoolean("Ban-ItemFrame-Place", true)) {

				if (!e.getPlayer().hasPermission(
						getConfig().getString("bypassBanItemFramePerm", "ItemClickLimit.bypass.ItemFrame"))) {

					if (getConfig().getString("ItemFrame", "ITEM_FRAME").equalsIgnoreCase(material)) {
						e.setCancelled(true);
						p.sendMessage(ItemFrameMSG);
					}

				}
			}

			if (getConfig().getBoolean("Ban-Painting-Place", true)) {

				if (!e.getPlayer().hasPermission(
						getConfig().getString("bypassBanPaintingPerm", "ItemClickLimit.bypass.Painting"))) {

					if (getConfig().getString("Painting", "PAINTING").equalsIgnoreCase(material)) {
						e.setCancelled(true);
						p.sendMessage(PaintingMSG);
					}

				}
			}
		}
	}

	public void reloadConfiguration() {
		PluginDescriptionFile pdFile = this.getDescription();
		File config = new File("plugins/" + pdFile.getName() + "/config.yml");

		if (config.exists()) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
			this.saveDefaultConfig();
			for (String key : cfg.getConfigurationSection("").getKeys(true)) {
				if (!this.getConfig().contains(key)) {
					this.getConfig().set(key, cfg.get(key));
				}
			}
		} else {
			this.saveDefaultConfig();
		}
		this.reloadConfig();

		if (!getConfig().getString("MineCart-msg").isEmpty()) {
			this.PaintingMSG = ChatColor.translateAlternateColorCodes('&', getConfig().getString("MineCart-msg"));

		}

		if (!getConfig().getStringList("Deny-Msg-inv").isEmpty()) {
			Deny_Msg_inv = "";
			for (String line : getConfig().getStringList("Deny-Msg-inv")) {
				this.Deny_Msg_inv = (this.Deny_Msg_inv + ChatColor.translateAlternateColorCodes('&', line) + "\n");
			}

		}

		if (!getConfig().getStringList("Deny-Msg-interact").isEmpty()) {
			Deny_Msg_interact = "";
			for (String line : getConfig().getStringList("Deny-Msg-interact")) {
				this.Deny_Msg_interact = (this.Deny_Msg_interact + ChatColor.translateAlternateColorCodes('&', line)
						+ "\n");
			}

		}

		if (!getConfig().getStringList("Deny-use-Msg-All-Region").isEmpty()) {
			Deny_Msg_inv_All = "";
			for (String line : getConfig().getStringList("Deny-use-Msg-All-Region")) {
				this.Deny_Msg_inv_All = (this.Deny_Msg_inv_All + ChatColor.translateAlternateColorCodes('&', line)
						+ "\n");
			}

		}

		if (!getConfig().getStringList("Deny-interact-Msg-All-Region").isEmpty()) {
			Deny_MSG_interact_All = "";
			for (String line : getConfig().getStringList("Deny-interact-Msg-All-Region")) {
				this.Deny_MSG_interact_All = (this.Deny_MSG_interact_All
						+ ChatColor.translateAlternateColorCodes('&', line) + "\n");
			}

		}
		
		
		
		
		if (!getConfig().getStringList("Deny_Msg_Item_dispensor").isEmpty()) {
			Deny_Msg_Item_dispensor = "";
			for (String line : getConfig().getStringList("Deny_Msg_Item_dispensor")) {
				this.Deny_Msg_Item_dispensor = (this.Deny_Msg_Item_dispensor
						+ ChatColor.translateAlternateColorCodes('&', line) + "\n");
			}

		}

		
		if (!getConfig().getStringList("Deny_Msg_Item_dispensor_All-Region").isEmpty()) {
			Deny_Msg_Item_dispensor_All = "";
			for (String line : getConfig().getStringList("Deny_Msg_Item_dispensor_All-Region")) {
				this.Deny_Msg_Item_dispensor_All = (this.Deny_Msg_Item_dispensor_All
						+ ChatColor.translateAlternateColorCodes('&', line) + "\n");
			}

		}

		

		blockInteractOP = getConfig().getBoolean("block-interact-for_OP", false);
		blockinvClickOP = getConfig().getBoolean("block-using-item-for_OP", true);
		blockDisinvClickOP = getConfig().getBoolean("block-using-item-dispensor-for_OP", true);
		useMSG_on_deny_dispensor_inv = getConfig().getBoolean("MSG-on-deny-dispensor-inv", true);
		
		
		this.useDeny_msg_inv = this.getConfig().getBoolean("MSG-on-deny-click-inv", false);

		useDeny_msg_interact = getConfig().getBoolean("MSG-on-deny-interact", true);

		if (!getConfig().getString("ArmorStandMSG").isEmpty()) {
			this.ArmorStandMSG = ChatColor.translateAlternateColorCodes('&', getConfig().getString("ArmorStandMSG"));

		}
		if (!getConfig().getString("ItemFrameStandMSG").isEmpty()) {
			this.ItemFrameMSG = ChatColor.translateAlternateColorCodes('&', getConfig().getString("ItemFrameStandMSG"));

		}

		if (!getConfig().getString("PaintingMSG").isEmpty()) {
			this.PaintingMSG = ChatColor.translateAlternateColorCodes('&', getConfig().getString("PaintingMSG"));

		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onClickitem(PlayerInteractEvent e) {

		Action action = e.getAction();
		Player p = e.getPlayer();
		String world = e.getPlayer().getWorld().getName();
		String material = "AIR";
		if (e.hasItem()) {
			material = e.getMaterial().toString();
		} else {
			return;
		}

		if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {

			if (!p.hasPermission("ItemClickLimit.bypass.use") || blockinvClickOP) {
				if (getConfig().getStringList("All-Region-use").contains(material)) {
					e.setCancelled(true);
					if (useDeny_msg_inv) {
						e.getPlayer().sendMessage(Deny_Msg_inv_All);
					}
					return;
				}

				if (getConfig().getStringList("world-use." + world + "." + "__global__").contains(material)) {
					e.setCancelled(true);
					if (useDeny_msg_inv) {
						e.getPlayer().sendMessage(Deny_Msg_inv);
					}

					return;

				}

				for (ProtectedRegion r : getRegions(p)) {

					if (getConfig().getStringList("world-use." + world + "." + r.getId()).contains(material)) {
						e.setCancelled(true);
						if (useDeny_msg_inv) {
							e.getPlayer().sendMessage(Deny_Msg_inv);
						}

						return;
					}

					return;
				}
			}

			return;
		}


	}
	
	@EventHandler
	public void DispensorClickEvent(InventoryClickEvent e) {
		HumanEntity clicker = e.getWhoClicked();

		Inventory topinv = clicker.getOpenInventory().getTopInventory();
		Location loc = clicker.getLocation();
		String world = loc.getWorld().getName();

		if (topinv.getType() == InventoryType.DISPENSER) {
			if ((!e.isCancelled()) && e.getCurrentItem() != null) {

				ItemStack item = e.getCurrentItem();
				String material = "AIR";

				if (item != null) {
					material = item.getType().toString();

				}

				if (!clicker.hasPermission("ItemClickLimit.bypass.dispensor") || blockDisinvClickOP) {

					if (getConfig().getStringList("All-Region-dispensor").contains(material)) {
						e.setCancelled(true);
						if (useMSG_on_deny_dispensor_inv) {
							clicker.sendMessage(Deny_Msg_Item_dispensor_All);
						}
						return;
					}

					if (getConfig().getStringList("world-dispensor." + world + "." + "__global__")
							.contains(material)) {
						e.setCancelled(true);
						if (useMSG_on_deny_dispensor_inv) {
							clicker.sendMessage(Deny_Msg_Item_dispensor);
						}
						return;

					}

					for (ProtectedRegion r : getRegions(loc)) {

						if (getConfig().getStringList("world-dispensor." + world + "." + r.getId())
								.contains(material)) {
							e.setCancelled(true);
							if (useMSG_on_deny_dispensor_inv) {
								clicker.sendMessage(Deny_Msg_Item_dispensor);
							}

							return;
						}

						return;
					}
				}

			}

		}
	}
	
	
	
	
	
	
	

	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent e) {

		Player p = e.getPlayer();
		String world = e.getPlayer().getWorld().getName();
		String material = "AIR";
		Block block = e.getClickedBlock();

		if (block != null) {
			material = block.getType().toString();
			world = block.getWorld().getName();
		} else {
			return;
		}

		if (!p.hasPermission("ItemClickLimit.bypass.interact") || blockInteractOP) {
			if (getConfig().getStringList("All-Region-interact").contains(material)) {
				e.setCancelled(true);
				if (useDeny_msg_interact) {
					p.sendMessage(Deny_MSG_interact_All);
				}
				return;
			}

			if (getConfig().getStringList("world-interact-target." + world + "." + "__global__").contains(material)) {
				e.setCancelled(true);
				if (useDeny_msg_interact) {
					p.sendMessage(Deny_Msg_interact);
				}

				return;

			}

			for (ProtectedRegion r : getRegions(block.getLocation())) {

				if (getConfig().getStringList("world-interact-target." + world + "." + r.getId()).contains(material)) {
					e.setCancelled(true);
					if (useDeny_msg_interact) {
						p.sendMessage(Deny_Msg_interact);
					}

					return;
				}

				return;
			}
		}

		return;

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInteractEntity(PlayerInteractEntityEvent e) {

		Player p = e.getPlayer();

		String world = e.getPlayer().getWorld().getName();
		String target = "AIR";
		Entity entity = e.getRightClicked();

		if (entity != null) {

			target = entity.getType().toString();
			world = entity.getWorld().getName();
		} else {
			return;
		}

		if (!p.hasPermission("ItemClickLimit.bypass.interact") || blockInteractOP) {
			if (getConfig().getStringList("All-Region-interact").contains(target)) {
				e.setCancelled(true);
				if (useDeny_msg_interact) {
					p.sendMessage(Deny_MSG_interact_All);
				}
				return;
			}

			if (getConfig().getStringList("world-interact-target." + world + "." + "__global__").contains(target)) {
				e.setCancelled(true);
				if (useDeny_msg_interact) {
					p.sendMessage(Deny_Msg_interact);
				}

				return;

			}

			for (ProtectedRegion r : getRegions(entity.getLocation())) {

				if (getConfig().getStringList("world-interact-target." + world + "." + r.getId()).contains(target)) {
					e.setCancelled(true);
					if (useDeny_msg_interact) {
						p.sendMessage(Deny_Msg_interact);
					}

					return;
				}

				return;
			}
		}

		return;

	}

	public Set<ProtectedRegion> getRegions(final Location loc) {
		final RegionManager rgm = WorldGuardPlugin.inst().getRegionManager(loc.getWorld());
		final ApplicableRegionSet ars = rgm.getApplicableRegions(loc);
		return ars.getRegions();
	}

	public Set<ProtectedRegion> getRegions(final Player p) {
		final RegionManager rgm = WorldGuardPlugin.inst().getRegionManager(p.getWorld());
		final ApplicableRegionSet ars = rgm.getApplicableRegions(p.getLocation());
		return ars.getRegions();
	}

}
