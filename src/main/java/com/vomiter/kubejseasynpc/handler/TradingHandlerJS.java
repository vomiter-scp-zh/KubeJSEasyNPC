package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.util.ScriptValidation;
import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import de.markusbordihn.easynpc.data.action.ActionDataEntry;
import de.markusbordihn.easynpc.data.action.ActionDataSet;
import de.markusbordihn.easynpc.data.action.ActionEventSet;
import de.markusbordihn.easynpc.data.action.ActionEventType;
import de.markusbordihn.easynpc.data.trading.TradingDataSet;
import de.markusbordihn.easynpc.data.trading.TradingType;
import de.markusbordihn.easynpc.entity.easynpc.data.ActionEventDataCapable;
import de.markusbordihn.easynpc.entity.easynpc.data.TradingDataCapable;
import de.markusbordihn.easynpc.handler.TradingOfferHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

/** Configured trade offers/settings. Read results are detached; setters apply and sync. */
public final class TradingHandlerJS extends NPCComponent {
    public TradingHandlerJS(NPCHandle npc) { super(npc); }

    private TradingDataCapable<?> trading() {
        TradingDataCapable<?> data = entity().getEasyNPCTradingData();
        if (data == null) throw new IllegalStateException("NPC does not support trading");
        return data;
    }
    private static MerchantOffer copyOffer(MerchantOffer offer) {
        return new MerchantOffer(offer.createTag());
    }
    private static MerchantOffers copyOffers(TradingDataCapable<?> data) {
        MerchantOffers offers = data.getTradingOffers();
        MerchantOffers copy = new MerchantOffers();
        if (offers != null) for (MerchantOffer offer : offers) copy.add(copyOffer(offer));
        return copy;
    }
    private static void checkOfferIndex(TradingDataCapable<?> data, int index) {
        MerchantOffers offers = data.getTradingOffers();
        ScriptValidation.index(index, offers == null ? 0 : offers.size(), "trade offer index");
    }
    private void updateSettings(Consumer<TradingDataSet> update) {
        TradingDataSet settings = getSettings();
        update.accept(settings);
        setSettings(settings);
    }
    public boolean isSupported() { return entity().getEasyNPCTradingData() != null; }
    public TradingDataSet createSettings() { return new TradingDataSet(); }
    public TradingDataSet getSettings() { return new TradingDataSet(trading().getTradingDataSet().createTag()); }
    public void setSettings(TradingDataSet settings) {
        TradingDataSet copy = new TradingDataSet(settings.createTag());
        TradingDataCapable<?> data = trading();
        data.setTradingDataSet(copy);
        TradingOfferHandler.updateBasicTradingOffers(data);
        data.updateMerchantTradingOffers();
    }
    public TradingType getType() { return trading().getTradingDataSet().getType(); }
    public void setType(TradingType type) { updateSettings(settings -> settings.setType(type)); }
    public void setMaxUses(int maxUses) { updateSettings(settings -> settings.setMaxUses(maxUses)); }
    public void setRewardedXp(int xp) { updateSettings(settings -> settings.setRewardedXP(xp)); }
    public void setResetIntervalMinutes(int minutes) { updateSettings(settings -> settings.setResetsEveryMin(minutes)); }

