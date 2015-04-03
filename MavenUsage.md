# Introduction #

Apparat requires Maven version 3.0 or higher. You can get Maven from http://maven.apache.org/. We synchronize our releases to Maven Central. Snapshot builds of Apparat can be found in the Sonatype repository.

# Maven Plugin #

The Apparat Maven plugin works like any standard plugin. Simply add it to your build cycle.

```
<build>
  <!--
    ...
   -->
  <plugin>
    <groupId>com.googlecode.apparat</groupId>
    <artifactId>apparat-maven-plugin</artifactId>
    <version>1.0.RC8</version>
    <executions>
      <execution>
        <id>reducer</id>
        <goals>
          <goal>reducer</goal>
        </goals>
      </execution>
    </executions>
    <configuration>
      <mergeABC>true</mergeABC>
    </configuration>
  </plugin>
</build>
```

# Maven Archetypes #

Apparat comes with several ready-to-use archetypes to get you started.

## TDSI Archetype ##

An archetype for a project that uses Alchemy memory.

```
mvn archetype:generate \
  -DarchetypeRepository=http://oss.sonatype.org/content/repositories/snapshots \
  -DarchetypeGroupId=com.googlecode.apparat \
  -DarchetypeArtifactId=apparat-archetype-tdsi \
  -DarchetypeVersion=1.0-SNAPSHOT
```

## ASM Archetype ##

An archetype that creates a project that can make use of inline assembler.

```
mvn archetype:generate \
  -DarchetypeRepository=http://oss.sonatype.org/content/repositories/snapshots \
  -DarchetypeGroupId=com.googlecode.apparat \
  -DarchetypeArtifactId=apparat-archetype-asm \
  -DarchetypeVersion=1.0-SNAPSHOT
```