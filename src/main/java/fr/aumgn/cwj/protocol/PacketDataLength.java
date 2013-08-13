package fr.aumgn.cwj.protocol;

public class PacketDataLength {

    public static final int VARIABLE        = -1;

    public static final int EMPTY           = 0;

    public static final int INT8            = 1;

    public static final int INT16           = 2;

    public static final int INT32           = 4;

    public static final int INT64           = 8;

    public static final int FLOAT           = 4;

    public static final int DOUBLE          = 8;

    public static final int INT32_VEC3      = INT32 * 3;

    public static final int INT64_VEC3      = INT64 * 3;

    public static final int VEC3            = FLOAT * 3;

    public static final int ITEM_UPGRADE    = 4 * INT8 + INT32;

    public static final int ITEM_DATA       = 2 * INT8 + 2 + 2 * INT32 + 3 * INT8 + 1 + INT16 + 2 + 32 * ITEM_UPGRADE
                                                    + INT32;

    public static final int APPEARANCE_DATA = 0;

    public static final int ENTITY_DATA     = 4456;
}
