package org.dbpedia.databus.moss.services;

import org.apache.commons.io.IOUtils;
import org.dbpedia.databus.utils.MossUtilityFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping(value = "/data")
public class LinkedDataController {

    private final MetadataService ms;

    public LinkedDataController(@Autowired MetadataService ms) {
        this.ms = ms;
    }

    @GetMapping(value = "/{publisher}/{group}/{artifact}/{version}/{fileName}/{metadata}")
    void getMetadata(
            @PathVariable String publisher,
            @PathVariable String group,
            @PathVariable String artifact,
            @PathVariable String version,
            @PathVariable String fileName,
            @PathVariable String metadata,
            HttpServletResponse response
    ) {
        String databusFilePath = publisher + "/" + group + "/" + artifact + "/" + version + "/" + fileName;
        File file = ms.getFile(databusFilePath,metadata);
        if(null != file) {
            try {
                response.setStatus(200);
                if(metadata.endsWith(".svg")) {
                    response.setContentType("image/svg+xml");
                } else {
                    response.setContentType("text/plain");
                }
                OutputStream os = response.getOutputStream();
                FileInputStream is = new FileInputStream(file);
                IOUtils.copy(is,os);
                is.close();
                os.close();
            } catch (IOException ioe) {
                response.setStatus(500);
                ioe.printStackTrace();
            }
        } else {
            response.setStatus(404);
        }
    }

    @GetMapping("fetch")
    void getMetadataByID(@RequestParam String id, @RequestParam(name = "file") String metadata, HttpServletResponse response) {
        String[] pathSegements = id.replace(MossUtilityFunctions.extractBaseFromURL(id), "").split("/");

        File file = ms.getFile(String.join("/", pathSegements), metadata);

        if(null != file) {
            try {
                response.setStatus(200);
                if(metadata.endsWith(".svg")) {
                    response.setContentType("image/svg+xml");
                } else {
                    response.setContentType("text/plain");
                }
                OutputStream os = response.getOutputStream();
                FileInputStream is = new FileInputStream(file);
                IOUtils.copy(is,os);
                is.close();
                os.close();
            } catch (IOException ioe) {
                response.setStatus(500);
                ioe.printStackTrace();
            }
        } else {
            response.setStatus(404);
        }
    }
}
