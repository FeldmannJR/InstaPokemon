/*
 *  _   __   _   _____   _____       ___       ___  ___   _____  
 * | | |  \ | | /  ___/ |_   _|     /   |     /   |/   | /  ___| 
 * | | |   \| | | |___    | |      / /| |    / /|   /| | | |     
 * | | | |\   | \___  \   | |     / / | |   / / |__/ | | | |     
 * | | | | \  |  ___| |   | |    / /  | |  / /       | | | |___  
 * |_| |_|  \_| /_____/   |_|   /_/   |_| /_/        |_| \_____| 
 * 
 * Projeto feito por Carlos Andre Feldmann Junior, Isaias Finger e Gabriel Augusto Souza
 */
package br.com.instamc.poke;

import br.com.instamc.poke.blockcrafting.BlockCrafting;
import br.com.instamc.poke.camera.CameraManager;
import br.com.instamc.poke.chaves.ChavesDB;
import br.com.instamc.poke.cmds.CmdAddChave;
import br.com.instamc.poke.cmds.CmdAlbum;
import br.com.instamc.poke.cmds.CmdCheck;
import br.com.instamc.poke.cmds.CmdGeracao;
import br.com.instamc.poke.cmds.CmdInsignia;
import br.com.instamc.poke.cmds.CmdItems;
import br.com.instamc.poke.cmds.CmdPcRave;
import br.com.instamc.poke.cmds.CmdPokeHeal;
import br.com.instamc.poke.cmds.CmdVanish;
import br.com.instamc.poke.cmds.CmdVerPC;
import br.com.instamc.poke.cmds.CmdVerParty;
import br.com.instamc.poke.cmds.Roupas;
import br.com.instamc.poke.customItems.CustomItemManager;
import br.com.instamc.poke.dbs.PlayerDB;
import br.com.instamc.poke.dbs.PvPDB;
import br.com.instamc.poke.dbs.PlayerDB.Flag;
import br.com.instamc.poke.elites.EliteManager;
import br.com.instamc.poke.fixes.FindChests;
import br.com.instamc.poke.fixes.TMInteraction;
import br.com.instamc.poke.ginasios.GinasioManager;
import br.com.instamc.poke.insignias.InsigniaDB;
import br.com.instamc.poke.insignias.InsigniasListener;
import br.com.instamc.poke.kits.KitManager;
import br.com.instamc.poke.listeners.DamageListener;
import br.com.instamc.poke.listeners.FlagsListener;
import br.com.instamc.poke.listeners.GeralListener;
import br.com.instamc.poke.listeners.PokeListeners;
import br.com.instamc.poke.logs.LogsDB;
import br.com.instamc.poke.minerar.Minerar;
import br.com.instamc.poke.placas.Sign;
import br.com.instamc.poke.pokeinicial.PokeInicialManager;
import br.com.instamc.poke.pokeloot.PokeLoot;
import br.com.instamc.poke.ranks.RankManager;
import br.com.instamc.poke.ranks.Stats;
import br.com.instamc.poke.raridade.RaridadeManager;
import br.com.instamc.poke.shop.ShopManager;
import br.com.instamc.poke.shop.chest.ChestShop;
import br.com.instamc.poke.shop.menus.CompraLendario;
import br.com.instamc.poke.shop.menus.CompraPokemon;
import br.com.instamc.poke.shop.menus.FazShinePokemon;
import br.com.instamc.poke.shop.menus.MenuFazAlgoPoke;
import br.com.instamc.poke.shop.menus.TrocaNaturePoke;
import br.com.instamc.poke.shop.menus.TrocarPokeball;
import br.com.instamc.poke.sorteios.SorteioManager;
import br.com.instamc.poke.tpscroll.TPScrollManager;
import br.com.instamc.poke.tpscroll.menu.ScrollMenu;
import br.com.instamc.poke.votecoins.VoteCoinManager;
import br.com.instamc.sponge.library.DB;
import br.com.instamc.sponge.library.SpongeLib;
import br.com.instamc.sponge.library.apis.ComandoAPI;
import br.com.instamc.sponge.library.apis.LibAPI;
import br.com.instamc.sponge.library.cmds.CmdAjuda;
import br.com.instamc.sponge.library.menu.ClickType;
import br.com.instamc.sponge.library.menu.Menu;
import br.com.instamc.sponge.library.menu.MenuButton;
import br.com.instamc.sponge.library.utils.ItemStackBuilder;
import br.com.instamc.sponge.library.utils.ItemUtils;
import br.com.instamc.sponge.library.utils.SchedulerUtils;
import br.com.instamc.sponge.library.utils.Txt;
import br.com.instamc.sponge.vip.ComprouPacote;
import br.com.instamc.sponge.vip.SpongeVIP;
import br.com.instamc.sponge.vip.pacotes.Pacote;
import br.com.instamc.sponge.vip.pacotes.PacoteVIP;
import br.instamc.protection.SpongeProtection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.blocks.BlockBerryTree;
import com.pixelmonmod.pixelmon.blocks.PixelmonBlock;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.config.PixelmonBlocksApricornTrees;
import com.pixelmonmod.pixelmon.config.PixelmonBlocksBerryTrees;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.InteractionTM;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.items.PixelmonItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBar;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.hanging.Hanging;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

