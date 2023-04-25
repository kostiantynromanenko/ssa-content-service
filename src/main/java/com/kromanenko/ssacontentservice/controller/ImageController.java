package com.kromanenko.ssacontentservice.controller;

import static com.kromanenko.ssacontentservice.utils.TokenHelper.getUserId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import com.kromanenko.ssacontentservice.facade.ImageFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@OpenAPIDefinition(info = @Info(title = "Image API", version = "1.0.0", description = "API for managing images"), security = {
    @SecurityRequirement(name = "bearerAuth")
})
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageFacade imageFacade;

  @Operation(summary = "Upload an image", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Image successfully uploaded"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping("/upload")
  public ResponseEntity<?> uploadImage(
      @RequestParam("file") MultipartFile image, @AuthenticationPrincipal Jwt token) {
    try {
      var userId = getUserId(token);
      imageFacade.saveImage(image, userId);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Operation(summary = "Get user's images", security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user's images"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping
  public ResponseEntity<?> getUserImages(@AuthenticationPrincipal Jwt token) {
    try {
      var userId = getUserId(token);
      return ResponseEntity.ok(imageFacade.getImages(userId));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
