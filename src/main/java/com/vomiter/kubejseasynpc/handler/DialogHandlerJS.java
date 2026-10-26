package com.vomiter.kubejseasynpc.handler;

import com.vomiter.kubejseasynpc.NPCComponent;
import com.vomiter.kubejseasynpc.NPCHandle;
import com.vomiter.kubejseasynpc.handler.action.DialogActionHandlerJS;
import de.markusbordihn.easynpc.data.action.ActionDataSet;
import de.markusbordihn.easynpc.data.condition.ConditionDataEntry;
import de.markusbordihn.easynpc.data.dialog.DialogButtonConditionMode;
import de.markusbordihn.easynpc.data.dialog.DialogButtonEntry;
import de.markusbordihn.easynpc.data.dialog.DialogButtonType;
import de.markusbordihn.easynpc.data.dialog.DialogDataEntry;
import de.markusbordihn.easynpc.data.dialog.DialogDataSet;
import de.markusbordihn.easynpc.data.dialog.DialogOptionsData;
import de.markusbordihn.easynpc.data.dialog.DialogTextData;
import de.markusbordihn.easynpc.data.dialog.DialogType;
import de.markusbordihn.easynpc.entity.easynpc.data.DialogDataCapable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/** Dialog data editing. Every entity write commits a detached upstream data set. */
public final class DialogHandlerJS extends NPCComponent {
    public DialogHandlerJS(NPCHandle npc) { super(npc); }

    public DialogActionHandlerJS getDialogAction() { return npc.getActions().getDialog(); }
    public boolean isSupported() { return entity().getEasyNPCDialogData() != null; }
    private DialogDataCapable<?> data() {
        DialogDataCapable<?> data = entity().getEasyNPCDialogData();
        if (data == null) throw new IllegalStateException("NPC does not support dialog data");
        return data;
    }

    public DialogDataSet createSettings(DialogType type) {
        return new DialogDataSet(type);
    }
    public DialogDataEntry createDialog(String label, String name, String text) {
        return copyDialog(new DialogDataEntry(label,
            name, text));
    }
    public DialogButtonEntry createButton(String label, String name, ActionDataSet actions) {
        return createTypedButton(label, name, DialogButtonType.DEFAULT, actions);
    }
    public DialogButtonEntry createTypedButton(String label, String name, DialogButtonType type, ActionDataSet actions) {
        return new DialogButtonEntry(name,
            label, type,
            new ActionDataSet(actions.createTag()));
    }
    public DialogTextData createText(String text) {
        return new DialogTextData(text);
    }
    public DialogOptionsData createDefaultOptions() { return DialogOptionsData.getDefault(); }
    public DialogOptionsData createOptions(boolean allowEscClose, boolean showCloseButton, boolean displayAvatar,
            @Nullable Integer avatarTop, @Nullable Integer avatarLeft, @Nullable Integer avatarScale,
            DialogButtonConditionMode buttonConditionMode) {
        return new DialogOptionsData(allowEscClose, showCloseButton, displayAvatar, avatarTop, avatarLeft,
            avatarScale, buttonConditionMode);
    }

