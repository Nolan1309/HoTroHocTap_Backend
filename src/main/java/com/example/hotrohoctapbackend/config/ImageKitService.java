package com.example.hotrohoctapbackend.config;


import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.exceptions.BadRequestException;
import io.imagekit.sdk.exceptions.ConflictException;
import io.imagekit.sdk.exceptions.ForbiddenException;
import io.imagekit.sdk.exceptions.InternalServerException;
import io.imagekit.sdk.exceptions.NotFoundException;
import io.imagekit.sdk.exceptions.PartialSuccessException;
import io.imagekit.sdk.exceptions.TooManyRequestsException;
import io.imagekit.sdk.exceptions.UnauthorizedException;
import io.imagekit.sdk.exceptions.UnknownException;
import io.imagekit.sdk.models.AITagsRequest;
import io.imagekit.sdk.models.BaseFile;
import io.imagekit.sdk.models.CopyFileRequest;
import io.imagekit.sdk.models.CopyFolderRequest;
import io.imagekit.sdk.models.CreateFolderRequest;
import io.imagekit.sdk.models.CustomMetaDataFieldCreateRequest;
import io.imagekit.sdk.models.CustomMetaDataFieldSchemaObject;
import io.imagekit.sdk.models.CustomMetaDataFieldUpdateRequest;
import io.imagekit.sdk.models.CustomMetaDataTypeEnum;
import io.imagekit.sdk.models.DeleteFileVersionRequest;
import io.imagekit.sdk.models.DeleteFolderRequest;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.FileUpdateRequest;
import io.imagekit.sdk.models.GetFileListRequest;
import io.imagekit.sdk.models.MoveFileRequest;
import io.imagekit.sdk.models.MoveFolderRequest;
import io.imagekit.sdk.models.RenameFileRequest;
import io.imagekit.sdk.models.TagsRequest;
import io.imagekit.sdk.models.results.*;
import io.imagekit.sdk.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageKitService {

    @Value("${imagekit.public.key}")
    private String publicKey;

    @Value("${imagekit.private.key}")
    private String privateKey;

    @Value("${imagekit.url.endpoint}")
    private String imageKitEndpoint;


    private final String imageKitUrl = "https://upload.imagekit.io/api/v1/files/upload";

    //    public String uploadImage(MultipartFile file) throws IOException {
//
//        byte[] fileBytes = file.getBytes();
//
//        // Chuyển đổi mảng byte thành chuỗi Base64
//        String base = Base64.getEncoder().encodeToString(fileBytes);
//        try {
//            ImageKit imageKit = ImageKit.getInstance();
//            Configuration config = new Configuration(publicKey, privateKey, imageKitEndpoint);
//            imageKit.setConfig(config);
//            FileCreateRequest fileCreateRequest = new FileCreateRequest(base, "women_in_red.jpg");
//            List<String> responseFields = new ArrayList<>();
//            responseFields.add("thumbnail");
//            responseFields.add("tags");
//            responseFields.add("customCoordinates");
//            fileCreateRequest.setResponseFields(responseFields);
//            List<String> tags = new ArrayList<>();
//            tags.add("Software");
//            tags.add("Developer");
//            tags.add("Engineer");
//            fileCreateRequest.setTags(tags);
//            Result result = ImageKit.getInstance().upload(fileCreateRequest);
//
//            // Kiểm tra kết quả upload và lấy URL của ảnh
//            if (result.getFileId() != null) {
//                return result.getUrl();  // Trả về URL của file đã upload
//            } else {
//                throw new IOException("File upload failed: " + result.getName());
//            }
//        } catch (BadRequestException e) {
//            throw new IOException("Bad request: " + e.getMessage(), e);
//        } catch (UnauthorizedException e) {
//            throw new IOException("Unauthorized request: " + e.getMessage(), e);
//        } catch (ForbiddenException e) {
//            throw new IOException("Forbidden request: " + e.getMessage(), e);
//        } catch (InternalServerException e) {
//            throw new IOException("Internal server error: " + e.getMessage(), e);
//        } catch (TooManyRequestsException e) {
//            throw new IOException("Too many requests: " + e.getMessage(), e);
//        } catch (UnknownException e) {
//            throw new IOException("Unknown error: " + e.getMessage(), e);
//        }
//
//    }
    private final ImageKit imageKit;

    // Inject ImageKit instance
    public ImageKitService(ImageKit imageKit) {
        this.imageKit = imageKit;
    }

    //    public String uploadFromBytes(MultipartFile file) throws InternalServerException, BadRequestException, UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException, IOException {
//        File tempFile = multipartFileToFile(file);
//        byte[] bytes = Utils.fileToBytes(tempFile);
//        FileCreateRequest fileCreateRequest = new FileCreateRequest(bytes, "sample_image_To.jpg");
//        fileCreateRequest.setUseUniqueFileName(false);
//        JsonObject optionsInnerObject = new JsonObject();
//        optionsInnerObject.addProperty("add_shadow", true);
//        optionsInnerObject.addProperty("bg_colour", "green");
//        JsonObject innerObject1 = new JsonObject();
//        innerObject1.addProperty("name", "remove-bg");
//        innerObject1.add("options", optionsInnerObject);
//        JsonObject innerObject2 = new JsonObject();
//        innerObject2.addProperty("name", "google-auto-tagging");
//        innerObject2.addProperty("minConfidence", 5);
//        innerObject2.addProperty("maxTags", 95);
//        JsonArray jsonArray = new JsonArray();
//        jsonArray.add(innerObject1);
//        jsonArray.add(innerObject2);
//        fileCreateRequest.setExtensions(jsonArray);
//        fileCreateRequest.setWebhookUrl("https://webhook.site/c78d617f-33bc-40d9-9e61-608999721e2e");
//        fileCreateRequest.setOverwriteFile(true);
//        fileCreateRequest.setOverwriteAITags(true);
//        fileCreateRequest.setOverwriteTags(true);
//        fileCreateRequest.setOverwriteCustomMetadata(true);
//        JsonObject jsonObjectCustomMetadata = new JsonObject();
////        jsonObjectCustomMetadata.addProperty("test10", 10);
//        fileCreateRequest.setCustomMetadata(jsonObjectCustomMetadata);
//        Result result = ImageKit.getInstance().upload(fileCreateRequest);
//        if ((result != null)) {
//            return result.getUrl();
//        } else return null;
//    }
    public Result uploadFromBytes(MultipartFile file) throws InternalServerException, BadRequestException, UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException, IOException {
        // Convert MultipartFile to File
        File tempFile = multipartFileToFile(file);
        byte[] bytes = Utils.fileToBytes(tempFile);

        // Create a FileCreateRequest to upload the file
        FileCreateRequest fileCreateRequest = new FileCreateRequest(bytes, file.getOriginalFilename());
        fileCreateRequest.setUseUniqueFileName(false);  // Optional: If you want to avoid name conflicts

        // Perform the file upload
        Result result = ImageKit.getInstance().upload(fileCreateRequest);

        // Return the uploaded file URL if successful
        if (result != null && result.getUrl() != null) {
            return result;
        } else {
            return null;  // Or throw an exception if needed
        }
    }


    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Create a temporary file
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }
//
//    private String extractImageUrlFromResponse(String responseBody) {
//        // Trích xuất URL của hình ảnh từ phản hồi JSON của ImageKit
//        int urlStartIndex = responseBody.indexOf("url\":\"") + 6;
//        int urlEndIndex = responseBody.indexOf("\"", urlStartIndex);
//        return responseBody.substring(urlStartIndex, urlEndIndex);
//    }
}
