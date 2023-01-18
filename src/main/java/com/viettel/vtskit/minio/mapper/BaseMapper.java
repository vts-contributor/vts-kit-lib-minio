package com.viettel.vtskit.minio.mapper;


import com.viettel.vtskit.minio.utils.MapperUtils;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public abstract class BaseMapper<D extends BaseDTO, M> {
    private Class<D> dtoClass;
    private Class<M> modelClass;
    private ModelMapper defaultMapper;

    public BaseMapper(Class<D> dtoClass, Class<M> modelClass){
        this.dtoClass = dtoClass;
        this.modelClass = modelClass;
        this.defaultMapper = MapperUtils.newMapperInstance();
    }

    public D mapToDTO(M model){
        if(model == null){
            return null;
        }
        return getModelMapper().map(model, dtoClass);
    }

    public List<D> mapToListDTO(List<M> list){
        if(list == null){
            return null;
        }
        Class dtoArrayClass = ((D[]) Array.newInstance(dtoClass, 0)).getClass();
        return Arrays.asList(getModelMapper().map(list.toArray(), (Type) dtoArrayClass));
    }

    public ModelMapper getModelMapper() {
        return defaultMapper;
    }
}
