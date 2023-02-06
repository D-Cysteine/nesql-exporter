## Not Enough SQL Exporter

The exporter module for NESQL. The server module can be found
[here](https://github.com/D-Cysteine/nesql-server). Still WIP.

The exported database is an
[HSQLDB database](http://hsqldb.org/), and can also be queried directly with the
HSQLDB client.

Currently supported exported data:

* NEI item list
* Forge ore dictionary
* Forge fluids and fluid container data
* Minecraft crafting table and furnace recipes
* GTNH GT5 recipe maps
* BetterQuesting quests
* Thaumcraft aspects for items

For GTNH, the exported database file is ~650 MB, and the export process takes my
computer ~75 min. There are ~227k exported images, which take up a total of
~170 MB raw; more on disk due to file overhead.

### Instructions

1. Place `NESQL-Exporter-*.jar` and `NESQL-Exporter-*-deps.jar` into your
   `mods/` folder. `NotEnoughItems` is the only required dependency.
2. If you have `bugtorch-1.7.10-*.jar` in your `mods/` folder, move it somewhere
   else temporarily, as it conflicts with rendering enchanted items. See below.
3. Start Minecraft and join a world. It is recommended that you use a new
   creative, single-player world for this. It is recommended to use a new world
   because the exporter uses your current player state, so e.g. if you have
   `Spice of Life` installed, tooltips will reflect which foods you have eaten.
4. If you're using the GTNH version of `NotEnoughItems`, open your inventory and
   view the NEI item list so that it gets loaded. If you forget to do this, some
   items might not be exported.
5. If you are exporting `Thaumcraft` data, it is recommended that you acquire
   all `Thaumcraft` knowledge, as missing knowledge may cause some data not to
   be exported properly. See below for detailed instructions.
6. Run the command `/nesql`. You may optionally specify a repository name with
   `/nesql your_repository_name`.
7. You can now pause the game, and the export will continue while the game is
   paused. Doing this can make things a little bit faster.
8. Wait for the export process to finish. It can take a very long time depending
   on how many mods you have installed.
9. Once the export is finished, you can delete the two mod jars. Remember to
   replace `bugtorch-1.7.10-*.jar` if you moved it earlier.

You may see Forge complaining about a call to `System.exit()` in the logs.
Please disregard it; this is due to one of Hibernate's libraries containing a
call to `System.exit()`.

### Enchanted item rendering issue

If enchanted items (anything with that purple glint overlay) are showing up as
blank images for you, then there is likely a conflict with the BugTorch mod.

I believe that
[this line](https://github.com/GTNewHorizons/BugTorch/blob/adec7fb0d48f499344cb9f4cf9c2f597b6ddb687/src/main/java/jss/bugtorch/mixins/minecraft/client/renderer/entity/MixinItemRenderer.java)
is causing the problem, but it probably can't be fixed by NESQL Exporter since
it is a mix-in. I recommend just temporarily removing BugTorch from your `mods/`
folder until after export is complete.

### Thaumcraft knowledge

If you are exporting `Thaumcraft` data, some data may not be exported properly
if your character is missing `Thaumcraft` knowledge. It is recommended that you
do the following to acquire all knowledge before exporting:

1. Read the creative Thaumonomicon
2. Run the following commands to purge all warp:
   * `/tc warp @p set 0`
   * `/tc warp @p set 0 PERM`
   * `/tc warp @p set 0 TEMP`
