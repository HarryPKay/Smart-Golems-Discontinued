package com.harrykay.smartgolems.common.init;

import com.harrykay.smartgolems.ModUtil;
import com.harrykay.smartgolems.SmartGolems;
import com.harrykay.smartgolems.common.entity.passive.SmartGolemEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(SmartGolems.MOD_ID)
public class ModEntities {
    public static final EntityType<SmartGolemEntity> SMART_GOLEM = ModUtil._null();

}
