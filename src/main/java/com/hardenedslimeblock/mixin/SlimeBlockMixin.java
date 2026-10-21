package com.hardenedslimeblock.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import com.hardenedslimeblock.HardenedSlimeBlockRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 仅对玩家禁用粘液块的弹跳与水平减速效果（Hardened Slime Block / 坚硬的粘液块）。
 *
 * <p>原版 26.1.2 中粘液块对实体的作用链：
 * <ul>
 *   <li>{@code updateEntityMovementAfterFallOn} -> {@code bounceUp}：落地时反转竖直速度（弹跳）</li>
 *   <li>{@code stepOn}：站在方块上时将水平速度乘以约 0.4 倍（减速）</li>
 *   <li>{@code fallOn}：摔落伤害乘数为 0（本模组不改动）</li>
 * </ul>
 *
 * <p>本 Mixin 只在实体为 {@link Player} 且对应 Carpet 规则开启时拦截相应方法：
 * 弹跳开关控制 {@code updateEntityMovementAfterFallOn}，减速开关控制
 * {@code stepOn}。开启时复刻 {@code Block} 的默认落地行为（竖直速度清零），
 * 使玩家将粘液块当作普通实心方块。玩家以外的任何实体（生物、掉落物、矿车、船、弹射物等）
 * 的行为路径完全不经过本拦截，保持原版特性。两条规则可分别通过
 * {@code /carpet slimeBlockNoPlayerBounce <bool>} 与
 * {@code /carpet slimeBlockNoPlayerSlowdown <bool>} 在游戏内热切换，
 * 无需重启。</p>
 */
@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

	/**
	 * 玩家落在粘液块上时不弹跳。
	 *
	 * <p>拦截 {@code updateEntityMovementAfterFallOn} 并复刻 {@code Block}
	 * 的默认实现（竖直速度清零），与原版玩家潜行（isSuppressingBounce）
	 * 时的落地行为一致；其他实体继续走原版分支：非潜行实体调用
	 * {@code bounceUp} 反弹（生物 1.0 倍、非生物 0.8 倍），潜行实体走父类默认实现。</p>
	 */
	@Inject(method = "updateEntityMovementAfterFallOn", at = @At("HEAD"), cancellable = true)
	private void slimeblockNoPlayerBounce$noPlayerBounce(BlockGetter level, Entity entity, CallbackInfo ci) {
		if (HardenedSlimeBlockRules.slimeBlockNoPlayerBounce && entity instanceof Player) {
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
			ci.cancel();
		}
	}

	/**
	 * 玩家在粘液块上行走时不被水平减速。
	 *
	 * <p>拦截 {@code stepOn}。父类 {@code Block#stepOn} 为空实现，因此取消后
	 * 对玩家无副作用，等同于普通方块；非玩家实体保持原版 0.4 倍水平减速。</p>
	 */
	@Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
	private void slimeblockNoPlayerBounce$noPlayerSlowdown(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
		if (HardenedSlimeBlockRules.slimeBlockNoPlayerSlowdown && entity instanceof Player) {
			ci.cancel();
		}
	}
}
