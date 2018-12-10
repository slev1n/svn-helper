package de.kostue.ui.actions;

import com.intellij.ide.CopyProvider;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.changes.ChangeList;
import de.kostue.config.SvnHelperAppState;
import org.jetbrains.annotations.NotNull;

public class EnhancedCopyAction extends AnAction implements DumbAware {


    public EnhancedCopyAction() {
        setEnabledInModalContext(true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        CopyProvider provider = PlatformDataKeys.COPY_PROVIDER.getData(dataContext);
        if (provider == null) {
            return;
        }
        //TODO: may use own COPY_PROVIDER instead
        SvnHelperAppState state = SvnHelperAppState.getInstance();
        ChangeList[] changeLists = e.getData(VcsDataKeys.CHANGE_LISTS);
        if (changeLists != null && state.autoCopyContent) {
            EnhancedVcsDetails.getDetails(changeLists);
        } else {
            provider.performCopy(dataContext);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        Presentation presentation = event.getPresentation();
        DataContext dataContext = event.getDataContext();
        CopyProvider provider = PlatformDataKeys.COPY_PROVIDER.getData(dataContext);
        presentation.setEnabled(provider != null && provider.isCopyEnabled(dataContext));
        if (event.getPlace().equals(ActionPlaces.EDITOR_POPUP) && provider != null) {
            presentation.setVisible(provider.isCopyVisible(dataContext));
        } else {
            presentation.setVisible(true);
        }
    }


}
