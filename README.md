MinIO Integrated Library for Spring Boot
-------
This library provides utilities that make it easy to integrate MinIO storage into a spring boot project

Feature List:
- Bucket
    * [Create Bucket](#create-bucket)
    * [Enable File Version in Bucket](#enable-file-version-in-bucket)
    * [Delete Bucket](#delete-bucket)
    * [Get List Bucket](#get-list-bucket)
- File
    * [Get List File](#get-list-file)
    * [Get List File With Version](#get-list-file-with-version)
    * [Get File Info](#get-file-info)
    * [Get File Data](#get-file-data)
    * [Get File Data with Version](#get-file-data-with-version-file)
    * [Delete File Data](#delete-file-data)
    * [Delete File Data with Version](#delete-file-data-with-version)
    * [Upload Multipartfile](#upload-multipartfile)
- User
    * [Get User Info](#get-user-info)
    * [File Permissions](#file-permissions)
    
Quick start
-------
* Just add the dependency to an existing Spring Boot project
```xml
<dependency>
    <groupId>com.atviettelsolutions</groupId>
    <artifactId>vts-kit-lib-minio</artifactId>
    <version>1.0.0</version>
</dependency>
```

* Then, add the following properties to your `application-*.yml` file.
```yaml
minio:
    server: http://<host>:<port>
    access-key: <access-key>
    secret-key: <secret-key>
    bucket: <bucket>
    auto-create-bucket: true
    allow-type: ["pdf","txt","json"] // type file allow to save
    max-size: 100 // maximum file upload size (megabyte)
```

* Finally, declare `MinioService` object
```java
@Autowired
private MinioService minioService;
```

Usage
-------
### Bucket
##### Create bucket
```java
String bucketName = minioService.createBucket(bucketname);
```

##### Enable file version in bucket
```java
String bucketName = minioService.setBucketVersion(bucketname);
```

##### Delete bucket
```java
String bucketName = minioService.deleteBucket(bucketname);
```

##### Get list bucket
```java
List<String> listBucket = minioService.getListBucket(bucketname);
```

### File
##### Get list file
```java
List<FileObjectDTO> listFileObject = minioService.getListFile(String bucketname, @Nullable String path);
```
##### Get list file with version
```java
List<FileObjectDTO> listFileObject = minioService.getListFileWithVersion(String bucketname, @Nullable String path);
```
##### Get file info
```java
FileObjectDTO fileObject = minioService.getFileInfo(String bucketname,String path);
```
##### Get file data
```java
byte[] fileData = minioService.getFileData(String bucketname,String path);
```


##### Get file data with version file
```java
byte[] fileData = minioService.getFileDataWithVersion(String bucketname,String path,String version);
```
##### Delete file data
```java
minioService.deleteFile(String bucketname,String path);
```
##### Delete file data with version
```java
minioService.deleteVersionFile(String bucketname,String path,String versionid);
```
##### Upload MultipartFile
```java
ObjectWriteResponse rsUpload = minioService.uploadMultipartFile(file);
```
### User
##### Get user info
```java
UserInfo  userInfo = minioService.getUserinfo(String username);
```
##### File permissions
Minio has a feature to configure file permissions, which can be configured using the cli or on the dashboard page <SERVER_MINIO>28568:/identity/users/add-user (note: 28568 is the default port of the minio console)


Build
-------
* Build with Unittest
```shell script
mvn clean install
```

* Build without Unittest
```shell script
mvn clean install -DskipTests
```

Contribute
-------
Please refer [Contributors Guide](CONTRIBUTING.md)

License
-------
This code is under the [MIT License](https://opensource.org/licenses/MIT).

See the [LICENSE](LICENSE) file for required notices and attributions.
