# IntelliJ Jalopy Formatter Plugin

Plugin IntelliJ IDEA pour formater le code Java avec Jalopy, modernisé pour IntelliJ IDEA 2025.1+.

## Fonctionnalités

- **Formatage manuel** : Utilisez `Ctrl+Shift+P` pour formater le document actif
- **Formatage automatique** : Option pour formater automatiquement à la sauvegarde
- **Profil personnalisé** : Supporte l'upload d'un fichier de convention XML Jalopy

## Prérequis

- IntelliJ IDEA 2025.1 ou supérieur
- Java 21 ou supérieur
- Gradle 8.11.1 (inclus via wrapper)

## Installation

### Option 1 : Compiler et installer depuis les sources

1. **Cloner le repository**
   ```bash
   git clone https://github.com/ahanin/intellij-jalopy-plugin.git
   cd intellij-jalopy-plugin
   ```

2. **Compiler le plugin**
   ```bash
   ./gradlew buildPlugin
   ```

   Le plugin sera généré dans : `build/distributions/intellij-jalopy-plugin-2.0.0.zip`

3. **Installer dans IntelliJ IDEA**
   - Ouvrez IntelliJ IDEA
   - Allez dans `File` → `Settings` (ou `IntelliJ IDEA` → `Preferences` sur macOS)
   - Sélectionnez `Plugins`
   - Cliquez sur l'icône ⚙️ (engrenage) → `Install Plugin from Disk...`
   - Sélectionnez le fichier `build/distributions/intellij-jalopy-plugin-2.0.0.zip`
   - Redémarrez IntelliJ IDEA

### Option 2 : Développement local avec IntelliJ IDEA

1. **Ouvrir le projet**
   - Lancez IntelliJ IDEA
   - `File` → `Open` → Sélectionnez le dossier du plugin
   - IntelliJ détectera automatiquement le projet Gradle

2. **Synchroniser Gradle**
   - Une notification apparaîtra pour synchroniser le projet Gradle
   - Cliquez sur "Load Gradle Project" ou utilisez le panneau Gradle à droite

3. **Lancer en mode développement**
   - Ouvrez le panneau Gradle : `View` → `Tool Windows` → `Gradle`
   - Naviguez vers : `intellij-jalopy-plugin` → `Tasks` → `intellij` → `runIde`
   - Double-cliquez sur `runIde`
   - Une nouvelle instance d'IntelliJ IDEA se lancera avec le plugin chargé

## Configuration

### Paramètres généraux

1. Allez dans `File` → `Settings` → `Tools` → `Jalopy`
2. Configurez les options :
   - **Reformat on file save** : Active le formatage automatique lors de la sauvegarde
   - **Convention file (XML)** : Chemin vers votre fichier de convention Jalopy

### Configuration du profil Jalopy

Pour utiliser un profil personnalisé :

1. Créez ou obtenez un fichier de convention Jalopy (format XML)
2. Dans les settings du plugin (`Settings` → `Tools` → `Jalopy`)
3. Cliquez sur "Browse" et sélectionnez votre fichier XML
4. Cliquez sur "Apply" puis "OK"

Le plugin chargera automatiquement cette convention lors du formatage.

### Accès aux paramètres avancés de Jalopy

Pour configurer en détail les règles de formatage :

1. Dans le menu `Code` → `Jalopy Settings...`
2. Une fenêtre de configuration s'ouvrira avec toutes les options Jalopy
3. Configurez vos préférences de formatage
4. Les paramètres seront sauvegardés dans le dossier de configuration d'IntelliJ

## Utilisation

### Formatage manuel

- Ouvrez un fichier Java
- Appuyez sur `Ctrl+Shift+P` (ou utilisez `Code` → `Format with Jalopy`)
- Le fichier sera formaté selon la convention configurée

### Formatage automatique

- Activez "Reformat on file save" dans les settings
- Chaque fois que vous sauvegardez un fichier Java, il sera automatiquement formaté

## Structure du projet

```
intellij-jalopy-plugin/
├── src/main/
│   ├── java/com/alexeyhanin/intellij/jalopyplugin/
│   │   ├── action/          # Actions utilisateur (formatage)
│   │   ├── listener/        # Listeners (sauvegarde)
│   │   ├── model/           # Modèles de données
│   │   ├── service/         # Services applicatifs
│   │   ├── settings/        # Configuration UI
│   │   └── util/            # Utilitaires
│   └── resources/
│       └── META-INF/
│           └── plugin.xml   # Descripteur du plugin
├── libs/                    # Dépendances Jalopy
│   ├── jalopy-1.5rc3.jar
│   ├── antlr-2.7.5.jar
│   └── log4j-1.2.8.jar
├── build.gradle.kts         # Configuration Gradle
├── settings.gradle.kts      # Settings Gradle
└── gradle.properties        # Propriétés Gradle
```

## Développement

### Commandes Gradle utiles

```bash
# Compiler le plugin
./gradlew build

# Générer le ZIP de distribution
./gradlew buildPlugin

# Lancer IntelliJ avec le plugin en mode dev
./gradlew runIde

# Vérifier la compatibilité avec différentes versions d'IntelliJ
./gradlew verifyPlugin

# Nettoyer les fichiers de build
./gradlew clean
```

### Architecture technique

Le plugin utilise les APIs modernes d'IntelliJ Platform :

- **Services** : `@Service` au lieu des anciens `ApplicationComponent`
- **Listeners** : Déclarés dans `plugin.xml` via `<applicationListeners>`
- **Configurable** : Interface moderne pour les settings
- **Actions** : Avec support de `ActionUpdateThread.BGT`

## Compatibilité

- **IntelliJ IDEA** : 2025.1+ (build 251+)
- **Java** : 17+
- **Jalopy** : 1.5rc3

## Licence

GNU General Public License v3.0

Copyright (C) 2012 Alexey Hanin

## Auteur

Alexey Hanin - [GitHub](https://github.com/ahanin)

Modernisé pour IntelliJ 2025.1+ avec support des profils XML personnalisés.
