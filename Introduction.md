# Introduction #

Apparat is a tool that helps you optimize your Flash Platform content. It is able to compress files and to access the full speed of the Flash Player.

A spinoff of Apparat is JITB; a Flash Player implementation in Java with OpenGL based rendering.

# Concepts #

We do not want to replace or modify the ActionScript compiler. This can be cumbersome and problematic for teams. Instead we focus on post-compile time transformations.

Apparat works with SWF, SWC and ABC files. It can be easily integrated in your current CI solution since we provide Maven plugins and Ant tasks.

## Compression ##

Apparat focusses currently only on PNG and code compression. We also support optimized DEFLATE compression using 7-Zip and advanced LZMA compression.

## Optimization ##

With our optimization techniques you can use the Flash Player to its full extent. We allow you do go completely low-level by inlining AVM2 bytecode and provide high-level abstractions for ready to use classes.

Apparat allows you to leverage the full feature set of Alchemy operations. We integrated some very helpful features like memory allocation and Structure types.