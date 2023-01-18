package com.viettel.vtskit.minio;

import com.google.common.collect.Lists;
import com.viettel.vtskit.minio.configuration.ConstantConfiguration;
import com.viettel.vtskit.minio.configuration.MinioProperties;
import com.viettel.vtskit.minio.mapper.FileInfoMapper;
import com.viettel.vtskit.minio.model.FileObjectDTO;
import com.viettel.vtskit.minio.model.UploadResult;
import com.viettel.vtskit.minio.utils.StringUtils;

import com.viettel.vtskit.minio.utils.Validate;
import io.minio.*;
import io.minio.admin.MinioAdminClient;
import io.minio.admin.UserInfo;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.VersioningConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class MinioService {

    private MinioAdminClient minioAdminClient ;
    private MinioClient minioClient;
    private MinioProperties minioProperties;
    private FileInfoMapper fileInfoMapper;

    public String createBucket(String bucketname) {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketname)
                            .build());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bucketname;
    }
    public String setBucketVersion(String bucketname) {

        try {
            VersioningConfiguration config = new VersioningConfiguration(VersioningConfiguration.Status.ENABLED, true);
            minioClient.setBucketVersioning(
                    SetBucketVersioningArgs.builder().bucket(bucketname).config(config).build());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String deleteBucket(String bucketname) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketname).build());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bucketname;
    }
    public UserInfo getUserinfo(String accestoken) {
        try {
            Map<String, String> a = minioAdminClient.listCannedPolicies();
            return minioAdminClient.getUserInfo(accestoken);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getListBucket() {
        try {
            List<Bucket> bucketList = minioClient.listBuckets();
            List<String> listName =  new ArrayList<>();
            for (Bucket b: bucketList
            ) {
                listName.add(b.name());
            }
            return listName;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List < FileObjectDTO > getListFile(String bucketname, @Nullable String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        if (path == null) path = "";
        ListObjectsArgs args = new ListObjectsArgs.Builder()
                .bucket(bucketname)
                .prefix(path)
                .includeUserMetadata(false)
                .includeVersions(true)
                .build();
        Iterable < Result < Item >> results = minioClient.listObjects(args);
//        Iterator < Result < Item >> result = results.iterator();
//        while (result.hasNext()) {
//            Item i = result.next().get();
//            System.out.println("VERSION " + i.versionId());
//        }
        List < FileObjectDTO > resultList = fileInfoMapper.mapToListDTO(Lists.newArrayList(results));
        orderData(resultList);
        return resultList;
    }
    public FileObjectDTO getFileInfo(String bucket, String path) {
        StatObjectArgs args = new StatObjectArgs.Builder()
                .bucket(bucket)
                .object(path)

                .build();
        try{
            StatObjectResponse result = minioClient.statObject(args);
            FileObjectDTO fileItem = new FileObjectDTO();
            fileItem.setName(FilenameUtils.getName(result.object()));
            fileItem.setContentType(result.contentType());
            fileItem.setSize(result.size());
            fileItem.setLastModifiedText(result.lastModified().toString());

            return fileItem;
        }catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public byte[] getFileData(String bucket,  String path) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        GetObjectResponse response = null;
        try {
            Map<String, String> header = new HashMap<>();
//            header.put("Range", range);
            GetObjectArgs args = new GetObjectArgs.Builder()
                    .bucket(bucket)
                    .object(path)
                    .extraHeaders(header)

                    .build();
            response = minioClient.getObject(args);
            return IOUtils.toByteArray(response);
        }catch (Exception e){
            throw e;
        }

    }
    public byte[] getFileDataWithVersion(String bucket,  String path, String version) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        GetObjectResponse response = null;
        try {
            Map<String, String> header = new HashMap<>();
//            header.put("Range", range);
            GetObjectArgs args = new GetObjectArgs.Builder()
                    .bucket(bucket)
                    .object(path)
                    .extraHeaders(header)
                    .versionId(version)

                    .build();
            response = minioClient.getObject(args);
            return IOUtils.toByteArray(response);
        }catch (Exception e){
            throw e;
        }

    }

    private UploadResult uploadBytes(String fileName, byte[] bytes, String contentType) throws Exception {
        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(bytes);
            PutObjectArgs.Builder uploadArgs = PutObjectArgs.builder();
            uploadArgs.contentType(contentType);
            uploadArgs.object(fileName);
            uploadArgs.bucket(minioProperties.getBucket());
            uploadArgs.stream(is, bytes.length, ConstantConfiguration.PART_SIZE);
            ObjectWriteResponse response = minioClient.putObject(uploadArgs.build());
            UploadResult result = new UploadResult();
            result.setEtag(response.etag());
            result.setFileName(fileName);
            result.setVersionId(response.versionId());
            return result;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public UploadResult uploadMultipartFile(MultipartFile file) throws Exception {
        byte[] fileData = file.getBytes();
        if (Validate.validateSize(file, minioProperties)) {
            throw new IllegalArgumentException("The file size is too large, please reconfigure");
        }

        if (Validate.validateType(file, minioProperties)) {
            return uploadBytes(file.getOriginalFilename(), fileData, file.getContentType());

        }
        throw new IllegalArgumentException("Content-type not allow");
    }

    public UploadResult uploadString(String content) throws Exception {

        byte[] fileData = content.getBytes(Charset.forName("UTF-8"));
        String randomName = String.format("%s.txt", StringUtils.randomString());
        return uploadBytes(randomName, fileData, "text/plain");
    }
    private void orderData(List < FileObjectDTO > dataList) {
        Collections.sort(dataList, new Comparator < FileObjectDTO > () {
            @Override
            public int compare(FileObjectDTO o1, FileObjectDTO o2) {
                if (o1.isDirectory() && !o2.isDirectory()) {
                    return -1;
                } else if (o1.isDirectory() && o2.isDirectory()) {
                    return o2.getName().compareTo(o1.getName());
                } else {
                    return o2.getLastModifiedVal().compareTo(o1.getLastModifiedVal());
                }
            }
        });
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Autowired
    public void setMinioProperties(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Autowired
    public void setMinioAdminClient(MinioAdminClient minioAdminClient){
        this.minioAdminClient = minioAdminClient;
    }

    @Autowired
    public void setFileInfoMapper(FileInfoMapper fileInfoMapper) {
        this.fileInfoMapper = fileInfoMapper;
    }

}