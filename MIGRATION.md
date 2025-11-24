# Guide de Migration vers IntelliJ 2025.1

Ce document détaille les modifications effectuées pour migrer le plugin Jalopy vers IntelliJ IDEA 2025.1+.

## Résumé des changements

Le plugin a été complètement modernisé pour utiliser les APIs actuelles d'IntelliJ Platform et supporter IntelliJ IDEA 2025.1 et versions ultérieures.

## 1. Système de Build

### Avant (IDEA 12)
- Pas de système de build moderne
- Configuration manuelle dans IntelliJ IDEA
- JARs dans `META-INF/lib/`

### Après (IDEA 2025.1+)
- **Gradle** avec IntelliJ Platform Gradle Plugin 2.2.1
- Configuration déclarative dans `build.gradle.kts`
- Wrapper Gradle inclus pour reproductibilité
- JARs déplacés dans `libs/`

**Fichiers ajoutés :**
- `build.gradle.kts` - Configuration du projet et dépendances
- `settings.gradle.kts` - Configuration Gradle du projet
- `gradle.properties` - Propriétés et paramètres
- `gradlew` / `gradlew.bat` - Scripts wrapper Gradle
- `gradle/wrapper/` - Configuration du wrapper

## 2. Structure du Plugin (plugin.xml)

### Avant
```xml
<application-components>
    <component>
        <implementation-class>...JalopyPluginRegistration</implementation-class>
    </component>
    <component>
        <implementation-class>...JalopySettingsComponent</implementation-class>
    </component>
</application-components>
```

### Après
```xml
<extensions defaultExtensionNs="com.intellij">
    <applicationService serviceImplementation="...JalopySettingsService"/>
    <applicationConfigurable instance="...JalopyConfigurable" .../>
</extensions>

<applicationListeners>
    <listener class="...JalopyFileDocumentManagerListener"
              topic="com.intellij.openapi.fileEditor.FileDocumentManagerListener"/>
</applicationListeners>
```

**Changements :**
- `<application-components>` → `<extensions>` + `<applicationListeners>`
- Composants déclarés comme services modernes
- Listeners déclarés explicitement avec leur topic

## 3. Architecture des Composants

### JalopySettingsComponent → JalopySettingsService

**Avant :**
```java
public class JalopySettingsComponent implements
    ApplicationComponent,
    Configurable,
    PersistentStateComponent<JalopySettingsModel> {
    // Multiples responsabilités
}
```

**Après :**
```java
@Service(Service.Level.APP)
@State(name = "JalopyPlugin.Settings", storages = @Storage("jalopy-formatter.xml"))
public final class JalopySettingsService implements PersistentStateComponent<JalopySettingsModel> {
    public static JalopySettingsService getInstance() {
        return ApplicationManager.getApplication().getService(JalopySettingsService.class);
    }
}
```

**Changements :**
- Annotation `@Service` pour déclarer comme service applicatif
- Méthode `getInstance()` statique pour récupération
- Séparation des responsabilités (UI dans `JalopyConfigurable`)

### JalopyPluginRegistration → JalopyFileDocumentManagerListener

**Avant :**
```java
public class JalopyPluginRegistration implements ApplicationComponent {
    private MessageBusConnection connection;

    @Override
    public void initComponent() {
        connection = ApplicationManager.getApplication().getMessageBus().connect();
        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, new JalopyDocumentFormatter());
    }
}
```

**Après :**
```java
public class JalopyFileDocumentManagerListener implements FileDocumentManagerListener {
    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        final JalopySettingsService settings = JalopySettingsService.getInstance();
        if (settings != null && settings.isFormatOnSaveEnabled()) {
            CommandProcessor.getInstance().runUndoTransparentAction(() ->
                JalopyDocumentFormatter.format(document)
            );
        }
    }
}
```

**Changements :**
- Implémente directement `FileDocumentManagerListener`
- Plus besoin de gérer manuellement le `MessageBusConnection`
- Déclaré dans `plugin.xml` via `<applicationListeners>`

### Nouveau : JalopyConfigurable

**Création d'un Configurable moderne pour l'UI des settings :**

