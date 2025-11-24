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
package com.alexeyhanin.intellij.jalopyplugin.model;

import com.intellij.util.xmlb.annotations.Tag;

@Tag("JalopySettings")
public class JalopySettingsModel {

    private boolean formatOnSaveEnabled;
    private String conventionFilePath;

    public boolean isFormatOnSaveEnabled() {
        return formatOnSaveEnabled;
    }

    public void setFormatOnSaveEnabled(final boolean formatOnSaveEnabled) {
        this.formatOnSaveEnabled = formatOnSaveEnabled;
    }

    public String getConventionFilePath() {
        return conventionFilePath;
    }

    public void setConventionFilePath(final String conventionFilePath) {
        this.conventionFilePath = conventionFilePath;
    }

    public JalopySettingsModel copy() {
        final JalopySettingsModel copy = new JalopySettingsModel();
        copy.formatOnSaveEnabled = this.formatOnSaveEnabled;
        copy.conventionFilePath = this.conventionFilePath;
        return copy;
    }
}
