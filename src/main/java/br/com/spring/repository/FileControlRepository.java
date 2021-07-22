package br.com.spring.repository;

import br.com.spring.domain.FileControl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileControlRepository extends CrudRepository<FileControl, Integer> {

    Optional<FileControl> findByName(String name);

    List<FileControl> findByNameIn(List<String> name);

}
