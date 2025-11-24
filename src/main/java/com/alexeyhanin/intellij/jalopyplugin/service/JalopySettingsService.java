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
package com.alexeyhanin.intellij.jalopyplugin.service;

import com.alexeyhanin.intellij.jalopyplugin.model.JalopySettingsModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Service(Service.Level.APP)
@State(
        name = "JalopyPlugin.Settings",
        storages = @Storage("jalopy-formatter.xml")
)
public final class JalopySettingsService implements PersistentStateComponent<JalopySettingsModel> {

    private JalopySettingsModel settings = new JalopySettingsModel();

    public static JalopySettingsService getInstance() {
        return ApplicationManager.getApplication().getService(JalopySettingsService.class);
    }

    @Override
    public @Nullable JalopySettingsModel getState() {
        return settings;
    }

    @Override
    public void loadState(@NotNull JalopySettingsModel state) {
        this.settings = state;
    }

    public boolean isFormatOnSaveEnabled() {
        return settings.isFormatOnSaveEnabled();
    }

    public void setFormatOnSaveEnabled(boolean enabled) {
        settings.setFormatOnSaveEnabled(enabled);
    }

    public String getConventionFilePath() {
        return settings.getConventionFilePath();
    }

    public void setConventionFilePath(String path) {
        settings.setConventionFilePath(path);
    }
}
