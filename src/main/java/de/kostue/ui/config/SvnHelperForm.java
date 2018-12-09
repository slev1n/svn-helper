package de.kostue.ui.config;

import com.intellij.openapi.ui.CheckBoxWithDescription;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.TextFieldWithHistory;


import javax.swing.*;


public class SvnHelperForm {
    private JPanel content;
    private CheckBoxWithDescription autoCopyContent;
    private JTextArea template;

    public JComponent getContent() {
        return content;
    }

    protected void createUIComponents() {
        autoCopyContent = new CheckBoxWithDescription(new JCheckBox("Auto copy content"),"If selected the SVN enhanced details automatically copied into clipboard");
        template = new JTextArea();
    }


    public boolean isAutoCopyContentEnabled() {
        return autoCopyContent.getCheckBox().isSelected();
    }

    public void setAutoCopyContentState(boolean value) {
        autoCopyContent.getCheckBox().setSelected(value);
    }
    public void setTemplate(String value) {
        template.setText(value);
    }

    public String getTemplate() {
        return template.getText();
    }

}