/**
 * Skype: junior.feldmann GitHub: https://github.com/feldmannjr Facebook:
 * https://www.facebook.com/carlosandre.feldmannjunior
 *
 * @author Feldmann
 */
@Plugin(id = "instapoke", name = "SpongePokemon-ITMC", authors = "Feldmann", dependencies = { @Dependency(id = "pixelmon"), @Dependency(id = "instalibrary"), @Dependency(id = "instavip"), @Dependency(id = "instaprotection") }, description = "Plugin Pokemon INSTAMC", version = "0.1")

public class InstaPokemon {
	public static Text prefix = Txt.f("§7[§cPoké§fmon§7] ");

	public static PluginContainer getPlugin() {
		return Sponge.getPluginManager().getPlugin("instapoke").get();
	}

	public static void sendMessage(Player player, String s) {
		sendMessage(player, Txt.f(s));
	}

	public static void sendMessage(Player player, Text t) {
		Text p = prefix.toBuilder().append(t).build();
		player.sendMessage(p);

	}

	public static void sendMessage(EntityPlayerMP p, String s) {
		Optional<Player> op = Sponge.getServer().getPlayer(p.getUniqueID());
		if (op.isPresent()) {
			sendMessage(op.get(), s);
		}
	}

	public static void sendMessage(UUID uuid, String s) {
		Optional<Player> p = Sponge.getServer().getPlayer(uuid);
		if (p.isPresent()) {
			sendMessage(p.get(), s);
		}
	}

	public static String getPrefixWithName(EntityPlayerMP pl) {
		Optional<Player> p = Sponge.getServer().getPlayer(pl.getUniqueID());
		if (p.isPresent()) {
			return getPrefixWithName(p.get());
		}
		return "";
	}

	public static String getPrefixWithName(Player p) {
		String nome = LibAPI.getPrefix(p, "", " ") + "§f" + p.getName();
		return nome;
	}

