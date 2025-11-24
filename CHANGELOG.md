# Changelog

All notable changes to the IntelliJ Jalopy Plugin will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [2.1.0] - 2024-11-24

### Changed
- **Upgraded Jalopy** from version 1.5rc3 to **1.10** for full separator comments support
- Migrated to Jalopy 1.10 API:
  - `Convention.importSettings()` → `Jalopy.setConvention(File)`
  - `setInput()/setOutput()/format()` → `format(CharSequence, String)`
- Updated imports from `de.hunsicker.jalopy` to `com.triemax`
- Cleaned up excessive debug logging for production readiness

### Fixed
- **Separator comments** (`//~`) now properly inserted between class sections (Fields, Constructors, Methods)
- Full compatibility with Eclipse Jalopy convention files

### Technical Details
- Jalopy 1.10.jar (4.5MB) replaces jalopy-1.5rc3.jar (1.3MB)
- Jalopy instance is now cached and reused for better performance
- Convention files are loaded once per Jalopy instance

---

## [2.0.0] - 2025-01-24

### Migration majeure vers IntelliJ IDEA 2025.1+

Cette version représente une réécriture complète du plugin pour supporter les versions modernes d'IntelliJ IDEA.

### Added
- **Support des profils XML Jalopy personnalisés** : Vous pouvez maintenant uploader et utiliser vos propres fichiers de convention XML
- **File chooser** pour sélectionner facilement le fichier de convention
- **Gradle build system** avec IntelliJ Platform Gradle Plugin 2.2.1
- **Wrapper Gradle** inclus pour reproductibilité du build
- **Logging amélioré** avec l'API Logger d'IntelliJ
- **Validation** des fichiers (uniquement .java sont formatés)
- **Cache intelligent** du profil de convention pour éviter les rechargements inutiles
- Documentation complète :
  - README.md modernisé
  - INSTALLATION.md - Guide d'installation détaillé
  - QUICK_START.md - Guide de démarrage rapide
  - MIGRATION.md - Détails techniques de la migration

### Changed
- **Compatibilité** : IntelliJ IDEA 2025.1+ (build 251+) au lieu de IDEA 12
- **Java** : Nécessite Java 21+ au lieu de Java 6+
- **Architecture** :
  - `ApplicationComponent` → `@Service(Service.Level.APP)`
  - `FileDocumentManagerAdapter` → `FileDocumentManagerListener` déclaré dans plugin.xml
  - `PlatformDataKeys.EDITOR` → `CommonDataKeys.EDITOR`
  - Séparation claire des responsabilités (services, listeners, settings UI)
- **Package structure** :
  - Ajout de `listener/` pour les listeners modernes
  - Ajout de `service/` pour les services applicatifs
  - Ajout de `settings/` pour l'UI de configuration
  - Suppression de `component/` et `swing/` obsolètes
- **UI Settings** :
  - Utilisation de `FormBuilder` IntelliJ au lieu de SwingX
  - `TextFieldWithBrowseButton` pour sélection de fichiers
  - Interface `Configurable` moderne
- **Actions** :
  - Support de `ActionUpdateThread.BGT` pour threading moderne
  - Méthode `update()` pour gérer la visibilité des actions
  - Gestion null-safe des éditeurs
- **Model** :
  - Ajout de l'annotation `@Tag` pour sérialisation XML
  - Ajout du champ `conventionFilePath` pour les profils personnalisés
  - Méthode `copy()` pour manipulation immutable

### Removed
- **Dépendances obsolètes** :
  - SwingX (remplacé par composants IntelliJ natifs)
  - RuntimeHelper (remplacé par lambdas Java 8+)
  - Provider interface générique (non nécessaire)
  - PreviewFrameHelper hack
- **Classes obsolètes** :
  - `JalopyPluginRegistration` (remplacé par `JalopyFileDocumentManagerListener`)
  - `JalopySettingsComponent` (séparé en `JalopySettingsService` + `JalopyConfigurable`)
  - `JalopySettingsForm`, `JalopySettingsDialog`, `JalopySettingsDialogWrapper`
- **APIs dépréciées** :
  - `ApplicationComponent` et son cycle de vie
  - `getComponentName()`, `initComponent()`, `disposeComponent()`
  - Gestion manuelle de `MessageBusConnection`
  - `StoragePathMacros.APP_CONFIG`

### Fixed
- **Thread safety** : Actions s'exécutent correctement en background
- **Null safety** : Vérification null explicite pour les éditeurs
- **Extension validation** : Ne tente de formater que les fichiers .java
- **Error handling** : Gestion robuste des erreurs avec logging approprié
- **Performance** : Cache du profil de convention pour éviter les rechargements

### Technical Details

#### Build System
- Gradle 8.11.1
- IntelliJ Platform Gradle Plugin 2.2.1
- Java source/target : 17

#### API Migrations
| Old API | New API |
|---------|---------|
| ApplicationComponent | @Service(Service.Level.APP) |
| FileDocumentManagerAdapter | FileDocumentManagerListener |
| PlatformDataKeys.EDITOR | CommonDataKeys.EDITOR |
| Manual MessageBus subscription | Declarative in plugin.xml |
| StoragePathMacros.APP_CONFIG | Simplified @Storage path |

#### New Features Implementation
- Convention file loading via `Convention.importSettings()`
- FileChooserDescriptor with XML filter
- Path persistence in application settings
- Synchronized loading to prevent race conditions

### Migration Guide
Pour migrer depuis la version 1.0.x, consultez [MIGRATION.md](MIGRATION.md).

### Breaking Changes
- **Minimum requirements** :
  - IntelliJ IDEA 2025.1+ (au lieu de 12)
  - Java 21+ (au lieu de 6+)
- **Installation** : Nécessite une nouvelle installation, pas de mise à jour automatique depuis 1.0.x
- **Configuration** : Les paramètres existants seront perdus, reconfiguration nécessaire

---

## [1.0.2] - 2013-03-02

### Fixed
- Fixed saving of Jalopy settings

## [1.0.1] - 2013-02-04

### Fixed
- Fixed NPE on trying to conduct formatting without an opened editor
- Fixed bug with disappearing Jalopy Settings dialog by assembling the dialog with IntelliJ API

## [1.0.0] - 2012-12-XX

### Added
- Initial release
- Format Java code with Jalopy using Ctrl+Shift+P
- Automatic formatting on file save (optional)
- Basic Jalopy settings integration
- Compatible with IntelliJ IDEA 12+
