# Prerequisites #

  * [Maven 3.x](http://maven.apache.org/download.html)
  * [Mercurial](http://mercurial.selenic.com/wiki/Download)

You will need a terminal to check everything is correct. The easiest way to open a terminal on Windows is by pressing `Win+R`, type `cmd` and press return.

Type `hg version` and press return. The output should be similar to this:

```
 joa.ebert>hg version
 Mercurial Distributed SCM (version 1.5.1)
 
 Copyright (C) 2005-2010 Matt Mackall <mpm@selenic.com> and others
 This is free software; see the source for copying conditions. There is NO
 warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
```

`mvn -version` should return something like this:

```
 joa.ebert>mvn -version
 Apache Maven 3.0-alpha-7 (r921173; 2010-03-09 23:31:07+0100)
 Java version: 1.6.0_18
 Java home: ...\jre
 Default locale: de_DE, platform encoding: Cp1252
 OS name: "windows xp" version: "5.1" arch: "x86" Family: "windows"
```

# Checkout and build #

To checkout Apparat from Google Code you have to create your local clone of the repository. This is done by typing `hg clone http://apparat.googlecode.com/hg/ apparat`. This command will create a new directory called `apparat`. Go into that directory by typing `cd apparat`.

To compile apparat you can use `mvn install`. If you want to package a distribution you have to do `mvn install`, then `cd apparat-assembly` and finally `mvn assembly:assembly`. A zip- and tarball-archive can be found in ...\apparat-assembly\target\ and is most likely named apparat-1.0-SNAPSHOT-bin.zip for instance.

Note that running Maven for the first time will probably take a while.

If the build fails because FlexMojos cannot be found you will have to add it to your Maven repository configuration. Detailed steps can be found [here](http://flexmojos.sonatype.org/flexmojos-public-maven-repository.html).

# LWJGL #
If you are missing LWJGL in your Maven repositories you will have to donwload and install it manually. Therefore go to http://www.lwjgl.org/ and download version 2.5.

When extracted go into the "jar" directory and execute the following commands:
```
mvn install:install-file -Dfile=lwjgl.jar -Dpackaging=jar -DgroupId=lwjgl -Dversion=2.5 -DartifactId=lwjgl
mvn install:install-file -Dfile=lwjgl_util.jar -Dpackaging=jar -DgroupId=lwjgl -Dversion=2.5 -DartifactId=lwjgl-util
mvn install:install-file -Dfile=jinput.jar -Dpackaging=jar -DgroupId=lwjgl -Dversion=2.5 -DartifactId=jinput
```