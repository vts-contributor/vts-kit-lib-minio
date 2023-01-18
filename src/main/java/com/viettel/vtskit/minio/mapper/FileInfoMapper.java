package com.viettel.vtskit.minio.mapper;


import com.viettel.vtskit.minio.model.FileObjectDTO;
import com.viettel.vtskit.minio.utils.MapperUtils;
import io.minio.Result;
import io.minio.messages.Item;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@EnableAutoConfiguration
public class FileInfoMapper extends BaseMapper<FileObjectDTO, Result<Item>>{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileInfoMapper.class);
    private static final String KEY_CONTENT_TYPE = "content-type";
    private static final String KEY_ACCESS_MODE = "X-Amz-Meta-Access-Mode";
    private static final String VAL_PUBLIC_MODE = "public-mode";
    private ModelMapper mapper;

    private Converter objectToDTOCvt = new Converter<Result<Item>, FileObjectDTO>() {
        @Override
        public FileObjectDTO convert(MappingContext<Result<Item>, FileObjectDTO> mappingContext) {
            FileObjectDTO dest = new FileObjectDTO();
            try {
                Item src = mappingContext.getSource().get();
                dest.setName(src.objectName());
                dest.setDirectory(src.isDir());
                if(!src.isDir()){
                    dest.setSize(src.size());
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(src.lastModified().toInstant(), ZoneId.systemDefault());
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);
                    dest.setLastModifiedText(localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                    dest.setLastModifiedVal(timestamp.getTime());
                    dest.setContentType(src.userMetadata().get(KEY_CONTENT_TYPE));
                    dest.setPublicMode(VAL_PUBLIC_MODE.equals(src.userMetadata().get(KEY_ACCESS_MODE)));
                    dest.setVersionId(src.versionId());
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return dest;
        }
    };

    public FileInfoMapper() {
        super(FileObjectDTO.class, (Class<Result<Item>>) new Result<Item>(new Exception()).getClass());
        mapper = MapperUtils.newMapperInstance();
        mapper.addConverter(objectToDTOCvt);
    }

    @Override
    public ModelMapper getModelMapper() {
        return mapper;
    }
}

