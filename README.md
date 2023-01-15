## Not Enough SQL Exporter

The exporter module for NESQL. The server module can be found
[here](https://github.com/D-Cysteine/nesql-server). Still very WIP.

Currently only supports exporting items, fluids, and vanilla crafting and
furnace recipes. The exported database is an
[H2 database](http://www.h2database.com/html/main.html), and can also be queried
directly with the H2 client.

### Instructions

1. Place `NESQL-Exporter-*.jar` and `NESQL-Exporter-*-deps.jar` into your
   `mods/` folder. `NotEnoughItems` is the only required dependency.
2. If you have `bugtorch-1.7.10-*.jar` in your `mods/` folder, move it somewhere
   else temporarily, as it conflicts with rendering enchanted items. See below.
3. Start Minecraft and join a single-player world.
4. If you're using the GTNH version of `NotEnoughItems`, open your inventory and
   view the NEI item list so that it gets loaded. If you forget to do this, some
   items might not be exported.
5. Run the command `/nesql`. You may optionally specify a repository name with
   `/nesql your_repository_name`.
6. Wait for the export process to finish. It can take a very long time depending
   on how many mods you have installed. For reference, exporting GTNH can take
   10-30 minutes, and the resulting repository is ~3GB, and contains ~83k image
   files.
7. Depending on your computer's specs, rendering may end up taking a lot longer
   to finish than exporting the database. Check your logs to see how many render
   jobs are left.
8. Once the export is finished, you can delete the two mod jars. Remember to
   replace `bugtorch-1.7.10-*.jar` if you moved it earlier.

### Enchanted item rendering issue

If enchanted items (anything with that purple glint overlay) are showing up as
blank images for you, then there is likely a conflict with the BugTorch mod.

I believe that
[this line](https://github.com/GTNewHorizons/BugTorch/blob/master/src/main/java/jss/bugtorch/mixins/minecraft/client/renderer/entity/MixinItemRenderer.java#L23)
is causing the problem, but it probably can't be fixed by NESQL Exporter since
it is a mix-in. I recommend just temporarily removing BugTorch from your `mods/`
folder until after export is complete.