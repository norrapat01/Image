package com.picture.picture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin
@Controller
@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/ping")
    @ResponseBody
    public String hello_world() {
        return "Hello World!";
    }

    // display image
    @GetMapping("/display")
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id) throws IOException, SQLException {
        Image image = imageService.viewById(id);
        byte[] imageBytes = null;
        imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);

    }

    // view All images
    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("index");
        List<Image> imageList = imageService.viewAll();
        mv.addObject("imageList", imageList);
        return mv;
    }

    // add image - get
    @GetMapping("/add")
    public ModelAndView addImage() {
        return new ModelAndView("addimage");
    }

    @PostMapping("/add")
    public ResponseEntity<Long> addImagePost(HttpServletRequest request, @RequestParam("image") MultipartFile file)
            throws IOException, SerialException, SQLException {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setImage(blob);
        Long imageId = imageService.create(image); // Assuming imageService.create returns the ID of the newly created
                                                   // image
        // Return the ID of the uploaded image in the response
        return ResponseEntity.ok(imageId);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateImage(@RequestParam("id") long id,
            @RequestParam("image") MultipartFile file,
            @RequestParam("imagelocation") long imagelocation,
            @RequestParam("name") String name)
            throws IOException, SerialException, SQLException {
        Image existingImage = imageService.viewById(id);
        if (existingImage == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the image data
        byte[] bytes = file.getBytes();
        Blob newImageBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
        existingImage.setImage(newImageBlob);

        // Update the imagelocation and name
        existingImage.setImagelocation(imagelocation);
        existingImage.setName(name);

        imageService.create(existingImage);

        return ResponseEntity.ok("Image updated successfully");
    }

    @PutMapping("/update/{name}/{imagelocation}")
    public ResponseEntity<String> updateImageForFreelance(
            @PathVariable("imagelocation") long imagelocation,
            @RequestParam("image") MultipartFile file,
            @RequestParam("name") String name)
            throws IOException, SerialException, SQLException {
        Image existingImage = imageService.getImageByNameAndImagelocation(name, imagelocation);
        if (existingImage == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the image data
        byte[] bytes = file.getBytes();
        Blob newImageBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
        existingImage.setImage(newImageBlob);

        // Update the name (if needed)
        existingImage.setName(name);

        imageService.create(existingImage);

        return ResponseEntity.ok("Image updated successfully");
    }

    @GetMapping("/getByNameAndImagelocation/{name}/{imagelocation}")
    public ResponseEntity<byte[]> getImageByNameAndImagelocation(@PathVariable("name") String name,
            @PathVariable("imagelocation") Long imagelocation) throws SQLException {
        Image image = imageService.getImageByNameAndImagelocation(name, imagelocation);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getImage().getBytes(1, (int) image.getImage().length()));
    }

    @GetMapping("/getByImagelocation")
    public ResponseEntity<byte[]> getImageByImagelocation(@RequestParam("imagelocation") Long imagelocation)
            throws SQLException {
        Image image = imageService.getImagesByImagelocation(imagelocation);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getImage().getBytes(1, (int) image.getImage().length()));
    }
}