```java
public class JalopyConfigurable implements Configurable {
    private JBCheckBox formatOnSaveCheckBox;
    private TextFieldWithBrowseButton conventionFileField;

    @Override
    public JComponent createComponent() {
        // Création de l'UI avec FormBuilder
        return FormBuilder.createFormBuilder()
            .addComponent(formatOnSaveCheckBox)
            .addLabeledComponent(new JBLabel("Convention file (XML):"), conventionFileField, 1, false)
            .getPanel();
    }
}
```

## 4. Actions

### FormatAction

**Avant :**
```java
public class FormatAction extends AnAction {
    public void actionPerformed(final AnActionEvent event) {
        final Editor editor = event.getData(PlatformDataKeys.EDITOR);
        // ...
    }
}
```

**Après :**
```java
public class FormatAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        final Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        // ...
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabledAndVisible(editor != null);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
```

**Changements :**
- `PlatformDataKeys.EDITOR` → `CommonDataKeys.EDITOR` (API dépréciée)
- Ajout de `update()` pour gérer la visibilité de l'action
- Ajout de `getActionUpdateThread()` pour threading moderne (BGT = Background Thread)
- Vérification null explicite

## 5. Modèle de Données

### JalopySettingsModel

**Avant :**
```java
public class JalopySettingsModel {
    private boolean isFormatOnSaveEnabled;
    // Getter/Setter
}
```

**Après :**
```java
@Tag("JalopySettings")
public class JalopySettingsModel {
    private boolean formatOnSaveEnabled;
    private String conventionFilePath;  // NOUVEAU

    public JalopySettingsModel copy() {
        // Méthode de copie pour immutabilité
    }
}
```

**Changements :**
- Annotation `@Tag` pour sérialisation XML
- Ajout du champ `conventionFilePath` pour supporter les profils personnalisés
- Méthode `copy()` pour manipulation immutable
- Renommage cohérent des propriétés

## 6. Formatage avec Profil

### JalopyDocumentFormatter

**Ajouts majeurs :**

```java
public class JalopyDocumentFormatter {
    private static final Logger LOG = Logger.getInstance(JalopyDocumentFormatter.class);
    private static String lastLoadedConventionPath = null;

    private static synchronized void loadConventionIfNeeded() {
        final JalopySettingsService settings = JalopySettingsService.getInstance();
        final String conventionPath = settings.getConventionFilePath();

        if (StringUtil.isNotEmpty(conventionPath)) {
            final File conventionFile = new File(conventionPath);
            if (conventionFile.exists() && conventionFile.isFile()) {
                Convention.importSettings(conventionFile);
                LOG.info("Loaded Jalopy convention file: " + conventionPath);
            }
        }
    }
}
```

**Changements :**
- Chargement automatique du profil XML configuré
- Cache du chemin pour éviter les rechargements inutiles
- Logging avec `Logger` IntelliJ
- Vérification de l'extension de fichier (.java uniquement)
- Gestion d'erreurs robuste

## 7. UI Settings

### JalopySettingsForm → JalopyConfigurable

**Avant :**
```java
public class JalopySettingsForm extends JBPanel implements ChangeListener {
    public JalopySettingsForm(final Provider<JalopySettingsModel> provider) {
        super(new HorizontalLayout()); // SwingX
        // ...
    }
}
```

**Après :**
```java
public class JalopyConfigurable implements Configurable {
    private TextFieldWithBrowseButton conventionFileField;

    @Override
    public JComponent createComponent() {
        conventionFileField = new TextFieldWithBrowseButton();
        conventionFileField.addBrowseFolderListener(
            "Select Jalopy Convention File",
            "Select the Jalopy convention XML file to use for formatting",
            null,
            new FileChooserDescriptor(...)
                .withFileFilter(file -> "xml".equalsIgnoreCase(file.getExtension()))
        );

        return FormBuilder.createFormBuilder()
            .addComponent(formatOnSaveCheckBox)
            .addLabeledComponent(new JBLabel("Convention file (XML):"), conventionFileField)
            .getPanel();
    }
}
```

**Changements :**
- Suppression de SwingX (dépendance externe obsolète)
- Utilisation de `FormBuilder` IntelliJ
- `TextFieldWithBrowseButton` pour sélection de fichier
- `FileChooserDescriptor` pour filtrer les fichiers XML
- Implémentation directe de `Configurable`

## 8. Fichiers Supprimés

