package com.viettel.vtskit.minio.utils;

import org.modelmapper.ModelMapper;

public class MapperUtils {
    public MapperUtils() {
    }

    public static ModelMapper newMapperInstance() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}