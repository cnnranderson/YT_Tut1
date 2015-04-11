package com.cnnranderson.tutorial.ashley.mappers;

import com.badlogic.ashley.core.ComponentMapper;
import com.cnnranderson.tutorial.ashley.components.PositionComponent;

public class Mapper {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<PositionComponent> velocity = ComponentMapper.getFor(PositionComponent.class);
}
