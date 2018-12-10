package de.kostue.ui.actions;

import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.CachingCommittedChangesProvider;
import com.intellij.openapi.vcs.changes.ChangeList;
import com.intellij.openapi.vcs.versionBrowser.CommittedChangeList;
import com.intellij.ui.BrowserHyperlinkListener;
import de.kostue.config.SvnHelperAppState;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.intellij.openapi.util.text.StringUtil.join;
import static com.intellij.openapi.vcs.VcsBundle.message;
import static com.intellij.openapi.vcs.VcsDataKeys.CHANGE_LISTS;
import static com.intellij.openapi.wm.impl.content.BaseLabel.getLabelFont;
import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static com.intellij.util.containers.ContainerUtil.packNullables;
import static com.intellij.util.ui.UIUtil.HTML_MIME;
import static com.intellij.util.ui.UIUtil.getCssFontDeclaration;
import static java.lang.String.format;
import static java.util.Arrays.stream;

public class EnhancedVcsDetails extends AnAction implements DumbAware {

    public static final String PLACEHOLDER = "{content}";

    @Override
    public void update(@NotNull AnActionEvent e) {
        ChangeList[] changeLists = e.getData(CHANGE_LISTS);

        e.getPresentation().setEnabled(
                e.getProject() != null && changeLists != null && changeLists.length >=1 && changeLists[0] instanceof CommittedChangeList);
    }


    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        ChangeList[] changeLists = e.getRequiredData(CHANGE_LISTS);

        showDetailsPopup(project, changeLists);
    }


    public static void showDetailsPopup(@NotNull Project project, @NotNull ChangeList[] changeLists) {
        String details =
                format("<html><head>%s</head><body>%s</body></html>", getCssFontDeclaration(getLabelFont()), getDetails(project, changeLists));
        JEditorPane editorPane = new JEditorPane(HTML_MIME, details);
        editorPane.setEditable(false);
        editorPane.setBackground(HintUtil.getInformationColor());
        editorPane.select(0, 0);
        editorPane.addHyperlinkListener(BrowserHyperlinkListener.INSTANCE);

        JBPopupFactory.getInstance().createComponentPopupBuilder(createScrollPane(editorPane), editorPane)
                .setDimensionServiceKey(project, "", false)
                .setResizable(true)
                .setMovable(true)
                .setRequestFocus(true)
                .setTitle(message("changelist.details.title"))
                .createPopup()
                .showInBestPositionFor(DataManager.getInstance().getDataContext());
    }

    @NotNull
    private static String getDetails(@NotNull Project project, @NotNull ChangeList[] changeLists) {
        String details = stream(changeLists).map(changeList -> {
            CommittedChangeList c = (CommittedChangeList) changeList;
            return join(packNullables(
                    getNumber(c),
                    getCommitter(c),
                    getCommittedDate(c),
                    formatCommittedMessage(project, c.getComment()),
                    getCustomDetails(c)
            ), "<br>");
        }).collect(Collectors.joining("<br><br>"));
        SvnHelperAppState state = SvnHelperAppState.getInstance();
        String template = state.template;
        if (template != null && !template.equals("")) {
            details = applyTemplate(details,template);
        }
        if (state.autoCopyContent) {
            copyDetailToClipboard(details);
        }
        return details;
    }

    private static String applyTemplate(String details, String template) {
        return template.replace(PLACEHOLDER,details);
    }

    @NotNull
    private static void copyDetailToClipboard(String completeDetail) {
        StringSelection stringSelection = new StringSelection(cleanTagPerservingLineBreaks(completeDetail));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public static String cleanTagPerservingLineBreaks(String html) {
        return html.replace("<br>","\n");
    }

    private static String formatCommittedMessage(Project project, String comment) {
        return "Message: <br>" + comment +"<br> ----";
    }

    @Nullable
    private static String getNumber(@NotNull CommittedChangeList changeList) {
        return Optional.ofNullable(changeList.getVcs())
                .map(AbstractVcs::getCachingCommittedChangesProvider)
                .map(CachingCommittedChangesProvider::getChangelistTitle)
                .map(changeListTitle -> changeListTitle + " #" + changeList.getNumber())
                .orElse(null);
    }

    @NotNull
    private static String getCommitter(@NotNull CommittedChangeList changeList) {
        @NonNls String committer = "<b>" + changeList.getCommitterName() + "</b>";
        return "Author: " + changeList.getCommitterName();
    }

    @NotNull
    private static String getCommittedDate(@NotNull CommittedChangeList changeList) {
        SimpleDateFormat dt = new SimpleDateFormat("EEEE, d. MMMM yyyy hh:mm:ss");
        return "Date: " + dt.format(changeList.getCommitDate());
    }

    @Nullable
    private static String getCustomDetails(@NotNull CommittedChangeList changeList) {
        AbstractVcs vcs = changeList.getVcs();
        return changeList.getChanges().stream().map(c -> {
            String formattedPath;
            if ( c.getVirtualFile() != null) {
                formattedPath = c.getVirtualFile().getCanonicalFile().toString().replace(vcs.getProject().getBasePath(), "").replace("file://", "");
            } else {
                formattedPath = c.toString();
            }
            return c.getFileStatus().getText() + ": " + formattedPath;
        }).collect(Collectors.joining("<br>"));
    }

}
