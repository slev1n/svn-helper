
package de.kostue.config;

import com.google.common.base.Strings;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.ui.JBColor;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.Nullable;

import java.awt.*;


@State(
    name = "SvnHelperAppConfig",
    storages = @Storage("SvnHelper.xml")
)
public class SvnHelperAppState implements PersistentStateComponent<SvnHelperAppState> {
    public boolean autoCopyContent = false;
    public String template="";

    public static SvnHelperAppState getInstance() {
        return ServiceManager.getService(SvnHelperAppState.class);
    }

    @Nullable
    @Override
    public SvnHelperAppState getState() {
        return this;
    }

    @Override
    public void loadState(SvnHelperAppState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isAutoCopyContentChanged(boolean enabled) {
        return autoCopyContent != enabled;
    }
    public boolean isTemplateChanged(String value) {
        return !template.equals(value);
    }

}
