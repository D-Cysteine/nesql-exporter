package com.github.dcysteine.nesql.exporter.util;

import com.github.dcysteine.nesql.exporter.proto.NbtPb;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** Converts Minecraft NBT objects to protos. */
public final class NbtProtoBuilder {
    // Static class.
    private NbtProtoBuilder() {}

    /*
     * Reference guide for NBTBase.getId() values:
     *   0: NBTTagEnd; also used for empty NBTTagList
     *   1: NBTTagByte; used internally for boolean fields
     *   2: NBTTagShort
     *   3: NBTTagInt
     *   4: NBTTagLong
     *   5: NBTTagFloat
     *   6: NBTTagDouble
     *   7: NBTTagByteArray
     *   8: NBTTagString
     *   9: NBTTagList
     *  10: NBTTagCompound
     *  11: NBTTagIntArray
     */

    public static NbtPb buildNbtPb(NBTBase nbt) {
        switch (nbt.getId()) {
            case 0:
                return NbtPb.getDefaultInstance();

            case 1:
                return NbtPb.newBuilder()
                        .setByte(((NBTBase.NBTPrimitive) nbt).func_150290_f())
                        .build();

            case 2:
                return NbtPb.newBuilder()
                        .setShort(((NBTBase.NBTPrimitive) nbt).func_150289_e())
                        .build();

            case 3:
                return NbtPb.newBuilder()
                        .setInt(((NBTBase.NBTPrimitive) nbt).func_150287_d())
                        .build();

            case 4:
                return NbtPb.newBuilder()
                        .setLong(((NBTBase.NBTPrimitive) nbt).func_150291_c())
                        .build();

            case 5:
                return NbtPb.newBuilder()
                        .setFloat(((NBTBase.NBTPrimitive) nbt).func_150291_c())
                        .build();

            case 6:
                return NbtPb.newBuilder()
                        .setDouble(((NBTBase.NBTPrimitive) nbt).func_150286_g())
                        .build();

            case 7: {
                NbtPb.Builder builder = NbtPb.newBuilder();
                Bytes.asList(((NBTTagByteArray) nbt).func_150292_c())
                        .forEach(builder::addByteArray);
                return builder.build();
            }

            case 8:
                return NbtPb.newBuilder()
                        .setString(((NBTTagString) nbt).func_150285_a_())
                        .build();

            case 9:
                return NbtPb.newBuilder()
                        .addAllList(buildNbtPbForList((NBTTagList) nbt))
                        .build();

            case 10: {
                NbtPb.Builder builder = NbtPb.newBuilder();
                NBTTagCompound compound = (NBTTagCompound) nbt;
                @SuppressWarnings("unchecked")
                Set<String> keys = compound.func_150296_c();
                for (String k : keys) {
                    builder.putCompound(k, buildNbtPb(compound.getTag(k)));
                }
                return builder.build();
            }

            case 11:
                return NbtPb.newBuilder()
                        .addAllIntArray(Ints.asList(((NBTTagIntArray) nbt).func_150302_c()))
                        .build();

            default:
                throw new IllegalArgumentException("Unhandled NBT tag: " + nbt);
        }
    }

    public static List<NbtPb> buildNbtPbForList(NBTTagList nbt) {
        IntFunction<NbtPb> retrieve;
        switch (nbt.func_150303_d()) {
            case 0:
                retrieve = i -> NbtPb.getDefaultInstance();
                break;

            // Cases 1-4 are not supported.

            case 5:
                retrieve = i -> NbtPb.newBuilder()
                        .setFloat(nbt.func_150308_e(i))
                        .build();
                break;

            case 6:
                retrieve = i -> NbtPb.newBuilder()
                        .setDouble(nbt.func_150309_d(i))
                        .build();
                break;

            // Case 7 is not supported.

            case 8:
                retrieve = i -> NbtPb.newBuilder()
                        .setString(nbt.getStringTagAt(i))
                        .build();
                break;

            // Case 9 is not supported.

            case 10:
                retrieve = i -> buildNbtPb(nbt.getCompoundTagAt(i));
                break;

            case 11:
                retrieve = i -> NbtPb.newBuilder()
                        .addAllIntArray(Ints.asList(nbt.func_150306_c(i)))
                        .build();
                break;

            default:
                throw new IllegalArgumentException("Unhandled NBT tag in list: " + nbt);
        }

        return IntStream.range(0, nbt.tagCount())
                .mapToObj(retrieve)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
