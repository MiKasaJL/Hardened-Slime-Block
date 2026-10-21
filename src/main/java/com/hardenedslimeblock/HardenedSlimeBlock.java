package com.hardenedslimeblock;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * Hardened Slime Block（坚硬的粘液块）——附属入口：向 Carpet 注册本模组的规则。
 *
 * <p>规则在 {@link #onGameStarted()} 中通过
 * {@code CarpetServer.settingsManager.parseSettingsClass} 注册，
 * 之后即可用 {@code /carpet slimeBlockNoPlayerBounce true|false}
 * 在游戏内热切换，无需重启。</p>
 */
public class HardenedSlimeBlock implements CarpetExtension, ModInitializer {

	@Override
	public String version() {
		return "slimeblock-no-player-bounce";
	}

	public static void loadExtension() {
		CarpetServer.manageExtension(new HardenedSlimeBlock());
	}

	@Override
	public void onInitialize() {
		HardenedSlimeBlock.loadExtension();
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(HardenedSlimeBlockRules.class);
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		InputStream langFile = HardenedSlimeBlock.class.getClassLoader()
				.getResourceAsStream("assets/slimeblock-no-player-bounce/lang/%s.json".formatted(lang));
		if (langFile == null) {
			return Collections.emptyMap();
		}
		String jsonData;
		try {
			jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return Collections.emptyMap();
		}
		@SuppressWarnings("deprecation") // gson won't ever drop this
		Gson gson = new GsonBuilder().setLenient().create();
		return gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
	}
}
