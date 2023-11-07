package com.picture.picture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.picture.picture.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Long create(Image image) {
        Image savedImage = imageRepository.save(image);
        return savedImage.getId(); 
    }
    @Override
    public List<Image> viewAll() {
        return (List<Image>) imageRepository.findAll();
    }
    @Override
    public Image viewById(long id) {
        return imageRepository.findById(id).get();
    }
    @Override
    public Image getImagesByImagelocation(Long imagelocation) {
        return imageRepository.findImageByImagelocation(imagelocation);
    }
    @Override
    public Image getImageByNameAndImagelocation(String name, Long imagelocation) {
        // TODO Auto-generated method stub
        return imageRepository.getImageByNameAndImagelocation(name,imagelocation);
    }
}
