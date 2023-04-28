package com.messaging.inbox.EmailFolder;


import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;
public interface EmailRepository extends  CassandraRepository<Email,UUID>{


}