    public DialogDataSet getSettings() { return copySettings(data().getDialogDataSet()); }
    public void setSettings(DialogDataSet settings) { data().setDialogDataSet(copySettings(settings)); }
    public DialogType getType() { return data().getDialogDataSet().getType(); }
    public void setType(DialogType type) {
        DialogDataCapable<?> target = data();
        DialogDataSet replacement = createSettings(type);
        for (DialogDataEntry entry : target.getDialogDataSet().getDialogsByLabel()) replacement.addDialog(copyDialog(entry));
        target.setDialogDataSet(replacement);
    }
    public List<DialogDataEntry> getDialogs() { return new ArrayList<>(getSettings().getDialogsByLabel()); }
    public boolean hasDialogs() { return data().hasDialog(); }
    public boolean hasDialog(String label) { return data().hasDialog(label); }
    public boolean hasDialogById(UUID id) { return data().hasDialog(id); }
    @Nullable
    public UUID getDialogId(String label) { return data().getDialogId(label); }
    @Nullable
    public DialogDataEntry getDialog(String label) {
        return copyIfPresent(data().getDialogDataSet().getDialog(label));
    }
    @Nullable
    public DialogDataEntry getDialogById(UUID id) {
        return copyIfPresent(data().getDialogDataSet().getDialog(id));
    }
    public void setDialog(DialogDataEntry dialog) {
        DialogDataCapable<?> target = data();
        DialogDataEntry copy = copyDialog(dialog);
        DialogDataSet settings = copySettings(target.getDialogDataSet());
        settings.setDialog(copy.getId(), copy);
        target.setDialogDataSet(settings);
    }
    public boolean removeDialog(String label) {
        UUID id = getDialogId(label);
        return id != null && removeDialogById(id);
    }
    public boolean removeDialogById(UUID id) {
        DialogDataCapable<?> target = data();
        DialogDataSet settings = copySettings(target.getDialogDataSet());
        if (!settings.removeDialog(id)) return false;
        target.setDialogDataSet(settings);
        return true;
    }
    public void clearDialogs() { data().clearDialogDataSet(); }

    public void setTexts(String label, List<String> texts) {
        DialogDataEntry entry = requireDialog(label);
        Set<DialogTextData> values = new LinkedHashSet<>();
        for (String text : texts) values.add(createText(text));
        entry.setDialogTexts(values);
        setDialog(entry);
    }
    public void setPriority(String label, int priority) {
        DialogDataEntry entry = requireDialog(label);
        entry.setPriority(priority);
        setDialog(entry);
    }
    public void setOptions(String label, DialogOptionsData options) {
        DialogDataEntry entry = requireDialog(label);
        entry.setDialogOptions(options);
        setDialog(entry);
    }
    public void setConditions(String label, List<ConditionDataEntry> conditions) {
        DialogDataEntry entry = requireDialog(label);
        Set<ConditionDataEntry> values = new LinkedHashSet<>();
        for (ConditionDataEntry condition : conditions) {
            values.add(condition);
        }
        entry.setConditions(values);
        setDialog(entry);
    }
    public void setButtons(String label, List<DialogButtonEntry> buttons) {
        DialogDataEntry entry = requireDialog(label);
        entry.setDialogButtons(new LinkedHashSet<>(buttons));
        setDialog(entry);
    }
    public void setButton(String label, DialogButtonEntry button) {
        DialogDataEntry entry = requireDialog(label);
        entry.setDialogButton(copyButton(button));
        setDialog(entry);
    }
    @Nullable
    public DialogButtonEntry getButton(String label, String buttonLabel) {
        DialogButtonEntry button = requireDialog(label).getDialogButton(buttonLabel);
        return button == null ? null : copyButton(button);
    }
    public boolean removeButton(String label, String buttonLabel) {
        DialogDataEntry entry = requireDialog(label);
        DialogButtonEntry button = entry.getDialogButton(buttonLabel);
        if (button == null) return false;
        entry.removeDialogButton(button.id());
        setDialog(entry);
        return true;
    }

    private DialogDataEntry requireDialog(String label) {
        DialogDataEntry entry = getDialog(label);
        if (entry == null) throw new IllegalArgumentException("Unknown dialog label: " + label);
        return entry;
    }
    @Nullable
    private static DialogDataEntry copyIfPresent(@Nullable DialogDataEntry dialog) {
        return dialog == null ? null : copyDialog(dialog);
    }
    private static DialogDataEntry copyDialog(DialogDataEntry dialog) {
        return new DialogDataEntry(dialog.createTag());
    }
    private static DialogButtonEntry copyButton(DialogButtonEntry button) {
        return new DialogButtonEntry(button.createTag());
    }
    private static DialogDataSet copySettings(DialogDataSet settings) {
        // The native NBT loader ignores Type when DialogDataSet is empty.
        // Copy entries individually so even an empty set retains its chosen type.
        DialogDataSet copy = new DialogDataSet(settings.getType());
        for (DialogDataEntry entry : settings.getDialogsByLabel()) copy.addDialog(copyDialog(entry));
        return copy;
    }
}
