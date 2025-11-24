/*
 * IntelliJ IDEA Jalopy Formatter plugin
 * Copyright (C) 2012  Alexey Hanin <mail@alexeyhanin.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alexeyhanin.intellij.jalopyplugin.settings;

import com.alexeyhanin.intellij.jalopyplugin.service.JalopySettingsService;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class JalopyConfigurable implements Configurable {

    private JBCheckBox formatOnSaveCheckBox;
    private TextFieldWithBrowseButton conventionFileField;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Jalopy";
    }

    @Override
    public @Nullable JComponent createComponent() {
        formatOnSaveCheckBox = new JBCheckBox("Reformat on file save");

        conventionFileField = new TextFieldWithBrowseButton();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false)
                .withFileFilter(file -> "xml".equalsIgnoreCase(file.getExtension()))
                .withTitle("Select Jalopy Convention File")
                .withDescription("Select the Jalopy convention XML file");
        conventionFileField.addBrowseFolderListener(
                "Select Jalopy Convention File",
                "Select the Jalopy convention XML file to use for formatting",
                null,
                descriptor
        );

        reset();

        return FormBuilder.createFormBuilder()
                .addComponent(formatOnSaveCheckBox)
                .addSeparator()
                .addLabeledComponent(new JBLabel("Convention file (XML):"), conventionFileField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    @Override
    public boolean isModified() {
        final JalopySettingsService settings = JalopySettingsService.getInstance();

        boolean formatOnSaveModified = formatOnSaveCheckBox.isSelected() != settings.isFormatOnSaveEnabled();
        boolean conventionFileModified = !Objects.equals(
                StringUtil.nullize(conventionFileField.getText()),
                settings.getConventionFilePath()
        );

        return formatOnSaveModified || conventionFileModified;
    }

    @Override
    public void apply() {
        final JalopySettingsService settings = JalopySettingsService.getInstance();
        settings.setFormatOnSaveEnabled(formatOnSaveCheckBox.isSelected());
        settings.setConventionFilePath(StringUtil.nullize(conventionFileField.getText()));
    }

    @Override
    public void reset() {
        final JalopySettingsService settings = JalopySettingsService.getInstance();
        formatOnSaveCheckBox.setSelected(settings.isFormatOnSaveEnabled());
        conventionFileField.setText(StringUtil.notNullize(settings.getConventionFilePath()));
    }

    @Override
    public void disposeUIResources() {
        formatOnSaveCheckBox = null;
        conventionFileField = null;
    }
}
