package com.messaging.inbox.folders;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends CassandraRepository<Folder, String> {

    List<Folder> findAllById(String id);

        /* default List<Folder> findAllById(String id) {
            System.out.println("Searching for folders with ID: " + id);
            List<Folder> folders = findAll();
            System.out.println("Retrieved all folders: " + folders);
            List<Folder> matchingFolders = folders.stream()
                    .filter(f -> f.getId().equals(id))
                    .collect(Collectors.toList());
            System.out.println("Matched folders: " + matchingFolders);
            return matchingFolders;
        }*/



}
