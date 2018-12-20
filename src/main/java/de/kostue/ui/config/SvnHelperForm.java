package de.kostue.ui.config;

import com.intellij.openapi.ui.CheckBoxWithDescription;


import javax.swing.*;


public class SvnHelperForm {
    private JPanel content;
    private CheckBoxWithDescription autoCopyContent;
    private CheckBoxWithDescription applyTemplateEachCommit;
    private JTextArea template;

    public JComponent getContent() {
        return content;
    }

    protected void createUIComponents() {
        autoCopyContent = new CheckBoxWithDescription(new JCheckBox("Auto copy content"),"If selected the SVN enhanced details automatically copied into clipboard");
        applyTemplateEachCommit = new CheckBoxWithDescription(new JCheckBox("Apply template each commit"),"If selected the template is applied each commit, only affects multi selection");
        template = new JTextArea();
    }


    public boolean isAutoCopyContentEnabled() {
        return autoCopyContent.getCheckBox().isSelected();
    }

    public void setAutoCopyContentState(boolean value) {
        autoCopyContent.getCheckBox().setSelected(value);
    }

    public boolean isApplyTemplateEachCommitEnabled() {
        return applyTemplateEachCommit.getCheckBox().isSelected();
    }

    public void setApplyTemplateEachCommitState(boolean value) {
        applyTemplateEachCommit.getCheckBox().setSelected(value);
    }

    public void setTemplate(String value) {
        template.setText(value);
    }

    public String getTemplate() {
        return template.getText();
    }

}
