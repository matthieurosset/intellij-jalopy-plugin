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

package com.alexeyhanin.intellij.jalopyplugin.util;

import com.alexeyhanin.intellij.jalopyplugin.service.JalopySettingsService;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.triemax.Jalopy;

import java.io.File;

public class JalopyDocumentFormatter {

    private static final Logger LOG = Logger.getInstance(JalopyDocumentFormatter.class);
    private static Jalopy jalopyInstance = null;
    private static String lastLoadedConventionPath = null;

    public static boolean format(final Document document) {
        final VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);

        if (virtualFile == null) {
            LOG.warn("Cannot format: virtualFile is null");
            return false;
        }

        // Only format Java files
        if (!"java".equalsIgnoreCase(virtualFile.getExtension())) {
            return false;
        }

        try {
            // Get or create Jalopy instance with convention loaded
            final Jalopy jalopy = getJalopyInstance();
            if (jalopy == null) {
                LOG.warn("Failed to create Jalopy instance");
                return false;
            }

            final String text = document.getText();

            // Jalopy 1.10 API: format(CharSequence, String filename) returns formatted string
            final String formattedText = jalopy.format(text, virtualFile.getPath());

            if (formattedText != null && !text.equals(formattedText)) {
                document.setText(formattedText);
                return true;
            }

            return formattedText != null;
        } catch (Exception e) {
            LOG.warn("Failed to format document: " + virtualFile.getPath(), e);
        }

        return false;
    }

    private static synchronized Jalopy getJalopyInstance() {
        final JalopySettingsService settings = JalopySettingsService.getInstance();
        final String conventionPath = settings.getConventionFilePath();

        // Check if we need to reload the Jalopy instance with a new convention
        if (jalopyInstance != null && StringUtil.equals(conventionPath, lastLoadedConventionPath)) {
            return jalopyInstance;
        }

        // Create new Jalopy instance
        try {
            jalopyInstance = new Jalopy();

            // Load convention file if configured
            if (StringUtil.isNotEmpty(conventionPath)) {
                final File conventionFile = new File(conventionPath);
                if (conventionFile.exists() && conventionFile.isFile()) {
                    try {
                        // Jalopy 1.10 API: setConvention(File)
                        jalopyInstance.setConvention(conventionFile);
                        lastLoadedConventionPath = conventionPath;
                    } catch (Exception e) {
                        LOG.warn("Failed to load Jalopy convention file: " + conventionPath, e);
                        lastLoadedConventionPath = null;
                    }
                } else {
                    LOG.warn("Convention file does not exist: " + conventionPath);
                    lastLoadedConventionPath = null;
                }
            } else {
                lastLoadedConventionPath = null;
            }

            return jalopyInstance;
        } catch (Exception e) {
            LOG.warn("Failed to create Jalopy instance", e);
            jalopyInstance = null;
            lastLoadedConventionPath = null;
            return null;
        }
    }

    public static void reloadConvention() {
        jalopyInstance = null;
        lastLoadedConventionPath = null;
    }
}