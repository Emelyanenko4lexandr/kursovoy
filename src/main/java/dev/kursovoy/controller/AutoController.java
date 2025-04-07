package dev.kursovoy.controller;

import dev.kursovoy.DTO.*;
import dev.kursovoy.service.AutoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/auto")
@AllArgsConstructor
public class AutoController {

    private final AutoService autoService;


    @GetMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AutoResponse> getFreeAuto(){
        return autoService.getFreeAuto();
    }

    @GetMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AllParametersDTO> getAllAuto() {
        return autoService.getAllAuto();
    }

    @PutMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public  AutoResponse verifyAuto(@RequestBody AutoIdRequest request, Principal principal) {
        return autoService.verifyAuto(request.getAutoId(), principal.getName());
    }

    @DeleteMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoResponse deleteUnverifiedAuto(@RequestBody UnverifiedRequest request, Principal principal) {
        return autoService.deleteUnverifiedAuto(request, principal.getName());
    }

    @PostMapping(value = "/auto", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getAuto(@RequestBody AutoIdRequest request) {
        return autoService.getAuto(request.getAutoId());
    }

    @PostMapping(value = "/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> publishAuto(
            @RequestPart("publishedAuto") String publishedAutoJson,  // Получаем JSON как строку
            @RequestParam("files") MultipartFile[] files,  // Принимаем массив файлов
            Principal principal) {

            return autoService.publishAuto(publishedAutoJson, files, principal.getName());

    }

    @DeleteMapping(value = "/owner", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoResponse deleteAuto(@RequestBody AutoIdRequest request) {
        return autoService.deleteAuto(request.getAutoId());
    }

    @GetMapping(value = "/owner", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OwnerAutoResponse> getOwnerCars(Principal principal) {
        return autoService.getOwnerCars(principal.getName());
    }

    @GetMapping(value = "/rents", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AutoResponse> getRentedAuto(Principal principal) {
        return autoService.getRentedAuto(principal.getName());
    }

    @GetMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public LocationResponse getCarLocation(@RequestParam("autoId") Long autoId) {
        return autoService.getCarLocation(autoId);
    }

}
