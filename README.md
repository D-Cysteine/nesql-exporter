## Not Enough SQL Exporter

The exporter module for NESQL. The server module can be found
[here](https://github.com/D-Cysteine/nesql-server). Still very WIP.

Currently only supports exporting items, fluids, and vanilla crafting and
furnace recipes. The exported database is an
[H2 database](http://www.h2database.com/html/main.html), and can also be queried
directly with the H2 client.

### Instructions

1. Place `NESQL-Exporter-0.1.0.jar` and `NESQL-Exporter-0.1.0-deps.jar` into
   your `mods/` folder. `NotEnoughItems` is the only dependency.
2. Start Minecraft and join a single-player world.
3. If you're using the GTNH version of `NotEnoughItems`, open your inventory and
   view the NEI item list so that it gets loaded. If you forget to do this, some
   items might not be exported.
4. Run the command `/nesql`. You may optionally specify a repository name with
   `/nesql your_repository_name`.
5. Wait for the export process to finish. It can take a very long time depending
   on how many mods you have installed. For reference, exporting GTNH can take
   10-30 minutes, and the resulting repository is 3GB, and contains 68k image
   files.
6. Depending on your computer's specs, rendering may end up taking a lot longer
   to finish than exporting the database. There is currently no logging of the
   render progress, so you may just need to be patient.
7. Once the export is finished, you can delete the two mod jars.