    public MerchantOffer createSimpleOffer(ItemStack cost, ItemStack result, int maxUses) {
        return createOffer(cost, ItemStack.EMPTY, result, maxUses, 0, 0);
    }
    public MerchantOffer createOffer(ItemStack costA, ItemStack costB, ItemStack result,
                                     int maxUses, int xp, float priceMultiplier) {
        return new MerchantOffer(costA.copy(), costB.copy(), result.copy(), maxUses, xp, priceMultiplier);
    }
    public List<MerchantOffer> getOffers() { return new ArrayList<>(copyOffers(trading())); }
    public MerchantOffer getOffer(int index) {
        MerchantOffers offers = copyOffers(trading());
        return offers.get(index);
    }
    public void setOffers(List<MerchantOffer> offers) {
        MerchantOffers copy = new MerchantOffers();
        for (MerchantOffer offer : offers) copy.add(copyOffer(offer));
        trading().setTradingOffers(copy);
    }
    public void addOffer(MerchantOffer offer) {
        MerchantOffer copy = copyOffer(offer);
        TradingDataCapable<?> data = trading();
        MerchantOffers offers = copyOffers(data);
        offers.add(copy);
        data.setTradingOffers(offers);
    }
    public void setOffer(int index, MerchantOffer offer) {
        MerchantOffer copy = copyOffer(offer);
        TradingDataCapable<?> data = trading();
        MerchantOffers offers = copyOffers(data);
        offers.set(index, copy);
        data.setTradingOffers(offers);
    }
    public MerchantOffer removeOffer(int index) {
        TradingDataCapable<?> data = trading();
        MerchantOffers offers = copyOffers(data);
        MerchantOffer removed = offers.remove(index);
        data.setTradingOffers(offers);
        return removed;
    }
    public void clearOffers() { trading().setTradingOffers(new MerchantOffers()); }
    public void setOfferMaxUses(int index, int maxUses) {
        TradingDataCapable<?> data = trading();
        TradingOfferHandler.setAdvancedTradingMaxUses(data, index, maxUses);
    }
    public void setOfferXp(int index, int xp) {
        TradingDataCapable<?> data = trading();
        TradingOfferHandler.setAdvancedTradingXp(data, index, xp);
    }
    public void setOfferPriceMultiplier(int index, float multiplier) {
        TradingDataCapable<?> data = trading();
        TradingOfferHandler.setAdvancedTradingPriceMultiplier(data, index, multiplier);
    }
    public void setOfferDemand(int index, int demand) {
        TradingDataCapable<?> data = trading();
        TradingOfferHandler.setAdvancedTradingDemand(data, index, demand);
    }
    private static ActionDataSet copyActions(ActionDataSet actions) {
        return new ActionDataSet(actions.createTag());
    }
    private ActionEventDataCapable<?> actionEvents() {
        ActionEventDataCapable<?> data = entity().getEasyNPCActionEventData();
        if (data == null) throw new IllegalStateException("NPC does not support action events");
        return data;
    }
    /** Returns detached actions for an existing offer; absent bindings yield an empty set. */
    public ActionDataSet getOfferActions(int index) {
        TradingDataCapable<?> data = trading();
        checkOfferIndex(data, index);
        return copyActions(data.getTradingDataSet().getOfferAction(index));
    }
    public boolean hasOfferActions(int index) { return !getOfferActions(index).isEmpty(); }
    public void setOfferActions(int index, ActionDataSet actions) {
        ActionDataSet copy = copyActions(actions);
        TradingDataCapable<?> data = trading();
        checkOfferIndex(data, index);
        TradingDataSet settings = new TradingDataSet(data.getTradingDataSet().createTag());
        settings.setOfferAction(index, copy);
        // Only action bindings changed: do not rebuild BASIC prices/stock or merchant offers.
        data.setTradingDataSet(settings);
    }
    public void addOfferAction(int index, ActionDataEntry action) {
        ActionDataSet actions = getOfferActions(index);
        actions.add(action);
        setOfferActions(index, actions);
    }
    public boolean removeOfferAction(int index, UUID actionId) {
        ActionDataSet actions = getOfferActions(index);
        int previousSize = actions.size();
        actions.remove(actionId);
        if (actions.size() == previousSize) return false;
        setOfferActions(index, actions);
        return true;
    }
    public void clearOfferActions(int index) { setOfferActions(index, new ActionDataSet()); }
    public void clearAllOfferActions() {
        TradingDataCapable<?> data = trading();
        TradingDataSet settings = new TradingDataSet(data.getTradingDataSet().createTag());
        if (settings.getOfferActions().isEmpty()) return;
        settings.getOfferActions().clear();
        data.setTradingDataSet(settings);
    }

    /** Shared ON_TRADE actions; editing them follows upstream pending-action cancellation. */
    public ActionDataSet getTradeActions() {
        ActionDataSet actions = actionEvents().getActionDataSet(ActionEventType.ON_TRADE);
        return actions == null ? new ActionDataSet() : copyActions(actions);
    }
    public boolean hasTradeActions() { return !getTradeActions().isEmpty(); }
    public void setTradeActions(ActionDataSet actions) {
        ActionDataSet copy = copyActions(actions);
        ActionEventDataCapable<?> data = actionEvents();
        ActionEventSet current = data.getActionEventSet();
        ActionEventSet events = current == null ? new ActionEventSet() : new ActionEventSet(current.createTag());
        events.setActionEvent(ActionEventType.ON_TRADE, copy);
        data.setActionEventSet(events);
    }
    public void addTradeAction(ActionDataEntry action) {
        ActionDataSet actions = getTradeActions();
        actions.add(action);
        setTradeActions(actions);
    }
    public boolean removeTradeAction(UUID actionId) {
        ActionDataSet actions = getTradeActions();
        int previousSize = actions.size();
        actions.remove(actionId);
        if (actions.size() == previousSize) return false;
        setTradeActions(actions);
        return true;
    }
    public void clearTradeActions() { setTradeActions(new ActionDataSet()); }

    public void resetUses() {
        TradingDataCapable<?> data = trading();
        data.resetTradingOffers();
        data.updateTradingDataSet();
    }
    public boolean resetIfExpired() {
        TradingDataCapable<?> data = trading();
        boolean reset = data.resetExpiredTradingOffers();
        if (reset) data.updateTradingDataSet();
        return reset;
    }
    public void open(ServerPlayer player) {
        checkPlayer(player);
        trading().openTradingScreen(player);
    }
}
