
package de.kostue.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;



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