Les fichiers suivants ont été supprimés car obsolètes :

- `src/main/java/com/alexeyhanin/intellij/jalopyplugin/component/` (tout le package)
  - `JalopyPluginRegistration.java`
  - `JalopySettingsComponent.java`

- `src/main/java/com/alexeyhanin/intellij/jalopyplugin/swing/` (tout le package)
  - `JalopySettingsForm.java`
  - `JalopySettingsDialog.java`
  - `JalopySettingsDialogWrapper.java`

- `src/main/java/com/alexeyhanin/intellij/jalopyplugin/util/`
  - `RuntimeHelper.java` (remplacé par lambdas)
  - `Provider.java` (interface générique non nécessaire)

- `src/main/java/de/hunsicker/jalopy/swing/`
  - `PreviewFrameHelper.java` (hack pour accéder à PreviewFrame)

## 9. Nouveaux Packages

Structure modernisée :

```
com/alexeyhanin/intellij/jalopyplugin/
├── action/              # Actions (inchangé)
│   ├── FormatAction.java
│   └── JalopySettingsAction.java
├── listener/            # NOUVEAU - Listeners modernes
│   └── JalopyFileDocumentManagerListener.java
├── model/               # Modèles (amélioré)
│   └── JalopySettingsModel.java
├── service/             # NOUVEAU - Services applicatifs
│   └── JalopySettingsService.java
├── settings/            # NOUVEAU - Configuration UI
│   └── JalopyConfigurable.java
└── util/                # Utilitaires (nettoyé)
    └── JalopyDocumentFormatter.java
```

## 10. Compatibilité

### Versions supportées

**Avant :**
- IntelliJ IDEA 12+ (build 107.105)
- Java 6+

**Après :**
- IntelliJ IDEA 2025.1+ (build 251+)
- Java 21+
- Gradle 8.11.1

### APIs dépréciées remplacées

| API Dépréciée | Remplacement |
|---------------|--------------|
| `ApplicationComponent` | `@Service(Service.Level.APP)` |
| `FileDocumentManagerAdapter` | `FileDocumentManagerListener` |
| `PlatformDataKeys.EDITOR` | `CommonDataKeys.EDITOR` |
| `StoragePathMacros.APP_CONFIG` | Chemin simplifié dans `@Storage` |
| `getComponentName()` | Plus nécessaire |
| `initComponent()` / `disposeComponent()` | Cycle de vie automatique |
| Manual `MessageBusConnection` | Déclaration dans plugin.xml |

## 11. Nouvelle Fonctionnalité : Profils XML

La fonctionnalité la plus importante ajoutée est le support des profils de convention Jalopy personnalisés.

### Configuration

1. **Dans l'UI** : L'utilisateur sélectionne un fichier XML via file chooser
2. **Persistance** : Le chemin est sauvegardé dans `jalopy-formatter.xml`
3. **Chargement** : Le formatter charge automatiquement le profil avant formatage
4. **Cache** : Le profil n'est rechargé que si le chemin change

### Avantages

- Permet d'avoir des conventions différentes par projet
- Compatible avec les fichiers de convention Jalopy existants
- Facilite le partage de conventions entre équipes

## 12. Build et Distribution

### Commandes Gradle

```bash
# Développement
./gradlew runIde              # Lance IDEA avec le plugin

# Build
./gradlew build               # Compile le projet
./gradlew buildPlugin         # Génère le ZIP de distribution

# Qualité
./gradlew verifyPlugin        # Vérifie la compatibilité
./gradlew test                # Lance les tests
```

### Artefacts générés

- `build/distributions/intellij-jalopy-plugin-2.0.0.zip` - Plugin installable
- `build/libs/` - JARs compilés
- `build/idea-sandbox/` - Sandbox de développement

## Conclusion

Cette migration modernise complètement le plugin pour :

1. **Compatibilité** : Fonctionne avec IntelliJ IDEA 2025.1+
2. **Maintenabilité** : Code moderne utilisant les meilleures pratiques
3. **Fonctionnalités** : Support des profils XML personnalisés
4. **Build** : Système de build reproductible avec Gradle
5. **APIs** : Utilisation des APIs actuelles, pas de dépréciations

Le plugin est maintenant prêt pour les futures versions d'IntelliJ IDEA.