	public static void broadcast(String s) {
		Text t = prefix.toBuilder().append(Txt.f(s)).build();

		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			player.sendMessage(t);
		}
		Sponge.getServer().getConsole().sendMessage(t);
	}

	Game game;
	Logger logger;
	public static InstaPokemon instancia;
	ConfigurationNode config;

	public static String banco = "pokemon2_geral";
	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	public Path getDefaultConfig() {
		return defaultConfig;
	}

	public void loadConfig() {
		if (!getDefaultConfig().toFile().exists()) {
			try {
				getDefaultConfig().toFile().createNewFile();
				config = configManager.load();

				config.getNode("DB", "banco").setValue("pokemon2_geral");
				configManager.save(config);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				config = configManager.load();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		banco = config.getNode("DB", "banco").getString();
		logger.debug("Banco selecionado:" + banco);
	}

	@Inject
	public InstaPokemon(Game game, Logger logger) {
		instancia = this;
		this.game = game;
		this.logger = logger;
	}

	// ISSO AQUI É ON ENABLE WTF
	@Listener
	public void onStart(GameStartedServerEvent ev) {
		registerListeners();
		registerCmds();
		ShopManager.init();
		loadConfig();
		InsigniaDB.init();
		KitManager.init();
		GinasioManager.init();
		PlayerDB.init();
		ChestShop.init();
		RankManager.init();
		PvPDB.init();
		PokeLoot.init();
		SorteioManager.init();
		EliteManager.init();
		Minerar.init();
		setupAjuda();
		setupTerrenos();
		CameraManager.init();
		RaridadeManager.init();
		LimpaItens.init();
		ChavesDB.init();
		fixHm();
		LogsDB.init();
		VoteCoinManager.init();
		TPScrollManager.init();
		SpongeVIP.comprapacote.add(new ComprouPacote() {

			@Override
			public void comprou(Player arg0, Pacote pac) {
				if (pac instanceof PacoteVIP) {
					PokeScore.updateTeam(arg0);
					PokeScore.buildSide(arg0);
				}
			}
		});

		SpongeLib.getPlugin().doLog("Finished onStart");
	}

	public void fixHm() {
		for (IInteraction i : new ArrayList<IInteraction>(EntityPixelmon.interactionList)) {
			if (i instanceof InteractionTM) {
				EntityPixelmon.interactionList.remove(i);
			}
		}
		EntityPixelmon.interactionList.add(new TMInteraction());
	}

	public void setupTerrenos() {
		List<String> id = SpongeProtection.idsbloquedos;
		id.add(PixelmonBlocks.boxBlock.getRegistryName().toString());
		id.add(PixelmonBlocks.fridgeBlock.getRegistryName().toString());
		id.add(PixelmonBlocks.endTableBlock.getRegistryName().toString());
		id.add(PixelmonBlocks.gymSignBlock.getRegistryName().toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreeBlack.toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreeBlue.toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreeGreen.toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreePink.toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreeRed.toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreeWhite.toString());
		id.add(PixelmonBlocksApricornTrees.apricornTreeYellow.toString());
		for(BlockBerryTree a : PixelmonBlocksBerryTrees.berryBlocks) {
			id.add(a.getRegistryName().toString());
		
		
		}
		
		id.add(PixelmonBlocks.mechanicalAnvil.getRegistryName().toString());
		id.add(PixelmonBlocks.fossilCleaner.getRegistryName().toString());
		id.add(PixelmonBlocks.fossilMachine.getRegistryName().toString());

	}

	public static Connection getDB() {
		return DB.getConnection(banco);
	}

	public void registerCmds() {

		// Sponge.getCommandManager().register(this,
		// CommandSpec.builder().description(Text.of("Pohha
		// nenhuma")).executor(new Roupas()).build(), "roupas");
		ComandoAPI.enable(new CmdInsignia());
		ComandoAPI.enable(new CmdItems());
		ComandoAPI.enable(new CmdPcRave());
		ComandoAPI.enable(new CmdAlbum());
		ComandoAPI.enable(new CmdVanish());
		ComandoAPI.enable(new CmdPokeHeal());
		ComandoAPI.enable(new CmdAddChave());
		ComandoAPI.enable(new CmdCheck());
		ComandoAPI.enable(new CmdGeracao());
		ComandoAPI.enable(new CmdVerParty());
		ComandoAPI.enable(new CmdVerPC());
		ComandoAPI.enable(new FindChests());

	}

	public void registerListeners() {

		Sponge.getEventManager().registerListeners(this, new BlockCrafting());
		Sponge.getEventManager().registerListeners(this, new PokeInicialManager());
		Sponge.getEventManager().registerListeners(this, new InsigniasListener());
		Sponge.getEventManager().registerListeners(this, new Sign());
		Sponge.getEventManager().registerListeners(this, new CustomItemManager());
		Sponge.getEventManager().registerListeners(this, new GeralListener());
		Sponge.getEventManager().registerListeners(this, new PokeScore());
		Sponge.getEventManager().registerListeners(this, new AntiAFK());
		Sponge.getEventManager().registerListeners(this, new VoteCoinManager());
		Sponge.getEventManager().registerListeners(this, new DamageListener());

		Pixelmon.EVENT_BUS.register(new PokeListeners());
		Pixelmon.EVENT_BUS.register(new FlagsListener());
	}

	public void setupAjuda() {

		Text kit = Txt.f("§c§l/kit §7 - §fItens iniciais e avançados.");
		kit = kit.toBuilder().onClick(TextActions.runCommand("/kit")).build();
		CmdAjuda.ajuda.add(kit);

		CmdAjuda.ajuda.add(Txt.f("§a§l/terreno §7- §fComando para comprar, editar terrenos!"));

		Text elite = Txt.f("§9§l/elite §7 - §fComandos de elite(grupo de jogadores)!");
		elite = elite.toBuilder().onClick(TextActions.runCommand("/elite")).build();

		CmdAjuda.ajuda.add(elite);

		Text mercado = Txt.f("§6§l/mercado §7 - §fCompre item de outros jogadores!");
		mercado = mercado.toBuilder().onClick(TextActions.runCommand("/mercado")).build();
		CmdAjuda.ajuda.add(mercado);

		Text insignias = Txt.f("§2§l/warp §7 - §fTeleportes do servidor!");
		insignias = insignias.toBuilder().onClick(TextActions.runCommand("/warp")).build();
		CmdAjuda.ajuda.add(insignias);

		Text shop = Txt.f("§b§l/shop §7 - §fVê a loja de gemas, boosts para o jogo!");
		shop = shop.toBuilder().onClick(TextActions.runCommand("/shop")).build();
		CmdAjuda.ajuda.add(shop);

		Text duvida = Txt.f("§3§l/duvida §7 - §fManda uma duvida para a staff!");
		duvida = duvida.toBuilder().onClick(TextActions.runCommand("/duvida")).build();
		CmdAjuda.ajuda.add(duvida);

		Text.Builder outros = Txt.f("§f§lOutros: §r").toBuilder();
		Text home = Txt.f("§a§l[HOME]").toBuilder().onHover(TextActions.showText(Txt.f(//
				"§e/sethome [nome] §7- §fSeta home.\n"//
						+ "§e/home [home] §7- §fTp para home.\n"//
						+ "§e/listhomes §7- §fLista suas homes.\n"//
						+ "§e/deletehome [home] §7- §fRemove uma home."

		))).build();
		Text money = Txt.f(" §6§l[MONEY]").toBuilder().onHover(TextActions.showText(Txt.f(//
				"§e/money [nome] §7- §fVê o money.\n"//
						+ "§e/money pay <nome> <valor> §7- §fDa dinheiro.\n"//
						+ "§e/money top §7- §fVê quem mais tem dinheiro"//

		))).build();
		Text perfil = Txt.f(" §d§l[PERFIL]").toBuilder().onHover(TextActions.showText(Txt.f(//
				"§e/perfil <nome> §7- §fVê o perfil de alguem.\n"//
						+ "§e/album§7 - §fVê um album de fotos!.\n"//
						+ "§e/insignias §7 - §fVê suas insignias ou de outros jogadores"

		))).build();

		outros.append(home);
		outros.append(money);
		outros.append(perfil);
		CmdAjuda.ajuda.add(outros.build());

	}

	public static boolean isOp(Player p) {
		
		return p.hasPermission("instamc.op");
		
	}
}
