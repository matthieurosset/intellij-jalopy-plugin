# Installation et Utilisation - Plugin Jalopy pour IntelliJ IDEA 2025.1+

## Vue d'ensemble

Ce plugin permet de formater le code Java avec Jalopy directement dans IntelliJ IDEA 2025.1+. Il supporte maintenant les profils de convention XML personnalisés.

## Prérequis

Avant de commencer, assurez-vous d'avoir :

- **IntelliJ IDEA 2025.1** ou version ultérieure
- **Java 21** ou supérieur ([Télécharger](https://adoptium.net/))
- **Git** (pour cloner le repository)

### Vérifier Java

```bash
java --version
```

Vous devriez voir quelque chose comme :
```
openjdk 17.0.x ou openjdk 21.0.x
```

Si Java 21+ n'est pas installé, installez-le depuis [Adoptium](https://adoptium.net/).

## Installation - Méthode 1 : Build depuis les sources (recommandé)

### Étape 1 : Cloner le repository

```bash
git clone https://github.com/ahanin/intellij-jalopy-plugin.git
cd intellij-jalopy-plugin
```

### Étape 2 : Compiler le plugin

```bash
# Sur Linux/macOS
./gradlew buildPlugin

# Sur Windows
gradlew.bat buildPlugin
```

**Temps estimé** : 2-5 minutes (téléchargement des dépendances la première fois)

Le plugin sera créé dans : `build/distributions/intellij-jalopy-plugin-2.0.0.zip`

### Étape 3 : Installer dans IntelliJ IDEA

1. **Ouvrez IntelliJ IDEA**

2. **Accédez aux Plugins**
   - Sur Windows/Linux : `File` → `Settings` → `Plugins`
   - Sur macOS : `IntelliJ IDEA` → `Preferences` → `Plugins`

3. **Installez depuis le disque**
   - Cliquez sur l'icône ⚙️ (engrenage) en haut
   - Sélectionnez `Install Plugin from Disk...`
   - Naviguez vers `build/distributions/intellij-jalopy-plugin-2.0.0.zip`
   - Cliquez sur `OK`

4. **Redémarrez IntelliJ IDEA**
   - Cliquez sur `Restart IDE` quand demandé

## Installation - Méthode 2 : Développement avec IntelliJ

Cette méthode est idéale si vous souhaitez modifier le plugin.

### Étape 1 : Ouvrir le projet

1. Lancez IntelliJ IDEA
2. `File` → `Open`
3. Sélectionnez le dossier `intellij-jalopy-plugin`
4. Cliquez sur `OK`

### Étape 2 : Synchroniser Gradle

Une notification apparaîtra en haut à droite : "Load Gradle Project"
- Cliquez sur `Load`

Gradle va télécharger toutes les dépendances (peut prendre quelques minutes).

### Étape 3 : Lancer le plugin en mode développement

1. Ouvrez le panneau Gradle :
   - `View` → `Tool Windows` → `Gradle`

2. Dans l'arbre Gradle, naviguez vers :
   ```
   intellij-jalopy-plugin
   └── Tasks
       └── intellij
           └── runIde
   ```

3. **Double-cliquez** sur `runIde`

4. Une nouvelle fenêtre IntelliJ IDEA s'ouvrira avec le plugin chargé

**Avantage** : Vous pouvez modifier le code et relancer `runIde` pour tester vos changements.

## Configuration

### Configuration de base

1. **Ouvrez les settings**
   - Windows/Linux : `File` → `Settings` → `Tools` → `Jalopy`
   - macOS : `IntelliJ IDEA` → `Preferences` → `Tools` → `Jalopy`

2. **Options disponibles** :
   - ☑️ **Reformat on file save** : Active le formatage automatique à la sauvegarde
   - **Convention file (XML)** : Chemin vers votre profil de convention Jalopy

### Configuration avec profil XML personnalisé

Si vous avez un fichier de convention Jalopy existant :

1. Dans les settings Jalopy
2. Cliquez sur **Browse** à côté de "Convention file (XML)"
3. Sélectionnez votre fichier `.xml`
4. Cliquez sur **Apply** puis **OK**

**Note** : Le profil sera chargé automatiquement à chaque formatage.

### Créer un nouveau profil

Si vous n'avez pas de profil :

1. Dans IntelliJ, menu `Code` → `Jalopy Settings...`
2. Une fenêtre de configuration complète s'ouvre
3. Configurez toutes vos préférences de formatage
4. Cliquez sur `OK` pour sauvegarder

Les paramètres seront sauvegardés dans votre configuration IntelliJ.

## Utilisation

### Formatage manuel

**Option 1 : Raccourci clavier**
```
Ctrl+Shift+P  (Windows/Linux)
Cmd+Shift+P   (macOS)
```

**Option 2 : Menu**
```
Code → Format with Jalopy
```

**Option 3 : Menu contextuel**
- Clic droit dans l'éditeur
- `Format with Jalopy`

### Formatage automatique

Si "Reformat on file save" est activé :

1. Ouvrez n'importe quel fichier `.java`
2. Effectuez des modifications
3. Sauvegardez (`Ctrl+S` / `Cmd+S`)
4. Le fichier est automatiquement formaté

**Important** : Seuls les fichiers Java (`.java`) sont formatés.

## Vérification de l'installation

Pour vérifier que le plugin est correctement installé :

1. **Vérifier la présence du plugin**
   - `Settings` → `Plugins`
   - Cherchez "Jalopy" dans la liste
   - Devrait afficher : "Jalopy Plugin 2.0.0"

2. **Vérifier le menu**
   - Menu `Code`
   - Vous devriez voir "Format with Jalopy" et "Jalopy Settings..."

3. **Vérifier le raccourci**
   - Ouvrez un fichier Java
   - Appuyez sur `Ctrl+Shift+P`
   - Le code devrait être formaté

## Dépannage

### Problème : Le plugin n'apparaît pas après installation

**Solution** :
1. Vérifiez votre version d'IntelliJ : `Help` → `About`
   - Doit être 2025.1 ou supérieur
2. Redémarrez IntelliJ IDEA complètement
3. Vérifiez dans `Settings` → `Plugins` que "Jalopy Plugin" est activé (case cochée)

### Problème : Erreur lors de la compilation

```
Error: Could not find or load main class
```

**Solution** :
1. Vérifiez Java 21+ : `java --version`
2. Nettoyez et recompilez :
   ```bash
   ./gradlew clean buildPlugin
   ```

### Problème : Le formatage ne fait rien

**Causes possibles** :

1. **Fichier non-Java** : Le plugin ne formate que les fichiers `.java`
2. **Fichier en lecture seule** : Vérifiez les permissions du fichier
3. **Pas de convention chargée** : Vérifiez les logs

**Vérifier les logs** :
- `Help` → `Show Log in Explorer`
- Cherchez les messages contenant "Jalopy"

### Problème : Le profil XML n'est pas chargé

**Vérifications** :

1. Le chemin du fichier est correct
2. Le fichier XML est valide (format Jalopy)
3. Regardez les logs pour les messages :
   - `Loaded Jalopy convention file: ...` (succès)
   - `Failed to load Jalopy convention file: ...` (échec)

**Test** :
```bash
# Vérifier que le fichier XML existe
ls -la /chemin/vers/votre/convention.xml

# Vérifier le contenu
head -20 /chemin/vers/votre/convention.xml
```

### Problème : Gradle build échoue avec erreur réseau

**Solution** :
Si vous êtes derrière un proxy, configurez Gradle :

Créez/éditez `~/.gradle/gradle.properties` :
```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
```

## Architecture du projet

```
intellij-jalopy-plugin/
├── src/main/
│   ├── java/com/alexeyhanin/intellij/jalopyplugin/
│   │   ├── action/          # Actions utilisateur
│   │   │   ├── FormatAction.java
│   │   │   └── JalopySettingsAction.java
│   │   ├── listener/        # Écoute des événements
│   │   │   └── JalopyFileDocumentManagerListener.java
│   │   ├── model/           # Modèles de données
│   │   │   └── JalopySettingsModel.java
│   │   ├── service/         # Services applicatifs
│   │   │   └── JalopySettingsService.java
│   │   ├── settings/        # Interface de configuration
│   │   │   └── JalopyConfigurable.java
│   │   ├── util/            # Utilitaires
│   │   │   └── JalopyDocumentFormatter.java
│   │   └── exception/       # Exceptions
│   │       └── JalopyPluginRuntimeException.java
│   └── resources/
│       └── META-INF/
│           └── plugin.xml   # Descripteur du plugin
├── libs/                    # Bibliothèques Jalopy
│   ├── jalopy-1.5rc3.jar
│   ├── antlr-2.7.5.jar
│   └── log4j-1.2.8.jar
├── build.gradle.kts         # Configuration Gradle
├── settings.gradle.kts
├── gradle.properties
├── gradlew                  # Script Gradle (Linux/Mac)
├── gradlew.bat             # Script Gradle (Windows)
└── gradle/wrapper/         # Wrapper Gradle
```

## Commandes utiles

```bash
# Compiler le plugin
./gradlew build

# Générer le ZIP de distribution
./gradlew buildPlugin

# Lancer IntelliJ avec le plugin en dev
./gradlew runIde

# Nettoyer les fichiers de build
./gradlew clean

# Vérifier la compatibilité
./gradlew verifyPlugin

# Voir toutes les tâches disponibles
./gradlew tasks
```

## Documentation supplémentaire

- **[README.md](README.md)** : Vue d'ensemble et features
- **[QUICK_START.md](QUICK_START.md)** : Guide de démarrage rapide
- **[MIGRATION.md](MIGRATION.md)** : Détails techniques de la migration
- **[Jalopy Documentation](http://jalopy.sourceforge.net/)** : Documentation officielle Jalopy

## Support et Contributions

- **Issues** : [GitHub Issues](https://github.com/ahanin/intellij-jalopy-plugin/issues)
- **Source Code** : [GitHub Repository](https://github.com/ahanin/intellij-jalopy-plugin)

## Licence

GNU General Public License v3.0

Copyright (C) 2012 Alexey Hanin

## Crédits

- **Auteur original** : Alexey Hanin
- **Modernisation 2025** : Migration vers IntelliJ IDEA 2025.1+ avec support des profils XML

---

**Bon formatage !** 🎉
