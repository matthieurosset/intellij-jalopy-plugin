# Guide de Démarrage Rapide - Plugin Jalopy pour IntelliJ IDEA

## Installation rapide

### Étape 1 : Compiler le plugin

```bash
cd intellij-jalopy-plugin
./gradlew buildPlugin
```

Le fichier ZIP sera créé dans : `build/distributions/intellij-jalopy-plugin-2.0.0.zip`

### Étape 2 : Installer dans IntelliJ

1. Ouvrez IntelliJ IDEA
2. `File` → `Settings` → `Plugins`
3. Cliquez sur ⚙️ → `Install Plugin from Disk...`
4. Sélectionnez `build/distributions/intellij-jalopy-plugin-2.0.0.zip`
5. Redémarrez IntelliJ IDEA

## Configuration en 3 minutes

### 1. Accéder aux paramètres

`File` → `Settings` → `Tools` → `Jalopy`

### 2. Configuration minimale

- ☑️ **Reformat on file save** : Cochez pour activer le formatage automatique

### 3. Configuration avec profil personnalisé (optionnel)

Si vous avez un fichier de convention Jalopy XML :

1. Dans les settings Jalopy, cliquez sur **Browse** à côté de "Convention file (XML)"
2. Sélectionnez votre fichier `.xml`
3. Cliquez sur **Apply** puis **OK**

## Utilisation

### Formatage manuel

**Méthode 1 : Raccourci clavier**
```
Ctrl+Shift+P
```

**Méthode 2 : Menu**
```
Code → Format with Jalopy
```

### Formatage automatique

Si vous avez activé "Reformat on file save" dans les settings :

1. Ouvrez un fichier Java
2. Modifiez le code
3. Sauvegardez (`Ctrl+S`)
4. Le fichier est automatiquement formaté

## Paramètres avancés Jalopy (optionnel)

Pour accéder à tous les paramètres de formatage Jalopy :

1. `Code` → `Jalopy Settings...`
2. Une fenêtre s'ouvre avec toutes les options de formatage
3. Configurez selon vos préférences
4. Cliquez sur **OK**

## Exemples de profils XML

### Profil basique

Créez un fichier `jalopy-convention.xml` :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jalopy>
    <general>
        <convention>
            <name>Mon Style Java</name>
        </convention>
    </general>

    <printer>
        <indentation>
            <policy>
                <style>SPACE</style>
            </policy>
            <sizes>
                <braceLeft>4</braceLeft>
                <continuation>8</continuation>
                <deep>4</deep>
                <extends>4</extends>
                <general>4</general>
                <leading>0</leading>
                <tabs>4</tabs>
                <throws>4</throws>
                <trailingComment>1</trailingComment>
            </sizes>
        </indentation>

        <blank-lines>
            <keepUpTo>2</keepUpTo>
            <after>
                <block>1</block>
                <braceLeft>0</braceLeft>
                <braceRight>0</braceRight>
                <class>2</class>
                <declaration>1</declaration>
                <footer>1</footer>
                <header>1</header>
                <interface>2</interface>
                <lastImport>2</lastImport>
                <method>1</method>
                <package>2</package>
            </after>
            <before>
                <block>1</block>
                <braceRight>0</braceRight>
                <caseBlock>0</caseBlock>
                <comment>
                    <javadoc>1</javadoc>
                    <multiline>1</multiline>
                    <singleline>1</singleline>
                </comment>
                <controlStatement>1</controlStatement>
                <declaration>0</declaration>
                <footer>1</footer>
                <header>0</header>
            </before>
        </blank-lines>

        <wrapping>
            <always>
                <after>
                    <arrayElement>0</arrayElement>
                    <braceRight>false</braceRight>
                    <extendsTypes>false</extendsTypes>
                    <implementsTypes>false</implementsTypes>
                    <methodCallChained>false</methodCallChained>
                    <parameters>false</parameters>
                    <throwsTypes>false</throwsTypes>
                </after>
                <before>
                    <braceLeft>false</braceLeft>
                    <braceRight>false</braceRight>
                    <extends>false</extends>
                    <implements>false</implements>
                    <throws>false</throws>
                </before>
            </always>
            <general>
                <afterLeftParen>false</afterLeftParen>
                <beforeRightParen>false</beforeRightParen>
                <beforeOperator>false</beforeOperator>
            </general>
            <ondemand>
                <after>80</after>
                <before>80</before>
                <deep>false</deep>
                <groupingParentheses>true</groupingParentheses>
            </ondemand>
        </wrapping>
    </printer>
</jalopy>
```

### Utiliser ce profil

1. Sauvegardez le fichier quelque part (ex: `~/jalopy-convention.xml`)
2. Dans IntelliJ : `Settings` → `Tools` → `Jalopy`
3. Convention file : Sélectionnez le fichier
4. Apply → OK

## Dépannage

### Le plugin n'apparaît pas après installation

- Vérifiez que vous utilisez IntelliJ IDEA 2025.1 ou supérieur
- Redémarrez IntelliJ IDEA
- Vérifiez dans `Settings` → `Plugins` que le plugin est activé

### Le formatage ne fonctionne pas

- Vérifiez que vous êtes dans un fichier `.java`
- Vérifiez que le fichier n'est pas en lecture seule
- Regardez les logs : `Help` → `Show Log in Explorer`

### Le profil XML n'est pas chargé

- Vérifiez que le chemin vers le fichier XML est correct
- Vérifiez que le fichier XML est valide
- Regardez les logs pour voir les messages de chargement

### Erreur lors de la compilation

- Vérifiez que Java 21+ est installé : `java --version`
- Nettoyez et recompilez : `./gradlew clean buildPlugin`

## Support

- **Issues** : https://github.com/ahanin/intellij-jalopy-plugin/issues
- **Documentation Jalopy** : http://jalopy.sourceforge.net/

## Prochaines étapes

- Lisez le [README.md](README.md) pour plus de détails
- Consultez [MIGRATION.md](MIGRATION.md) pour comprendre les changements techniques
- Personnalisez votre profil Jalopy selon vos besoins

Bon formatage ! 🎉
