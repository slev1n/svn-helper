
package de.kostue.ui.config;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import de.kostue.config.SvnHelperAppState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class SvnHelperConfigurable extends BaseConfigurable {
    private volatile SvnHelperForm form;

    @Nls
    @Override
    public String getDisplayName() {
        return "Svn Helper";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;  //TODO: auto-generated method implementation
    }

    private synchronized void initComponent() {
        if (form == null) {
            form = new SvnHelperForm();
        }
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        initComponent();
        return form.getContent();
    }

    @Override
    public void apply() throws ConfigurationException {
        initComponent();

        SvnHelperAppState state = SvnHelperAppState.getInstance();

        if (isAutoCopyChanged()) {
            state.autoCopyContent = form.isAutoCopyContentEnabled();
        }
        if (isTemplateChanged()) {
            state.template = form.getTemplate();
        }
    }

    @Override
    public boolean isModified() {
        return isAutoCopyChanged() || isTemplateChanged();
    }

    private boolean isAutoCopyChanged() {
        SvnHelperAppState state = SvnHelperAppState.getInstance();
        return state.isAutoCopyContentChanged(form.isAutoCopyContentEnabled());
    }

    private boolean isTemplateChanged() {
        SvnHelperAppState state = SvnHelperAppState.getInstance();
        return state.isTemplateChanged(form.getTemplate());
    }

    @Override
    public void reset() {
        initComponent();
        SvnHelperAppState state = SvnHelperAppState.getInstance();
        form.setAutoCopyContentState(state.autoCopyContent);
        form.setTemplate(state.template);
    }

    @Override
    public void disposeUIResources() {
        form = null;
    }

}
