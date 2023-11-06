package com.picture.picture;


import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@EnableJpaRepositories
@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
List<Image>   findAll();

Image findImageByImagelocation(long imagelocation);

Image getImageByNameAndImagelocation(String name, Long imagelocation);
}
