package com.picture.picture;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ImageService {
    public Long create(Image image);
    public List<Image> viewAll();
    public Image viewById(long id);
    public Image getImagesByImagelocation(Long imagelocation);
    public Image getImageByNameAndImagelocation(String name, Long imagelocation);
    public Image getImageByImgFreeId(long imgfree_id);
}
