# Scala Installation #

You can grab a copy of Scala at http://www.scala-lang.org/downloads. Currently Apparat is built using version 2.8.0.

Once you downloaded the archive suitable for your operating system you have to extract it somewhere. I call that directory now `SCALA_HOME`.

## PATH ##

Once you have extracted Scala somewhere you need to add the `SCALA_HOME/bin` directory to your [PATH variable](http://en.wikipedia.org/wiki/PATH_(variable)). Since this depends on your operating system I will not explain it.

You also have to create a variable named `SCALA_HOME` which should point to the Scala installation directory.

### Linux ###

You can simply attach those two lines to the end of your `~/.bashrc` or `~/.profile` for OS X users.

```
export SCALA_HOME=/path/to/scala
export PATH=$SCALA_HOME/bin:$PATH
```

# Verify your installation #

To make sure you have a valid Scala installation you can simply type `scala -version` into a command prompt and the output look similar to this.

```
joa@macaron:~$ scala -version
Scala code runner version 2.8.0.final -- Copyright 2002-2010, LAMP/EPFL
```

Note: If you do not get the desired output try restarting your command prompt.