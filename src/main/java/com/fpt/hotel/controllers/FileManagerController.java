package com.fpt.hotel.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.hotel.service.FileManagerService;

@RestController
@RequestMapping("api/rest/files")
@CrossOrigin("*")
public class FileManagerController {

    @Autowired
    FileManagerService fileService;

    @GetMapping("{folder}")
    public List<String> list(@PathVariable("folder") String folder) {
        return fileService.list(folder);
    }

    @GetMapping("{folder}/{file}")
    public byte[] dowload(@PathVariable("folder") String folder,
                          @PathVariable("file") String filename) {
    	
        return fileService.read(folder, filename);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("{folder}")
    public List<String> upload(@PathVariable("folder") String folder,
                               @PathParam("files") List<MultipartFile>  files) {
    	
        return fileService.save(folder, files);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{folder}/{file}")
    public void delete(@PathVariable("folder") String folder,
                       @PathVariable("file") String filename) {
        fileService.delete(folder, filename);
    }
}
