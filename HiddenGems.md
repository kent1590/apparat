# Overview #
| **Variable** | **Default** | **Comment** |
|:-------------|:------------|:------------|
| apparat.threads | true | Use fork/join pool |
| apparat.7z.enabled | true | 7-Zip support for DELFATE compression |
| apparat.7z.path | "7z" | Path to your 7z executable |
| apparat.debug | false| Whether or not to enable debug logging |
| apparat.quiet | false| Whether or not to suppress all log output |

# Introduction #

Apparat has some features for debugging that are discussed on this page.


# Single-thread mode #

Apparat uses a fork/join pool. When developing software that uses Apparat you might run into trouble sometimes and are not sure if Apparat or your program is broken.

We are lazy about exceptions at the moment. They could occurr in a thread of the pool and leave Apparat waiting forever.
You can set Apparat into a single-thread mode by specifing **`-Dapparat.threads=false`**. In that case Apparat will not make use of the fork/join pool at all. Only ApparatShell will still run on a threaded actor. However the tools it will spawn run all in the actor thread.

# 7-Zip support #

You can speficy the path to your 7-zip executable via **`-Dapparat.7z.path=C:\Program Files\7-Zip\7z.exe`**. Another option is to have 7-zip on your PATH.

You can disable 7z support completely by specifying **`-Dapparat.7z.enabled=false`**.