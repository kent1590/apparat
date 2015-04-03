<a href='http://flattr.com/thing/7177/Apparat'>
<img src='http://api.flattr.com/button/button-static-50x60.png' align='right' border='0' title='Flattr this' /></a>
# Apparat #
**IMPORTANT:** Apparat has moved to GitHub. You can fork it via https://github.com/joa/apparat

A framework to optimize ABC, SWC and SWF files.
Apparat features include
  * Compression
    * 7-Zip DEFLATE compression
    * LZMA compression
    * ABC merging
    * Constant pool sorting
    * DefineBitsLossless2 to DefineBitsJPEG3/4 conversion
    * Bytecode merging
    * Bytecode replacements
    * Flowgraph optimizations
    * Sliding-window optimizations
  * Code analysis
    * UML diagram generation
    * Bytecode flowgraph generation
    * Static check for `[Abstract]` methods
    * Detailed ABC information
    * Code coverage instrumentation
    * [Typesafe](http://stopcoding.wordpress.com/2010/04/21/how-omit-trace-statements-works-or-does-not/) `trace()` removal keeping side-effects
    * ASMify existing code
  * Optimization
    * Generic peephole optimizations
    * InlineExpansion
    * MacroExpansion
    * InlineAssembler
    * Access to Alchemy's fast memory operations
    * Special transformations for Alchemy-generated code
    * C inspired Structure type

## Getting Started ##
Apparat is written in [Scala](http://www.scala-lang.org/) -- a conciese functional-oop-hybrid language running on the JVM. Therefore you will have to install Scala version 2.8.0 and Java 1.6 to run any Apparat application.
A [step-by-step tutorial](http://webdevotion.be/blog/2010/06/02/how-to-get-up-and-running-with-apparat/) is available as well.

Apparat is simple to experiment with thanks to Scala.

```
#!/bin/sh
exec scala $0 $@
!#
import apparat.swf._
import apparat.abc._
import apparat.bytecode._

for {
	tag <- Swf fromFile "input.swf"
	abc <- Abc fromTag tag
} {
	abc.loadBytecode()

	for {
		method <- abc.methods
		body <- method.body
		bytecode <- body.bytecode
	} {
		bytecode.dump()
	}
}
```

## Tooling Support ##
FlexMojos is making use of Apparat for file compression and coverage report generation. If you are using FlexMojos you might be already using Apparat without even knowing it.

We also provide:
  * Ant tasks
  * Maven goals
  * Maven archetypes

You do not need anything else than a working Maven installation to get started with TDSI for instance.

```
mvn archetype:generate \
  -DarchetypeRepository=http://oss.sonatype.org/content/repositories/snapshots \
  -DarchetypeGroupId=com.googlecode.apparat \
  -DarchetypeArtifactId=apparat-archetype-tdsi \
  -DarchetypeVersion=1.0-SNAPSHOT
```

## License ##
The Apparat framework is licensed under the [GNU Lesser General Public License](http://www.gnu.org/licenses/lgpl-2.1.html), version 2.1 (LGPL-2.1).