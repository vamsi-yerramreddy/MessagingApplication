package io.javabrains.inbox.EmailFolder;


import io.javabrains.inbox.EmailFolder.Email;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;
public interface EmailRepository extends  CassandraRepository<Email,UUID>{


}


