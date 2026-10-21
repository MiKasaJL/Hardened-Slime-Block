package com.hardenedslimeblock;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;

/**
 * Carpet 规则定义（Hardened Slime Block / 坚硬的粘液块）。
 *
 * <p>规则切换时 Carpet 会直接写回静态字段
 * （见 fabric-carpet {@code carpet.settings.ParsedRule#set}），
 * 因此 Mixin 读取该字段即可获得实时生效的热切换能力。</p>
 *
 * <p>规则名保持 {@code slimeBlockNoPlayerBounce} 与
 * {@code slimeBlockNoPlayerSlowdown} 不变，以兼容既有命令习惯。</p>
 */
public class HardenedSlimeBlockRules {

	/**
	 * false（默认）：原版行为，粘液块照常使玩家弹跳；
	 * true：禁用粘液块对玩家的弹跳。
	 * 对玩家以外的实体无任何影响。
	 */
	@Rule(categories = {RuleCategory.SURVIVAL, RuleCategory.FEATURE})
	public static boolean slimeBlockNoPlayerBounce = false;

	/**
	 * false（默认）：原版行为，粘液块照常使玩家减速；
	 * true：禁用粘液块对玩家的水平减速。
	 * 对玩家以外的实体无任何影响。
	 */
	@Rule(categories = {RuleCategory.SURVIVAL, RuleCategory.FEATURE})
	public static boolean slimeBlockNoPlayerSlowdown = false;
}
